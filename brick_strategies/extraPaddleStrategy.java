package bricker.brick_strategies;

import bricker.main.BrickerGameManager;
import danogl.GameObject;

/**
 * extraPaddleStrategy is a collision strategy that extends
 * BasicCollisionStrategy.
 * It handles the collision of a game object with a brick by creating an
 * extra paddle
 * in the game.
 */
public class extraPaddleStrategy extends BasicCollisionStrategy implements CollisionStrategy {
    private final BrickerGameManager manager;

    /**
     * Constructs a new extraPaddleStrategy.
     *
     * @param manager The game manager responsible for managing game objects.
     */
    public extraPaddleStrategy(BrickerGameManager manager) {
        super(manager);
        this.manager = manager;
    }

    /**
     * Handles the collision event between two game objects.
     * If the collision involves a brick, an extra paddle is created in the
     * game.
     *
     * @param thisObj  The game object that this strategy is associated with.
     * @param otherObj The other game object involved in the collision.
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        super.onCollision(thisObj, otherObj);
        manager.createExtraPaddle();
    }
}
