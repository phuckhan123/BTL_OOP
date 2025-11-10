package org.oop.arknoid_oop.Service;

import org.mindrot.jbcrypt.BCrypt;
import org.oop.arknoid_oop.Database.Database;

import java.sql.*;
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
