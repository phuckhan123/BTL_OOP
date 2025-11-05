package org.oop.arknoid_oop.Controllers;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import org.oop.arknoid_oop.Entity.Ball;
import org.oop.arknoid_oop.Entity.Brick;
import org.oop.arknoid_oop.Entity.Paddle;

import org.oop.arknoid_oop.Orserver.GameData;
import org.oop.arknoid_oop.Orserver.GameDataObserver;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class GameController {

    @FXML private AnchorPane root;
    @FXML private Rectangle paddleView;
    @FXML private ImageView ballImage;
    @FXML private Text livesText;
    @FXML private Pane brickContainer;
    @FXML private Text scoreText;

    private Scene scene;

    private Paddle paddle;
    private Ball ball;
    private List<Brick> bricks = new ArrayList<>();
    private boolean ballLaunched = false;
    private boolean leftPressed = false;
    private boolean rightPressed = false;

    // 🔵 CHANGE: bỏ lives và score riêng lẻ → quản lý qua GameData
    private GameData gameData; // 🟢 NEW

    private int breakableBrickCount = 0;
    private AnimationTimer gameTimer;

    private final double SCENE_WIDTH = 641;
    private final double SCENE_HEIGHT = 446;
    private int currentLevel = 1;
    private int maxLevel = 2;

    private final double BRICK_WIDTH = 70;
    private final double BRICK_HEIGHT = 30;
    private final double BRICK_GAP = 5;

    @FXML
    public void initialize() {
        

        ballImage.setVisible(true);
        ballImage.setOpacity(1.0);
        ballImage.setSmooth(true);
        ballImage.setCache(true);
        ballImage.toFront();

        paddle = new Paddle(paddleView, 5.0);
        ball = new Ball(ballImage);

        // 🟢 NEW: Khởi tạo GameData & Observer UI
        gameData = new GameData(3); // 3 mạng ban đầu
        GameDataObserver observer = new GameDataObserver(scoreText, livesText);
        gameData.addObserver(new GameDataObserver(scoreText, livesText));
        observer.update(gameData);

        loadLevel(currentLevel);
        resetBallToPaddle();
        startGameLoop();
    }

    private void loadLevel(int levelNumber) {
        bricks.clear();
        brickContainer.getChildren().clear();
        breakableBrickCount = 0;
        resetBallToPaddle();

        try {
            String bgPath = "/images/gameviewlevel" + levelNumber + ".jpg";
            URL bgUrl = getClass().getResource(bgPath);
            if (bgUrl != null) {
                String bgCss = "-fx-background-image: url('" + bgUrl.toExternalForm() + "'); -fx-background-size: cover;";
                root.setStyle(bgCss);
            } else {
                root.setStyle("-fx-background-color: #000022;");
            }
        } catch (Exception e) {
            root.setStyle("-fx-background-color: #000022;");
        }

        String levelFile = "/levels/level" + levelNumber + ".txt";
        try (InputStream is = getClass().getResourceAsStream(levelFile);
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {

            String line;
            int row = 0;
            double totalWidth = 8 * (BRICK_WIDTH + BRICK_GAP) - BRICK_GAP;
            double offsetX = (SCENE_WIDTH - totalWidth) / 2;
            double offsetY = 50;

            while ((line = reader.readLine()) != null) {
                String[] types = line.split(" ");
                for (int col = 0; col < types.length; col++) {
                    char type = types[col].charAt(0);
                    if (type != '0') {
                        double x = offsetX + col * (BRICK_WIDTH + BRICK_GAP);
                        double y = offsetY + row * (BRICK_HEIGHT + BRICK_GAP);
                        createBrickAt(x, y, type);
                    }
                }
                row++;
            }
        } catch (Exception e) {
            System.err.println("Không thể tải level: " + levelFile);
        }
    }

    public void initScene(Scene scene) {
        this.scene = scene;
        setupControls();
    }

    private void createBrickAt(double x, double y, char type) {
        Rectangle brickView = new Rectangle(x, y, BRICK_WIDTH, BRICK_HEIGHT);
        int health = 1;
        int score = 10;
        switch (type) {
            case '1': health = 1; score = 10; break;
            case '2': health = 2; score = 25; break;
            case 'U': health = -1; score = 0; break;
        }

        Brick brick = new Brick(brickView, score, health);
        if (!brick.isUnbreakable()) breakableBrickCount++;
        bricks.add(brick);
        brickContainer.getChildren().add(brickView);
    }

    private void setupControls() {
        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.LEFT) leftPressed = true;
            if (e.getCode() == KeyCode.RIGHT) rightPressed = true;
            if (e.getCode() == KeyCode.SPACE && !ballLaunched) launchBall();
        });
        scene.setOnKeyReleased(e -> {
            if (e.getCode() == KeyCode.LEFT) leftPressed = false;
            if (e.getCode() == KeyCode.RIGHT) rightPressed = false;
        });
    }

    private void launchBall() {
        ballLaunched = true;
        ball.setVelocity(0, -6.0);
    }

    private void resetBallToPaddle() {
        ballLaunched = false;
        ball.setVelocity(0, 0);
        updateStuckBallPosition();
    }

    private void updateStuckBallPosition() {
        double paddleCenterX = paddle.getX() + paddle.getWidth() / 2;
        double ballY = paddle.getY() - ball.getRadius();
        ball.setPosition(paddleCenterX, ballY);
    }

    private void gameOver() {
        ballLaunched = false;
        ball.stop();
        scoreText.setText("Game Over! Final Score: " + gameData.getScore()); // 🔵 CHANGE
        scoreText.setFill(Color.RED);
        gameTimer.stop();
    }

    private void startGameLoop() {
        gameTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
            }
        };
        gameTimer.start();
    }

    private void update() {
        movePaddle();
        if (ballLaunched) {
            handleBallMovement();
            checkCollisions();
        } else {
            updateStuckBallPosition();
        }
    }

    private void movePaddle() {
        if (leftPressed) paddle.moveLeft(0);
        if (rightPressed) paddle.moveRight(SCENE_WIDTH);
    }

    private void handleBallMovement() {
        ball.move();
        ball.checkWallCollision(SCENE_WIDTH, SCENE_HEIGHT);
        if (ball.isOutOfBottom(SCENE_HEIGHT)) {
            
            gameData.loseLife(); // 🔵 CHANGE

            if (gameData.getLives() <= 0) {
                gameOver();
            } else {
                resetBallToPaddle();
            }
        }
    }

    private void checkCollisions() {
        if (ball.getBounds().intersects(paddle.getBounds())) {
            ball.setPosition(ball.getX(), paddle.getY() - ball.getRadius());
            ball.calculatePaddleBounce(paddle);
        }

        List<Brick> toRemove = new ArrayList<>();
        for (Brick brick : bricks) {
            if (ball.getBounds().intersects(brick.getBounds())) {
                Bounds ballBounds = ball.getImageView().getBoundsInParent();
                Bounds brickBounds = brick.getRectView().getBoundsInParent();

                if (ballBounds.intersects(brickBounds)) {
                    double overlapWidth = Math.min(ballBounds.getMaxX(), brickBounds.getMaxX())
                            - Math.max(ballBounds.getMinX(), brickBounds.getMinX());
                    double overlapHeight = Math.min(ballBounds.getMaxY(), brickBounds.getMaxY())
                            - Math.max(ballBounds.getMinY(), brickBounds.getMinY());
                    ball.resolveBrickCollision(brick, overlapWidth, overlapHeight);
                }

                if (brick.isUnbreakable()) {
                    gameData.hitUnbreakableBrick();
                    brick.playHitEffect();
                    break;
                }

                
                boolean destroyed = brick.takeHit();
                if (destroyed) {
                    toRemove.add(brick);
                    breakableBrickCount--;
                    gameData.addScore(brick.getScoreValue()); // 🔵 CHANGE
                }
                break;
            }
        }

        for (Brick brick : toRemove) {
            bricks.remove(brick);
            brickContainer.getChildren().remove(brick.getView());
        }

        if (breakableBrickCount <= 0) {
            currentLevel++;
            if (currentLevel > maxLevel) {
                ball.stop();
                scoreText.setText("YOU WIN! Score: " + gameData.getScore()); // 🔵 CHANGE
                gameTimer.stop();
            } else {
                loadLevel(currentLevel);
            }
        }
    }
}
