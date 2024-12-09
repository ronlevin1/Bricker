package bricker.gameobjects;

import bricker.main.BrickerGameManager;
import bricker.main.Constants;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.Layer;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * ExtraPaddle is a game object that represents an additional paddle in the
 * game.
 * It handles its own collisions and can be removed after a certain number
 * of collisions.
 */
public class ExtraPaddle extends Paddle {
    private final BrickerGameManager manager;
    private int collisionCounter;

    /**
     * Construct a new ExtraPaddle instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates
     *                      (pixels).
     *                      Note that (0,0) is the top-left corner of the
     *                      window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be
     *                      null, in which case
     *                      the GameObject will not be rendered.
     * @param inputListener The input listener to handle user inputs.
     * @param manager       The game manager responsible for managing game
     *                      objects.
     */
    public ExtraPaddle(Vector2 topLeftCorner, Vector2 dimensions,
                       Renderable renderable,
                       UserInputListener inputListener,
                       BrickerGameManager manager) {
        super(topLeftCorner, dimensions, renderable, inputListener);
        this.manager = manager;
        collisionCounter = 0;
    }

    /**
     * Called when this object collides with another object.
     *
     * @param other     The other game object involved in the collision.
     * @param collision The collision information.
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (other.getTag() == Constants.BORDER_TAG)
            return;
        collisionCounter++;
        System.out.println("extra paddle collision: " + collisionCounter);
        if (removalCondition()) {
            resetExtraPaddle();
        }
    }

    /**
     * Reset the extra paddle by removing it from the game and updating the
     * manager.
     */
    private void resetExtraPaddle() {
        System.out.println(">>> calling manager.removeGameObject() from " +
                "ExtraPaddle class");
        this.collisionCounter = 0;
        // update manager field
        manager.setHasExtraPaddle(false);
        manager.removeGameObject(this, Layer.DEFAULT);
    }

    /**
     * Check if the removal condition for the extra paddle is met.
     *
     * @return True if the extra paddle should be removed, false otherwise.
     */
    public boolean removalCondition() {
        return (this.collisionCounter >= 4);
    }
}
