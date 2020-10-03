package de.foorcee.viaboundingbox.version.v1_16;

import de.foorcee.viaboundingbox.api.asm.ClassTransformer;
import de.foorcee.viaboundingbox.api.asm.MethodTransformer;
import de.foorcee.viaboundingbox.api.versions.AbstractBoundingBoxInjector;
import de.foorcee.viaboundingbox.api.versions.ICollisionBridge;
import de.foorcee.viaboundingbox.api.versions.WrappedVoxelShapes;
import net.minecraft.server.v1_16_R2.AxisAlignedBB;
import net.minecraft.server.v1_16_R2.EntityPlayer;
import net.minecraft.server.v1_16_R2.VoxelShape;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.ArrayList;
import java.util.List;

public class BoundingBox_v1_16 extends AbstractBoundingBoxInjector<EntityPlayer, AxisAlignedBB, VoxelShape, BlockData, Material> {

    private WrappedVoxelShapes_v1_16 wrappedVoxelShapes;

    public BoundingBox_v1_16() {
        this.wrappedVoxelShapes = new WrappedVoxelShapes_v1_16();
    }

    @Override
    public ClassTransformer[] getClassTransformers() {
        return classTransformers;
    }

    @Override
    public ICollisionBridge<EntityPlayer, AxisAlignedBB, VoxelShape> getBridge() {
        return new CollisionBridge_v1_16(this);
    }

    @Override
    public WrappedVoxelShapes<VoxelShape> getVoxelShapes() {
        return wrappedVoxelShapes;
    }

    public static ClassTransformer[] classTransformers = new ClassTransformer[]{
            new ClassTransformer("net.minecraft.server.v1_16_R2.PlayerConnection",
                    new MethodTransformer("a", "(Lnet/minecraft/server/v1_16_R2/PacketPlayInFlying;)V") {
                        @Override
                        public void transform(ClassNode classNode, MethodNode methodNode) {
                            List<AbstractInsnNode> toRemove = new ArrayList<>();

                            AbstractInsnNode[] array = methodNode.instructions.toArray();
                            for (AbstractInsnNode node : array) {
                                if (node instanceof MethodInsnNode) {
                                    MethodInsnNode methodInsnNode = (MethodInsnNode) node;
                                    if (methodInsnNode.getOpcode() == Opcodes.INVOKEVIRTUAL && methodInsnNode.owner.equals("net/minecraft/server/v1_16_R2/WorldServer")
                                            && methodInsnNode.name.equals("getCubes") && methodInsnNode.desc.equals("(Lnet/minecraft/server/v1_16_R2/Entity;Lnet/minecraft/server/v1_16_R2/AxisAlignedBB;)Z")) {
                                        methodInsnNode.setOpcode(Opcodes.INVOKESTATIC);
                                        methodInsnNode.owner = "de/foorcee/viaboundingbox/common/asm/BoundingBoxCollisionBridge";
                                        methodInsnNode.name = "checkCollision";
                                        methodInsnNode.desc = "(Ljava/lang/Object;Ljava/lang/Object;)Z";

                                        AbstractInsnNode prev = node.getPrevious();
                                        while (prev != null) {
                                            if (prev instanceof VarInsnNode) {
                                                VarInsnNode varInsnNode = (VarInsnNode) prev;
                                                if (varInsnNode.getOpcode() == Opcodes.ALOAD && varInsnNode.var == 2) {
                                                    toRemove.add(varInsnNode);
                                                    break;
                                                }
                                            }
                                            prev = prev.getPrevious();
                                        }
                                    }
                                }
                            }
                            toRemove.forEach(r -> methodNode.instructions.remove(r));
                        }
                    },
                    new MethodTransformer("a", "(Lnet/minecraft/server/v1_16_R2/IWorldReader;Lnet/minecraft/server/v1_16_R2/AxisAlignedBB;)Z") {
                        @Override
                        public void transform(ClassNode classNode, MethodNode methodNode) {

                            AbstractInsnNode[] array = methodNode.instructions.toArray();

                            int labelId = 0;
                            for (AbstractInsnNode node : array) {
                                if (node instanceof LineNumberNode) continue;
                                if (node instanceof LabelNode) {
                                    if (labelId >= 1) break;
                                    labelId++;
                                    continue;
                                }
                                if (node instanceof VarInsnNode) {
                                    VarInsnNode varInsnNode = (VarInsnNode) node;
                                    if (varInsnNode.getOpcode() == Opcodes.ALOAD && varInsnNode.var == 1) {
                                        methodNode.instructions.remove(varInsnNode);
                                    }
                                }
                                if (node instanceof InvokeDynamicInsnNode) {
                                    InvokeDynamicInsnNode methodInsnNode = (InvokeDynamicInsnNode) node;
                                    if (methodInsnNode.name.equals("test")) {
                                        methodNode.instructions.remove(methodInsnNode);
                                    }
                                }

                                if (node instanceof MethodInsnNode) {
                                    MethodInsnNode methodInsnNode = (MethodInsnNode) node;
                                    if (methodInsnNode.name.equals("d")) {
                                        methodInsnNode.setOpcode(Opcodes.INVOKESTATIC);
                                        methodInsnNode.owner = "de/foorcee/viaboundingbox/common/asm/BoundingBoxCollisionBridge";
                                        methodInsnNode.name = "checkBlockCollision";
                                        methodInsnNode.desc = "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/util/stream/Stream;";
                                    }
                                }

                            }
                        }
                    })

    };
}
