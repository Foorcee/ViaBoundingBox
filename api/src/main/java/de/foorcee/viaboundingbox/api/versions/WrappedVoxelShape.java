package de.foorcee.viaboundingbox.api.versions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class WrappedVoxelShape<V> {
    @Getter
    private final V voxelShape;
}
