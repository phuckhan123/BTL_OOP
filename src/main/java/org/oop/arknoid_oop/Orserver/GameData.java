package org.oop.arknoid_oop.Orserver;

import java.util.ArrayList;
import java.util.List;

public class GameData {
    private int score;
    private int lives;
    private final List<Observer> observers = new ArrayList<>();

    private boolean scoreJustChanged = false;
    private boolean lifeJustLost = false;
    private boolean levelJustWon = false;
    private boolean gameJustOver = false;
    private boolean hitUnbreakable = false;

    public GameData(int initialLives) {
        this.lives = initialLives;
        this.score = 0;
    }

    public void addObserver(Observer o) {
        observers.add(o);
    }

    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    private void notifyObservers() {
        for (Observer o : observers) {
            o.update(this);
        }
        resetFlags();
    }

    private void resetFlags() {
        scoreJustChanged = false;
        lifeJustLost = false;
        levelJustWon = false;
        gameJustOver = false;
        hitUnbreakable = false;
    }

    // --- Các hành động logic ---
    public void addScore(int value) {
        score += value;
        scoreJustChanged = true;
        notifyObservers();
    }

    public void addLife(){
        if(lives<3)
        lives++;
        notifyObservers();
    }

    public void resetLife(){
        
        lives = 3;
        notifyObservers();
    }

    public void loseLife() {
        lives--;
        lifeJustLost = true;
        notifyObservers();
    }

    public void hitUnbreakableBrick() {
        hitUnbreakable = true;
        notifyObservers();
    }

    public void winLevel() {
        levelJustWon = true;
        notifyObservers();
    }

    public void gameOver() {
        gameJustOver = true;
        notifyObservers();
    }

    // --- Getter ---
    public int getScore() { return score; }
    public int getLives() { return lives; }

    public boolean isScoreJustChanged() { return scoreJustChanged; }
    public boolean isLifeJustLost() { return lifeJustLost; }
    public boolean isLevelJustWon() { return levelJustWon; }
    public boolean isGameJustOver() { return gameJustOver; }
    public boolean isHitUnbreakable() { return hitUnbreakable; }
}
