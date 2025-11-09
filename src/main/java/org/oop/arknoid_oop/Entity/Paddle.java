// File: src/main/java/org/oop/arknoid_oop/Models/Paddle.java
package org.oop.arknoid_oop.Entity;

import javafx.animation.PauseTransition;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;


public class Paddle extends GameObject {

    private double speed;
    private double paddleWidthTmp;
    public double getWidth() {
        return getRectView().getWidth();
    }

    public Paddle(Rectangle view, double speed) {
        super(view);
        paddleWidthTmp = view.getWidth();
        this.speed = speed;
    }

    public Rectangle getRectView() {
        return (Rectangle) view;
    }

    public void reset(){
        Rectangle rect = getRectView();
        rect.setWidth(paddleWidthTmp);
    }
    // Tự xử lý di chuyển và kiểm tra biên
    public void moveLeft(double leftBoundary) {
        if (getX() > leftBoundary) {
            view.setLayoutX(getX() - speed);
        }
        
    }

    public void moveRight(double rightBoundary) {
        double width = getRectView().getWidth();
        // rightBoundary chính là chiều rộng màn hình
        if (getX() + width < rightBoundary) {
            view.setLayoutX(getX() + speed);
        }
        
    }
    
    public void expand() {
        Rectangle rect = getRectView();

       
        
        // Tăng 50% chiều rộng
        rect.setWidth(rect.getWidth() * 1.5);

        PauseTransition timer = new PauseTransition(Duration.millis(5000));
            
            timer.setOnFinished(event -> {
            rect.setWidth(rect.getWidth() / 1.5);
            });
            // e. Bắt đầu đếm
        timer.play();


    }
    
}