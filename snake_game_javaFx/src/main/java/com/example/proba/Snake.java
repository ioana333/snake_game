package com.example.proba;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class Snake {
    private List<Point2D> body;
    private Point2D head;
    private int direction;

    public Snake() {
        reset();
    }

    public void reset() {
        body = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            body.add(new Point2D(5, GameView.ROWS / 2));
        }
        head = body.get(0);
        direction = GameView.RIGHT;
    }

    public void move() {
        for (int i = body.size() - 1; i > 0; i--) {
            body.set(i, body.get(i - 1));
        }

        switch (direction) {
            case GameView.RIGHT -> head = new Point2D(head.getX() + 1, head.getY());
            case GameView.LEFT -> head = new Point2D(head.getX() - 1, head.getY());
            case GameView.UP -> head = new Point2D(head.getX(), head.getY() - 1);
            case GameView.DOWN -> head = new Point2D(head.getX(), head.getY() + 1);
        }

        body.set(0, head);
    }

    public boolean checkCollision() {
        if (head.getX() < 0 || head.getY() < 0 || head.getX() >= GameView.COLUMNS || head.getY() >= GameView.ROWS) {
            return true;
        }
        for (int i = 1; i < body.size(); i++) {
            if (head.equals(body.get(i))) {
                return true;
            }
        }
        return false;
    }

    public boolean eat(Food food) {
        Point2D foodPosition = food.getPosition();
        if (head.equals(foodPosition)) {
            body.add(new Point2D(-1, -1));


            String foodImageUrl = food.getImage();
            if (foodImageUrl.contains("pizza.png")) {
                playSound("/sound/HATZ.wav");
            } else if (foodImageUrl.contains("coliva.png")) {
                playSound("/sound/DOAMNE.wav");
            }

            return true; // Mâncarea a fost consumată
        }
        return false;
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

    public void draw(GraphicsContext gc) {
        gc.setFill(Color.WHITE);
        gc.fillRoundRect(head.getX() * GameView.SQUARE_SIZE, head.getY() * GameView.SQUARE_SIZE, GameView.SQUARE_SIZE, GameView.SQUARE_SIZE, 35, 35);
        for (int i = 1; i < body.size(); i++) {
            gc.fillRoundRect(body.get(i).getX() * GameView.SQUARE_SIZE, body.get(i).getY() * GameView.SQUARE_SIZE, GameView.SQUARE_SIZE, GameView.SQUARE_SIZE, 20, 20);
        }
    }

    public List<Point2D> getBody() {
        return body;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }
}
