package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Result extends Application {
    Stage window;
    
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

    	 primaryStage.initStyle(StageStyle.UNDECORATED);

         GridPane grid = new GridPane();
         grid.setAlignment(Pos.CENTER);
         grid.setPadding(new Insets(20, 10, 10, 20));
         grid.setVgap(5);
         grid.setHgap(10);
         grid.setGridLinesVisible(true); // Uncomment if you want to see grid lines

         ImageView starGood = new ImageView(new Image("yeyStar.png"));
         ImageView starBad = new ImageView(new Image("notYeyStar.png"));
         Label stageLevel = new Label("Level 1");
         GridPane.setConstraints(stageLevel, 0, 0, 3, 1);
         stageLevel.setAlignment(Pos.CENTER);

         HBox stars = new HBox(10);
         stars.setAlignment(Pos.CENTER);
         stars.getChildren().addAll(starGood, starGood, starBad);
         GridPane.setConstraints(stars, 0, 1, 3, 1);

         grid.getChildren().addAll(stageLevel, stars);

         Scene scene = new Scene(grid, 960, 520);
         scene.getStylesheets().add("design.css"); // Make sure design.css is in your resources

         primaryStage.setScene(scene);
         primaryStage.show();
//        Button close = new Button("Close");
//
//        window.initStyle(StageStyle.TRANSPARENT);
//        window.initModality(Modality.APPLICATION_MODAL);
//
//        VBox modalLayout = new VBox(10);
//        modalLayout.setPrefSize(300, 430); 
//        modalLayout.setAlignment(Pos.TOP_CENTER);
//        modalLayout.getStyleClass().add("modal-dialog");
//        modalLayout.getChildren().addAll(stageLevel, close);
//
//        close.setOnAction(e -> window.close());
//
//        Scene modalScene = new Scene(modalLayout);
//        modalScene.setFill(Color.TRANSPARENT);
//
//        
//        modalScene.getStylesheets().add("design.css");
//
//        window.setScene(modalScene);
//        window.showAndWait();
    }
}
