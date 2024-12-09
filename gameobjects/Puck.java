package bricker.gameobjects;

import bricker.main.BrickerGameManager;
import bricker.main.Constants;
import danogl.collisions.Layer;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.util.Random;

/**
 * Puck is a game object that represents a puck in the game.
 * It extends the Ball class and handles its own collisions and movement.
 */
public class Puck extends Ball {
    private final BrickerGameManager manager;
    private Vector2 centerVec;

    /**
     * Construct a new Puck instance.
     *
     * @param topLeftCorner  Position of the object, in window coordinates
     *                       (pixels).
     *                       Note that (0,0) is the top-left corner of the
     *                       window.
     * @param dimensions     Width and height in window coordinates.
     * @param renderable     The renderable representing the object. Can be
     *                       null, in which case
     *                       the GameObject will not be rendered.
     * @param collisionSound The sound to play upon collision.
     * @param manager        The game manager responsible for managing game
     *                       objects.
     */
    public Puck(Vector2 topLeftCorner, Vector2 dimensions,
                Renderable renderable, Sound collisionSound,
                BrickerGameManager manager) {
        super(topLeftCorner, dimensions, renderable, collisionSound);
        float x = topLeftCorner.x() + dimensions.x() / Constants.Number2;
        float y = topLeftCorner.y() + dimensions.y() / Constants.Number2;
        this.centerVec = new Vector2(x, y); // center coordinates of brick
        this.manager = manager;
        resetPuck();
    }

    /**
     * Reset the puck to its initial state.
     */
    private void resetPuck() {
        Random rand = new Random();
        double angle = rand.nextDouble() * Math.PI;
        float ballVelY = (float) Math.cos(angle) * Constants.BALL_SPEED;
        float ballVelX = (float) Math.sin(angle) * Constants.BALL_SPEED;

        this.setVelocity(new Vector2(ballVelX, ballVelY));
        this.setCenter(this.centerVec);
    }

    /**
     * Check if the removal condition for the puck is met.
     *
     * @return True if the puck should be removed, false otherwise.
     */
    public boolean removalCondition() {
        return (this.getCenter().y() >= Constants.WINDOW_HEIGHT);
    }

    /**
     * Update the state of the puck.
     *
     * @param deltaTime The time elapsed since the last update.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if (removalCondition()) {
            System.out.println(">>> calling manager.removeGameObject() from " +
                    "Puck class");
            this.manager.removeGameObject(this, Layer.DEFAULT);
        }
    }
}
