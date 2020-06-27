package de.foorcee.viaboundingbox.common.asm;

import de.foorcee.viaboundingbox.api.asm.ByteCodeUtils;
import de.foorcee.viaboundingbox.api.asm.ClassTransformer;
import de.foorcee.viaboundingbox.api.util.ClassLoaderUtil;
import de.foorcee.viaboundingbox.api.versions.AbstractBoundingBoxInjector;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class PlayerConnectionInjector {

    private AbstractBoundingBoxInjector injector;

    public PlayerConnectionInjector(AbstractBoundingBoxInjector injector){
        this.injector = injector;
    }

    public void inject(ClassLoader classLoader) throws IOException, InvocationTargetException, IllegalAccessException {

        ClassTransformer[] classTransformers = injector.getClassTransformers();

        for (ClassTransformer classTransformer : classTransformers) {
           byte[] classData = ByteCodeUtils.injectClass(classTransformer);
            ClassLoaderUtil.loadClass(classLoader, classData, classTransformer.getClassName());
        }

        try {
            Class clazz = classLoader.loadClass("de.foorcee.viaboundingbox.common.asm.BoundingBoxCollisionBridge");
            Field field = clazz.getField("injector");
            field.set(null, injector.getBridge());
        } catch (NoSuchFieldException | ClassNotFoundException | IllegalAccessException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void loadClasses(ClassLoader loader) throws IOException, InvocationTargetException, IllegalAccessException {
        ClassLoaderUtil.loadClass(loader, "de.foorcee.viaboundingbox.api.versions.ICollisionBridge");
        ClassLoaderUtil.loadClass(loader, "de.foorcee.viaboundingbox.common.asm.BoundingBoxCollisionBridge");
    }
}
