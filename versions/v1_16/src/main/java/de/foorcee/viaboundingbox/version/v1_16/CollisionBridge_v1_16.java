package de.foorcee.viaboundingbox.version.v1_16;

import com.google.common.collect.Streams;
import de.foorcee.viaboundingbox.api.versions.ICollisionBridge;
import de.foorcee.viaboundingbox.api.versions.WrappedVoxelShape;
import lombok.RequiredArgsConstructor;
import net.minecraft.server.v1_16_R2.*;
import org.bukkit.block.BlockState;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Objects;
import java.util.Spliterators;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@RequiredArgsConstructor
public class CollisionBridge_v1_16 implements ICollisionBridge<EntityPlayer, AxisAlignedBB, VoxelShape> {
    private final BoundingBox_v1_16 injector;

    @Override
    public boolean checkCollision(EntityPlayer player, AxisAlignedBB boundingBox) {
        boolean value = Streams.concat(
                checkBlockCollision(player, boundingBox),
                player.world.c(player, boundingBox, entity -> true) //checkEntityCollision
        ).allMatch(VoxelShape::isEmpty);
        return value;
    }

    @Override
    public Stream<VoxelShape> checkBlockCollision(EntityPlayer player, AxisAlignedBB boundingBox) {
        return StreamSupport.stream(new CustomVoxelShapeSpliterator(player.world, player, boundingBox), false);
    }

    public class CustomVoxelShapeSpliterator extends VoxelShapeSpliterator {

        private Entity entity;
        private CursorPosition cursor;
        private ICollisionAccess collisionAccess;
        private BiPredicate<IBlockData, BlockPosition> predicate;
        private AxisAlignedBB aABB;
        private VoxelShape entityShape;
        private VoxelShapeCollision context;
        private boolean needsBorderCheck;

        public CustomVoxelShapeSpliterator(ICollisionAccess var0, @Nullable Entity var1, AxisAlignedBB var2) {
            this(var0, var1, var2, (iBlockData, blockPosition) -> true);
        }

        public CustomVoxelShapeSpliterator(ICollisionAccess var0, @Nullable Entity var1, AxisAlignedBB aABB, BiPredicate<IBlockData, BlockPosition> var3) {
            super(var0, var1, aABB, var3);
            this.entity = var1;
            this.collisionAccess = var0;
            int integer6 = MathHelper.floor(aABB.minX - 1.0E-7) - 1;
            int integer7 = MathHelper.floor(aABB.maxX + 1.0E-7) + 1;
            int integer8 = MathHelper.floor(aABB.minY - 1.0E-7) - 1;
            int integer9 = MathHelper.floor(aABB.maxY + 1.0E-7) + 1;
            int integer10 = MathHelper.floor(aABB.minZ - 1.0E-7) - 1;
            int integer11 = MathHelper.floor(aABB.maxZ + 1.0E-7) + 1;
            this.cursor = new CursorPosition(integer6, integer8, integer10, integer7, integer9, integer11);
            this.predicate = var3;
            this.aABB = aABB;
            this.entityShape = VoxelShapes.a(aABB);
            this.context = var1 == null ? VoxelShapeCollision.a() : VoxelShapeCollision.a(var1);
            this.needsBorderCheck = entity != null;
        }

        @Override
        public boolean tryAdvance(Consumer<? super VoxelShape> var0) {
            return this.needsBorderCheck && this.worldBorderCheck(var0) || collisionCheck(var0);
        }

        boolean collisionCheck(Consumer<? super VoxelShape> consumer) {
            while (this.cursor.a()) {
                int x = this.cursor.b();
                int y = this.cursor.c();
                int z = this.cursor.d();
                int type = this.cursor.e();
                if (type == 3) {
                    continue;
                }
                IBlockAccess chunk = getChunk(x, z);
                if (chunk == null) {
                    continue;
                }
                BlockPosition position = new BlockPosition(x,y,z);
                IBlockData blockData = chunk.getType(position);
                if (!this.predicate.test(blockData, position)) {
                    continue;
                }
                if (type == 1 && !blockData.d()) {
                    continue;
                }
                if (type == 2 && !blockData.a(Blocks.MOVING_PISTON)) {
                    continue;
                }

                VoxelShape blockShape;
                WrappedVoxelShape<VoxelShape> wrappedBlockShape = injector.getRemappingProvider().getRemappedBlockBoundingBox(entity.getBukkitEntity(), new WrappedBlockData_v1_16(blockData));
                if (wrappedBlockShape == null) {
                    blockShape =  blockData.b(this.collisionAccess, position, this.context);
                }else{
                    blockShape = wrappedBlockShape.getVoxelShape();
                }
                if (blockShape == VoxelShapes.b()) {
                    if (this.aABB.a(x, y, z, x + 1.0, y + 1.0, z + 1.0)) {
                        consumer.accept(blockShape.a(x, y, z));
                        return true;
                    }
                    continue;
                }
                else {
                    VoxelShape voxelShape10 = blockShape.a(x, y, z);
                    if (VoxelShapes.c(voxelShape10, this.entityShape, OperatorBoolean.AND)) {
                        consumer.accept(voxelShape10);
                        return true;
                    }
                    continue;
                }
            }
            return false;
        }

        @Nullable
        private IBlockAccess getChunk(int x, int y) {
            int var2 = x >> 4;
            int var3 = y >> 4;
            return this.collisionAccess.c(var2, var3);
        }

        boolean worldBorderCheck(Consumer<? super VoxelShape> consumer) {
            Objects.requireNonNull(this.entity);
            this.needsBorderCheck = false;
            WorldBorder worldBorder3 = this.collisionAccess.getWorldBorder();
            AxisAlignedBB aABB4 = this.entity.getBoundingBox();
            if (!a(worldBorder3, aABB4)) {
                VoxelShape voxelShape5 = worldBorder3.c();
                if (!isOutsideBorder(voxelShape5, aABB4) && isCloseToBorder(voxelShape5, aABB4)) {
                    consumer.accept(voxelShape5);
                    return true;
                }
            }
            return false;
        }
    }

    private static boolean isCloseToBorder(VoxelShape var0, AxisAlignedBB var1) {
        return VoxelShapes.c(var0, VoxelShapes.a(var1.g(1.0E-7D)), OperatorBoolean.AND);
    }

    private static boolean isOutsideBorder(VoxelShape var0, AxisAlignedBB var1) {
        return VoxelShapes.c(var0, VoxelShapes.a(var1.shrink(1.0E-7D)), OperatorBoolean.AND);
    }
}
