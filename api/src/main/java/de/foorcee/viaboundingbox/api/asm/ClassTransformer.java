package de.foorcee.viaboundingbox.api.asm;

import lombok.Getter;

@Getter
public class ClassTransformer {
    private final String className;
    private final MethodTransformer[] methodTransformers;

    public ClassTransformer(String className, MethodTransformer... methodTransformers) {
        this.className = className;
        this.methodTransformers = methodTransformers;
    }
}
