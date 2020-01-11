package de.foorcee.viaboundingbox.version.v_1_15;

import de.foorcee.viaboundingbox.api.versions.WrappedBlockData;
import net.minecraft.server.v1_15_R1.IBlockData;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.v1_15_R1.block.data.CraftBlockData;

public class WrappedBlockData_v1_15 extends WrappedBlockData<BlockData, Material> {

    public WrappedBlockData_v1_15(IBlockData blockData) {
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
