package ru.vsu.cs.nazarova;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class Heart {
    private int x;
    private int y;
    private int width;
    private int height;
    private Color color;
    private final double baseWidth = 30;
    private final double baseHeight = 40;
    private int thumpTickFromStart;


    public Heart(int x, int y, int width, int height, Color color) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = color;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        double thumpScale = 1.0;

        if (thumpTickFromStart >= 10) {
            thumpScale = thumpScale * 1.25;
        } else if (thumpTickFromStart >= 5) {
            thumpScale = thumpScale * 1.1;
        }

        double scaleX = width / baseWidth * thumpScale;
        double scaleY = height / baseHeight * thumpScale;

        // mirroring

        AffineTransform heartTransform = g2d.getTransform();

        g2d.translate(this.x + this.width / 2, this.y + this.height / 2); // сдвинули точку отсчета в центр сердца, чтобы потом зеркалить от нее
        g2d.scale(scaleX, scaleY);
        g2d.setColor(color);

        // left part of heart

        g2d.rotate(Math.toRadians(-25));
        g2d.fillOval((int) (-baseWidth / 2), (int) (-baseHeight / 2), (int) (baseWidth / 2), (int) (baseHeight));

        // left part circuit

        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawOval((int) (-baseWidth / 2), (int) (-baseHeight / 2), (int) (baseWidth / 2), (int) (baseHeight));


        g2d.rotate(Math.toRadians(25));
        g2d.scale(-1, 1);

        // right part of heart

        g2d.setColor(color);
        g2d.rotate(Math.toRadians(-25));
        g2d.fillOval((int) (-baseWidth / 2), (int) (-baseHeight / 2), (int) (baseWidth / 2), (int) (baseHeight));


        // right part circuit

        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawOval((int) (-baseWidth / 2), (int) (-baseHeight / 2), (int) (baseWidth / 2), (int) (baseHeight));
        g2d.setColor(color);
        g2d.setStroke(new BasicStroke(3));
        g2d.drawArc((int) (-baseWidth / 2), (int) (-baseHeight / 2), (int) (baseWidth / 2), (int) (baseHeight), -54, 48);

        g2d.setTransform(heartTransform);

    }

    public void updateHeart() {
        ++thumpTickFromStart;

        if (thumpTickFromStart >= 15) {
            thumpTickFromStart = 0;
        }

    }



}
