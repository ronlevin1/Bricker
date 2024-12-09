package bricker.brick_strategies;

import bricker.main.BrickerGameManager;
import danogl.GameObject;

/**
 * extraLifeStrategy is a collision strategy that extends
 * BasicCollisionStrategy.
 * It handles the collision of a game object with a brick by creating a
 * falling heart
 * at the position of the brick.
 */
public class extraLifeStrategy extends BasicCollisionStrategy implements CollisionStrategy {
    private BrickerGameManager manager;

    /**
     * Constructs a new extraLifeStrategy.
     *
     * @param manager The game manager responsible for managing game objects.
     */
    public extraLifeStrategy(BrickerGameManager manager) {
        super(manager);
        this.manager = manager;
    }

    /**
     * Handles the collision event between two game objects.
     * If the collision involves a brick, a falling heart is created at the
     * position of the brick.
     *
     * @param thisObj  The game object that this strategy is associated with.
     * @param otherObj The other game object involved in the collision.
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        super.onCollision(thisObj, otherObj);
        manager.createFallingHeart(thisObj.getCenter());
    }
}