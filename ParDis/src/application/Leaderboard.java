package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Leaderboard extends Application {

    Stage window;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
    	
    	window = primaryStage;
        window.setTitle("BRAINZZZ");

        // GridPane
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(20, 10, 10, 20));
        grid.setVgap(5);
        grid.setHgap(10);
        
        Scene scene = new Scene(grid, 960, 520);
        scene.getStylesheets().add("loginStyle.css");
        window.setScene(scene);
        window.show();
    }
    
}