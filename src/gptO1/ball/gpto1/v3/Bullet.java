package gptO1.ball.gpto1.v3;

import java.awt.Color;
import java.awt.Graphics;

public class Bullet {
    private int x;
    private int y;
    private int size = 10;
    private int speed = 5;

    public Bullet(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void move() {
        y -= speed;
    }

    public void draw(Graphics g) {
        g.setColor(Color.red);
        g.fillRect(x, y, size, size);
    }

    // Gettery
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getSize() {
        return size;
    }
}

