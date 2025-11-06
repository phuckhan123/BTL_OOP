package org.oop.arknoid_oop.Entity;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import org.oop.arknoid_oop.Orserver.GameData;
import java.util.List;

public class PowerUp {
    private ImageView imageView;
    private String type;
    private double speedY = 0.5;

    public PowerUp(double x, double y, String type) {
        this.type = type;

        String imagePath = "/images/" + type + ".png";
        Image img;
        try {
            img = new Image(getClass().getResourceAsStream(imagePath));
        } catch (Exception e) {
            System.out.println(imagePath);
            img = new Image(getClass().getResourceAsStream("/images/ball.jpg"));
        }

        imageView = new ImageView(img);
        imageView.setFitWidth(25);
        imageView.setFitHeight(25);
        imageView.setX(x);
        imageView.setY(y);
    }

    public ImageView getView() {
        return imageView;
    }

    public String getType() {
        return type;
    }

    public void move() {
        imageView.setY(imageView.getY() + speedY);
    }

    public boolean isOutOfBottom(double sceneHeight) {
        return imageView.getY() > sceneHeight;
    }

    // ⚙️ Cập nhật: thêm danh sách bóng & root
    public void applyEffect(Paddle paddle, GameData gameData, Ball baseBall,
                            List<Ball> balls, Pane root) {
        switch (type) {
            case "paddleLonger":
                paddle.expand();
                break;
            case "addLife":
                gameData.addLife();
                break;
            case "ballFaster":
                baseBall.gotFaster();
                break;
            
        }
    }


    

}
