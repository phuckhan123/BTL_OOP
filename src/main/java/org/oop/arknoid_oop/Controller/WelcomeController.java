package org.oop.arknoid_oop.Controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image; // Đảm bảo đã import
import javafx.stage.Stage;
import org.oop.arknoid_oop.ArknoidApplication;

import java.io.IOException;
import java.net.URL; // Cần import URL

public class WelcomeController {
    @FXML
    private Button startButton;
    @FXML
    private Button leaderboardButton;

    @FXML
    public void onStart() {
        try {
            FXMLLoader loader = new FXMLLoader(ArknoidApplication.class.getResource("/game-view.fxml"));
            Scene gameScene = new Scene(loader.load());
            GameController controller = loader.getController();
            controller.initScene(gameScene);

            // Load CSS file from resources
            URL cssUrl = ArknoidApplication.class.getResource("/css/mainStyle.css");
            if (cssUrl != null) {
                gameScene.getStylesheets().add(cssUrl.toExternalForm());
            } else {
                System.err.println("Không tìm thấy tệp CSS: css/mainStyle.css");
            }

            Stage stage = new Stage(); // Đây là cửa sổ GAME
            stage.setScene(gameScene);
            stage.setTitle("Arknoid Game");

            // ✨ SỬA LỖI COMMENT: Cách tải Image đúng
            URL iconUrl = ArknoidApplication.class.getResource("/org/oop/arknoid_oop/icons/book_icon.png");
            if (iconUrl != null) {
                Image image = new Image(iconUrl.toExternalForm());
                stage.getIcons().add(image);
            } else {
                System.err.println("Không tìm thấy tệp icon: icons/book_icon.png");
            }

            stage.setOnCloseRequest(event -> {
                Platform.exit();
                System.exit(0);
            });

            stage.show(); // Hiển thị cửa sổ game

            // Lấy và đóng cửa sổ WELCOME (currentStage)
            Stage currentStage = (Stage) startButton.getScene().getWindow();
            currentStage.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void onLeaderboardClick() {
        try {
            FXMLLoader loader = new FXMLLoader(ArknoidApplication.class.getResource("leaderboard-view.fxml"));
            Scene scene = new Scene(loader.load());

            URL cssUrl = ArknoidApplication.class.getResource("/css/mainStyle.css");
            if (cssUrl != null) {
                scene.getStylesheets().add(cssUrl.toExternalForm());
            }

            Stage stage = new Stage();
            stage.setTitle("Bảng Xếp Hạng");
            stage.setScene(scene);
            stage.setResizable(false); // Bạn nên tắt co giãn cho cửa sổ pop-up

            stage.show(); // Hiển thị cửa sổ Bảng xếp hạng

        } catch (IOException e) {
            System.err.println("Không thể tải leaderboard-view.fxml");
            e.printStackTrace();
        }
    }
}