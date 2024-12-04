package bricker.gameobjects;

import danogl.util.Vector2;

public class Constants {
    // Game constants
    public static final int BORDER_WIDTH = 10;
    public static final int GAP_SIZE = 5;

    public static final int PADDLE_HEIGHT = 15;
    public static final int PADDLE_WIDTH = 100;
    public static final float PADDLE_MOVEMENT_SPEED = 400;

    public static final int BALL_RADIUS = 20;
    public static final float BALL_SPEED = 300;

    public static final float HEART_SIZE = 30;
    public static final Vector2 HEART_SIZE_VEC = new Vector2(HEART_SIZE,
            HEART_SIZE);

    public static final int DEFAULT_BRICKS_PER_ROW = 8;
    public static final int DEFAULT_NUM_ROWS = 7;

    public static final int WINDOW_WIDTH = 1000;
    public static final int WINDOW_HEIGHT = 700;

    public static final String GAME_END_PROMPT_TEMPLATE = "You %s! Play " +
            "again?";

    private Constants() {
        // Prevent instantiation
    }
}
