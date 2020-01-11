package de.foorcee.viaboundingbox.version.v_1_15;

import de.foorcee.viaboundingbox.api.asm.ClassTransformer;
import de.foorcee.viaboundingbox.api.asm.MethodTransformer;
import de.foorcee.viaboundingbox.api.versions.AbstractBoundingBoxInjector;
import de.foorcee.viaboundingbox.api.versions.ICollisionBridge;
import de.foorcee.viaboundingbox.api.versions.WrappedVoxelShapes;
import net.minecraft.server.v1_15_R1.AxisAlignedBB;
import net.minecraft.server.v1_15_R1.EntityPlayer;
import net.minecraft.server.v1_15_R1.VoxelShape;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

public class BoundingBox_v1_15 extends AbstractBoundingBoxInjector<EntityPlayer, AxisAlignedBB, VoxelShape, BlockData, Material> {

    private WrappedVoxelShapes_v1_15 wrappedVoxelShapes;

    public BoundingBox_v1_15(){
        this.wrappedVoxelShapes = new WrappedVoxelShapes_v1_15();
    }

    @Override
    public ClassTransformer[] getClassTransformers() {
        return classTransformers;
    }

    @Override
    public ICollisionBridge<EntityPlayer, AxisAlignedBB> getBridge() {
        return new CollisionBridge_v1_15(this);
    }

    @Override
    public WrappedVoxelShapes<VoxelShape> getVoxelShapes() {
        return wrappedVoxelShapes;
    }

    public static ClassTransformer[] classTransformers = new ClassTransformer[] {
            new ClassTransformer("net.minecraft.server.v1_15_R1.PlayerConnection",
                    new MethodTransformer("a", "(Lnet/minecraft/server/v1_15_R1/IWorldReader;)Z") {
                        @Override
                        public void transform(ClassNode classNode, MethodNode methodNode) {
                            methodNode.instructions.clear();
                            InsnList list = new InsnList();
                            list.add(new VarInsnNode(Opcodes.ALOAD, 0));
                            list.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/server/v1_15_R1/PlayerConnection", "player", "Lnet/minecraft/server/v1_15_R1/EntityPlayer;"));
                            list.add(new VarInsnNode(Opcodes.ALOAD, 0));
                            list.add(new FieldInsnNode(Opcodes.GETFIELD, "net/minecraft/server/v1_15_R1/PlayerConnection", "player", "Lnet/minecraft/server/v1_15_R1/EntityPlayer;"));
                            list.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/server/v1_15_R1/EntityPlayer", "getBoundingBox", "()Lnet/minecraft/server/v1_15_R1/AxisAlignedBB;", false));
                            list.add(new LdcInsnNode(9.999999747378752E-6));
                            list.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, "net/minecraft/server/v1_15_R1/AxisAlignedBB", "shrink", "(D)Lnet/minecraft/server/v1_15_R1/AxisAlignedBB;", false));
                            list.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "de/foorcee/viaboundingbox/common/asm/BoundingBoxCollisionBridge", "checkCollision", "(Ljava/lang/Object;Ljava/lang/Object;)Z", false));
                            list.add(new InsnNode(Opcodes.IRETURN));

                            methodNode.instructions.add(list);
                        }
                    })
    };
}
