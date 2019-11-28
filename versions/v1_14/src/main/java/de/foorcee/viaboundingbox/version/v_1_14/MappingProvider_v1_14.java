package de.foorcee.viaboundingbox.version.v_1_14;

import de.foorcee.viaboundingbox.api.remapping.AbstractBoundingBoxRemappingProvider;
import de.foorcee.viaboundingbox.api.versions.WrappedVoxelShape;
import de.foorcee.viaboundingbox.api.versions.WrappedVoxelShapes;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;

import static us.myles.ViaVersion.api.protocol.ProtocolVersion.*;

public class MappingProvider_v1_14 extends AbstractBoundingBoxRemappingProvider<BlockData, Material, WrappedVoxelShape> {

    //TODO Edit for anvil
    //(((Directional) data.getBlockData()).getFacing() == BlockFace.WEST ||  ((Directional) data.getBlockData()).getFacing() == BlockFace.EAST)
    //                ? WrappedVoxelShapes.createVoxelShape(0.0D, 0.0D, 0.125D, 1.0D, 1.0D, 0.875D) : WrappedVoxelShapes.createVoxelShape(0.125D, 0.0D, 0.0D, 0.875D, 1.0D, 1.0D)
    @Override
    public void register() {
        remapBlockBoundingBox(equalOrBelow(v1_13_2), data -> data.isBlock(Material.CAMPFIRE), data -> WrappedVoxelShapes.getEmptyVoxelShape());
        remapBlockBoundingBox(equalOrBelow(v1_13_2), data -> data.isBlock(Material.GRINDSTONE), data -> WrappedVoxelShapes.getEmptyVoxelShape());
        remapBlockBoundingBox(equalOrBelow(v1_13_2), data -> data.isBlock(Material.BAMBOO), data -> WrappedVoxelShapes.getEmptyVoxelShape());
    }
}
