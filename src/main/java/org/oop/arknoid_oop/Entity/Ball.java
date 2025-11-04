package org.oop.arknoid_oop.Entity;

import javafx.geometry.Bounds;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Ball backed by an ImageView (loads /images/ball.jpg by default).
 * Coordinates used by the rest of the game use the node's layoutX/layoutY
 * as top-left of the image; several helper methods expose center-based
 * coordinates to match existing controller code.
 */
public class Ball extends GameObject {

    private ImageView imageView;
    private double dx = 0.0;
    private double dy = 0.0;

    public Ball(ImageView imageView) {
        
        super(imageView);
        this.imageView = imageView;

        // Ensure the image is present; if not, load the bundled JPG
        try {
            if (this.imageView.getImage() == null) {
                Image img = new Image(getClass().getResource("/images/ball.png").toExternalForm());
                this.imageView.setImage(img);
            }
        } catch (Exception e) {
            System.err.println("Failed to load ball image: " + e.getMessage());
        }

        // Default size (can be overridden in FXML). Keep it small.
        if (this.imageView.getFitWidth() == 0) this.imageView.setFitWidth(16);
        if (this.imageView.getFitHeight() == 0) this.imageView.setFitHeight(16);
        this.imageView.setPreserveRatio(true);
        this.imageView.setVisible(true);
        System.out.println("Ball fitWidth=" + imageView.getFitWidth());
System.out.println("Ball fitHeight=" + imageView.getFitHeight());

    }

    public ImageView getImageView() {
        return imageView;
    }

    // center-based helpers (controller expects center coordinates)
    public double getRadius() {
        return imageView.getFitWidth() / 2.0;
    }

    public double getX() {
        return imageView.getLayoutX() + getRadius();
    }

    public double getY() {
        return imageView.getLayoutY() + getRadius();
    }

    public void setPosition(double centerX, double centerY) {
        imageView.setLayoutX(centerX - getRadius());
        imageView.setLayoutY(centerY - getRadius());
    }

    public Bounds getBounds() {
        return imageView.getBoundsInParent();
    }

    public void setVelocity(double dx, double dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public void stop() {
        this.dx = 0;
        this.dy = 0;
    }

    public void move() {
        imageView.setLayoutX(imageView.getLayoutX() + dx);
        imageView.setLayoutY(imageView.getLayoutY() + dy);
    }

    public void checkWallCollision(double sceneWidth, double sceneHeight) {
        double left = imageView.getLayoutX();
        double top = imageView.getLayoutY();
        double right = left + imageView.getFitWidth();

        if (left <= 0 && dx < 0) {
            SoundManager.getInstance().playSound("bounce");
            dx = -dx;
            imageView.setLayoutX(0);
        } else if (right >= sceneWidth && dx > 0) {
            SoundManager.getInstance().playSound("bounce");
            dx = -dx;
            imageView.setLayoutX(sceneWidth - imageView.getFitWidth());
        }

        if (top <= 0 && dy < 0) {
            SoundManager.getInstance().playSound("bounce");
            dy = -dy;
            imageView.setLayoutY(0);
        }
    }

    public boolean isOutOfBottom(double sceneHeight) {
        // Consider the ball out when its top is beyond the scene height
        return imageView.getLayoutY() > sceneHeight;
    }

    public void calculatePaddleBounce(Paddle paddle) {

        double currentSpeed = Math.sqrt(dx * dx + dy * dy);
        final double speed = (currentSpeed == 0) ? 6.0 : currentSpeed;
        double ballCenterX = getX();

        double paddleCenterX = paddle.getX() + paddle.getWidth() / 2;
        // 5. Tính vị trí va chạm tương đối (từ -1.0 đến +1.0)
        double intersectX = ballCenterX - paddleCenterX;
        double normalizedIntersect = intersectX / (paddle.getWidth() / 2);
        // Đảm bảo giá trị không vượt quá -1.0 hoặc 1.0
        if (normalizedIntersect > 1.0) normalizedIntersect = 1.0;
        if (normalizedIntersect < -1.0) normalizedIntersect = -1.0;
        // 6. Tính góc nảy mới (bằng Radian)
        double maxBounceAngle = 5 * Math.PI / 12; // 75 độ
        double bounceAngle = normalizedIntersect * maxBounceAngle;
        dy = -speed * Math.cos(bounceAngle);
        dx = speed * Math.sin(bounceAngle);
    }
    public void resolveBrickCollision(Brick brick, double overlapWidth, double overlapHeight) {
        if (overlapWidth < overlapHeight) {
            this.dx = -this.dx; // horizontal collision
        } else {
            this.dy = -this.dy; // vertical collision
        }
    }
}
