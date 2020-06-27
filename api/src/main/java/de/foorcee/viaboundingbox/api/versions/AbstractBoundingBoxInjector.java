package de.foorcee.viaboundingbox.api.versions;

import de.foorcee.viaboundingbox.api.asm.ClassTransformer;
import de.foorcee.viaboundingbox.api.remapping.AbstractBoundingBoxRemappingProvider;
import lombok.Getter;
import lombok.Setter;

public abstract class AbstractBoundingBoxInjector<P,A,V,D,M> {

    @Getter@Setter
    private AbstractBoundingBoxRemappingProvider<D,M,V> remappingProvider;

    public abstract ClassTransformer[] getClassTransformers();

    public abstract ICollisionBridge<P, A, V> getBridge();
    public abstract WrappedVoxelShapes<V> getVoxelShapes();
}
