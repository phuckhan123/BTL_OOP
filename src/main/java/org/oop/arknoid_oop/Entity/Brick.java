// File: src/main/java/org/oop/arknoid_oop/Models/Brick.java
package org.oop.arknoid_oop.Entity;

import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

import java.awt.*;

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

        // Xóa tất cả các "lớp" cũ
        rectView.getStyleClass().clear();

        // Thêm "lớp" chung cho mọi viên gạch
        rectView.getStyleClass().add("brick");

        // Thêm "lớp" cụ thể dựa trên máu
        if (isUnbreakable()) {
            // Sẽ khớp với .brick-unbreakable trong CSS
            rectView.getStyleClass().add("brick-unbreakable");
        } else if (health == 2) {
            // Sẽ khớp với .brick-health2 trong CSS
            rectView.getStyleClass().add("brick-health2");
        } else if (health == 1) {
            // Sẽ khớp với .brick-health1 trong CSS
            rectView.getStyleClass().add("brick-health1");
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