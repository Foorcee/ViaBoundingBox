package de.foorcee.viaboundingbox.api.asm;

import lombok.Getter;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

@Getter
public abstract class MethodTransformer {
    private String name, description;

    public MethodTransformer(String name, String description){
        this.name = name;
        this.description = description;
    }

    public abstract void transform(ClassNode classNode, MethodNode methodNode);
}
