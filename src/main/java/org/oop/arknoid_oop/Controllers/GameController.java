// File: GameController.java
package org.oop.arknoid_oop.Controllers;
import javafx.scene.image.ImageView;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;

// ✨ 1. IMPORT CÁC MODELS MỚI
import org.oop.arknoid_oop.Entity.Ball;
import org.oop.arknoid_oop.Entity.Brick;
import org.oop.arknoid_oop.Entity.Paddle;
import org.oop.arknoid_oop.Entity.SoundManager;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class GameController {

    @FXML
    private AnchorPane root;

    // ✨ 2. ĐỔI TÊN FXML ID (để phân biệt view và object)
    @FXML
    private Rectangle paddleView; // Tên cũ: paddle

    @FXML
    private ImageView ballImage;

    @FXML
    private Text livesText;

    @FXML
    private Pane brickContainer;

    @FXML
    private Text scoreText;

    private Scene scene;

    // ✨ 3. THAY ĐỔI BIẾN: Dùng các lớp Model mới
    private Paddle paddle;
    private Ball ball;
    private List<Brick> bricks = new ArrayList<>();
    private int lives = 3;
    private boolean ballLaunched = false; // Trạng thái: bóng đã phóng chưa?

    // Các biến này sẽ được chuyển vào class Ball/Paddle
    // private double ballDX = 3; 
    // private double ballDY = -3;

    private boolean leftPressed = false;
    private boolean rightPressed = false;
    private int score = 0;
    // biến đếm gạch có thể vỡ
    private int breakableBrickCount = 0;

    private AnimationTimer gameTimer; // Đặt tên cho timer để có thể stop

    // Định nghĩa hằng số kích thước màn hình
    private final double SCENE_WIDTH = 641;
    private final double SCENE_HEIGHT = 446;
    // ✨ 1. Thêm biến quản lý level
    private int currentLevel = 1;
    private int maxLevel = 2; // Số level cao nhất bạn có

    // ✨ 2. Định nghĩa kích thước gạch (để dùng chung)
    private final double BRICK_WIDTH = 70;
    private final double BRICK_HEIGHT = 30;
    private final double BRICK_GAP = 5; // Khoảng cách giữa các gạch

    @FXML
    public void initialize() {
            SoundManager.getInstance().playMusic();

            // Set up the ball image before creating Ball instance
            ballImage.setVisible(true);
            ballImage.setOpacity(1.0);
            ballImage.setSmooth(true);
            ballImage.setCache(true);
            ballImage.toFront(); // Ensure ball is drawn on top

        paddle = new Paddle(paddleView, 5.0);
        ball = new Ball(ballImage);

        // Diagnostic log to help confirm ball image is loaded and positioned
        try {
            System.out.println("[DEBUG] ballImage.getImage() = " + (ballImage.getImage() != null));
            System.out.println("[DEBUG] ballImage layoutX,Y = " + ballImage.getLayoutX() + "," + ballImage.getLayoutY());
            System.out.println("[DEBUG] ballImage fitW,H = " + ballImage.getFitWidth() + "," + ballImage.getFitHeight());
        } catch (Exception e) {
            System.err.println("[DEBUG] error inspecting ballImage: " + e.getMessage());
        }

        livesText.setText("Lives: " + lives);
        
        // ✨ 3. Bỏ createBricks(), gọi loadLevel()
        // createBricks(); // Bỏ dòng này
        loadLevel(currentLevel);
       

        resetBallToPaddle();
        startGameLoop();
    }
    /**
     * Xóa gạch cũ và tải gạch mới từ file level
     */
    private void loadLevel(int levelNumber) {
        // a. Xóa gạch cũ
        bricks.clear();
        brickContainer.getChildren().clear();
        breakableBrickCount = 0;

        // b. Đặt lại bóng
        resetBallToPaddle();
// ✨ 2. THÊM ĐOẠN CODE MỚI NÀY ĐỂ TẢI NỀN
        try {
            // ✨ SỬA LẠI ĐÂY: Dùng "/" thay vì "../"
            String bgPath = "/images/gameviewlevel" + levelNumber + ".jpg";

            // Lấy URL của ảnh từ thư mục resources
            URL bgUrl = getClass().getResource(bgPath);

            if (bgUrl != null) {
                // Chuyển URL thành cú pháp CSS
                String bgCss = "-fx-background-image: url('" + bgUrl.toExternalForm() + "'); " +
                        "-fx-background-size: cover;";

                // Áp dụng style cho 'root' (AnchorPane chính)
                root.setStyle(bgCss);
            } else {
                // Nếu không tìm thấy ảnh (ví dụ: level 3 không có ảnh)
                System.err.println("Không tìm thấy ảnh nền: " + bgPath);
                // Đặt một màu nền dự phòng (ví dụ: màu xanh vũ trụ đậm)
                root.setStyle("-fx-background-color: #000022;");
            }

        } catch (Exception e) {
            e.printStackTrace();
            // Đặt màu nền dự phòng nếu có lỗi
            root.setStyle("-fx-background-color: #000022;");
        }

        // c. Đọc file level
        String levelFile = "/levels/level" + levelNumber + ".txt";
        try (InputStream is = getClass().getResourceAsStream(levelFile);
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {

            String line;
            int row = 0;

            // Tính toán offset để căn giữa
            // (Bạn có thể điều chỉnh lại công thức này)
            double totalWidth = 8 * (BRICK_WIDTH + BRICK_GAP) - BRICK_GAP;
            double offsetX = (SCENE_WIDTH - totalWidth) / 2;
            double offsetY = 50; // Vị trí bắt đầu của hàng gạch trên cùng

            while ((line = reader.readLine()) != null) {
                String[] types = line.split(" "); // Tách các ký tự bằng dấu cách
                for (int col = 0; col < types.length; col++) {
                    char type = types[col].charAt(0);
                    if (type != '0') {
                        // Tính toán vị trí
                        double x = offsetX + col * (BRICK_WIDTH + BRICK_GAP);
                        double y = offsetY + row * (BRICK_HEIGHT + BRICK_GAP);

                        // Tạo gạch dựa trên 'type'
                        createBrickAt(x, y, type);
                    }
                }
                row++;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Không thể tải level: " + levelFile);
        }
    }

    public void initScene(Scene scene) {
        this.scene = scene;
        setupControls();
    }

    /**
     * Hàm trợ giúp: Tạo 1 viên gạch tại vị trí x, y với loại 'type'
     */
    private void createBrickAt(double x, double y, char type) {
        Rectangle brickView = new Rectangle(x, y, BRICK_WIDTH, BRICK_HEIGHT);
        int health = 1;
        int score = 10;
        // ✨ 4. Phân loại gạch dựa trên file text
        switch (type) {
            case '1':
                health = 1;
                score = 10;
                // Màu sẽ được set trong Brick.java
                break;
            case '2':
                health = 2;
                score = 25;
                break;
            case 'U':
                health = -1; // Bất tử
                score = 0;
                break;
        }

        Brick brick = new Brick(brickView, score, health);
        if(!brick.isUnbreakable()) {
            breakableBrickCount++;
        }
        bricks.add(brick);
        brickContainer.getChildren().add(brickView);
    }

    // Giữ nguyên setupControls
    private void setupControls() {
        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.LEFT) leftPressed = true;
            if (e.getCode() == KeyCode.RIGHT) rightPressed = true;
            if (e.getCode() == KeyCode.SPACE && !ballLaunched) {
                launchBall();
            }
        });
        scene.setOnKeyReleased(e -> {
            if (e.getCode() == KeyCode.LEFT) leftPressed = false;
            if (e.getCode() == KeyCode.RIGHT) rightPressed = false;
        });
    }
    // ✨ 6. THÊM HÀM MỚI: Phóng bóng
    private void launchBall() {
        ballLaunched = true;
        ball.setVelocity(0, -6.0); // Phóng bóng (bạn có thể đổi dx, dy tùy ý)
    }

    // ✨ 7. THÊM HÀM MỚI: Reset bóng về paddle
    private void resetBallToPaddle() {
        ballLaunched = false;
        ball.setVelocity(0, 0); // Dừng bóng lại
        updateStuckBallPosition(); // Đặt bóng vào tâm paddle
    }

    // ✨ 8. THÊM HÀM MỚI: Cập nhật vị trí bóng khi dính vào paddle
    private void updateStuckBallPosition() {
        // Tâm X của paddle
        double paddleCenterX = paddle.getX() + paddle.getWidth() / 2;
        // Ngay trên đỉnh paddle
        double ballY = paddle.getY() - ball.getRadius();
        ball.setPosition(paddleCenterX, ballY);
    }

    // ✨ 9. THÊM HÀM MỚI: Xử lý khi thua
    private void gameOver() {
        ballLaunched = false; // Ngừng logic update
        ball.stop();
        scoreText.setText("Game Over! Final Score: " + score);
        scoreText.setFill(Color.RED); // Đổi màu text cho kịch tính
        gameTimer.stop(); // Dừng game
    }

    private void startGameLoop() {
        gameTimer = new AnimationTimer() { // Gán timer cho biến
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
            // Nếu bóng đã phóng, chạy logic game
            handleBallMovement();
            checkCollisions();
        } else {
            // Nếu bóng chưa phóng, cho nó dính theo paddle
            updateStuckBallPosition();
        }
    }


    private void movePaddle() {
        // ✨ 6. ỦY THÁC: Để Paddle tự di chuyển
        if (leftPressed) {
            paddle.moveLeft(0); // 0 là biên trái
        }
        if (rightPressed) {
            paddle.moveRight(SCENE_WIDTH); // Chiều rộng màn hình là biên phải
        }
    }

    private void handleBallMovement() {
        ball.move();
        ball.checkWallCollision(SCENE_WIDTH, SCENE_HEIGHT);
        // Kiểm tra rơi ra ngoài
        if (ball.isOutOfBottom(SCENE_HEIGHT)) {
            SoundManager.getInstance().playSound("deaths");
            lives--; // Trừ mạng
            livesText.setText("Lives: " + lives);

            if (lives <= 0) {
                // Hết mạng
                gameOver();
            } else {
                // Còn mạng, reset
                resetBallToPaddle();
            }
        }
    }

    private void checkCollisions() {
        // ✨ 8. SỬ DỤNG HÀM getBounds() CỦA CÁC OBJECT
        // Va chạm Paddle
        if (ball.getBounds().intersects(paddle.getBounds())) {
            ball.setPosition(ball.getX(), paddle.getY() - ball.getRadius());
            ball.calculatePaddleBounce(paddle);
        }

        List<Brick> toRemove = new ArrayList<>();
        for (Brick brick : bricks) {
            if (ball.getBounds().intersects(brick.getBounds())) {
                // 1. Lấy "hình" (shape) - use image/bounds directly from objects

                // 2. Tính vùng giao nhau (overlap)
                Bounds ballBounds = ball.getImageView().getBoundsInParent();
                Bounds brickBounds = brick.getRectView().getBoundsInParent();

                if (ballBounds.intersects(brickBounds)) {
                    double overlapWidth = Math.min(
                        ballBounds.getMaxX(), brickBounds.getMaxX()
                    ) - Math.max(ballBounds.getMinX(), brickBounds.getMinX());

                    double overlapHeight = Math.min(
                        ballBounds.getMaxY(), brickBounds.getMaxY()
                    ) - Math.max(ballBounds.getMinY(), brickBounds.getMinY());

                    ball.resolveBrickCollision(brick, overlapWidth, overlapHeight);
                    // ... phần xử lý gạch sau đó giữ nguyên
                }

                // ✨ 2. SỬA LOGIC XỬ LÝ GẠCH
                if (brick.isUnbreakable()) {
                    // Nếu là gạch bất tử, chỉ nảy bóng và thoát
                    SoundManager.getInstance().playSound("brickUnbreakable");
                    brick.playHitEffect();
                    break;
                }
                // Gạch nhận sát thương
                SoundManager.getInstance().playSound("brickBreak");
                boolean destroyed = brick.takeHit();
                if (destroyed) {
                    // Nếu gạch bị phá hủy
                    toRemove.add(brick);
                    breakableBrickCount--;
                    score += brick.getScoreValue();
                    scoreText.setText("Score: " + score);
                }
                // Nếu gạch chưa bị phá hủy (chỉ mất máu),
                // nó sẽ tự đổi màu (trong hàm takeHit) và bóng vẫn nảy ra.
                break; // Chỉ vỡ 1 gạch mỗi frame
            }
        }

        // Xóa gạch đã vỡ (cả object và view)
        for (Brick brick : toRemove) {
            bricks.remove(brick); // Xóa khỏi List logic
            brickContainer.getChildren().remove(brick.getView()); // Xóa khỏi Scene
        }
        if (breakableBrickCount <= 0) {
            // Hết gạch
            currentLevel++;
            if (currentLevel > maxLevel) {
                // THẮNG GAME
                ball.stop();
                scoreText.setText("YOU WIN! Score: " + score);
                gameTimer.stop();
            } else {
                // Tải level tiếp theo
                loadLevel(currentLevel);
            }
        }
    }
}