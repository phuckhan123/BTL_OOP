// File: src/main/java/org/oop/arknoid_oop/Models/Ball.java
package org.oop.arknoid_oop.Entity;

import javafx.scene.shape.Circle;

public class Ball extends GameObject {

    private double dx;
    private double dy;
    private double speed;

    // ✨ 1. SỬA HÀM KHỞI TẠO (Constructor)
    // Xóa dx, dy khỏi tham số. Bóng sẽ bắt đầu đứng yên.
    public Ball(Circle view) {
        super(view);
        this.dx = 0;
        this.dy = 0;
        this.speed = 0;
    }

    public Circle getCircleView() {
        return (Circle) view;
    }

    public double getRadius() {
        // Lấy bán kính từ view
        return getCircleView().getRadius();
    }

    // Di chuyển bóng
    public void move() {
        view.setLayoutX(getX() + dx);
        view.setLayoutY(getY() + dy);
    }
    // Tự kiểm tra va chạm tường
    public void checkWallCollision(double sceneWidth, double sceneHeight) {
        double radius = getCircleView().getRadius();
        // Viền trái/phải
        if (getX() - radius <= 0 || getX() + radius >= sceneWidth) {
            dx *= -1;
        }
        // Viền trên
        if (getY() - radius <= 0) {
            dy *= -1;
        }
    }

    // Kiểm tra rơi ra ngoài
    public boolean isOutOfBottom(double sceneHeight) {
        return getY() + getCircleView().getRadius() > sceneHeight;
    }

    public void bounceY() {
        dy *= -1;
    }

    public void bounceX() {
        dx *= -1;
    }

    public void stop() {
        dx = 0;
        dy = 0;
    }
    public void setPosition(double x, double y) {
        view.setLayoutX(x);
        view.setLayoutY(y);
    }

    // ✨ 3. THÊM HÀM MỚI: Dùng để ra lệnh "phóng" hoặc "dừng"
    public void setVelocity(double dx, double dy) {
        this.dx = dx;
        this.dy = dy;
        // Tính toán lại tốc độ tổng (quan trọng cho logic nảy Arkanoid)
        this.speed = Math.sqrt(dx * dx + dy * dy);
    }
    public void calculatePaddleBounce(Paddle paddle) {
        // 1. Lấy tâm quả bóng
        double ballCenterX = getX();
        // 2. Lấy tâm của paddle
        double paddleCenterX = paddle.getX() + paddle.getWidth() / 2;
        // 3. Tính vị trí va chạm tương đối (từ -1.0 đến +1.0)
        double intersectX = ballCenterX - paddleCenterX;
        double normalizedIntersect = intersectX / (paddle.getWidth() / 2);

        // Đảm bảo giá trị không vượt quá -1.0 hoặc 1.0
        if (normalizedIntersect > 1.0) normalizedIntersect = 1.0;
        if (normalizedIntersect < -1.0) normalizedIntersect = -1.0;

        // 4. Tính góc nảy mới (bằng Radian)
        //    Góc 75 độ (5 * PI / 12) là góc nảy tối đa
        double maxBounceAngle = 5 * Math.PI / 12; // 75 độ
        double bounceAngle = normalizedIntersect * maxBounceAngle;

        // 5. Tính dx và dy mới dựa trên tốc độ (speed) và góc nảy
        //    LƯU Ý: Phải dùng speed, vì speed đã được set khi launchBall()
        dy = -speed * Math.cos(bounceAngle);
        dx = speed * Math.sin(bounceAngle);
    }
    public void resolveBrickCollision(Brick brick, double overlapWidth, double overlapHeight) {

        // Nếu không có va chạm (lỗi hiếm)
        if (overlapWidth < 0 || overlapHeight < 0) return;
        // So sánh kích thước vùng lấn
        if (overlapWidth < overlapHeight) {
            // Lấn theo chiều ngang ÍT HƠN -> Va chạm ngang
            // 1. Đẩy lùi bóng ra khỏi gạch (theo chiều ngang)
            //    dx là hướng VỪA MỚI va chạm (hướng CŨ)
            if (dx > 0) {
                // Đang đi sang PHẢI -> đẩy lùi về TRÁI một khoảng overlapWidth
                setPosition(getX() - overlapWidth, getY());
            } else {
                // Đang đi sang TRÁI -> đẩy lùi về PHẢI một khoảng overlapWidth
                setPosition(getX() + overlapWidth, getY());
            }

            // 2. Đổi hướng ngang
            bounceX();

        } else {
            // Lấn theo chiều dọc ÍT HƠN (hoặc bằng) -> Va chạm dọc
            // 1. Đẩy lùi bóng ra khỏi gạch (theo chiều dọc)
            //    dy là hướng VỪA MỚI va chạm (hướng CŨ)
            if (dy > 0) {
                // Đang đi XUỐNG -> đẩy lùi LÊN TRÊN một khoảng overlapHeight
                setPosition(getX(), getY() - overlapHeight);
            } else {
                // Đang đi LÊN TRÊN -> đẩy lùi XUỐNG một khoảng overlapHeight
                setPosition(getX(), getY() + overlapHeight);
            }

            // 2. Đổi hướng dọc
            bounceY();
        }
    }
}