package com.example.proba;

import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        GameController gameController = new GameController();
        gameController.start(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
