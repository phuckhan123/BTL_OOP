package org.oop.arknoid_oop.Level;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import org.oop.arknoid_oop.Entity.Brick;

import java.io.BufferedReader;
import java.net.URL;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class LevelFactory {

    private static final double BRICK_WIDTH = 70;
    private static final double BRICK_HEIGHT = 30;
    private static final double BRICK_GAP = 5;

    public static List<Brick> buildLevel(int level, Pane brickContainer) {
    List<Brick> bricks = new ArrayList<>();

    try (BufferedReader reader = new BufferedReader(
            new InputStreamReader(LevelFactory.class.getResourceAsStream("/levels/level" + level + ".txt")))) {

        String line;
        int row = 0;
        while ((line = reader.readLine()) != null) {
            for (int col = 0; col < line.length(); col++) {
                char type = line.charAt(col);
                if (type == ' ') continue;

                double x = col * (BRICK_WIDTH + BRICK_GAP);
                double y = row * (BRICK_HEIGHT + BRICK_GAP);
                Brick brick = createBrickAt(x, y, type);

                brickContainer.getChildren().add(brick.getView());
                bricks.add(brick);
            }
            row++;
        }
    } catch (Exception e) {
        e.printStackTrace();
    }

    return bricks;
}


   private static Brick createBrickAt(double x, double y, char type) {
    String imgPath;
    int health = 1;
    int score = 10;

    switch (type) {
        case '1' -> {
            imgPath = "/images/Brickheath1.jpg";
            health = 1;
            score = 10;
        }
        case '2' -> {
            imgPath = "/images/Brickheath2.jpg";
            health = 2;
            score = 25;
        }
        case 'U' -> {
            imgPath = "/images/Bricku.jpg";
            health = -1;
            score = 0;
        }
        default -> {
            imgPath = "/images/Brickheath1.jpg";
        }
    }

    URL imageUrl = LevelFactory.class.getResource(imgPath);
    if (imageUrl == null) {
        // If the specific texture isn't found, fall back to the default brick texture
        imageUrl = LevelFactory.class.getResource("/images/Brickheath1.jpg");
        if (imageUrl == null) {
            throw new RuntimeException("Could not find default brick texture!");
        }
    }
    ImageView view = new ImageView(new Image(imageUrl.toExternalForm()));
    view.setFitWidth(BRICK_WIDTH);
    view.setFitHeight(BRICK_HEIGHT);
    view.setX(x);
    view.setY(y);

    return new Brick(view, score, health);
   }


}
