package de.foorcee.viaboundingbox.api.util;

import lombok.experimental.UtilityClass;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.CodeSource;
import java.security.SecureClassLoader;

@UtilityClass
public class ClassLoaderUtil {

    public Class loadClass(ClassLoader loader, byte[] b, String className) throws InvocationTargetException, IllegalAccessException {
        Method method = null;
        try {
            method = SecureClassLoader.class.getDeclaredMethod("defineClass", String.class, byte[].class, int.class, int.class, CodeSource.class);
        } catch (NoSuchMethodException ex) {
            ex.printStackTrace();
            return null;
        }

        method.setAccessible(true);
        try {
            return (Class) method.invoke(loader, className, b, 0, b.length, null);
        } finally {
            method.setAccessible(false);
        }
    }

    public Class loadClass(ClassLoader loader, String className) throws IOException, InvocationTargetException, IllegalAccessException {
        return loadClass(loader, className, className);
    }

    public  Class loadClass(ClassLoader loader, String from, String to) throws IOException, InvocationTargetException, IllegalAccessException {
        final InputStream inputStream = ClassLoaderUtil.class.getResourceAsStream("/" + from.replace('.', '/') + ".class");
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        int nRead;
        byte[] data = new byte[16384];

        while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, nRead);
        }

        buffer.flush();

        return loadClass(loader, buffer.toByteArray(), to);
    }
}