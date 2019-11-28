package de.foorcee.viaboundingbox.api.remapping;

import de.foorcee.viaboundingbox.api.versions.WrappedBlockData;
import de.foorcee.viaboundingbox.api.versions.WrappedVoxelShape;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

@RequiredArgsConstructor
public class BlockBoundingBoxRemapContainer<D,M,V> {
    private final int protocolId;
    private final Map<WrappedBlockData<D,M>, Function<WrappedBlockData<D,M>, WrappedVoxelShape<V>>> cache = new HashMap<>();
    private final Map<Predicate<WrappedBlockData<D,M>>, Function<WrappedBlockData<D,M>, WrappedVoxelShape<V>>> remaps = new HashMap<>();

    public void remap(Predicate<WrappedBlockData<D,M>> predicate, Function<WrappedBlockData<D,M>, WrappedVoxelShape<V>> mapper) {
        remaps.put(predicate, mapper);
    }

    public WrappedVoxelShape<V> getRemappedShape(WrappedBlockData<D,M> blockData) {
        return this.cache.computeIfAbsent(blockData, this::findRemap).apply(blockData);
    }

    public void combine(BlockBoundingBoxRemapContainer other){
        this.remaps.putAll(other.remaps);
    }

    private Function<WrappedBlockData<D,M>, WrappedVoxelShape<V>> findRemap(WrappedBlockData<D,M> blockData) {
        return remaps.entrySet().stream().filter(entry -> entry.getKey().test(blockData)).findFirst().map(Map.Entry::getValue).orElse(b -> null);
    }
}
