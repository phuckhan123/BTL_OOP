// File: src/main/java/org/oop/arknoid_oop/Models/Paddle.java
package org.oop.arknoid_oop.Entity;

import javafx.scene.shape.Rectangle;

public class Paddle extends GameObject {

    private double speed;
    public double getWidth() {
        return getRectView().getWidth();
    }

    public Paddle(Rectangle view, double speed) {
        super(view);
        this.speed = speed;
    }

    public Rectangle getRectView() {
        return (Rectangle) view;
    }

    // Tự xử lý di chuyển và kiểm tra biên
    public void moveLeft(double leftBoundary) {
        
            view.setLayoutX(getX() - speed);
        
    }

    public void moveRight(double rightBoundary) {
        double width = getRectView().getWidth();
        // rightBoundary chính là chiều rộng màn hình
        
            view.setLayoutX(getX() + speed);
        
    }
    
    public void expand() {
    Rectangle rect = getRectView();

    // Lấy tâm hiện tại của paddle
    double centerX = rect.getX() + rect.getWidth() / 2;

    // Tăng 50% chiều rộng
    rect.setWidth(rect.getWidth() * 1.5);

    // Căn lại để paddle vẫn nằm giữa vị trí cũ
    rect.setX(centerX - rect.getWidth() / 2);
    }
    
}