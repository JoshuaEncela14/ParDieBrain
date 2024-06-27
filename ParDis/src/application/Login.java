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

public class Login extends Application {
	
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
        grid.addRow(1, logo);

        // Name Label
        Label nameLabel = new Label("Enter Name: ");
        GridPane.setConstraints(nameLabel, 0, 4);

        TextField nameInput = new TextField();
        nameInput.setPromptText("Enter Name");
        GridPane.setConstraints(nameInput, 0, 5);
        nameInput.setPrefHeight(35);
        nameInput.setPrefWidth(250);
        nameInput.getStyleClass().add("name-field-container");

        // Password Label
        Label passLabel = new Label("Enter Password: ");
        GridPane.setConstraints(passLabel, 0, 7);

        PasswordField passInput = new PasswordField();
        passInput.setPromptText("Enter Password");
        passInput.setPrefHeight(35);
        passInput.setPrefWidth(500);
        passInput.getStyleClass().add("password-field");

        // ImageView for toggling password visibility
        ImageView eyeImageView = new ImageView(new Image("Blind.png"));
        ImageView eyeImageView2 = new ImageView(new Image("Eye.png"));
        
        eyeImageView.setFitHeight(24);
        eyeImageView.setFitWidth(24);
        eyeImageView.setPreserveRatio(true);

        // HBox to contain PasswordField and ImageView
        HBox passBox = new HBox();
        passBox.setAlignment(Pos.CENTER_LEFT);
        passBox.getChildren().addAll(passInput, eyeImageView);
        GridPane.setConstraints(passBox, 0, 8);
        passBox.getStyleClass().add("password-field-container");
//        passBox.setStyle("-fx-background-color: red;");
        
        
        nameInput.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
            	nameInput.getStyleClass().add("password-field-container-active");
            } else {
            	nameInput.getStyleClass().remove("password-field-container-active");
            }
        });
        
        passInput.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                passBox.getStyleClass().add("password-field-container-active");
            } else {
                passBox.getStyleClass().remove("password-field-container-active");
            }
        });

        // Toggle password visibility
        eyeImageView.setOnMousePressed(event -> {
            if (passInput.isVisible()) {
                passInput.setVisible(false);
            	passInput.setStyle("-fx-background-color: transparent;");
                passInput.setManaged(false);
                
            } else {
                passInput.setVisible(true);
            	passInput.setStyle("-fx-background-color: transparent;");
                passInput.setManaged(true);
            }

        });

        Button login = new Button("LOG-IN");
        login.setPrefWidth(250);
        login.getStyleClass().add("button-login");

        // Database connection and login logic
        login.setOnAction(e -> {
            String name = nameInput.getText();
            String passwordValue = passInput.getPromptText().equals("Enter Password") ? passInput.getText() : passInput.getPromptText();

            try {
                Connection connection = DriverManager.getConnection(url, username, password);
                String sql = "SELECT * FROM `create` WHERE name = ? AND password = ?";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, name);
                statement.setString(2, passwordValue);

                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    System.out.println("Login successful!");
                    try {
                        window.close();

                        Stage categoriesStage = new Stage();
                        new Categories().start(categoriesStage);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                } else {
                    System.out.println("Invalid username or password.");
                    // Display error message or handle unsuccessful login
                }

                resultSet.close();
                statement.close();
                connection.close();
            } catch (SQLException ex) {
                System.out.println("Connection failed or error in SQL statement!");
                ex.printStackTrace();
            }
        });

        // 
        HBox hboxlogin = new HBox();
        hboxlogin.setAlignment(Pos.CENTER);
        hboxlogin.getChildren().add(login);
        GridPane.setConstraints(hboxlogin, 0, 15);

        // Name Label
        Label signLabel = new Label("Don't Have an Account? ");
        Hyperlink signUpLink = new Hyperlink("Sign-up");
        signUpLink.getStyleClass().add("hyperlink-sign-up");
        signLabel.getStyleClass().add("label-sign-up");

        HBox hboxsignup = new HBox();
        hboxsignup.setAlignment(Pos.CENTER);
        hboxsignup.getChildren().addAll(signLabel, signUpLink);
        GridPane.setConstraints(hboxsignup, 0, 16);

        signUpLink.setOnAction(e -> {
            try {
                window.close();

                Stage signUpStage = new Stage();
                new Signup().start(signUpStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        grid.getChildren().addAll(nameLabel, nameInput, passLabel, passBox, hboxlogin, hboxsignup);

        Scene scene = new Scene(grid, 960, 520);
        scene.getStylesheets().add("loginStyle.css");
        window.setScene(scene);
        window.show();
    }
}
