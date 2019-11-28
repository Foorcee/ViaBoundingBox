package de.foorcee.viaboundingbox.common.asm;

import de.foorcee.viaboundingbox.api.versions.ICollisionBridge;

public class BoundingBoxCollisionBridge {
    public static ICollisionBridge injector;

    public static <P,A> boolean checkCollision(P player,A boundingBox) {
        return injector.checkCollision(player, boundingBox);
    }
}
