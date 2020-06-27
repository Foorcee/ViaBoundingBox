package de.foorcee.viaboundingbox.version.v1_16;

import de.foorcee.viaboundingbox.api.versions.WrappedBlockData;
import net.minecraft.server.v1_16_R1.IBlockData;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.v1_16_R1.block.data.CraftBlockData;

public class WrappedBlockData_v1_16 extends WrappedBlockData<BlockData, Material> {

    public WrappedBlockData_v1_16(IBlockData blockData) {
        super(CraftBlockData.fromData(blockData));
    }

    @Override
    public boolean isEquals(BlockData blockData) {
        return getBlockData().equals(blockData);
    }

    @Override
    public boolean isBlock(Material material) {
        return getBlockData().getMaterial() == material;
    }
}
