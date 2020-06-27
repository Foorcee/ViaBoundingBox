package de.foorcee.viaboundingbox.version.v1_13;

import de.foorcee.viaboundingbox.api.asm.ClassTransformer;
import de.foorcee.viaboundingbox.api.asm.MethodTransformer;
import de.foorcee.viaboundingbox.api.versions.*;
import net.minecraft.server.v1_13_R2.AxisAlignedBB;
import net.minecraft.server.v1_13_R2.EntityPlayer;
import net.minecraft.server.v1_13_R2.VoxelShape;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

public class BoundingBox_v1_13 extends AbstractBoundingBoxInjector<EntityPlayer, AxisAlignedBB, VoxelShape, BlockData, Material> {

    private WrappedVoxelShapes_v1_13 wrappedVoxelShapes;

    public BoundingBox_v1_13(){
        this.wrappedVoxelShapes = new WrappedVoxelShapes_v1_13();
    }

    @Override
    public ClassTransformer[] getClassTransformers() {
        return classTransformers;
    }

    @Override
    public ICollisionBridge<EntityPlayer, AxisAlignedBB, VoxelShape> getBridge() {
        return new CollisionBridge_v1_13(this);
    }

    @Override
    public WrappedVoxelShapes<VoxelShape> getVoxelShapes() {
        return wrappedVoxelShapes;
    }

    public static ClassTransformer[] classTransformers = new ClassTransformer[]{
            new ClassTransformer("net.minecraft.server.v1_13_R2.PlayerConnection",
                    new MethodTransformer("a", "(Lnet/minecraft/server/v1_13_R2/PacketPlayInFlying;)V") {
                        @Override
                        public void transform(ClassNode classNode, MethodNode methodNode) {

                            AbstractInsnNode[] array = methodNode.instructions.toArray();
                            for (AbstractInsnNode node : array) {
                                if(node instanceof MethodInsnNode){
                                    MethodInsnNode methodInsnNode = (MethodInsnNode) node;
                                    if(methodInsnNode.getOpcode() == Opcodes.INVOKEVIRTUAL && methodInsnNode.owner.equals("net/minecraft/server/v1_13_R2/WorldServer")
                                            && methodInsnNode.name.equals("getCubes") && methodInsnNode.desc.equals("(Lnet/minecraft/server/v1_13_R2/Entity;Lnet/minecraft/server/v1_13_R2/AxisAlignedBB;)Z")){
                                        methodInsnNode.setOpcode(Opcodes.INVOKESTATIC);
                                        methodInsnNode.owner = "de/foorcee/viaboundingbox/common/asm/BoundingBoxCollisionBridge";
                                        methodInsnNode.name = "checkCollision";
                                        methodInsnNode.desc = "(Ljava/lang/Object;Ljava/lang/Object;)Z";

                                        AbstractInsnNode prev = node.getPrevious();
                                        while(prev != null){
                                            if(prev instanceof VarInsnNode){
                                                VarInsnNode varInsnNode = (VarInsnNode) prev;
                                                if(varInsnNode.getOpcode() == Opcodes.ALOAD && varInsnNode.var == 2){
                                                    methodNode.instructions.remove(varInsnNode);
                                                    break;
                                                }
                                            }
                                            prev = prev.getPrevious();
                                        }
                                    }
                                }
                            }
                         }
                    })
    };
}
