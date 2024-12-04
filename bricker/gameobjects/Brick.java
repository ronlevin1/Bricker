package bricker.gameobjects;

import bricker.gameobjects.Constants;
import bricker.brick_strategies.CollisionStrategy;
import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

public class Brick extends GameObject {
    private final CollisionStrategy collisionStrategy;
    private static final danogl.util.Counter numberOfBricks =
            new danogl.util.Counter(0);

    public Brick(Vector2 topLeftCorner, Vector2 dimensions,
                 Renderable renderable,
                 CollisionStrategy collisionStrategy) {
        super(topLeftCorner, dimensions, renderable);
        this.collisionStrategy = collisionStrategy;
        numberOfBricks.increment();
        System.out.println("Ctor - NumberOfBricks(): " + numberOfBricks.value());
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
//        super.onCollisionEnter(other, collision);
        collisionStrategy.onCollision(this, other);
        numberOfBricks.decrement();
    }

    public static int getNumberOfBricks() {
        return (int) numberOfBricks.value();
    }

    public static void resetBricks() {
        numberOfBricks.reset();
    }
}
