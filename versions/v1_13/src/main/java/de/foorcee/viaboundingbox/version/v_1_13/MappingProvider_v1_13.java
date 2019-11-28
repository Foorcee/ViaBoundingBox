package de.foorcee.viaboundingbox.version.v_1_13;

import de.foorcee.viaboundingbox.api.remapping.AbstractBoundingBoxRemappingProvider;
import de.foorcee.viaboundingbox.api.versions.WrappedVoxelShape;
import de.foorcee.viaboundingbox.api.versions.WrappedVoxelShapes;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.SeaPickle;

import static us.myles.ViaVersion.api.protocol.ProtocolVersion.*;

@RequiredArgsConstructor
public class MappingProvider_v1_13 extends AbstractBoundingBoxRemappingProvider<BlockData, Material, WrappedVoxelShape> {

    private Material[] SHULKER_BOXES = new Material[]{Material.SHULKER_BOX, Material.WHITE_SHULKER_BOX, Material.ORANGE_SHULKER_BOX, Material.MAGENTA_SHULKER_BOX, Material.LIGHT_BLUE_SHULKER_BOX, Material.YELLOW_SHULKER_BOX, Material.GREEN_SHULKER_BOX, Material.PINK_SHULKER_BOX, Material.GRAY_SHULKER_BOX, Material.LIGHT_GRAY_SHULKER_BOX, Material.CYAN_SHULKER_BOX, Material.PURPLE_SHULKER_BOX, Material.BLUE_SHULKER_BOX, Material.BROWN_SHULKER_BOX, Material.GREEN_SHULKER_BOX, Material.RED_SHULKER_BOX, Material.BLACK_SHULKER_BOX};

    @Override
    public void register() {
        remapBlockBoundingBox(equalOrBelow(v1_11), data -> data.isBlock(SHULKER_BOXES), blockData -> WrappedVoxelShapes.createVoxelShape(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.875D, 0.9375D) /* Enderchest  */);
        remapBlockBoundingBox(equalOrBelow(v1_12_2), data -> data.isBlock(Material.SEA_PICKLE), blockData -> CocoaShapes.getShape(blockData.getBlockData()));
    }

    private static final class CocoaShapes{
        private static final WrappedVoxelShape[] east = new WrappedVoxelShape[]{a(11.0D, 7.0D, 6.0D, 15.0D, 12.0D, 10.0D), a(9.0D, 5.0D, 5.0D, 15.0D, 12.0D, 11.0D), a(7.0D, 3.0D, 4.0D, 15.0D, 12.0D, 12.0D)};
        private static final WrappedVoxelShape[] west = new WrappedVoxelShape[]{a(1.0D, 7.0D, 6.0D, 5.0D, 12.0D, 10.0D), a(1.0D, 5.0D, 5.0D, 7.0D, 12.0D, 11.0D), a(1.0D, 3.0D, 4.0D, 9.0D, 12.0D, 12.0D)};
        private static final WrappedVoxelShape[] north = new WrappedVoxelShape[]{a(6.0D, 7.0D, 1.0D, 10.0D, 12.0D, 5.0D), a(5.0D, 5.0D, 1.0D, 11.0D, 12.0D, 7.0D), a(4.0D, 3.0D, 1.0D, 12.0D, 12.0D, 9.0D)};
        private static final WrappedVoxelShape[] south = new WrappedVoxelShape[]{a(6.0D, 7.0D, 11.0D, 10.0D, 12.0D, 15.0D), a(5.0D, 5.0D, 9.0D, 11.0D, 12.0D, 15.0D), a(4.0D, 3.0D, 7.0D, 12.0D, 12.0D, 15.0D)};

        private static WrappedVoxelShape a(double x1, double y1, double z1, double x2, double y2, double z2){
            return WrappedVoxelShapes.createVoxelShape(x1, y1, z1, x2, y2, z2);
        }

        public static WrappedVoxelShape getShape(BlockData data){
            SeaPickle ageable = (SeaPickle) data;
            int age = getNewAge(ageable.getPickles());
            return north[age];
        }

        private static int getNewAge(int age){
            if(age == 0 || age == 1) return 0;
            if(age == 2) return 1;
            return 2;
        }
    }
}
