package de.foorcee.viaboundingbox.version.v_1_15;

import de.foorcee.viaboundingbox.api.versions.WrappedVoxelShape;
import de.foorcee.viaboundingbox.api.versions.WrappedVoxelShapes;
import net.minecraft.server.v1_15_R1.AxisAlignedBB;
import net.minecraft.server.v1_15_R1.VoxelShape;
import net.minecraft.server.v1_15_R1.VoxelShapes;

public class WrappedVoxelShapes_v1_15 extends WrappedVoxelShapes<VoxelShape> {

    private final WrappedVoxelShape<VoxelShape> EMPTY;
    private final WrappedVoxelShape<VoxelShape> FULL;

    public WrappedVoxelShapes_v1_15() {
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
}
