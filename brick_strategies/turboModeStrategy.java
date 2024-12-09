package bricker.brick_strategies;

import bricker.gameobjects.Ball;
import bricker.main.BrickerGameManager;
import danogl.GameObject;

/**
 * turboModeStrategy is a collision strategy that extends
 * BasicCollisionStrategy.
 * It handles the collision of a game object with a brick by activating
 * turbo mode
 * if the collision involves a ball.
 */
public class turboModeStrategy extends BasicCollisionStrategy implements CollisionStrategy {
    private final BrickerGameManager manager;

    /**
     * Constructs a new turboModeStrategy.
     *
     * @param manager The game manager responsible for managing game objects.
     */
    public turboModeStrategy(BrickerGameManager manager) {
        super(manager);
        this.manager = manager;
    }

    /**
     * Handles the collision event between two game objects.
     * If the collision involves a ball, turbo mode is activated.
     *
     * @param thisObj  The game object that this strategy is associated with.
     * @param otherObj The other game object involved in the collision.
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        super.onCollision(thisObj, otherObj);
        //  check if collided with BALL and not PUCKs
        // todo: change?
        if (otherObj instanceof Ball) {
            manager.createTurboMode();
        }
    }
}
