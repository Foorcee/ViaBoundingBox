package de.foorcee.viaboundingbox.common.asm;

import de.foorcee.viaboundingbox.api.versions.ICollisionBridge;

import java.util.stream.Stream;

public class BoundingBoxCollisionBridge {
    public static ICollisionBridge injector;

    public static <P,A> boolean checkCollision(P player,A boundingBox) {
        return injector.checkCollision(player, boundingBox);
    }

    public static <P,A,V> Stream<V> checkBlockCollision(P player, A boundingBox) {
        return injector.checkBlockCollision(player, boundingBox);
    }
}
