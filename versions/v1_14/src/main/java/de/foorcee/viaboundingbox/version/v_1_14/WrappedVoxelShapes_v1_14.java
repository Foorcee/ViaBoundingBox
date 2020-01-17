package de.foorcee.viaboundingbox.version.v_1_14;

import de.foorcee.viaboundingbox.api.versions.WrappedVoxelShape;
import de.foorcee.viaboundingbox.api.versions.WrappedVoxelShapes;
import net.minecraft.server.v1_14_R1.AxisAlignedBB;
import net.minecraft.server.v1_14_R1.IBlockData;
import net.minecraft.server.v1_14_R1.VoxelShape;
import net.minecraft.server.v1_14_R1.VoxelShapes;
import org.bukkit.block.data.BlockData;

public class WrappedVoxelShapes_v1_14 extends WrappedVoxelShapes<VoxelShape> {

    private final WrappedVoxelShape<VoxelShape> EMPTY;
    private final WrappedVoxelShape<VoxelShape> FULL;

    public WrappedVoxelShapes_v1_14() {
        super();
        EMPTY = new WrappedVoxelShape<>(VoxelShapes.a());
        FULL = new WrappedVoxelShape<>(VoxelShapes.b());
        setInstance(this);
    }

    @Override
    public WrappedVoxelShape<VoxelShape> getEmptyVoxelShapeImpl() {
        return EMPTY;
    }

    @Override
    public WrappedVoxelShape<VoxelShape> getFullBlockVoxelShapeImpl() {
        return FULL;
    }

    @Override
    public WrappedVoxelShape<VoxelShape> createVoxelShapeImpl(double x1, double y1, double z1, double x2, double y2, double z2) {
        return new WrappedVoxelShape<>(VoxelShapes.a(new AxisAlignedBB(x1, y1, z1, x2, y2, z2)));
    }

    @Override
    public WrappedVoxelShape<VoxelShape> divideImpl(WrappedVoxelShape<VoxelShape> voxelShape, double val) {
        AxisAlignedBB bb = voxelShape.getVoxelShape().getBoundingBox();
        return createVoxelShapeImpl(bb.minX / val, bb.minY / val, bb.minZ / val, bb.maxX / val, bb.maxY / val, bb.maxZ / val);
    }
}
