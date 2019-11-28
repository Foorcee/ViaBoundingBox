package de.foorcee.viaboundingbox.version.v_1_13;

import de.foorcee.viaboundingbox.api.versions.ICollisionBridge;
import de.foorcee.viaboundingbox.api.versions.WrappedVoxelShape;
import lombok.RequiredArgsConstructor;
import net.minecraft.server.v1_13_R2.*;

import java.util.*;

@RequiredArgsConstructor
public class CollisionBridge_v1_13 implements ICollisionBridge<EntityPlayer, AxisAlignedBB> {

    private final BoundingBox_v1_13 injector;

    @Override
    public boolean checkCollision(EntityPlayer player, AxisAlignedBB boundingBox) {
        System.out.println("Check collision " + player.getName() + ": " + boundingBox);
        return this.getVoxelShapeList(player, VoxelShapes.a(boundingBox), VoxelShapes.a()).stream().allMatch(VoxelShape::isEmpty);
    }

    private List<VoxelShape> getVoxelShapeList(Entity entity, VoxelShape voxelshape, VoxelShape voxelshape1) {
        boolean flag = entity != null && entity.bG();
        boolean flag1 = entity != null && entity.world.i(entity);

        if (entity != null && flag == flag1) {
            entity.n(!flag1);
        }

        return this.checkBlockCollision(entity, voxelshape, voxelshape1, flag1);
    }

    private List<VoxelShape> checkBlockCollision(Entity player, VoxelShape voxelshape, VoxelShape voxelshape1, boolean flag) {
        int x1 = MathHelper.floor(voxelshape.b(EnumDirection.EnumAxis.X)) - 1;
        int x2 = MathHelper.f(voxelshape.c(EnumDirection.EnumAxis.X)) + 1;
        int y1 = MathHelper.floor(voxelshape.b(EnumDirection.EnumAxis.Y)) - 1;
        int y2 = MathHelper.f(voxelshape.c(EnumDirection.EnumAxis.Y)) + 1;
        int z1 = MathHelper.floor(voxelshape.b(EnumDirection.EnumAxis.Z)) - 1;
        int z2 = MathHelper.f(voxelshape.c(EnumDirection.EnumAxis.Z)) + 1;
        WorldBorder worldborder = player.world.getWorldBorder();
        boolean inWorldBorder = worldborder.b() < x1 && x2 < worldborder.d() && worldborder.c() < z1 && z2 < worldborder.e();
        VoxelShapeBitSet voxelshapebitset = new VoxelShapeBitSet(x2 - x1, y2 - y1, z2 - z1);

        List<VoxelShape> voxels = new ArrayList<>();

        Iterable<BlockPosition.MutableBlockPosition> blockPosIterable = BlockPosition.MutableBlockPosition.b(x1, y1, z1, x2 - 1, y2 - 1, z2 - 1);
        for (BlockPosition.MutableBlockPosition blockposition : blockPosIterable) {
            int x = blockposition.getX();
            int y = blockposition.getY();
            int z = blockposition.getZ();
            boolean maxX = x == x1 || x == x2 - 1;
            boolean maxY = y == y1 || y == y2 - 1;
            boolean maxZ = z == z1 || z == z2 - 1;

            VoxelShape shape;

            if ((!maxX || !maxY) && (!maxY || !maxZ) && (!maxZ || !maxX) && player.world.isLoaded(blockposition)) {
                if (flag && !inWorldBorder && !worldborder.a(blockposition)) {
                    shape = VoxelShapes.b();
                } else {
                    IBlockData data = player.world.getType(blockposition);
                    WrappedVoxelShape<VoxelShape> wrappedVoxelShape = injector.getRemappingProvider().getRemappedBlockBoundingBox(player.getBukkitEntity(), new WrappedBlockData_v1_13(data));
                    if(wrappedVoxelShape != null){
                        shape = wrappedVoxelShape.getVoxelShape();
                    }else{
                        shape = data.getCollisionShape(player.world, blockposition);
                    }
                }

                VoxelShape voxelshape3 = voxelshape1.a(-x, -y, -z);

                if (VoxelShapes.c(voxelshape3, shape, OperatorBoolean.AND)) {
                    shape = VoxelShapes.a();
                } else if (shape == VoxelShapes.b()) {
                    voxelshapebitset.a(x - x1, y - y1, z - z1, true, true);
                    shape = VoxelShapes.a();
                } else {
                    shape = shape.a(x, y, z);
                }
            } else {
                shape = VoxelShapes.a();
            }

            if (!shape.isEmpty() && VoxelShapes.c(voxelshape, shape, OperatorBoolean.AND)) {
                voxels.add(shape);
            }
        }

        VoxelShapeWorldRegion worldShape = new VoxelShapeWorldRegion(voxelshapebitset, x1, y1, z1);
        if (!worldShape.isEmpty() && VoxelShapes.c(voxelshape, worldShape, OperatorBoolean.AND)) {
            voxels.add(worldShape);
        }

        return voxels;
    }
}
