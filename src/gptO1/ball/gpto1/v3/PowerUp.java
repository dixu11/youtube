package gptO1.ball.gpto1.v3;

import java.awt.Color;
import java.awt.Graphics;

public class PowerUp {
    private int x;
    private int y;
    private int type;
    private int size = 20;
    private int speed = 2;

    public PowerUp(int x, int y, int type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public void move() {
        y += speed;
    }

    public void draw(Graphics g) {
        switch (type) {
            case 0:
                g.setColor(Color.cyan);
                break;
            case 1:
                g.setColor(Color.orange);
                break;
            case 2:
                g.setColor(Color.pink);
                break;
            case 3:
                g.setColor(Color.red);
                break;
            case 4:
                g.setColor(Color.blue); // Nowy power-up
                break;
        }
        g.fillRect(x, y, size, size);
    }

    // Gettery
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getType() {
        return type;
    }

    public int getSize() {
        return size;
    }
}

