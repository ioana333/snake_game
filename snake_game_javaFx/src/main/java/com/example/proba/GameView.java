package com.example.proba;

import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class GameView {
    public static final int WIDTH = 700;
    public static final int HEIGHT = WIDTH;
    public static final int ROWS = 20;
    public static final int COLUMNS = 20;
    public static final int SQUARE_SIZE = WIDTH / ROWS;
    public static final int RIGHT = 0;
    public static final int LEFT = 1;
    public static final int UP = 2;
    public static final int DOWN = 3;

    public static void drawBackground(GraphicsContext gc) {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                gc.setFill((i + j) % 2 == 0 ? Color.web("#00033b") : Color.web("#01044a"));
                gc.fillRect(j * SQUARE_SIZE, i * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
            }
        }
    }

    public static void drawScore(GraphicsContext gc, int score) {
        gc.setFill(Color.WHITE);
        gc.setFont(Font.loadFont(GameView.class.getResourceAsStream("/fonts/digital-7.ttf"), 35));
        gc.fillText("Score: " + score, 10, 35);
    }

    public static Button createRestartButton(Runnable restartAction) {
        Button button = new Button("PLAY");
        button.setStyle("-fx-font-size: 16px; -fx-background-color: white; -fx-text-fill: blue;");
        button.setLayoutX(WIDTH / 1.8 - 50);
        button.setLayoutY(HEIGHT / 2.0 + 50);
        button.setOnAction(e -> restartAction.run());
        button.setVisible(false);
        return button;
    }

    public static void setupKeyEvents(Scene scene, Snake snake) {
        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case RIGHT, D -> snake.setDirection(RIGHT);
                case LEFT, A -> snake.setDirection(LEFT);
                case UP, W -> snake.setDirection(UP);
                case DOWN, S -> snake.setDirection(DOWN);
            }
        });
    }

    public static void displayGameOver(GraphicsContext gc, Button restartButton) {
        gc.setFill(Color.RED);
        gc.setFont(Font.loadFont(GameView.class.getResourceAsStream("/fonts/digital-7.ttf"), 70));
        gc.fillText("Game Over", WIDTH / 3.0, HEIGHT / 2.0);
        restartButton.setVisible(true);
    }

    public static void displayGameWon(GraphicsContext gc, Button restartButton) {
        gc.setFill(Color.GREEN);
        gc.setFont(Font.loadFont(GameView.class.getResourceAsStream("/fonts/digital-7.ttf"), 70));
        gc.fillText("You Win!", WIDTH / 3.0, HEIGHT / 2.0);
        restartButton.setVisible(true);
    }
}
