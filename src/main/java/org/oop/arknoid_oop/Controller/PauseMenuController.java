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

    @FXML
    private AnchorPane pauseRoot;

    private GameController gameController;

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }


    public Node getRoot() {
        return pauseRoot;
    }

    @FXML
    public void initialize() {
        resumeButton.setOnAction(event -> {
            if (gameController != null) {
                gameController.resumeGame();
            }
        });

        quitButton.setOnAction(event -> {
            if (gameController != null) {

                gameController.quitToMenu();
            }
        });
    }
}