package bricker.brick_strategies;

import bricker.gameobjects.Brick;
import bricker.main.BrickerGameManager;
import danogl.GameObject;
import danogl.collisions.Layer;

/**
 * BasicCollisionStrategy is a simple implementation of the
 * CollisionStrategy interface.
 * It handles the collision of a game object with a brick by removing the
 * brick from the game
 * and decrementing the brick count.
 */
public class BasicCollisionStrategy implements CollisionStrategy {
    private final BrickerGameManager manager;

    /**
     * Constructs a new BasicCollisionStrategy.
     *
     * @param manager The game manager responsible for managing game objects.
     */
    public BasicCollisionStrategy(BrickerGameManager manager) {
        this.manager = manager;
    }

    /**
     * Handles the collision event between two game objects.
     * If the collision involves a brick, the brick is removed from the game
     * and the brick count is decremented.
     *
     * @param thisObj  The game object that this strategy is associated with.
     * @param otherObj The other game object involved in the collision.
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        System.out.println("collision with brick detected");
//        manager.removeGameObject(thisObj, Layer.STATIC_OBJECTS);
        boolean removed = manager.removeGameObject(thisObj,
                Layer.STATIC_OBJECTS);
        if (removed) {
            Brick.decrementBrickCount();
        }
    }
}
