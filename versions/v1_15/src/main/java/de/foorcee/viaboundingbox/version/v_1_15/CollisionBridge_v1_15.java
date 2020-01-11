package de.foorcee.viaboundingbox.version.v_1_15;

import com.google.common.collect.Streams;
import de.foorcee.viaboundingbox.api.versions.ICollisionBridge;
import de.foorcee.viaboundingbox.api.versions.WrappedVoxelShape;
import lombok.RequiredArgsConstructor;
import net.minecraft.server.v1_15_R1.*;

import java.util.Collections;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@RequiredArgsConstructor
public class CollisionBridge_v1_15 implements ICollisionBridge<EntityPlayer, AxisAlignedBB> {

    private final BoundingBox_v1_15 injector;

    @Override
    public boolean checkCollision(EntityPlayer player, AxisAlignedBB boundingBox) {
        boolean value = Streams.concat(
                checkBlockCollision(player.world, player, boundingBox),
                player.world.b(player, boundingBox, Collections.emptySet()) //checkEntityCollision
        ).allMatch(VoxelShape::isEmpty);
        return value;
    }


    private Stream<VoxelShape> checkBlockCollision(World world, EntityPlayer player, AxisAlignedBB boundingBox) {
        final VoxelShapeCollision collisionShape = VoxelShapeCollision.a(player);

        final CursorPosition cursor = new CursorPosition(
                MathHelper.floor(boundingBox.minX - 1.0E-7D) - 1,
                MathHelper.floor(boundingBox.minY - 1.0E-7D) - 1,
                MathHelper.floor(boundingBox.minZ - 1.0E-7D) - 1,
                MathHelper.floor(boundingBox.maxX + 1.0E-7D) + 1,
                MathHelper.floor(boundingBox.maxY + 1.0E-7D) + 1,
                MathHelper.floor(boundingBox.maxZ + 1.0E-7D) + 1);
        final BlockPosition.MutableBlockPosition position = new BlockPosition.MutableBlockPosition();
        final VoxelShape voxelShape = VoxelShapes.a(boundingBox);

        return StreamSupport.stream(new Spliterators.AbstractSpliterator<VoxelShape>(Long.MAX_VALUE, 1280) {
            boolean checkBorder = false;

            public boolean tryAdvance(Consumer<? super VoxelShape> consumer) {
                if (!this.checkBorder) {
                    this.checkBorder = true;
                    VoxelShape borderShape = world.getWorldBorder().a();
                    boolean flag = VoxelShapes.c(borderShape, VoxelShapes.a(player.getBoundingBox().shrink(1.0E-7D)), OperatorBoolean.AND);
                    boolean flag1 = VoxelShapes.c(borderShape, VoxelShapes.a(player.getBoundingBox().g(1.0E-7D)), OperatorBoolean.AND);

                    if (!flag && flag1) {
                        consumer.accept(borderShape);
                        return true;
                    }
                }

                while (cursor.a()) {
                    int x = cursor.b();
                    int y = cursor.c();
                    int z = cursor.d();

                    int d = cursor.e();

                    if(d == 3) continue;

                    IBlockAccess chunk = world.c(x >> 4, z >> 4);
                    if (chunk == null) continue;

                    position.d(x, y, z);
                    IBlockData blockData = chunk.getType(position);

                    if ((d != 1 || blockData.f()) && (d != 2 || blockData.getBlock() == Blocks.MOVING_PISTON)) {

                        WrappedVoxelShape<VoxelShape> wrappedBlockShape = injector.getRemappingProvider().getRemappedBlockBoundingBox(player.getBukkitEntity(), new WrappedBlockData_v1_15(blockData));
                        VoxelShape blockShape;

                        if (wrappedBlockShape == null) {
                            blockShape = blockData.b(world, position, collisionShape);
                        }else{
                            blockShape = wrappedBlockShape.getVoxelShape();
                        }

                        blockShape = blockShape.a(x, y, z);

                        if (VoxelShapes.c(voxelShape, blockShape, OperatorBoolean.AND)) {
                            consumer.accept(blockShape);
                            return true;
                        }
                    }
                }

                return false;
            }
        }, false);
    }
}
