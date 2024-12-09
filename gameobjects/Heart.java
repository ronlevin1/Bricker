package bricker.gameobjects;

import bricker.main.BrickerGameManager;
import bricker.main.Constants;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.Layer;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * Heart is a game object that represents a heart in the game.
 * It handles its own collisions and can be removed based on certain
 * conditions.
 */
public class Heart extends GameObject {

    /**
     * Construct a new Heart instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates
     *                      (pixels).
     * Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be
     *                      null, in which case
     * the GameObject will not be rendered.
     * @param manager       The game manager responsible for managing game
     *                      objects.
     */
    private BrickerGameManager manager;

    public Heart(Vector2 topLeftCorner, Vector2 dimensions,
                 Renderable renderable, BrickerGameManager manager) {
        super(topLeftCorner, dimensions, renderable);
        this.manager = manager;
    }

    /**
     * Determine if this object should collide with another object.
     *
     * @param other The other game object.
     * @return True if the other object is a paddle, false otherwise.
     */
    @Override
    public boolean shouldCollideWith(GameObject other) {
        return other.getTag().equals(Constants.PADDLE_TAG);
    }

    /**
     * Called when this object collides with another object.
     *
     * @param other     The other game object involved in the collision.
     * @param collision The collision information.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        if (shouldCollideWith(other)) {
            // if other is main paddle
            manager.incrementLife();
            manager.removeGameObject(this, Layer.DEFAULT);
        }
    }

    /**
     * Update the state of the heart.
     *
     * @param deltaTime The time elapsed since the last update.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (removalCondition()) {
            manager.removeGameObject(this, Layer.DEFAULT);
        }
    }

    /**
     * Check if the removal condition for the heart is met.
     *
     * @return True if the heart should be removed, false otherwise.
     */
    public boolean removalCondition() {
        return (this.getTopLeftCorner().y() > Constants.WINDOW_HEIGHT);
    }
}
