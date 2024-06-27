package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class db {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/login"; // Replace with the name of your database
        String username = "root";
        String password = ""; // Replace with your MySQL password if you have set one

        System.out.println("Connecting to the database...");

        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            System.out.println("Connected to the database!");
            // Perform database operations here
            
            // For example, registering a new user
            String name = "exampleName"; // Replace with actual user input
            String passwordValue = "examplePassword"; // Replace with actual user input
            String confirmValue = "examplePassword"; // Replace with actual user input

            if (!passwordValue.equals(confirmValue)) {
                System.out.println("Passwords do not match!");
            } else {
                String sql = "INSERT INTO `create` (name, password) VALUES (?, ?)";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, name);
                statement.setString(2, passwordValue);

                int rowsInserted = statement.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("A new user has been registered successfully!");
                }

                statement.close();
            }

            connection.close();
        } catch (SQLException e) {
            System.out.println("Connection failed!");
            e.printStackTrace();
        }
    }
}
