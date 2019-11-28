package de.foorcee.viaboundingbox.bukkit;

import de.foorcee.viaboundingbox.api.ViaBoundingBoxModule;
import de.foorcee.viaboundingbox.api.versions.AbstractBoundingBoxInjector;
import de.foorcee.viaboundingbox.common.asm.PlayerConnectionInjector;
import org.bukkit.Bukkit;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class ViaBoundingBoxBukkitModule extends ViaBoundingBoxModule {

    private AbstractBoundingBoxInjector injector;

    public ViaBoundingBoxBukkitModule(AbstractBoundingBoxInjector injector) throws IllegalAccessException, IOException, InvocationTargetException {
        this.injector = injector;
        new PlayerConnectionInjector(injector).inject(Bukkit.class.getClassLoader());
        setInstance(this);
    }

    @Override
    public AbstractBoundingBoxInjector getInjector() {
        return injector;
    }
}
