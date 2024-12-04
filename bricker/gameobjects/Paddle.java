package bricker.gameobjects;

import danogl.GameObject;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;

public class Paddle extends GameObject {

    private UserInputListener inputListener;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates
     *                      (pixels).
     *                      Note that (0,0) is the top-left corner of the
     *                      window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be
     *                      null, in which case
     * @param inputListener
     */
    public Paddle(Vector2 topLeftCorner, Vector2 dimensions,
                  Renderable renderable,
                  UserInputListener inputListener) {
        super(topLeftCorner, dimensions, renderable);
        this.inputListener = inputListener;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        Vector2 movementDir = Vector2.ZERO;

        if (inputListener.isKeyPressed(KeyEvent.VK_LEFT)) {
            movementDir = movementDir.add(Vector2.LEFT);
        }
        if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT)) {
            movementDir = movementDir.add(Vector2.RIGHT);
        }
        if (this.getTopLeftCorner().x() <= 0) {
            this.setTopLeftCorner(new Vector2(0, this.getTopLeftCorner().y()));
        }
        if (this.getTopLeftCorner().x() >= Constants.WINDOW_WIDTH - this.getDimensions().x()) {
            this.setTopLeftCorner(new Vector2(Constants.WINDOW_WIDTH - this.getDimensions().x()
                    , this.getTopLeftCorner().y()));
        }
        setVelocity(movementDir.mult(Constants.PADDLE_MOVEMENT_SPEED));
    }
}
