package org.oop.arknoid_oop.Service;

import org.mindrot.jbcrypt.BCrypt;
import org.oop.arknoid_oop.Database.Database;
import org.oop.arknoid_oop.Entity.UserScore;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserService {
    private static final Connection connect = Database.connect();

    public static boolean login(String username, String password) throws SQLException {
        String sql = "SELECT password FROM User WHERE username = ?";

        try (PreparedStatement statement = connect.prepareStatement(sql)) {
            statement.setString(1, username);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    String hashedPasswordFromDB = rs.getString("password");
                    return BCrypt.checkpw(password, hashedPasswordFromDB);
                }

                return false;
            }
        }
    }

    public static void updateHighScore(String username, int newScore) throws SQLException{
        String getScoreSql = "SELECT highest_score FROM User WHERE username = ?";
        String updateScoreSql = "UPDATE User SET highest_score = ? WHERE username = ?";
        int currentHighScore = 0;
        try (PreparedStatement getStatement = connect.prepareStatement(getScoreSql)) {
            getStatement.setString(1, username);
            try (ResultSet rs = getStatement.executeQuery()) {
                if (rs.next()) {
                    currentHighScore = rs.getInt("highest_score");
                }
            }
    }
        if (newScore > currentHighScore) {
            try (PreparedStatement updateStatement = connect.prepareStatement(updateScoreSql)) {
                updateStatement.setInt(1, newScore);
                updateStatement.setString(2, username);
                updateStatement.executeUpdate();
                System.out.println("Updated new high score for " + username + ": " + newScore);
            }
        }
    }
    public static List<UserScore> getLeaderboard() throws SQLException{
        List<UserScore> leaderboard = new ArrayList<>();
        String sql = "SELECT username, highest_score FROM User ORDER BY highest_score DESC LIMIT 10";
        try (PreparedStatement statement = connect.prepareStatement(sql)) {
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    String username = rs.getString("username");
                    int score = rs.getInt("highest_score");
                    leaderboard.add(new UserScore(username, score));
                }
            }
        }
        return leaderboard;
    }



    public static boolean register(String username, String password, String confirmPassword) throws Exception {
        if (!Objects.equals(password, confirmPassword)) {
            throw new Exception("Something went wrong");
        }
        String checkSql = "SELECT 1 FROM User WHERE username = ?";
        try (PreparedStatement checkStatement = connect.prepareStatement(checkSql)) {
            checkStatement.setString(1, username);
            try (ResultSet rs = checkStatement.executeQuery()) {
                if (rs.next()) {
                    throw new Exception("Username existed!");
                }
            }
        }
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        String insertSql = "INSERT INTO User (username, password) VALUES (?, ?)";
        try (PreparedStatement insertStatement = connect.prepareStatement(insertSql)) {
            insertStatement.setString(1, username);
            insertStatement.setString(2, hashedPassword);
            int rowsAffected = insertStatement.executeUpdate();
            return rowsAffected > 0;
        }
    }

}
