package org.oop.arknoid_oop;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class ArknoidApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader =
                new FXMLLoader(ArknoidApplication.class.getResource("welcome-view.fxml"));
        Scene welcomeScene = new Scene(loader.load());
        URL cssUrl = ArknoidApplication.class.getResource("/css/mainStyle.css");
        if (cssUrl != null) {
            welcomeScene.getStylesheets().add(cssUrl.toExternalForm());
        } else {
            System.err.println("LỖI: Không tìm thấy file /css/mainStyle.css");
        }
        stage.setTitle("Arknoid Game");
        stage.setScene(welcomeScene);
        stage.show();
    }
    public static void main(String[] args) {
        launch(); // 🔹 Đây là nơi chương trình thật sự bắt đầu
    }
}
