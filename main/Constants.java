package bricker.main;

import danogl.util.Vector2;
/**
 * Constants class holds all the constant values used throughout the game.
 * This class cannot be instantiated.
 */
public class Constants {
    //todo: magic numbers
    public static final int Number2 = 2;
    // Game constants
    public static final int BORDER_WIDTH = 200;
    public static final String BORDER_TAG = "border";
    public static final int GAP_SIZE = 5;

    public static final int PADDLE_HEIGHT = 15;
    public static final int PADDLE_WIDTH = 100;
    public static final float PADDLE_MOVEMENT_SPEED = 400;
    public static final String PADDLE_TAG = "paddle";
    public static final String EXTRA_PADDLE_TAG ="extraPaddle";
    public static final int PADDLE_Y_OFFSET = 30;

    public static final int BALL_RADIUS = 20;
    public static final float BALL_SPEED = 250;

    public static final float HEART_SIZE = 30;
    public static final Vector2 HEART_SIZE_VEC = new Vector2(HEART_SIZE,
            HEART_SIZE);
    public static final float HEART_FALL_SPEED = 100;


    public static final int DEFAULT_STARTING_LIFE = 3;
    public static final int DEFAULT_EXTRA_LIFE = 1;
    public static final Vector2 DEFAULT_UI_BAR_TOP_LEFT_CORNER =
            new Vector2(Constants.GAP_SIZE,
                    Constants.WINDOW_HEIGHT - Constants.HEART_SIZE - Constants.GAP_SIZE * 2);


    public static final int DEFAULT_BRICKS_PER_ROW = 8;
    public static final int DEFAULT_NUM_ROWS = 7;
    public static final int BRICK_HEIGHT = 20;

    public static final int WINDOW_WIDTH = 1000;
    public static final int WINDOW_HEIGHT = 700;
    public static final Vector2 WINDOW_DIMENSIONS_VEC =
            new Vector2(WINDOW_WIDTH,
                    WINDOW_HEIGHT);

    public static final String GAME_END_PROMPT_TEMPLATE = "You %s! Play " +
            "again?";

    // todo: delete? object names for factory
//    public static final String BACKGROUND = "BACKGROUND";
//    public static final String BORDERS = "BORDERS";
//    public static final String LIVES_UI_BAR = "LIVES_UI_BAR";
//    public static final String BRICKS = "BRICK";
//    public static final String PADDLE = "PADDLE";
//    public static final String BALL = "BALL";
//    public static final String PUCK = "PUCK";
//    private static final String BACKGROUND = "BACKGROUND";

    // Private constructor to prevent instantiation
    private Constants() {}
}
