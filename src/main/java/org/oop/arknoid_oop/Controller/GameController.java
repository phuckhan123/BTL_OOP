package org.oop.arknoid_oop.Controller;

import javafx.animation.AnimationTimer;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import javafx.stage.Stage;
import org.oop.arknoid_oop.ArknoidApplication;
import org.oop.arknoid_oop.Database.Database;
import org.oop.arknoid_oop.Entity.*;
import org.oop.arknoid_oop.Orserver.GameData;
import org.oop.arknoid_oop.Orserver.GameDataObserver;
import org.oop.arknoid_oop.Service.DataService;
import org.oop.arknoid_oop.Service.UserService;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.Connection;
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
    private List<PowerUp> powerUps = new ArrayList<>();
   

    private boolean ballLaunched = false;
    private boolean leftPressed = false;
    private boolean rightPressed = false;

    private GameData gameData;

    private int breakableBrickCount = 0;
    private AnimationTimer gameTimer;

    private final double SCENE_WIDTH = 641;
    private final double SCENE_HEIGHT = 446;
    private int currentLevel = 1;
    private int maxLevel = 5;

    private final double BRICK_WIDTH = 70;
    private final double BRICK_HEIGHT = 30;
    private final double BRICK_GAP = 5;
    private Node pauseMenuNode;

    private Connection connect = null;

    @FXML
    public void initialize() {

        try  {
            connect = Database.connect();

        } catch (Exception e) {
            e.printStackTrace();
        }
        ballImage.setVisible(true);
        ballImage.setOpacity(1.0);
        ballImage.setSmooth(true);
        ballImage.setCache(true);
        ballImage.toFront();

        paddle = new Paddle(paddleView, 6.0);
        ball = new Ball(ballImage);

        gameData = new GameData(3); // 3 mạng ban đầu
        GameDataObserver observer = new GameDataObserver(scoreText, livesText);
        gameData.addObserver(new GameDataObserver(scoreText, livesText));
        observer.update(gameData);

        loadLevel(currentLevel);
        resetBallToPaddle();
        startGameLoop();
    }
    private void pauseGame() {
        gameTimer.stop();
        try {

            FXMLLoader loader = new FXMLLoader(ArknoidApplication.class.getResource("pause-menu.fxml"));
            pauseMenuNode = loader.load(); // Tải FXML và lưu Node gốc
            PauseMenuController pauseController = loader.getController();

            pauseController.setGameController(this);
            root.getChildren().add(pauseMenuNode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void resumeGame() {

        if (pauseMenuNode != null) {
            root.getChildren().remove(pauseMenuNode);
            pauseMenuNode = null; // Reset
        }

        gameTimer.start(); // Tiếp tục vòng lặp game
    }
    public void quitToMenu() {
        try {

            // 1. Tải lại FXML của Welcome
            FXMLLoader loader = new FXMLLoader(ArknoidApplication.class.getResource("welcome-view.fxml"));

            // 2. Load FXML thành một đối tượng Parent
            Parent welcomeRoot = loader.load();

            // 3. Tạo một Scene mới từ FXML vừa load
            Scene welcomeScene = new Scene(welcomeRoot);
            URL cssURL = ArknoidApplication.class.getResource("/css/mainStyle.css");
            if (cssURL != null) {
                welcomeScene.getStylesheets().add(cssURL.toExternalForm());
            } else {
                System.err.println("Không tìm thấy file mainStyle.css!");
            }

            // 4. Lấy Stage (cửa sổ) hiện tại.
            Stage currentStage = (Stage) root.getScene().getWindow();
            // 5. Đặt Scene của màn hình Welcome vào Stage
            currentStage.setScene(welcomeScene);
            currentStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadLevel(int levelNumber) {
        bricks.clear();
        brickContainer.getChildren().clear();
        breakableBrickCount = 0;
        ball.setVelocity(3, -3);
        gameData.resetLife();
        resetBallToPaddle();
        if(levelNumber>0)
        paddle.reset();
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
            brickView.setStroke(Color.BLACK);
            brickView.setStrokeWidth(1);
        
            int health = 1;
            int score = 10;
            switch (type) {
                case '1': health = 1; score = 10; break;
                case '2': health = 2; score = 25; break;
                case 'U': health = -1; score = 0; break;
            }

            // Set initial color before creating the brick
            if (health == -1) {
                brickView.setFill(Color.GRAY);
            } else if (health == 2) {
                brickView.setFill(Color.RED);
            } else {
                brickView.setFill(Color.ORANGE);
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
            if (e.getCode() == KeyCode.ESCAPE) {
                pauseGame();
            }
        });
        scene.setOnKeyReleased(e -> {
            if (e.getCode() == KeyCode.LEFT) leftPressed = false;
            if (e.getCode() == KeyCode.RIGHT) rightPressed = false;
        });

    }

    private void launchBall() {
        ballLaunched = true;
        ball.setVelocity(0, -5.0);
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
        scoreText.setText("Game Over! Final Score: " + gameData.getScore());
        scoreText.setFill(Color.RED);
        gameTimer.stop();
        updateFinalScore();
        try {
            Thread.sleep(1000); // dừng 1 giây để hiển thị thông báo
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        quitToMenu();
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
            updatePowerUps();
            
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
            
            gameData.loseLife();

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
            SoundManager.getInstance().playSound("bounce");
            ball.calculatePaddleBounce(paddle);
        }

        List<Brick> toRemove = new ArrayList<>();
        for (Brick brick : bricks) {
            if (ball.getBounds().intersects(brick.getBounds())) {
                Bounds ballBounds = ball.getImageView().getBoundsInParent();
                Bounds brickBounds = brick.getView().getBoundsInParent();

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
                    gameData.addScore(brick.getScoreValue());
                }

                if (destroyed && Math.random() < 0.25) {
                    
                    String[] types = {"ballFaster", "addLife", "paddleLonger"};
                    String type = types[(int)(Math.random() * types.length)];
                    PowerUp powerUp = new PowerUp(
                        brick.getX() + brick.getWidth() / 2 - 15,
                        brick.getY() + brick.getHeight(),
                        type
                    );
                    powerUps.add(powerUp);
                    brickContainer.getChildren().add(powerUp.getView());
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
                scoreText.setText("YOU WIN! Score: " + gameData.getScore());
                gameTimer.stop();
                updateFinalScore();
            } else {
                loadLevel(currentLevel);
            }
        }
    }
    
    private void updatePowerUps() {
    List<PowerUp> toRemove = new ArrayList<>();
    for (PowerUp p : powerUps) {
        p.move();

        if (p.getView().getBoundsInParent().intersects(paddle.getBounds())) {
            p.applyEffect(paddle, gameData, ball, null, root);
            toRemove.add(p);
        } else if (p.isOutOfBottom(SCENE_HEIGHT)) {
            toRemove.add(p);
        }
    }

    for (PowerUp p : toRemove) {
        brickContainer.getChildren().remove(p.getView());
        powerUps.remove(p);
    }
    }
    private void updateFinalScore() {
        String username = DataService.username;
        int finalScore = gameData.getScore();
        if (username != null) {
            Task<Void> task = new Task<>() {
                @Override
                protected Void call() throws Exception {
                    UserService.updateHighScore(username, finalScore);
                    return null;
                }
            };
            new Thread(task).start();
        }
    }





}
