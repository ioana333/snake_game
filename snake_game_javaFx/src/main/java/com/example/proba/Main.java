package com.example.proba;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.control.Button;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;

public class Main extends Application {

    private static final int WIDTH = 800;
    private static final int HEIGHT = WIDTH;
    private static final int ROWS = 20;
    private static final int COLUMNS = ROWS;
    private static final int SQUARE_SIZE = WIDTH / ROWS;
    private static final String[] FOODS_IMAGE = new String[]{
            "/img/coliva.png", "/img/cozonac.png", "/img/dubai.png", "/img/castraveti.png",
            "/img/ciorba.png", "/img/kurtos.png", "/img/mamaliga.png", "/img/mici.png",
            "/img/papanasi.png", "/img/lanogs.png", "/img/mici2.png", "/img/pizza.png",
            "/img/salata.png", "/img/sarmale.png", "/img/tuica.png", "/img/pulpa.png",
            "/img/sarmale2.png", "/img/visinata.png", "/img/zacusca.png"
    };

    private static final int RIGHT = 0;
    private static final int LEFT = 1;
    private static final int UP = 2;
    private static final int DOWN = 3;

    private GraphicsContext gc;
    private List<Point2D> snakeBody = new ArrayList<>();
    private Point2D snakeHead;
    private Image foodImage;
    private int foodX;
    private int foodY;

    private boolean gameOver;
    private boolean gameWon = false;

    private int currentDirection;
    private int score = 0;
    private Timeline timeline;
    private Button restartButton;


    public static void main(String[] args) {
        launch(args);
    }

    private void run(GraphicsContext gc) {

        if(gameOver)
        {
            gc.setFill(Color.RED);
            gc.setFont(new Font("Digital-7", 70));
            gc.fillText("Game Over", WIDTH / 3, HEIGHT / 2);

            restartButton.setVisible(true);

            return;
        }

        if (gameWon)
        {
            gc.setFill(Color.GREEN);
            gc.setFont(new Font("Digital-7", 70));
            gc.fillText("You Win!", WIDTH / 2.8, HEIGHT / 2);

            restartButton.setVisible(true);
            return;
        }

        drawBackground(gc);
        drawFood(gc);

        drawSnake(gc);

        drawScore();

        for(int i = snakeBody.size() - 1; i >= 1;  i--)
        {
            Point2D previousPoint = snakeBody.get(i - 1);
            snakeBody.set(i, new Point2D(previousPoint.getX(), previousPoint.getY()));

        }

        switch (currentDirection)
        {
            case RIGHT:
                moveRight();
                break;
            case LEFT:
                moveLeft();
                break;
            case UP:
                moveUP();
                break;
            case DOWN:
                moveDown();
                break;
        }
        snakeBody.set(0, snakeHead);

        gameOver();
        eatFood();
    }

    private void drawBackground(GraphicsContext gc) {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                if ((i + j) % 2 == 0) {
                    gc.setFill(Color.web("#00033b"));
                } else {
                    gc.setFill(Color.web("#01044a"));
                }
                gc.fillRect(j * SQUARE_SIZE, i * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
            }
        }
    }

    private void generateFood()
    {
        start:
        while (true)
        {
            foodX = (int) (Math.random() * ROWS);
            foodY = (int) (Math.random() * COLUMNS);

            for (Point2D snake : snakeBody)
            {
                if (snake.getX() == foodX && snake.getY() == foodY)
                    continue start;
            }

            foodImage = new Image(getClass().getResource(FOODS_IMAGE[(int)(Math.random() * FOODS_IMAGE.length)]).toExternalForm());

            break;
        }
    }

    private void drawFood(GraphicsContext gc)
    {
        gc.drawImage(foodImage, foodX * SQUARE_SIZE, foodY * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);
    }

    private void drawSnake(GraphicsContext gc)
    {
        gc.setFill(Color.web("#ffffff"));
        gc.fillRoundRect(snakeHead.getX() * SQUARE_SIZE, snakeHead.getY() * SQUARE_SIZE, SQUARE_SIZE-1, SQUARE_SIZE-1, 35, 35);

        for(int i = 1; i < snakeBody.size(); i++)
        {
            gc.fillRoundRect(snakeBody.get(i).getX() * SQUARE_SIZE, snakeBody.get(i).getY() * SQUARE_SIZE, SQUARE_SIZE-1, SQUARE_SIZE-1, 20, 20);
        }

    }

    private void moveRight()
    {
        snakeHead = new Point2D(snakeHead.getX() + 1, snakeHead.getY());
    }

    private void moveLeft()
    {
        snakeHead = new Point2D(snakeHead.getX() - 1, snakeHead.getY());
    }

    private void moveUP()
    {
        snakeHead = new Point2D(snakeHead.getX(), snakeHead.getY() - 1);
    }

    private void moveDown()
    {
        snakeHead = new Point2D(snakeHead.getX() , snakeHead.getY() + 1);
    }

    public void gameOver()
    {
        if(snakeHead.getX() < 0 || snakeHead.getY() * SQUARE_SIZE >= HEIGHT || snakeHead.getY() < 0 || snakeHead.getX() * SQUARE_SIZE >= WIDTH)
        {
            gameOver = true;
        }

        for (int i = 1; i < snakeBody.size(); i++)
        {
            if (snakeHead.getX() == snakeBody.get(i).getX() && snakeHead.getY() == snakeBody.get(i).getY())
            {
                gameOver = true;
                break;
            }
        }
    }

    private void eatFood()
    {
        if(snakeHead.getX() == foodX && snakeHead.getY() == foodY)
        {
            if (foodImage.getUrl().contains("pizza.png"))
            {
            playSound("/sound/HATZ.wav");
        }
            else if (foodImage.getUrl().contains("coliva.png"))
            {
            playSound("/sound/DOAMNE.wav");
        }

            snakeBody.add(new Point2D(-1, -1));
            generateFood();
            score++;

            double newSpeed = Math.max(50, timeline.getKeyFrames().get(0).getTime().toMillis() - 5);
            timeline.stop();
            timeline.getKeyFrames().set(0, new KeyFrame(Duration.millis(newSpeed), e -> run(gc)));
            timeline.play();

            if(score == 10)
            {
                gameWon = true;
            }
        }
    }

    private void drawScore()
    {
        gc.setFill(Color.WHITE);
        gc.setFont(new Font("Digital-7", 35));
        gc.fillText("Score: " + score, 10, 35);
    }

    private void playSound(String soundFile) {
        try {
            Media sound = new Media(getClass().getResource(soundFile).toExternalForm());
            MediaPlayer mediaPlayer = new MediaPlayer(sound);
            mediaPlayer.play();
        } catch (Exception e) {
            System.out.println("Error loading sound: " + soundFile);
            e.printStackTrace();
        }
    }


    private void restartGame() {
        // Resetează starea jocului
        snakeBody.clear();
        for (int i = 0; i < 3; i++) {
            snakeBody.add(new Point2D(5, ROWS / 2));
        }
        snakeHead = snakeBody.get(0);
        generateFood();
        score = 0;
        gameOver = false;
        gameWon = false;
        currentDirection = RIGHT;

        // Ascunde butonul de restart
        restartButton.setVisible(false);

        // Repornește animația
        timeline.stop();
        timeline.getKeyFrames().set(0, new KeyFrame(Duration.millis(100), e -> run(gc)));
        timeline.play();
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("SNAKE");

        Group root = new Group();
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        gc = canvas.getGraphicsContext2D();

        root.getChildren().add(canvas);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);

        primaryStage.show();

        gc = canvas.getGraphicsContext2D();

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                KeyCode code = keyEvent.getCode();

                if(code == KeyCode.RIGHT || code == KeyCode.D)
                {
                    if(currentDirection != LEFT)
                    {
                        currentDirection = RIGHT;
                    }
                }
                else if(code == KeyCode.LEFT || code == KeyCode.A)
                {
                    if(currentDirection != RIGHT)
                    {
                        currentDirection = LEFT;
                    }
                }
                else if(code == KeyCode.UP || code == KeyCode.W)
                {
                    if(currentDirection != DOWN)
                    {
                        currentDirection = UP;
                    }
                }
                else if(code == KeyCode.DOWN || code == KeyCode.S)
                {
                    if(currentDirection != UP)
                    {
                        currentDirection = DOWN;
                    }
                }

            }
        });


        for (int i=0; i<3; i++)
        {
            snakeBody.add(new Point2D(5, ROWS/2));
        }

        snakeHead = snakeBody.get(0);
        generateFood();

        drawBackground(gc);

        restartButton = new Button("PLAY");
        restartButton.setStyle("-fx-font-size: 16px; -fx-background-color: white; -fx-text-fill: blue;");
        restartButton.setLayoutX(WIDTH / 2.0 - 50);
        restartButton.setLayoutY(HEIGHT / 2.0 + 50);
        restartButton.setVisible(false);

        restartButton.setOnAction(e -> restartGame());

        root.getChildren().add(restartButton);

        //run(gc);
        timeline = new Timeline(new KeyFrame(Duration.millis(100), e->run(gc)));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
}
