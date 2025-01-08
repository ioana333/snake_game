package com.example.proba;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.Group;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GameController {
    private GraphicsContext gc;
    private Snake snake;
    private Food food;
    private Timeline timeline;
    private boolean gameOver = false;
    private boolean gameWon = false;
    private int score = 0;
    private Button restartButton;

    public void start(Stage primaryStage) {
        primaryStage.setTitle("SNAKE");

        Group root = new Group();
        Canvas canvas = new Canvas(GameView.WIDTH, GameView.HEIGHT);
        gc = canvas.getGraphicsContext2D();
        root.getChildren().add(canvas);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();

        snake = new Snake();
        GameView.setupKeyEvents(scene, snake);

        food = new Food();
        food.generate(snake.getBody());

        restartButton = GameView.createRestartButton(this::restartGame);
        root.getChildren().add(restartButton);

        timeline = new Timeline(new KeyFrame(Duration.millis(100), e -> runGame()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void runGame() {
        if (gameOver) {
            GameView.displayGameOver(gc, restartButton);
            return;
        }
        if (gameWon) {
            GameView.displayGameWon(gc, restartButton);
            return;
        }
        GameView.drawBackground(gc);
        food.draw(gc);
        snake.move();
        snake.draw(gc);

        if (snake.checkCollision()) {
            gameOver = true;
        }

        if (snake.eat(food)) {
            score++;
            food.generate(snake.getBody());
            if (score == 10) {
                gameWon = true;
            }
        }

        GameView.drawScore(gc, score);
    }

    private void restartGame() {
        gameOver = false;
        gameWon = false;
        score = 0;
        snake.reset();
        food.generate(snake.getBody());
        restartButton.setVisible(false);
        timeline.play();
    }
}
