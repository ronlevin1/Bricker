package bricker.gameobjects;

import bricker.main.Constants;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.ImageReader;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.util.Random;

/**
 * Ball is a game object that represents a ball in the game.
 * It handles its own collisions, movement, and turbo mode.
 */
public class Ball extends GameObject {
    private final Sound collisionSound;
    private int collisionCounter;
    private boolean isTurboOn;
    private boolean isTurboOffCalled;
    private ImageReader imageReader;

    /**
     * Construct a new Ball instance.
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
     */
    public Ball(Vector2 topLeftCorner, Vector2 dimensions,
                Renderable renderable, Sound collisionSound) {
        super(topLeftCorner, dimensions, renderable);
        this.collisionSound = collisionSound;
        this.isTurboOn = false;
        this.isTurboOffCalled = false;
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
        // set velocity
        Vector2 newVel = getVelocity().flipped(collision.getNormal());
        setVelocity(newVel);
        collisionCounter++;
        collisionSound.play();
        System.out.println(">>> ball velocity: " + this.getVelocity());
//        if (this.isTurboOn) {
//            System.out.println(">>> REDBALL collision: " +
//                    this.collisionCounter);
//        }
        // check if turbo mode should be turned off
        if (this.isTurboOn && this.getCollisionCounter() >= 6) {
            setTurboOff();
        }
    }

    /**
     * Get the number of collisions this ball has had.
     *
     * @return The collision counter.
     */
    public int getCollisionCounter() {
        return collisionCounter;
    }

    /**
     * Reset the collision counter to zero.
     */
    private void resetCollisionCounter() {
        this.collisionCounter = 0;
    }

    /**
     * Reset the ball to its initial state.
     */
    public void resetBall() {
        float ballVelX = Constants.BALL_SPEED;
        float ballVelY = Constants.BALL_SPEED;
        Random rand = new Random();

        if (rand.nextBoolean())
            ballVelX *= -1;
        if (rand.nextBoolean())
            ballVelY *= -1;

        this.setVelocity(new Vector2(ballVelX, ballVelY));
        if (this.isTurboOn)
            setBallVelocityToTurbo();
        this.setCenter(Constants.WINDOW_DIMENSIONS_VEC.mult(0.5f));
//        ball.setCenter(windowDimensions.mult(0.5f));
    }

    /**
     * Activate turbo mode for the ball.
     *
     * @param imageReader The image reader to use for changing the ball's
     *                    image.
     */
    public void setTurboOn(ImageReader imageReader) {
        if (!this.isTurboOn) {
            System.out.println(">>> ACTIVATING TURBO MODE");
            this.isTurboOn = true;
            this.isTurboOffCalled = false; // Reset the flag
            this.imageReader = imageReader;
            setBallVelocityToTurbo();
            // set ball red
            Renderable redBallImage = imageReader.readImage("assets/redball" +
                            ".png",
                    true);
            this.renderer().setRenderable(redBallImage);
            // for counting 6 collision. after that returns ball to normal
            this.resetCollisionCounter();
        }
    }

    /**
     * Set the ball's velocity to turbo mode.
     */
    private void setBallVelocityToTurbo() {
        // set ball vel *= 1.4
        Vector2 oldVel = this.getVelocity();
        this.setVelocity(oldVel.mult(1.4f));
    }

    /**
     * Deactivate turbo mode for the ball.
     */
    private void setTurboOff() {
        if (this.isTurboOn && !this.isTurboOffCalled) {
            System.out.println(">>> TURBO MODE OFF");
            // normalize velocity vector, then multiply by original speed.
//            Vector2 turboVel = this.getVelocity().normalized();
//            this.setVelocity(turboVel.mult(Constants.BALL_SPEED));
            // reset ball pic
            Renderable regularBallImage = this.imageReader.readImage("assets" +
                            "/ball" +
                            ".png",
                    true);
            this.renderer().setRenderable(regularBallImage);
            this.isTurboOn = false;
            this.isTurboOffCalled = true; // Set the flag
            // reset ball vel by /= 1.4
            Vector2 turboVel = this.getVelocity();
            float divFactor = 1.0f / 1.4f;
            this.setVelocity(turboVel.mult(divFactor));
        }
    }
}
