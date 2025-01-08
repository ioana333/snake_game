package com.example.proba;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.List;

public class Food {
    private Point2D position;
    private Image image;
    String nume;

    private static final String[] FOODS_IMAGE = new String[]{
            "/img/coliva.png", "/img/cozonac.png", "/img/dubai.png", "/img/castraveti.png",
            "/img/ciorba.png", "/img/kurtos.png", "/img/mamaliga.png", "/img/mici.png",
            "/img/papanasi.png", "/img/lanogs.png", "/img/mici2.png", "/img/pizza.png",
            "/img/salata.png", "/img/sarmale.png", "/img/tuica.png", "/img/pulpa.png",
            "/img/sarmale2.png", "/img/visinata.png", "/img/zacusca.png"
    };

    public void generate(List<Point2D> snakeBody) {
        do {
            int x = (int) (Math.random() * GameView.COLUMNS);
            int y = (int) (Math.random() * GameView.ROWS);
            position = new Point2D(x, y);
        } while (snakeBody.contains(position));

        // Selectăm o imagine random din lista FOODS_IMAGE
        String imagePath = FOODS_IMAGE[(int) (Math.random() * FOODS_IMAGE.length)];
        nume = imagePath;
        image = new Image(getClass().getResource(imagePath).toExternalForm());
    }

    public Point2D getPosition() {
        return position;
    }

    public String getImage() {
        return nume;
    }

    public void draw(GraphicsContext gc) {

        gc.drawImage(image, position.getX() * GameView.SQUARE_SIZE, position.getY() * GameView.SQUARE_SIZE, GameView.SQUARE_SIZE, GameView.SQUARE_SIZE);

    }
}
