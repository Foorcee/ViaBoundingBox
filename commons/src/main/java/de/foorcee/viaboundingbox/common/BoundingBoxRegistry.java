package de.foorcee.viaboundingbox.common;

import de.foorcee.viaboundingbox.api.versions.AbstractBoundingBoxInjector;
import de.foorcee.viaboundingbox.version.v_1_13.BoundingBox_v1_13;
import de.foorcee.viaboundingbox.version.v_1_14.BoundingBox_v1_14;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BoundingBoxRegistry<T extends AbstractBoundingBoxInjector> {

    private static Map<String, Class> registeredBoundingBoxes = new HashMap<>();

    static {
        register(BoundingBox_v1_14.class, "v1_14_R1");
        register(BoundingBox_v1_13.class, "v1_13_R2");
    }

    public static <T extends AbstractBoundingBoxInjector> void register(Class<T> clazz, String... versions){
        for (String version : versions) {
            registeredBoundingBoxes.put(version, clazz);
        }
    }

    public static Set<String> getVersions(){
        return registeredBoundingBoxes.keySet();
    }

    public static AbstractBoundingBoxInjector getInjector(String serverVersion){
        Class clazz = registeredBoundingBoxes.get(serverVersion);
        if(clazz == null) return null;
        try {
            AbstractBoundingBoxInjector injector = (AbstractBoundingBoxInjector) clazz.newInstance();
            injector.setRemappingProvider(BoundingBoxMappingRegistry.loadMappings(injector));
            return injector;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

}
