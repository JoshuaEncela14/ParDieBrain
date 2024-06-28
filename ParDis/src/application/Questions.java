package application;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Questions extends Application {

    private Stage window;
    private int currentNumber = 1;
    private Timeline timeline;

    private String[] questions = {
            "Which number is a prime number?",
            "If you subtract 9 from 15, what do you get?",
            "What is the sum of 7 and 5?",
            "Which shape has four equal sides?",
            "If you have 10 apples and you give away 3, how many apples do you have left?"
    };
    private String[] answerTexts = {"6", "7", "8", "9"};
    private String[] imageFiles = {"A.png", "B.png", "C.png", "D.png"};

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        window.setTitle("BRAINZZZ");

        GridPane grid = createGridPane();
        HBox header = createHeader();
        HBox timer = createTimer();
        VBox questionOptions = createQuestionOptions();

        grid.getChildren().addAll(header, timer, createQuestionContainer(), questionOptions);

        Scene scene = new Scene(grid, 960, 520);
        scene.getStylesheets().add("design.css");
        window.setScene(scene);
        window.show();
    }

    private GridPane createGridPane() {
        GridPane grid = new GridPane();
        grid.setHgap(10);

        // use full width
        ColumnConstraints colConstraints = new ColumnConstraints();
        colConstraints.setPercentWidth(100);
        grid.getColumnConstraints().add(colConstraints);

        return grid;
    }

    private HBox createHeader() {
        Button lifelineOne = createLifelineButton("50:50");
        Button lifelineTwo = createLifelineButton("Pause");

        HBox lifelines = new HBox(10, lifelineOne, lifelineTwo);
        lifelines.setAlignment(Pos.CENTER);
        HBox.setMargin(lifelines, new Insets(0, 0, 0, 10));

        Label currentQuestion = new Label(currentNumber + " / 5");
        currentQuestion.getStyleClass().add("label-question");

        HBox questionTracking = new HBox(currentQuestion);
        questionTracking.getStyleClass().add("hbox-question-tracking");
        questionTracking.setAlignment(Pos.CENTER);
        questionTracking.setSpacing(10);

        HBox header = new HBox(10, lifelines, questionTracking);
        header.getStyleClass().add("hbox-questions-header");
        header.setAlignment(Pos.CENTER_LEFT);
        GridPane.setConstraints(header, 0, 0, GridPane.REMAINING, 1);

        return header;
    }

    private Button createLifelineButton(String text) {
        Button lifelineButton = new Button(text);
        lifelineButton.setPrefWidth(90);
        lifelineButton.setPrefHeight(40);
        lifelineButton.getStyleClass().add("lifeline-buttons");

        lifelineButton.setOnAction(event -> {
            if (text.equals("Pause")) {
                pauseTimer();
            } else if (text.equals("50:50")) {
                // 50:50 lifeline logic
            }
        });

        return lifelineButton;
    }

    private HBox createTimer() {
        HBox timer = new HBox();
        timer.getStyleClass().add("hbox-questions-timer");
        GridPane.setConstraints(timer, 0, 1, GridPane.REMAINING, 1);

        Rectangle timerBar = new Rectangle(960, 5, Color.web("#960A0A"));
        timer.getChildren().add(timerBar);

        timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(timerBar.widthProperty(), 960)),
                new KeyFrame(Duration.seconds(30), new KeyValue(timerBar.widthProperty(), 0))
        );
        timeline.setCycleCount(1);
        timeline.play();

        return timer;
    }

    private void pauseTimer() {
        timeline.pause();

        // Resume after 10 seconds
        Timeline resumeTimeline = new Timeline(
                new KeyFrame(Duration.seconds(10), e -> timeline.play())
        );
        resumeTimeline.play();
    }

    private VBox createQuestionOptions() {
        Button[] optionButtons = new Button[answerTexts.length];
        for (int i = 0; i < answerTexts.length; i++) {
            optionButtons[i] = createOptionButton(answerTexts[i], imageFiles[i]);
        }

        optionButtons[0].setOnAction(e -> {

            Results resultWindow = new Results(window); 
            try {
                resultWindow.start(new Stage());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        HBox optionOneThree = new HBox(125, optionButtons[0], optionButtons[2]);
        optionOneThree.getStyleClass().add("hbox-question-optionAC");
        optionOneThree.setAlignment(Pos.CENTER);
        GridPane.setConstraints(optionOneThree, 0, 3, GridPane.REMAINING, 1);

        HBox optionTwoFour = new HBox(125, optionButtons[1], optionButtons[3]);
        optionTwoFour.getStyleClass().add("hbox-question-optionAC");
        optionTwoFour.setAlignment(Pos.CENTER);
        GridPane.setConstraints(optionTwoFour, 0, 4, GridPane.REMAINING, 1);

        VBox questionOptions = new VBox(25, optionOneThree, optionTwoFour);
        questionOptions.getStyleClass().add("hbox-question-optionAC");
        questionOptions.setAlignment(Pos.CENTER);
        GridPane.setConstraints(questionOptions, 0, 3, GridPane.REMAINING, 1);

        return questionOptions;
    }

    private Button createOptionButton(String answer, String imageFilename) {
        Button optionButton = new Button();
        optionButton.getStyleClass().add("label-question-option");

        HBox optionContent = new HBox(10);
        optionContent.setAlignment(Pos.CENTER_LEFT);

        Label answerLabel = new Label(answer);
        answerLabel.getStyleClass().add("label-question-answer");

        Pane imagePane = new Pane();
        imagePane.setStyle("-fx-background-image: url('" + imageFilename + "');" +
                "-fx-background-size: 80% 80%;" +
                "-fx-background-repeat: no-repeat;" +
                "-fx-background-position: left center;");

        imagePane.setPrefSize(30, 50);
        optionContent.getChildren().addAll(imagePane, answerLabel);

        optionButton.setGraphic(optionContent);
        optionContent.setPadding(new Insets(0, 10, 0, 10));

        return optionButton;
    }


    private HBox createQuestionContainer() {
        Label questionLabel = new Label(questions[0]); // Display the first question initially

        questionLabel.getStyleClass().add("label-question-label");
        questionLabel.setLineSpacing(10);

        HBox questionContainer = new HBox(questionLabel);
        questionContainer.setAlignment(Pos.CENTER);
        questionContainer.getStyleClass().add("hbox-questions-container");

        GridPane.setConstraints(questionContainer, 0, 2);
        GridPane.setMargin(questionContainer, new Insets(30, 0, 30, 0));
        GridPane.setHalignment(questionContainer, HPos.CENTER);

        return questionContainer;
    }

}
