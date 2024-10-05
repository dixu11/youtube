package gptO1.ball.gpto1.v2;

public class Ball {
    private int x;
    private int y;
    private int dirX;
    private int dirY;
    private int size;

    public Ball(int x, int y, int dirX, int dirY, int size) {
        this.x = x;
        this.y = y;
        this.dirX = dirX;
        this.dirY = dirY;
        this.size = size;
    }

    public boolean move() {
        x += dirX;
        y += dirY;
        return y > 570; // Zwraca true, jeśli piłeczka spadła poniżej paletki
    }

    // Gettery i settery
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getDirX() {
        return dirX;
    }

    public int getDirY() {
        return dirY;
    }

    public int getSize() {
        return size;
    }

    public void setDirX(int dirX) {
        this.dirX = dirX;
    }

    public void setDirY(int dirY) {
        this.dirY = dirY;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
