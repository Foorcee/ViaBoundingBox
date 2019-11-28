package de.foorcee.viaboundingbox.api;

import de.foorcee.viaboundingbox.api.versions.AbstractBoundingBoxInjector;
import lombok.Getter;
import lombok.Setter;

public abstract class ViaBoundingBoxModule {
    @Getter@Setter
    private static ViaBoundingBoxModule instance;
    public abstract AbstractBoundingBoxInjector getInjector();
    public static ViaBoundingBoxModule getModule(){
        return instance;
    }
}
