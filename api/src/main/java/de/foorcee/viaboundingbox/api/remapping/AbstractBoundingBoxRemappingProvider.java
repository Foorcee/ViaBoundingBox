package de.foorcee.viaboundingbox.api.remapping;

import de.foorcee.viaboundingbox.api.versions.WrappedBlockData;
import de.foorcee.viaboundingbox.api.versions.WrappedVoxelShape;
import us.myles.ViaVersion.api.Via;
import us.myles.ViaVersion.api.protocol.ProtocolVersion;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

public abstract class AbstractBoundingBoxRemappingProvider<D,M,V> {
    private Map<Integer, BlockBoundingBoxRemapContainer> blockBoundingBoxRemaps = new HashMap<>();

    public abstract void register();

    public void combine(AbstractBoundingBoxRemappingProvider<D,M, V> other){
        other.blockBoundingBoxRemaps.forEach((id, container) -> {
            BlockBoundingBoxRemapContainer remapContainer = this.blockBoundingBoxRemaps.computeIfAbsent(id, BlockBoundingBoxRemapContainer::new);
            remapContainer.combine(container);
        });
    }

    public void remapBlockBoundingBox(Predicate<Integer> protocolPredicate, D blockData, Function<WrappedBlockData<D,M>, WrappedVoxelShape<V>> mapper) {
        remapBlockBoundingBox(protocolPredicate, data -> data.isEquals(blockData), mapper);
    }

    public void remapBlockBoundingBox(Predicate<Integer> protocolPredicate, Predicate<WrappedBlockData<D,M>> blockDataPredicate, Function<WrappedBlockData<D,M>, WrappedVoxelShape<V>> mapper) {
        ProtocolVersion.getProtocols().stream().map(ProtocolVersion::getId).filter(protocolPredicate).forEach(protocolId -> {
            getRemapContainer(protocolId).remap(blockDataPredicate, mapper);
        });
    }

    public BlockBoundingBoxRemapContainer getRemapContainer(int protocolId) {
        return blockBoundingBoxRemaps.computeIfAbsent(protocolId, BlockBoundingBoxRemapContainer::new);
    }

    public WrappedVoxelShape<V> getRemappedBlockBoundingBox(Object player, WrappedBlockData<D,M> blockData) {
        int id = Via.getAPI().getPlayerVersion(player);
        BlockBoundingBoxRemapContainer remapContainer = blockBoundingBoxRemaps.get(id);
        if (remapContainer != null) {
            return remapContainer.getRemappedShape(blockData);
        }
        return null;
    }

    public Predicate<Integer> equalOrBelow(ProtocolVersion protocolVersion) {
        return id -> id <= protocolVersion.getId();
    }
}
