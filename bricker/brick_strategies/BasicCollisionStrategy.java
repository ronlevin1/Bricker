package bricker.brick_strategies;

import bricker.main.BrickerGameManager;
import danogl.GameObject;

public class BasicCollisionStrategy implements CollisionStrategy {
    private final BrickerGameManager manager;

    public BasicCollisionStrategy(BrickerGameManager manager) {
        this.manager = manager;
    }

    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        System.out.println("collision with brick detected");
        manager.removeGameObject(thisObj);
    }
}
