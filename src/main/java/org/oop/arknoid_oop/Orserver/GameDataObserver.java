package org.oop.arknoid_oop.Orserver;

import javafx.animation.ScaleTransition;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.oop.arknoid_oop.Entity.SoundManager;

public class GameDataObserver implements Observer {
    private final Text scoreText;
    private final Text livesText;

    public GameDataObserver(Text scoreText, Text livesText) {
        this.scoreText = scoreText;
        this.livesText = livesText;

        scoreText.setFill(Color.WHITE);
        scoreText.setFont(Font.font("Arial", 23));

        livesText.setFill(Color.WHITE);
        livesText.setFont(Font.font("Arial", 23));
    }

    @Override
    public void update(GameData gameData) {
        // Cập nhật UI
        scoreText.setText("⭐ " + gameData.getScore());

        String heart = "\u2764"; // ♥ không bị ô vuông
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < gameData.getLives(); i++) sb.append(heart).append(" ");
        livesText.setText(sb.toString().trim());

        // --- Âm thanh và hiệu ứng ---
        if (gameData.isScoreJustChanged()) {
            SoundManager.getInstance().playSound("brickBreak");
            bounce(scoreText);
        }
        if (gameData.isLifeJustLost()) {
            SoundManager.getInstance().playSound("deaths");
        }
        if (gameData.isHitUnbreakable()) {
            SoundManager.getInstance().playSound("brickUnbreakable");
        }
        if (gameData.isLevelJustWon()) {
            SoundManager.getInstance().playSound("winLevel");
        }
        if (gameData.isGameJustOver()) {
            SoundManager.getInstance().playSound("gameOver");
        }

        // Màu cảnh báo khi sắp hết mạng
        livesText.setFill(gameData.getLives() <= 1 ? Color.RED : Color.WHITE);
    }

    private void bounce(Text text) {
        ScaleTransition st = new ScaleTransition(Duration.millis(200), text);
        st.setFromX(1.0);
        st.setFromY(1.0);
        st.setToX(1.2);
        st.setToY(1.2);
        st.setAutoReverse(true);
        st.setCycleCount(2);
        st.play();
    }
}
