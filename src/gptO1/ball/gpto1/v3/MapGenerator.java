package gptO1.ball.gpto1.v3;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;

public class MapGenerator {
    public int map[][];
    public int brickWidth;
    public int brickHeight;
    private int totalBricks = 0;
    private Random random = new Random();

    public MapGenerator(int row, int col) {
        map = new int[row][col];
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                // Losowe wartości od 1 do 3 (liczba uderzeń potrzebna do zniszczenia)
                map[i][j] = random.nextInt(3) + 1;
                totalBricks++;
            }
        }
        brickWidth = 540 / col;
        brickHeight = 150 / row;
    }

    public void draw(Graphics2D g) {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (map[i][j] > 0) {
                    // Kolor cegiełki zależny od liczby uderzeń potrzebnych do zniszczenia
                    if (map[i][j] == 3) {
                        g.setColor(Color.red);
                    } else if (map[i][j] == 2) {
                        g.setColor(Color.orange);
                    } else {
                        g.setColor(Color.yellow);
                    }
                    g.fillRect(j * brickWidth + 80, i * brickHeight + 50, brickWidth, brickHeight);

                    // Obramowanie cegiełek
                    g.setStroke(new BasicStroke(3));
                    g.setColor(Color.black);
                    g.drawRect(j * brickWidth + 80, i * brickHeight + 50, brickWidth, brickHeight);
                }
            }
        }
    }

    // Zmniejsza wartość cegiełki po uderzeniu
    public boolean hitBrick(int row, int col) {
        if (map[row][col] > 0) {
            map[row][col]--;
            return map[row][col] == 0;
        }
        return false;
    }

    public int getTotalBricks() {
        return totalBricks;
    }
}

