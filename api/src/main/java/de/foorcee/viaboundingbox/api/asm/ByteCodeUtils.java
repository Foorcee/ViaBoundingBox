package de.foorcee.viaboundingbox.api.asm;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import java.io.IOException;

public class ByteCodeUtils {
    public static byte[] injectClass(ClassLoader classLoader, ClassTransformer classTransformer) throws IOException {
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        ClassNode classNode = new ClassNode();
        ClassReader reader =  new ClassReader(ClassLoader.getSystemResourceAsStream(classTransformer.getClassName().replace('.', '/') + ".class"));
        reader.accept(classNode, 0);

        for (Object object : classNode.methods) {
            MethodNode method = (MethodNode) object;
            for (MethodTransformer mt : classTransformer.getMethodTransformers()) {
                if(method.name.equals(mt.getName()) && method.desc.equals(mt.getDescription())){
                    mt.transform(classNode, method);
                }
            }
        }

        classNode.accept(classWriter);
        return classWriter.toByteArray();
    }
}
