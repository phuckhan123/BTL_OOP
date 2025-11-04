// File: src/main/java/org/oop/arknoid_oop/Models/Brick.java
package org.oop.arknoid_oop.Entity;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.animation.PauseTransition;
import javafx.scene.paint.Paint; // Dùng để lưu màu/ảnh cũ
import javafx.animation.PauseTransition;
import javafx.util.Duration;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;


public class Brick extends GameObject {

    private int scoreValue;
    private int health;
    public Brick(Rectangle view, int scoreValue, int health) {
        super(view);
        this.scoreValue = scoreValue;
        this.health = health;
        updateAppearance(); // Cập nhật màu sắc ban đầu
    }
    public Brick(Rectangle view, int scoreValue) {
        super(view);
        this.scoreValue = scoreValue;
    }
    public void playHitEffect() {
        // Chỉ chạy hiệu ứng nếu là gạch bất tử
        if (!isUnbreakable()) {
            return;
        }
        Rectangle rectView = getRectView();
        // a. Lưu lại màu/ảnh "sơn" (fill) ban đầu
        Paint originalFill = rectView.getFill();
        // b. Đặt màu "flash" (ví dụ: màu trắng sáng)
        rectView.setFill(Color.WHITE);
        // c. Tạo bộ đếm thời gian (100ms là đủ cho 1 cú flash)
        PauseTransition timer = new PauseTransition(Duration.millis(100));
        // d. Đặt hành động sau khi đếm xong: Trả lại màu/ảnh cũ
        timer.setOnFinished(event -> {
            rectView.setFill(originalFill); // Trả lại màu/ảnh gốc
        });
        // e. Bắt đầu đếm
        timer.play();
    }
    public boolean isUnbreakable() {
        return health < 0; // Gạch bất tử nếu health < 0
    }
    public boolean isDestroyed() {
        return health <= 0; // Bị phá hủy nếu hết máu
    }
    public boolean takeHit() {
        if (isUnbreakable()) {
            return false;
        }
        health--; // Giảm máu
        updateAppearance(); // Cập nhật lại màu sắc
        return isDestroyed();
    }
    public void updateAppearance() {
    Rectangle rectView = getRectView();
        //thêm ảnh cho các loại gạch
    if (isUnbreakable()) {
        Image img = new Image(getClass().getResource("/images/Bricku.jpg").toExternalForm());
        rectView.setFill(new ImagePattern(img));
    } else if (health == 2) {
        Image img = new Image(getClass().getResource("/images/Brickhealth2.jpg").toExternalForm());
        rectView.setFill(new ImagePattern(img));
    } else if (health == 1) {
        Image img = new Image(getClass().getResource("/images/Brickhealth1.jpg").toExternalForm());
        rectView.setFill(new ImagePattern(img));
    } else {
        // Nếu bị phá hủy thì làm trong suốt
        rectView.setFill(null);
    }
}
    // Bạn có thể thêm 'int health' ở đây nếu muốn
    // ✨ THÊM HÀM NÀY
    public Rectangle getRectView() {
        // Có thể bạn chưa có hàm này, nó giúp ép kiểu
        return (Rectangle) view;
    }

    // ✨ THÊM HÀM NÀY
    public double getWidth() {
        return getRectView().getWidth();
    }

    // ✨ THÊM HÀM NÀY
    public double getHeight() {
        return getRectView().getHeight();
    }



    public int getScoreValue() {
        return scoreValue;
    }
}