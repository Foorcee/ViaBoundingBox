package de.foorcee.viaboundingbox.api.versions;


import java.util.stream.Stream;

public interface ICollisionBridge<P,A,V> {
    boolean checkCollision(P player, A boundingBox);
    Stream<V> checkBlockCollision(P player, A boundingBox);
}
