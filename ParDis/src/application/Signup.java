package application;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class Signup extends Application {

    Stage window;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        String url = "jdbc:mysql://localhost:3306/login";
        String username = "root";
        String password = "";

        window = primaryStage;
        window.setTitle("BRAINZZZ");

        // GridPane
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(20, 10, 10, 20));
        grid.setVgap(5);
        grid.setHgap(10);

        ImageView logo = new ImageView(new Image("logooo.png"));
        ImageView bg = new ImageView(new Image("bg.png"));
        grid.addRow(1, logo);

        // Name Label
        Label nameLabel = new Label("Enter Username: ");
        GridPane.setConstraints(nameLabel, 0, 4);

        TextField nameInput = new TextField();
        nameInput.setPromptText("Enter Username");
        GridPane.setConstraints(nameInput, 0, 5);
        nameInput.setPrefHeight(35);
        nameInput.setPrefWidth(250);

        // Password Label
        Label passLabel = new Label("Enter Password: ");
        GridPane.setConstraints(passLabel, 0, 7);

        PasswordField passInput = new PasswordField();
        passInput.setPromptText("Enter Password");
        GridPane.setConstraints(passInput, 0, 8);
        passInput.setPrefHeight(35);
        passInput.setPrefWidth(250);

        // Confirm Password Label
        Label confirmLabel = new Label("Confirm Password: ");
        GridPane.setConstraints(confirmLabel, 0, 10);

        PasswordField confirmInput = new PasswordField();
        confirmInput.setPromptText("Confirm Password");
        GridPane.setConstraints(confirmInput, 0, 11);
        confirmInput.setPrefHeight(35);
        confirmInput.setPrefWidth(250);

        Button signUpButton = new Button("SIGN-UP");
        signUpButton.setPrefWidth(250);

        // Inserting the new account to the database
        signUpButton.setOnAction(e -> {
            String name = nameInput.getText();
            String passwordValue = passInput.getText();
            String confirmValue = confirmInput.getText();

            if (!passwordValue.equals(confirmValue)) {
                System.out.println("Passwords do not match!");
            } else {
            	  try {
                      Connection connection = DriverManager.getConnection(url, username, password);

                      String checkSql = "SELECT * FROM `create` WHERE name = ?";
                      PreparedStatement checkStatement = connection.prepareStatement(checkSql);
                      checkStatement.setString(1, name);

                      ResultSet resultSet = checkStatement.executeQuery();

                      if (resultSet.next()) {
                          System.out.println("Username already taken!");
                      } else {
                          String insertSql = "INSERT INTO `create` (name, password) VALUES (?, ?)";
                          PreparedStatement insertStatement = connection.prepareStatement(insertSql);
                          insertStatement.setString(1, name);
                          insertStatement.setString(2, passwordValue);

                          int rowsInserted = insertStatement.executeUpdate();
                          if (rowsInserted > 0) {
                        	  System.out.println("Account Created");
                        	  try {
                                  window.close();
                                  Stage loginStage = new Stage();
                                  new Login().start(loginStage);
                              } catch (Exception ex) {
                                  ex.printStackTrace();
                              }
                          }

                          insertStatement.close();
                      }

                      resultSet.close();
                      checkStatement.close();
                      connection.close();
                  } catch (SQLException ex) {
                      System.out.println("Connection failed or error in SQL statement!");
                      ex.printStackTrace();
                  }
              }
          });

        HBox hboxsignup = new HBox();
        hboxsignup.setAlignment(Pos.CENTER);
        hboxsignup.getChildren().addAll(signUpButton);
        GridPane.setConstraints(hboxsignup, 0, 17);

        // Login Label
        Label logLabel = new Label("Already Have an Account? ");
        Hyperlink logInLink = new Hyperlink("Log-in");
        logInLink.getStyleClass().add("hyperlink-sign-up");
        logLabel.getStyleClass().add("label-sign-up");

        HBox hboxlogin = new HBox();
        hboxlogin.setAlignment(Pos.CENTER);
        hboxlogin.getChildren().addAll(logLabel, logInLink);
        GridPane.setConstraints(hboxlogin, 0, 18);

        logInLink.setOnAction(e -> {
            try {
                window.close();
                Stage loginStage = new Stage();
                new Login().start(loginStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        grid.getChildren().addAll(nameLabel, nameInput, passLabel, passInput, confirmLabel, confirmInput, hboxsignup, hboxlogin);

        Scene scene = new Scene(grid, 960, 520);
        scene.getStylesheets().add("loginStyle.css");
        window.setScene(scene);
        window.show();
    }
}
