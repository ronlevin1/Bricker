package bricker.main;

import bricker.gameobjects.*;
import bricker.brick_strategies.BasicCollisionStrategy;
import bricker.brick_strategies.CollisionStrategy;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.*;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Random;


public class BrickerGameManager extends GameManager {
    //todo: remove constants and pull from the file?
    //todo: remove debugging-prints from all over the project.
    private static final int BORDER_WIDTH = 10;
    private static final int PADDLE_HEIGHT = 15;
    private static final int PADDLE_WIDTH = 100;
    private static final int BALL_RADIUS = 20;
    private static final float BALL_SPEED = 300;
    private static final int DEFAULT_BRICKS_PER_ROW = 8;
    private static final int DEFAULT_NUM_ROWS = 7;
    private static final int DEFAULT_STARTING_LIFE = 3;
    private static final int DEFAULT_EXTRA_LIFE = 3;
    private static final Vector2 DEFAULT_UI_BAR_TOP_LEFT_CORNER =
            new Vector2(Constants.GAP_SIZE,
                    Constants.WINDOW_HEIGHT - Constants.HEART_SIZE - Constants.GAP_SIZE * 2);
    private static final Renderable BORDER_RENDERABLE =
            new RectangleRenderable(new Color(80, 140, 250));

    private final int bricksPerRow;
    private final int numOfRows;
    private bricker.gameobjects.Ball ball;
    private Vector2 windowDimensions;
    private WindowController windowController;

    private int lifeCounter;
    private GameObject[] lifeUIObjectsArr =
            new GameObject[DEFAULT_STARTING_LIFE + DEFAULT_EXTRA_LIFE + 1];
    private UserInputListener inputListener;

    public BrickerGameManager(String windowTitle, Vector2 windowDimensions,
                              int bricksPerRow, int numOfRows) {
        super(windowTitle, windowDimensions);
        this.bricksPerRow = bricksPerRow;
        this.numOfRows = numOfRows;
        this.lifeCounter = DEFAULT_STARTING_LIFE; //todo change by input
    }

    @Override
    public void initializeGame(ImageReader imageReader,
                               SoundReader soundReader,
                               UserInputListener inputListener,
                               WindowController windowController) {

        super.initializeGame(imageReader, soundReader, inputListener,
                windowController);

        this.windowController = windowController;
        this.windowDimensions = windowController.getWindowDimensions();
        this.inputListener = inputListener;

        createBackground(windowDimensions, imageReader);
        createBall(imageReader, soundReader, windowController);
        createPaddle(imageReader, inputListener);
        createBorders(windowDimensions);
        createBricks(imageReader);
        createLivesUIBar(imageReader);
    }

    private void createLivesUIBar(ImageReader imageReader) {
        // set number on UI
        Vector2 curTopLeftCorner = DEFAULT_UI_BAR_TOP_LEFT_CORNER;
        createLifeCounter(curTopLeftCorner);
        //
        for (int i = 0; i < this.lifeCounter; i++) {
            // update topleftcorner location
            curTopLeftCorner =
                    curTopLeftCorner.add(new Vector2(Constants.HEART_SIZE + Constants.GAP_SIZE,
                            0));
            createHeart(curTopLeftCorner, imageReader, i + 1, true);
        }
    }

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

    private void createHeart(Vector2 topLeftCorner, ImageReader imageReader,
                             int heartIdx, boolean isUIHeart) {
        // calc dims of each obj (num or heart)
        Renderable heartImage = imageReader.readImage("assets/heart.png",
                true);
        Heart heart = new Heart(topLeftCorner, Constants.HEART_SIZE_VEC,
                heartImage);
        int layer = Layer.DEFAULT; // for non-UI heart, a.k.a falling heart
//        heart.setCenter();
//        String tag = "heart" + String.valueOf(tagNumber);
//        heart.setTag(tag);
        if (isUIHeart) {
            this.lifeUIObjectsArr[heartIdx] = heart;
            layer = Layer.UI;
        }
        this.gameObjects().addGameObject(heart, layer);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        double ballHeight = this.ball.getCenter().y();
        double windowLimit = windowController.getWindowDimensions().y();
        boolean isWKeyPressed = this.inputListener.isKeyPressed(KeyEvent.VK_W);

        if (ballHeight > windowLimit) {
            if (this.lifeCounter > 1) {
                // lifeCounter is done - end game
                GameObject curHeart = this.lifeUIObjectsArr[this.lifeCounter];
                // remove heart from gameObjects()
                this.gameObjects().removeGameObject(curHeart, Layer.UI);
                // reduce lives
                this.lifeCounter--;
                // remove & re-create lifeCounter UI (prevents overlapping)
                this.gameObjects().removeGameObject(this.lifeUIObjectsArr[0]
                        , Layer.UI);
                createLifeCounter(DEFAULT_UI_BAR_TOP_LEFT_CORNER);
                // reset ball location.
                resetBall(this.ball);
            } else { // lifeCounter == 0 a.k.a LOSE
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

    private void handleGameEnd(String condition) {
        //end game
        String prompt = String.format(Constants.GAME_END_PROMPT_TEMPLATE,
                condition);
        if (windowController.openYesNoDialog(prompt)) {
            this.lifeCounter = DEFAULT_STARTING_LIFE;
//            Brick.resetBricks(this.numOfRows * this.bricksPerRow);
            windowController.resetGame();
        } else
            windowController.closeWindow();
    }

    public void removeGameObject(GameObject object) {
        gameObjects().removeGameObject(object, Layer.STATIC_OBJECTS);
        System.out.println(object + " removed by manager");
    }
    // todo: treat layer option
//    public void removeGameObject(GameObject object, int layer) {
//        gameObjects().removeGameObject(object, layer);
//        System.out.println(object + " removed by manager");
////        System.out.println("gameObjects: "+gameObjects());
//    }

    private void createBricks(ImageReader imageReader) {
        Renderable brickImage = imageReader.readImage("assets/brick.png",
                false);
        CollisionStrategy strategy = new BasicCollisionStrategy(this);

        float brickHeight = 15;
        float brickWidth =
                (windowDimensions.x() - BORDER_WIDTH * 2) / bricksPerRow - 5;
        // build bricks
        for (int row = 0; row < this.numOfRows; row++) {
            for (int col = 0; col < this.bricksPerRow; col++) {
                Vector2 position = new Vector2(
                        BORDER_WIDTH + col * (brickWidth + 5),
                        row * (brickHeight + 5)
                );
                GameObject brick = new Brick(position,
                        new Vector2(brickWidth, brickHeight), brickImage,
                        strategy);
                gameObjects().addGameObject(brick, Layer.STATIC_OBJECTS);
            }
        }
//        brick.setCenter(new Vector2(windowDimensions.x() / 2,
//                0));
        // todo: notice that LAYER is required for removal of brick.
//        gameObjects().addGameObject(brick, Layer.STATIC_OBJECTS);
    }

    private void createPaddle(ImageReader imageReader,
                              UserInputListener inputListener) {
        // creating paddle
        Renderable paddleImage = imageReader.readImage("assets/paddle.png",
                true);
        GameObject paddle = new Paddle(
                Vector2.ZERO,
                new Vector2(PADDLE_WIDTH, PADDLE_HEIGHT),
                paddleImage,
                inputListener);
        paddle.setCenter(new Vector2(windowDimensions.x() / 2,
                windowDimensions.y() - 30));
        gameObjects().addGameObject(paddle);

    }

    private void createBall(ImageReader imageReader, SoundReader soundReader
            , WindowController windowController) {
        // creating ball
        Renderable ballImage = imageReader.readImage("assets/ball.png", true);
        Sound collisionSound = soundReader.readSound("assets/blop.wav");
        Ball ball = new Ball(Vector2.ZERO, new Vector2(BALL_RADIUS,
                BALL_RADIUS),
                ballImage, collisionSound);

        resetBall(ball);

        this.ball = ball;
        this.gameObjects().addGameObject(ball);
    }

    //todo: move to Ball class
    private void resetBall(Ball ball) {
        float ballVelX = BALL_SPEED;
        float ballVelY = BALL_SPEED;
        Random rand = new Random();
        if (rand.nextBoolean())
            ballVelX *= -1;
        if (rand.nextBoolean())
            ballVelY *= -1;
        ball.setVelocity(new Vector2(ballVelX, ballVelY));
        ball.setCenter(windowDimensions.mult(0.5f));
    }

    private void createBackground(Vector2 windowDimensions,
                                  ImageReader imageReader) {
        Renderable backImage = imageReader.readImage("assets/DARK_BG2_small" +
                        ".jpeg"
                , false);
        GameObject background = new GameObject(Vector2.ZERO,
                new Vector2(windowDimensions.x(), windowDimensions.y()),
                backImage);
        gameObjects().addGameObject(background, Layer.BACKGROUND);
        background.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
    }

    private void createBorders(Vector2 windowDimensions) {
        // NOTE: can replace null to BORDER_RENDERABLE to make them visible
        gameObjects().addGameObject(
                new GameObject(
                        Vector2.ZERO,
                        new Vector2(BORDER_WIDTH, windowDimensions.y()),
                        null), Layer.STATIC_OBJECTS);
        gameObjects().addGameObject(
                new GameObject(
                        new Vector2(windowDimensions.x() - BORDER_WIDTH, 0),
                        new Vector2(BORDER_WIDTH, windowDimensions.y()),
                        null), Layer.STATIC_OBJECTS
        );
        // top
        gameObjects().addGameObject(
                new GameObject(Vector2.ZERO,
                        new Vector2(windowDimensions.x(), BORDER_WIDTH),
                        null), Layer.STATIC_OBJECTS
        );
    }

    public static void main(String[] args) {
//        new BrickerGameManager("Bricker",
//                new Vector2(700, 500)).run();
        int bricksPerRow = DEFAULT_BRICKS_PER_ROW;
        int numRows = DEFAULT_NUM_ROWS;
        // change vals by user input, if legal
        if (args.length == 2) {
            bricksPerRow = Integer.parseInt(args[0]);
            numRows = Integer.parseInt(args[1]);
        }

        BrickerGameManager gameManager = new BrickerGameManager("Bricker",
                new Vector2(Constants.WINDOW_WIDTH, Constants.WINDOW_HEIGHT),
                bricksPerRow, numRows);
        gameManager.run();
    }
}
