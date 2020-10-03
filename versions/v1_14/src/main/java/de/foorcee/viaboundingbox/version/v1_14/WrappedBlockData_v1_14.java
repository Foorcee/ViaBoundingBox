package de.foorcee.viaboundingbox.version.v1_14;

import de.foorcee.viaboundingbox.api.versions.WrappedBlockData;
import net.minecraft.server.v1_14_R1.IBlockData;
import net.minecraft.server.v1_14_R1.IRegistry;
import net.minecraft.server.v1_14_R1.MinecraftKey;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.v1_14_R1.block.data.CraftBlockData;

public class WrappedBlockData_v1_14 extends WrappedBlockData<BlockData, Material> {

    public WrappedBlockData_v1_14(IBlockData blockData) {
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
