package bricker.brick_strategies;

import bricker.main.BrickerGameManager;
import danogl.GameObject;

/**
 * extraBallsStrategy is a collision strategy that extends
 * BasicCollisionStrategy.
 * It handles the collision of a game object with a brick by creating two
 * additional pucks
 * at the position of the brick.
 */
public class extraBallsStrategy extends BasicCollisionStrategy implements CollisionStrategy {
    private final BrickerGameManager manager;

    /**
     * Constructs a new extraBallsStrategy.
     *
     * @param manager The game manager responsible for managing game objects.
     */
    public extraBallsStrategy(BrickerGameManager manager) {
        super(manager);
        this.manager = manager;
    }

    /**
     * Handles the collision event between two game objects.
     * If the collision involves a brick, two additional pucks are created
     * at the position of the brick.
     *
     * @param thisObj  The game object that this strategy is associated with.
     * @param otherObj The other game object involved in the collision.
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        super.onCollision(thisObj, otherObj);
        thisObj.getCenter(); //thisObj is brick
        manager.createPuck(thisObj.getCenter());
        manager.createPuck(thisObj.getCenter());
    }
}
