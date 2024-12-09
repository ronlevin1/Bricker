package bricker.main;

import bricker.brick_strategies.*;
import bricker.gameobjects.*;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.*;
import danogl.gui.rendering.Renderable;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Random;

/**
 * The BrickerGameManager class manages the game logic and state for the
 * Bricker game.
 * It extends the GameManager class from the danogl library.
 */
public class BrickerGameManager extends GameManager {
    // todo: remove debugging-prints from all over the project.
    // constants
    private static final int VISIBLE_BORDER_WIDTH = 10; // dont touch!
    // todo: remove constants and pull from the file?
//    private static final Renderable BORDER_RENDERABLE =
//            new RectangleRenderable(new Color(80, 140, 250));

//    private static final int PADDLE_HEIGHT = 15;
//    private static final int PADDLE_WIDTH = 100;

//    private static final int BALL_RADIUS = 20;
//    private static final float BALL_SPEED = 300;

//    private static final int DEFAULT_NUM_ROWS = 7;
//    private static final int DEFAULT_BRICKS_PER_ROW = 8;

    //    private static final int DEFAULT_STARTING_LIFE = 3;
//    private static final int DEFAULT_EXTRA_LIFE = 1;
//    private static final Vector2 DEFAULT_UI_BAR_TOP_LEFT_CORNER =
//            new Vector2(Constants.GAP_SIZE,
//                    Constants.WINDOW_HEIGHT - Constants.HEART_SIZE -
//                    Constants.GAP_SIZE * 2);
    // fields
    private Vector2 windowDimensions;
    private WindowController windowController;
    private UserInputListener inputListener;
    private ImageReader imageReader;
    private SoundReader soundReader;
    //todo: fix or delete everything associated with factory
//    private GameObjectFactory gameObjectFactory;

    private final int bricksPerRow;
    private final int numOfRows;

    private bricker.gameobjects.Ball ball;
    private boolean hasExtraPaddle = false;
    private int lifeCounter;
    private final GameObject[] lifeUIObjectsArr =
            new GameObject[Constants.DEFAULT_STARTING_LIFE + Constants.DEFAULT_EXTRA_LIFE + 1];

    /**
     * Constructor for the BrickerGameManager class.
     *
     * @param windowTitle      The title of the game window.
     * @param windowDimensions The dimensions of the game window.
     * @param bricksPerRow     The number of bricks per row.
     * @param numOfRows        The number of rows of bricks.
     */
    public BrickerGameManager(String windowTitle, Vector2 windowDimensions,
                              int bricksPerRow, int numOfRows) {
        super(windowTitle, windowDimensions);
        this.bricksPerRow = bricksPerRow;
        this.numOfRows = numOfRows;
        this.lifeCounter = Constants.DEFAULT_STARTING_LIFE; //todo change by
        // input?
    }

    /**
     * Initializes the game by setting up the game objects and their initial
     * states.
     *
     * @param imageReader      The ImageReader instance for loading images.
     * @param soundReader      The SoundReader instance for loading sounds.
     * @param inputListener    The UserInputListener instance for handling
     *                         user input.
     * @param windowController The WindowController instance for controlling
     *                         the game window.
     */
    @Override
    public void initializeGame(ImageReader imageReader,
                               SoundReader soundReader,
                               UserInputListener inputListener,
                               WindowController windowController) {

        super.initializeGame(imageReader, soundReader, inputListener,
                windowController);
        this.windowController = windowController;
        this.windowDimensions = windowController.getWindowDimensions();
        this.imageReader = imageReader;
        this.soundReader = soundReader;
        this.inputListener = inputListener;
//        this.gameObjectFactory = new GameObjectFactory(this.windowDimensions,
//                this, imageReader, soundReader);

//        this.gameObjectFactory.createObject(Constants.BALL);
//        this.gameObjectFactory.createObject(Constants.BACKGROUND);
//        this.gameObjectFactory.createObject(Constants.BORDERS);
//        this.gameObjectFactory.createObject(Constants.LIVES_UI_BAR);
//        this.gameObjectFactory.createObject(Constants.BRICKS);
//        this.gameObjectFactory.createObject(Constants.PADDLE);
        createBackground();
        createBorders();
        createLivesUIBar();
        createBricks();
        createPaddle();
        createBall();
    }

    /**
     * Updates the game state on each frame.
     *
     * @param deltaTime The time elapsed since the last frame.
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        double ballHeight = this.ball.getCenter().y();
        double windowLimit = windowController.getWindowDimensions().y();
        boolean isWKeyPressed = this.inputListener.isKeyPressed(KeyEvent.VK_W);

        if (ballHeight > windowLimit) {
            if (this.lifeCounter > 1) {
                updateLivesUI(false);
                // reset ball location.
                this.ball.resetBall();
            } else { // lifeCounter == 0, a.k.a LOST
                Brick.resetBricks();
                System.out.println("LOST! Brick.getNumberOfBricks(): " + Brick.getNumberOfBricks());
                handleGameEnd("lost");
            }
        }
        // win cond.
        else if (Brick.getNumberOfBricks() == 0 || isWKeyPressed) {
            // reset in case 'W' key was pressed.
            Brick.resetBricks();
            System.out.println("WIN! Brick.getNumberOfBricks(): " + Brick.getNumberOfBricks());
            handleGameEnd("win");
        }
    }

    /**
     * Handles the end of the game by displaying a prompt and resetting or
     * closing the game.
     *
     * @param condition The condition of the game end (win or lost).
     */
    private void handleGameEnd(String condition) {
        //end game
        String prompt = String.format(Constants.GAME_END_PROMPT_TEMPLATE,
                condition);
        if (windowController.openYesNoDialog(prompt)) {
            this.lifeCounter = Constants.DEFAULT_STARTING_LIFE;
//            Brick.resetBricks(this.numOfRows * this.bricksPerRow);
            windowController.resetGame();
        } else
            windowController.closeWindow();
    }

    /**
     * Adds a game object to the game.
     * Can be used from outer scope, like strategies.
     *
     * @param object The game object to add.
     * @param layer  The layer to add the game object to.
     */
    public void addGameObject(GameObject object, int layer) {
        gameObjects().addGameObject(object, layer);
    }

    /**
     * Removes a game object from the game.
     *
     * @param object The game object to remove.
     * @param layer  The layer to remove the game object from.
     * @return True if the game object was removed, false otherwise.
     */
    public boolean removeGameObject(GameObject object, int layer) {
        boolean removed = gameObjects().removeGameObject(object, layer);
        if (removed) {
            System.out.println(object + " removed by manager");
        }
        return removed;
    }

    /**
     * The main method to start the game.
     *
     * @param args Command line arguments for the number of bricks per row
     *             and number of rows.
     */
    public static void main(String[] args) {
        int bricksPerRow = Constants.DEFAULT_BRICKS_PER_ROW;
        int numRows = Constants.DEFAULT_NUM_ROWS;
        // change vals by user input, if legal
        if (args.length == Constants.Number2) {
            bricksPerRow = Integer.parseInt(args[0]);
            numRows = Integer.parseInt(args[1]);
        }

        BrickerGameManager gameManager = new BrickerGameManager("Bricker",
                Constants.WINDOW_DIMENSIONS_VEC,
                bricksPerRow, numRows);
        gameManager.run();
    }

    /**
     * ------------------------Object Creators--------------------------------
     */

    /**
     * Creates the UI bar for displaying lives.
     */
    private void createLivesUIBar() {
        // set number on UI
        Vector2 curTopLeftCorner = Constants.DEFAULT_UI_BAR_TOP_LEFT_CORNER;
        createLifeCounter(curTopLeftCorner);
        //
        for (int i = 0; i < this.lifeCounter; i++) {
            // update topleftcorner location
            curTopLeftCorner =
                    curTopLeftCorner.add(new Vector2(Constants.HEART_SIZE + Constants.GAP_SIZE,
                            0));
            createHeart(curTopLeftCorner, i + 1, true);
        }
    }

    /**
     * Removes the UI bar for displaying lives.
     */
    private void removeLivesUIBar() {
        // remove life counter UI
        this.gameObjects().removeGameObject(this.lifeUIObjectsArr[0],
                Layer.UI);
        // remove all heart UI objects
        for (int i = 1; i <= this.lifeCounter; i++) {
            this.gameObjects().removeGameObject(this.lifeUIObjectsArr[i],
                    Layer.UI);
        }
    }

    /**
     * Updates the UI for displaying lives.
     *
     * @param incrementing True if incrementing the life counter, false if
     *                     decrementing.
     */
    private void updateLivesUI(boolean incrementing) {
        if (incrementing) {
            // reset UI bar, re-create UI bar with new heart to UI
            removeLivesUIBar();
            this.lifeCounter++;
            createLivesUIBar();
        } else {
            // reduce lives n remove heart from gameObjects()
            GameObject curHeart = this.lifeUIObjectsArr[this.lifeCounter];
            this.removeGameObject(curHeart, Layer.UI);
            this.lifeCounter--;
            // update UI:
            // remove & re-create lifeCounter UI (prevents overlapping)
            this.removeGameObject(this.lifeUIObjectsArr[0], Layer.UI);
            createLifeCounter(Constants.DEFAULT_UI_BAR_TOP_LEFT_CORNER);
        }
    }

    /**
     * Creates the life counter UI element.
     *
     * @param topLeftCorner The top-left corner position of the life counter.
     */
    private void createLifeCounter(Vector2 topLeftCorner) {
        TextRenderable lifeCounterUI =
                new TextRenderable(String.valueOf(this.lifeCounter));
        switch (this.lifeCounter) {
            case 1:
                lifeCounterUI.setColor(Color.RED);
                break;
            case 2:
                lifeCounterUI.setColor(Color.YELLOW);
                break;
            default: // when count >= 3
                lifeCounterUI.setColor(Color.GREEN);
        }

        GameObject lifeCounterGameObj = new GameObject(topLeftCorner,
                Constants.HEART_SIZE_VEC, lifeCounterUI);

        this.lifeUIObjectsArr[0] = lifeCounterGameObj;
        this.gameObjects().addGameObject(lifeCounterGameObj, Layer.UI);
    }

    /**
     * Creates a heart UI element.
     *
     * @param topLeftCorner The top-left corner position of the heart.
     * @param heartIdx      The index of the heart in the UI array.
     * @param isUIHeart     True if the heart is a UI element, false if it
     *                      is a falling heart.
     * @return The created Heart object.
     */
    private Heart createHeart(Vector2 topLeftCorner,
                              int heartIdx, boolean isUIHeart) {
        // calc dims of each obj (num or heart)
        Renderable heartImage = this.imageReader.readImage("assets/heart.png",
                true);
        Heart heart = new Heart(topLeftCorner, Constants.HEART_SIZE_VEC,
                heartImage, this);
        int layer = Layer.DEFAULT; // for non-UI heart, a.k.a falling heart
//        heart.setCenter();
//        String tag = "heart" + String.valueOf(tagNumber);
//        heart.setTag(tag);
        if (isUIHeart) {
            this.lifeUIObjectsArr[heartIdx] = heart;
            layer = Layer.UI;
        }
        this.gameObjects().addGameObject(heart, layer);
        return heart;
    }

    /**
     * Creates a falling heart game object.
     *
     * @param brickCenter The center position of the brick from which the
     *                    heart falls.
     */
    public void createFallingHeart(Vector2 brickCenter) {
        Vector2 topLeftCorner =
                brickCenter.subtract(Constants.HEART_SIZE_VEC.mult(0.5f));
        Heart heart = createHeart(topLeftCorner, -1, false);
        heart.setCenter(brickCenter);
        heart.setVelocity(new Vector2(0, Constants.HEART_FALL_SPEED));
    }

    /**
     * Increments the life counter and updates the UI.
     */
    public void incrementLife() {
        int maxLives =
                Constants.DEFAULT_STARTING_LIFE + Constants.DEFAULT_EXTRA_LIFE;
        if (this.getLifeCounter() < maxLives) {
            // increment lifeCounter and update UI - done inside
            updateLivesUI(true);
        }
    }

    /**
     * Creates the bricks for the game.
     */
    private void createBricks() {
        Renderable brickImage = this.imageReader.readImage("assets/brick.png",
                false);

        float brickHeight = Constants.BRICK_HEIGHT;
        float brickWidth =
                (windowDimensions.x() - VISIBLE_BORDER_WIDTH * Constants.Number2) / bricksPerRow - 5;
        Vector2 brickSizeVec = new Vector2(brickWidth, brickHeight);
        // build bricks
        for (int row = 0; row < this.numOfRows; row++) {
            for (int col = 0; col < this.bricksPerRow; col++) {
                // update position
                Vector2 position = new Vector2(
                        VISIBLE_BORDER_WIDTH + col * (brickWidth + 5),
                        row * (brickHeight + 5)
                );
                CollisionStrategy strategy = getBrickStrategy(0);
                // create a single brick
                GameObject brick = new Brick(position, brickSizeVec,
                        brickImage, strategy);
                gameObjects().addGameObject(brick, Layer.STATIC_OBJECTS);
            }
        }
    }

    /**
     * Gets a random brick strategy for collision handling.
     *
     * @param doubleStrategiesCount The count of double strategies.
     * @return The selected CollisionStrategy.
     */
    private CollisionStrategy getBrickStrategy(int doubleStrategiesCount) {
        Random rand = new Random();
        int result = rand.nextInt(1, 11);

        switch (result) {
            case 1:
                return new extraBallsStrategy(this);
            case 2:
                return new extraPaddleStrategy(this);
            case 3:
                return new turboModeStrategy(this);
            case 4:
                return new extraLifeStrategy(this);
            case 5:
                doubleStrategiesCount++;
                System.out.println("\n>>>> doubleStrategiesCount: " + doubleStrategiesCount + "\n");
                return createDoubleSpecialStrategy(doubleStrategiesCount);
            default:
                return new BasicCollisionStrategy(this);
        }
    }

    /**
     * Creates a double special strategy for brick collision handling.
     *
     * @param doubleStrategiesCount The count of double strategies.
     * @return The created CollisionStrategy.
     */
    private CollisionStrategy createDoubleSpecialStrategy(int doubleStrategiesCount) {
        if (doubleStrategiesCount <= 1) {
            CollisionStrategy strategy1 =
                    getBrickStrategy(doubleStrategiesCount);
            CollisionStrategy strategy2 =
                    getBrickStrategy(doubleStrategiesCount);
            return new doubleSpecialStrategy(this, strategy1, strategy2);
        } else
            return getBrickStrategy(doubleStrategiesCount);
    }

    /**
     * Creates the paddle game object.
     */
    private void createPaddle() {
        // creating paddle
        Renderable paddleImage = this.imageReader.readImage("assets/paddle" +
                        ".png",
                true);
        GameObject paddle = new Paddle(
                Vector2.ZERO,
                new Vector2(Constants.PADDLE_WIDTH, Constants.PADDLE_HEIGHT),
                paddleImage,
                this.inputListener);
        paddle.setCenter(new Vector2(windowDimensions.x() / Constants.Number2,
                windowDimensions.y() - Constants.PADDLE_Y_OFFSET));
        paddle.setTag(Constants.PADDLE_TAG);
        gameObjects().addGameObject(paddle);
    }

    /**
     * Creates an extra paddle game object.
     */
    public void createExtraPaddle() {
        if (!this.hasExtraPaddle) {
            // creating paddle
            Renderable paddleImage = this.imageReader.readImage("assets" +
                    "/paddle.png", true);
            GameObject extraPaddle = new ExtraPaddle(
                    Vector2.ZERO,
                    new Vector2(Constants.PADDLE_WIDTH,
                            Constants.PADDLE_HEIGHT),
                    paddleImage,
                    this.inputListener, this);
            // set position to half screen on both axes
            extraPaddle.setCenter(new Vector2(windowDimensions.x() / Constants.Number2,
                    windowDimensions.y() / Constants.Number2));
            extraPaddle.setTag(Constants.EXTRA_PADDLE_TAG);
            gameObjects().addGameObject(extraPaddle);
            this.hasExtraPaddle = true;
        }
    }

    /**
     * Sets the hasExtraPaddle flag.
     *
     * @param value The new value for the hasExtraPaddle flag.
     */
    public void setHasExtraPaddle(boolean value) {
        this.hasExtraPaddle = value;
    }

    /**
     * Creates the ball game object.
     */
    private void createBall() {
        // creating ball
        Renderable ballImage = this.imageReader.readImage("assets/ball.png",
                true);
        Sound collisionSound = this.soundReader.readSound("assets/blop.wav");
        Ball ball = new Ball(Vector2.ZERO, new Vector2(Constants.BALL_RADIUS,
                Constants.BALL_RADIUS),
                ballImage, collisionSound);

        this.ball = ball;
        this.ball.resetBall();
        this.gameObjects().addGameObject(ball);
    }

    /**
     * Creates a puck game object.
     *
     * @param brickCenter The center coordinates of the brick from where the
     *                   pucks emerge.
     */
    public void createPuck(Vector2 brickCenter) {
        // creating ball
        Renderable puckImage = this.imageReader.readImage("assets/mockBall" +
                        ".png",
                true);
        Sound collisionSound = this.soundReader.readSound("assets/blop.wav");
        float puckRadius = (float) (0.75 * Constants.BALL_RADIUS);
        // calc pucks top-left corner coordinates
        float x = brickCenter.x() - puckRadius;
        float y = brickCenter.y() - puckRadius;
        Vector2 puckTopLeftCorner = new Vector2(x, y);
        Vector2 puckDimensions = new Vector2(puckRadius, puckRadius);
        // create new puck and add it to game object
        Puck puck = new Puck(puckTopLeftCorner, puckDimensions,
                puckImage, collisionSound, this);
//        this.ball = puck;
//        puck.resetBall();
        puck.setTag("puck");
        this.gameObjects().addGameObject(puck);
    }

    /**
     * Activates turbo mode for the ball.
     */
    public void createTurboMode() {
        this.ball.setTurboOn(this.imageReader);
    }

    /**
     * Creates the background game object.
     */
    private void createBackground() {
        Renderable backImage = this.imageReader.readImage("assets" +
                        "/DARK_BG2_small" +
                        ".jpeg"
                , false);
        GameObject background = new GameObject(Vector2.ZERO,
                new Vector2(this.windowDimensions.x(),
                        this.windowDimensions.y()),
                backImage);
        gameObjects().addGameObject(background, Layer.BACKGROUND);
        background.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
    }

    /**
     * Creates the borders for the game window.
     * The borders are invisible by default but can be made visible by
     * replacing
     * the null renderable with a visible one.
     */
    private void createBorders() {
        //left
        GameObject leftBorder = new GameObject(
                new Vector2(-Constants.BORDER_WIDTH, 0),
                new Vector2(Constants.BORDER_WIDTH,
                        Constants.WINDOW_HEIGHT * Constants.Number2),
                null);
        leftBorder.setTag(Constants.BORDER_TAG);
        gameObjects().addGameObject(leftBorder, Layer.STATIC_OBJECTS);

        //right
        GameObject rightBorder = new GameObject(
                new Vector2(Constants.WINDOW_WIDTH, 0),
                new Vector2(Constants.BORDER_WIDTH,
                        Constants.WINDOW_HEIGHT * Constants.Number2),
                null);
        rightBorder.setTag(Constants.BORDER_TAG);
        gameObjects().addGameObject(rightBorder, Layer.STATIC_OBJECTS);

        //top
        GameObject topBorder = new GameObject(
                new Vector2(0, -Constants.BORDER_WIDTH),
                new Vector2(Constants.WINDOW_WIDTH * Constants.Number2,
                        Constants.BORDER_WIDTH),
                null);
        topBorder.setTag(Constants.BORDER_TAG);
        gameObjects().addGameObject(topBorder, Layer.STATIC_OBJECTS);
    }


    /**
     * ------------------------ Getters n Setters--------------------------
     */

    // todo: delete if GameObjectFactory is deleted.
    // Setters
//    public void setBall(Ball ball) {
//        this.ball = ball;
//    }
//
//    // Getters
//    public Ball getBall() {
//        return this.ball;
//    }

    /**
     * Returns the current life counter.
     *
     * @return The current number of lives.
     */
    public int getLifeCounter() {
        return lifeCounter;
    }

//    public GameObject[] getLifeUIObjectsArr() {
//        return lifeUIObjectsArr;
//    }
//
//    public int getBricksPerRow() {
//        return bricksPerRow;
//    }
//
//    public int getNumOfRows() {
//        return numOfRows;
//    }
//
//    public UserInputListener getInputListener() {
//        return inputListener;
//    }
}
