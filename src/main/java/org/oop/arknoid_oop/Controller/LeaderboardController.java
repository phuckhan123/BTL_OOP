package org.oop.arknoid_oop.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.oop.arknoid_oop.Entity.UserScore;
import org.oop.arknoid_oop.Service.UserService;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class LeaderboardController implements Initializable {
    @FXML
    private TableView<UserScore> leaderboardTable;
    @FXML
    private TableColumn<UserScore, String> usernameColumn;
    @FXML
    private TableColumn<UserScore, Integer> scoreColumn;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        scoreColumn.setCellValueFactory(new PropertyValueFactory<>("score"));
        List<UserScore> topScores = new ArrayList<>();
        try {
            topScores = UserService.getLeaderboard();
            for(UserScore a : topScores) {
                System.out.println(a.getUsername() + "  "+ a.getScore());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        ObservableList<UserScore> data = FXCollections.observableArrayList(topScores);
        System.out.println(data.stream().count());
        leaderboardTable.setItems(data);
    }
}