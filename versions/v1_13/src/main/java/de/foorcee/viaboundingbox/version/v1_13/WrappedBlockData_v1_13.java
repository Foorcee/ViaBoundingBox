package de.foorcee.viaboundingbox.version.v1_13;

import de.foorcee.viaboundingbox.api.versions.WrappedBlockData;
import net.minecraft.server.v1_13_R2.IBlockData;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.v1_13_R2.block.data.CraftBlockData;

public class WrappedBlockData_v1_13 extends WrappedBlockData<BlockData, Material> {

    public WrappedBlockData_v1_13(IBlockData blockData) {
        super(CraftBlockData.fromData(blockData));
    }

    @Override
    public boolean isEquals(BlockData data) {
        return getBlockData().equals(data);
    }

    @Override
    public boolean isBlock(Material material) {
        return getBlockData().getMaterial() == material;
    }
}
