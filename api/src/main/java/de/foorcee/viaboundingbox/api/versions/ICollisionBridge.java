package de.foorcee.viaboundingbox.api.versions;


public interface ICollisionBridge<P,A> {
    boolean checkCollision(P player, A boundingBox);
}
