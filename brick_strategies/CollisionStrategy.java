package bricker.brick_strategies;

import danogl.GameObject;
/**
 * CollisionStrategy is an interface that defines the method to handle collision events
 * between game objects.
 */
public interface CollisionStrategy {
    /**
     * Handles the collision event between two game objects.
     *
     * @param thisObj  The game object that this strategy is associated with.
     * @param otherObj The other game object involved in the collision.
     */
    void onCollision(GameObject thisObj, GameObject otherObj);
}
