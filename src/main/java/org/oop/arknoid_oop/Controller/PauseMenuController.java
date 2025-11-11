package org.oop.arknoid_oop.Controller;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane; // Hoặc VBox, tùy file FXML của bạn

public class PauseMenuController {

    @FXML
    private Button resumeButton;

    @FXML
    private Button quitButton;

    // Node gốc của file pause-menu.fxml
    @FXML
    private AnchorPane pauseRoot;

    // Biến để lưu tham chiếu đến GameController
    private GameController gameController;

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }


    public Node getRoot() {
        return pauseRoot;
    }

    @FXML
    public void initialize() {
        // Cài đặt hành động cho các nút
        resumeButton.setOnAction(event -> {
            if (gameController != null) {
                // Ra lệnh cho GameController tiếp tục
                gameController.resumeGame();
            }
        });

        quitButton.setOnAction(event -> {
            if (gameController != null) {
                // Ra lệnh cho GameController thoát
                gameController.quitToMenu();
            }
        });
    }
}