package de.foorcee.viaboundingbox.version.v_1_15;

import de.foorcee.viaboundingbox.api.remapping.AbstractBoundingBoxRemappingProvider;
import de.foorcee.viaboundingbox.api.versions.WrappedVoxelShape;
import de.foorcee.viaboundingbox.api.versions.WrappedVoxelShapes;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;

import static us.myles.ViaVersion.api.protocol.ProtocolVersion.*;

public class MappingProvider_v1_15 extends AbstractBoundingBoxRemappingProvider<BlockData, Material, WrappedVoxelShape> {

    @Override
    public void register() {
    }
}
