package org.oop.arknoid_oop.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private static final String URL =
            "jdbc:mysql://localhost:3306/arknoid";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection connect() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connected to the database");
        } catch (SQLException e) {
            System.out.println("Failed to connect to the database " + e.getMessage());
        }
        return connection;
    }
}