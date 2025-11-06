package org.oop.arknoid_oop.Level;

import javafx.scene.layout.Pane;
import org.oop.arknoid_oop.Entity.Brick;

import java.util.ArrayList;
import java.util.List;

public class LevelManager {

    private List<Brick> bricks = new ArrayList<>();
    private int currentLevelNumber = 1;
    private int maxLevel = 2;

    public List<Brick> loadCurrentLevel(Pane brickContainer) {
        bricks = LevelFactory.buildLevel(currentLevelNumber, brickContainer);
        return bricks;
    }

    public boolean hasNextLevel() {
        return currentLevelNumber < maxLevel;
    }

    public void nextLevel() {
        if (hasNextLevel()) {
            currentLevelNumber++;
        }
    }

    public int getCurrentLevelNumber() {
        return currentLevelNumber;
    }

    public void reset() {
        currentLevelNumber = 1;
    }
}
