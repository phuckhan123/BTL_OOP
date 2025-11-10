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
        progressIndicator.setVisible(false);
        String imagePath = "resources/images/login.png";
        Image image = new Image(new File(imagePath).toURI().toString());
        loginImageView.setImage(image);
    }

    public void registerAction() throws IOException {
        FXMLLoader loader = new FXMLLoader(ArknoidApplication.class.getResource("register-view.fxml"));
        Scene mainScene = new Scene(loader.load());
//                    mainScene.getStylesheets().add(ArknoidApplication.class.getResource("css/mainStyle.css").toExternalForm());
        Stage stage = new Stage();
        stage.setScene(mainScene);
//                    stage.setTitle("Library Management");
//                    Image image = new Image(String.valueOf(ArknoidApplication.class.getResource("icons/book_icon.png")));
//                    stage.getIcons().add(image);
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
                alert.setTitle("Welcome");
                alert.setHeaderText("Welcome " + username);
                alert.showAndWait();
                try {
                    FXMLLoader loader = new FXMLLoader(ArknoidApplication.class.getResource("welcome-view.fxml"));
                    Scene mainScene = new Scene(loader.load());
//                    mainScene.getStylesheets().add(ArknoidApplication.class.getResource("css/mainStyle.css").toExternalForm());
                    Stage stage = new Stage();
                    stage.setScene(mainScene);
//                    stage.setTitle("Library Management");
//                    Image image = new Image(String.valueOf(ArknoidApplication.class.getResource("icons/book_icon.png")));
//                    stage.getIcons().add(image);
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