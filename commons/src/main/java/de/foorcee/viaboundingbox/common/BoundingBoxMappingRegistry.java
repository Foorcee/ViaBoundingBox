package de.foorcee.viaboundingbox.common;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import de.foorcee.viaboundingbox.api.remapping.AbstractBoundingBoxRemappingProvider;
import de.foorcee.viaboundingbox.api.versions.AbstractBoundingBoxInjector;
import de.foorcee.viaboundingbox.version.v_1_13.BoundingBox_v1_13;
import de.foorcee.viaboundingbox.version.v_1_13.MappingProvider_v1_13;
import de.foorcee.viaboundingbox.version.v_1_14.BoundingBox_v1_14;
import de.foorcee.viaboundingbox.version.v_1_14.MappingProvider_v1_14;

public class BoundingBoxMappingRegistry<T extends AbstractBoundingBoxRemappingProvider> {
    private static Multimap<Class, Class> registeredRemappingProvider = ArrayListMultimap.create();

    static {
        register(MappingProvider_v1_14.class, BoundingBox_v1_14.class);
        register(MappingProvider_v1_13.class, BoundingBox_v1_13.class, BoundingBox_v1_14.class);
    }

    public static <T extends AbstractBoundingBoxRemappingProvider> void register(Class<T> mappingProvider, Class... iClass){
        for (Class aClass : iClass) {
            registeredRemappingProvider.put(aClass, mappingProvider);
        }
    }

    public static AbstractBoundingBoxRemappingProvider loadMappings(AbstractBoundingBoxInjector injector){
        Class clazz = injector.getClass();
        AbstractBoundingBoxRemappingProvider provider = null;
        for (Class aClass : registeredRemappingProvider.get(clazz)) {
            AbstractBoundingBoxRemappingProvider loadedMappingProvider = loadMapping(aClass);
            if(loadedMappingProvider == null) continue;
            if(provider != null){
                provider.combine(loadedMappingProvider);
            }else{
                provider = loadedMappingProvider;
            }
        }
        return provider;
    }

    private static AbstractBoundingBoxRemappingProvider loadMapping(Class mappingsClass){
        try {
            AbstractBoundingBoxRemappingProvider remappingProvider = (AbstractBoundingBoxRemappingProvider) mappingsClass.newInstance();
            remappingProvider.register();
            return remappingProvider;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
