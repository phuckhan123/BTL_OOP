
package org.oop.arknoid_oop.Entity;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.animation.PauseTransition;
import javafx.scene.paint.Paint; // Dùng để lưu màu/ảnh cũ

import javafx.util.Duration;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.ImagePattern;
import javafx.scene.Node;


public class Brick extends GameObject {

    private int scoreValue;
    private int health;
    public Brick(Rectangle view, int scoreValue, int health) {
        super(view);
        this.scoreValue = scoreValue;
        this.health = health;
        updateAppearance();
    }
    public Brick(Rectangle view, int scoreValue) {
        super(view);
        this.scoreValue = scoreValue;
    }

    public Brick(Node view, int scoreValue, int health) {
    super(view);
    this.scoreValue = scoreValue;
    this.health = health;

    if (view instanceof Rectangle) {
        updateAppearance();
    }
    }

        public double getX() {
        if (view instanceof ImageView img) {
            return img.getX();
        } else if (view instanceof Rectangle rect) {
            return rect.getX();
        } else {
            return view.getLayoutX();
        }
    }

    public double getY() {
        if (view instanceof ImageView img) {
            return img.getY();
        } else if (view instanceof Rectangle rect) {
            return rect.getY();
        } else {
            return view.getLayoutY();
        }
    }

    public void playHitEffect() {
        if (!isUnbreakable()) {
            return;
        }
        Rectangle rectView = getRectView();
        Paint originalFill = rectView.getFill();
        rectView.setFill(Color.WHITE);
        PauseTransition timer = new PauseTransition(Duration.millis(100));
        timer.setOnFinished(event -> {
            rectView.setFill(originalFill); // Trả lại màu/ảnh gốc
        });

        timer.play();
    }
    public boolean isUnbreakable() {
        return health < 0;
    }
    public boolean isDestroyed() {
        return health <= 0;
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

    public Rectangle getRectView() {
        // Có thể bạn chưa có hàm này, nó giúp ép kiểu
        return (Rectangle) view;
    }


    public double getWidth() {
        return getRectView().getWidth();
    }


    public double getHeight() {
        return getRectView().getHeight();
    }



    public int getScoreValue() {
        return scoreValue;
    }
}