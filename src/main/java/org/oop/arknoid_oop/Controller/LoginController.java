package org.oop.arknoid_oop.Controller;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.oop.arknoid_oop.ArknoidApplication;
import org.oop.arknoid_oop.Entity.SoundManager;
import org.oop.arknoid_oop.Service.DataService;
import org.oop.arknoid_oop.Service.UserService;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginController implements Initializable {
    private static final Logger LOGGER = Logger.getLogger(LoginController.class.getName());

    @FXML
    private TextField usernameInput;

    @FXML
    private TextField passwordInput;

    @FXML
    private Button loginButton;

    @FXML
    private ProgressIndicator progressIndicator;

    @FXML
    private ImageView loginImageView;

    @FXML
    private Button registerButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        SoundManager.getInstance().playMusic();
        progressIndicator.setVisible(false);
        String imagePath = "resources/images/login.png";
        Image image = new Image(new File(imagePath).toURI().toString());
        loginImageView.setImage(image);
    }

    public void registerAction() throws IOException {
        FXMLLoader loader = new FXMLLoader(ArknoidApplication.class.getResource("register-view.fxml"));
        Scene mainScene = new Scene(loader.load());
        Stage stage = new Stage();
        stage.setScene(mainScene);
        URL cssUrl = ArknoidApplication.class.getResource("/css/mainStyle.css");
        if (cssUrl != null) {
            mainScene.getStylesheets().add(cssUrl.toExternalForm());
        } else {
            System.err.println("Không tìm thấy tệp CSS: css/mainStyle.css");
        }

        stage.show();
        Stage currentStage = (Stage) registerButton.getScene().getWindow();
        currentStage.setOnCloseRequest(event2 -> {
            Platform.exit();
            System.exit(0);
        });
        currentStage.close();
    }

    public void enterkey(KeyEvent ke) {
        if (ke.getCode().equals(KeyCode.ENTER)) {
            loginButton.fire();
        }
    }

    public void loginAction(ActionEvent event) {
        progressIndicator.setVisible(true);
        String username = usernameInput.getText();
        String password = passwordInput.getText();
        Task<Boolean> task = new Task<>() {
            @Override
            protected Boolean call() {
                try {
                    return UserService.login(username, password);
                } catch (SQLException e) {
                    LOGGER.log(Level.WARNING, "Something went wrong!");
                    return false;
                }
            }
        };

        task.setOnSucceeded(event1 -> {
            progressIndicator.setVisible(false);
            boolean loginSuccessful = task.getValue();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            if (loginSuccessful) {
                DataService.username = usernameInput.getText();
                alert.setTitle("Welcome");
                alert.setHeaderText("Welcome " + username);
                alert.showAndWait();
                try {
                    FXMLLoader loader = new FXMLLoader(ArknoidApplication.class.getResource("welcome-view.fxml"));
                    Scene mainScene = new Scene(loader.load());
                    URL cssURL = ArknoidApplication.class.getResource("/css/mainStyle.css");
                    if (cssURL != null) {
                        mainScene.getStylesheets().add(cssURL.toExternalForm());
                    } else {
                        System.err.println("Không tìm thấy file mainStyle.css!");
                    }
                    Stage stage = new Stage();
                    stage.setScene(mainScene);
                    stage.show();

                    Stage currentStage = (Stage) loginButton.getScene().getWindow();
                    currentStage.setOnCloseRequest(event2 -> {
                        Platform.exit();
                        System.exit(0);
                    });
                    currentStage.close();
                } catch (Exception e) {
                    LOGGER.log(Level.WARNING, "Something went wrong!");
                }
            } else {
                alert.setTitle("Invalid credentials");
                alert.setHeaderText("Invalid username or password");
                alert.showAndWait();
            }
        });

        new Thread(task).start();
    }
}