package bricker.brick_strategies;

import bricker.main.BrickerGameManager;
import danogl.GameObject;

/**
 * A collision strategy that combines two other collision strategies.
 */
public class doubleSpecialStrategy extends BasicCollisionStrategy implements CollisionStrategy {
    private final CollisionStrategy strategy1;
    private final CollisionStrategy strategy2;

    /**
     * Constructs a doubleSpecialStrategy with two collision strategies.
     *
     * @param manager The game manager.
     * @param strategy1 The first collision strategy.
     * @param strategy2 The second collision strategy.
     */
    public doubleSpecialStrategy(BrickerGameManager manager,
                                 CollisionStrategy strategy1,
                                 CollisionStrategy strategy2) {
        super(manager);
        this.strategy1 = strategy1;
        this.strategy2 = strategy2;
    }

    /**
     * Handles the collision by invoking both collision strategies.
     *
     * @param thisObj The object that this strategy is applied to.
     * @param otherObj The other object involved in the collision.
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        strategy1.onCollision(thisObj, otherObj);
        strategy2.onCollision(thisObj, otherObj);
    }
}