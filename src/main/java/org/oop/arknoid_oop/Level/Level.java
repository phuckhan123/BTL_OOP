package org.oop.arknoid_oop.Level;

import org.oop.arknoid_oop.Entity.Brick;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

public class Level {
    private final int levelNumber;
    private final String layoutFile;
    private final double ballSpeed;
    private final List<Brick> bricks;

    public Level(int levelNumber, String layoutFile, double ballSpeed) {
        this.levelNumber = levelNumber;
        this.layoutFile = layoutFile;
        this.ballSpeed = ballSpeed;
        this.bricks = new ArrayList<>();
    }

    public int getLevelNumber() {
        return levelNumber;
    }

    public String getLayoutFile() {
        return layoutFile;
    }

    public double getBallSpeed() {
        return ballSpeed;
    }

    public List<Brick> getBricks() {
        return bricks;
    }

    public void addBrick(Brick brick) {
        bricks.add(brick);
    }

    // Xóa toàn bộ brick trong level (dùng khi restart)
    public void clearBricks(Pane brickContainer) {
        brickContainer.getChildren().clear();
        bricks.clear();
    }
}
