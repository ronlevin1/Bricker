package bricker.gameobjects;

import bricker.brick_strategies.CollisionStrategy;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * Brick is a game object that represents a brick in the game.
 * It handles its own collisions and keeps track of the number of bricks.
 */
public class Brick extends GameObject {
    // fields
    private final CollisionStrategy collisionStrategy;
    private static final danogl.util.Counter numberOfBricks =
            new danogl.util.Counter(0);

    /**
     * Constructs a new Brick instance.
     *
     * @param topLeftCorner     Position of the object, in window
     *                          coordinates (pixels).
     *                          Note that (0,0) is the top-left corner of
     *                          the window.
     * @param dimensions        Width and height in window coordinates.
     * @param renderable        The renderable representing the object. Can
     *                          be null, in which case
     *                          the GameObject will not be rendered.
     * @param collisionStrategy The strategy to use for handling collisions.
     */
    public Brick(Vector2 topLeftCorner, Vector2 dimensions,
                 Renderable renderable,
                 CollisionStrategy collisionStrategy) {
        super(topLeftCorner, dimensions, renderable);
        this.collisionStrategy = collisionStrategy;
        numberOfBricks.increment();
        System.out.println("Ctor - NumberOfBricks(): " + numberOfBricks.value());
    }

    /**
     * Called when this object collides with another object.
     *
     * @param other     The other game object involved in the collision.
     * @param collision The collision information.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
//        super.onCollisionEnter(other, collision);
        collisionStrategy.onCollision(this, other);
    }

    /**
     * Decrement the number of bricks by one.
     */
    public static void decrementBrickCount() {
        numberOfBricks.decrement();
    }

    /**
     * Get the current number of bricks.
     *
     * @return The number of bricks.
     */
    public static int getNumberOfBricks() {
        return numberOfBricks.value();
    }

    /**
     * Reset the number of bricks to zero.
     */
    public static void resetBricks() {
        numberOfBricks.reset();
    }
}
