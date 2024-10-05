package gptO1.ball.gpt4o.v2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.Timer;
import java.util.ArrayList;
import java.util.Random;

public class BrickBreakerGpt4 extends JPanel implements ActionListener {
    private boolean play = false;
    private int score = 0;
    private int totalBricks = 21;
    private Timer timer;
    private int delay = 8;

    private int playerX = 310;
    private int ballPosX = 120;
    private int ballPosY = 350;
    private int ballDirX = -1;
    private int ballDirY = -2;
    private int paddleWidth = 100;
    private int ballSize = 20;

    private MapGenerator map;
    private ArrayList<PowerUp> powerUps;
    private Random random;

    public BrickBreakerGpt4() {
        map = new MapGenerator(3, 7);
        powerUps = new ArrayList<>();
        random = new Random();
        addKeyListener(new KeyListener());
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay, this);
        timer.start();
    }

    public void paint(Graphics g) {
        // Background
        g.setColor(Color.black);
        g.fillRect(1, 1, 692, 592);

        // Drawing map
        map.draw((Graphics2D) g);

        // Borders
        g.setColor(Color.yellow);
        g.fillRect(0, 0, 3, 592);
        g.fillRect(0, 0, 692, 3);
        g.fillRect(691, 0, 3, 592);

        // Scores
        g.setColor(Color.white);
        g.setFont(new Font("serif", Font.BOLD, 25));
        g.drawString("Score: " + score, 550, 30);

        // Paddle
        g.setColor(Color.green);
        g.fillRect(playerX, 550, paddleWidth, 8);

        // Ball
        g.setColor(Color.yellow);
        g.fillOval(ballPosX, ballPosY, ballSize, ballSize);

        // Power-Ups
        for (PowerUp powerUp : powerUps) {
            powerUp.draw(g);
        }

        // Game Over
        if (ballPosY > 570) {
            play = false;
            ballDirX = 0;
            ballDirY = 0;
            g.setColor(Color.red);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("Game Over, Score: " + score, 190, 300);

            g.setFont(new Font("serif", Font.BOLD, 20));
            g.drawString("Press Enter to Restart", 230, 350);
        }

        // Win
        if (totalBricks <= 0) {
            play = false;
            ballDirX = 0;
            ballDirY = 0;
            g.setColor(Color.green);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("You Won, Score: " + score, 190, 300);

            g.setFont(new Font("serif", Font.BOLD, 20));
            g.drawString("Press Enter to Restart", 230, 350);
        }

        g.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        timer.start();
        if (play) {
            if (new Rectangle(ballPosX, ballPosY, ballSize, ballSize).intersects(new Rectangle(playerX, 550, paddleWidth, 8))) {
                ballDirY = -ballDirY;
            }

            A:
            for (int i = 0; i < map.map.length; i++) {
                for (int j = 0; j < map.map[0].length; j++) {
                    if (map.map[i][j] > 0) {
                        int brickX = j * map.brickWidth + 80;
                        int brickY = i * map.brickHeight + 50;
                        int brickWidth = map.brickWidth;
                        int brickHeight = map.brickHeight;

                        Rectangle rect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
                        Rectangle ballRect = new Rectangle(ballPosX, ballPosY, ballSize, ballSize);
                        Rectangle brickRect = rect;

                        if (ballRect.intersects(brickRect)) {
                            map.setBrickValue(0, i, j);
                            totalBricks--;
                            score += 5;

                            if (ballPosX + ballSize - 1 <= brickRect.x || ballPosX + 1 >= brickRect.x + brickRect.width) {
                                ballDirX = -ballDirX;
                            } else {
                                ballDirY = -ballDirY;
                            }
                            generatePowerUp(brickX, brickY);
                            break A;
                        }
                    }
                }
            }

            ballPosX += ballDirX;
            ballPosY += ballDirY;
            if (ballPosX < 0) {
                ballDirX = -ballDirX;
            }
            if (ballPosY < 0) {
                ballDirY = -ballDirY;
            }
            if (ballPosX > 670) {
                ballDirX = -ballDirX;
            }

            // Update power-ups
            updatePowerUps();
        }
        repaint();
    }

    private void generatePowerUp(int x, int y) {
        int powerUpType = random.nextInt(4);
        powerUps.add(new PowerUp(x + 20, y, powerUpType));
    }

    private void updatePowerUps() {
        ArrayList<PowerUp> collectedPowerUps = new ArrayList<>();
        for (PowerUp powerUp : powerUps) {
            powerUp.move();
            if (new Rectangle(powerUp.getX(), powerUp.getY(), powerUp.getWidth(), powerUp.getHeight()).intersects(new Rectangle(playerX, 550, paddleWidth, 8))) {
                applyPowerUp(powerUp.getType());
                collectedPowerUps.add(powerUp);
            }
        }
        powerUps.removeAll(collectedPowerUps);
    }

    private void applyPowerUp(int type) {
        switch (type) {
            case 0: // Multiply Balls
                // Add logic to multiply balls (not implemented in this version)
                break;
            case 1: // Increase Paddle Size
                paddleWidth += 30;
                break;
            case 2: // Increase Ball Size
                ballSize += 5;
                break;
            case 3: // Increase Ball Speed
                delay -= 2;
                timer.setDelay(delay);
                break;
        }
    }

    private class KeyListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_RIGHT:
                    handleRightKey();
                    break;
                case KeyEvent.VK_LEFT:
                    handleLeftKey();
                    break;
                case KeyEvent.VK_ENTER:
                    handleEnterKey();
                    break;
            }
        }

        private void handleRightKey() {
            if (playerX >= 600) {
                playerX = 600;
            } else {
                moveRight();
            }
        }

        private void handleLeftKey() {
            if (playerX < 10) {
                playerX = 10;
            } else {
                moveLeft();
            }
        }

        private void handleEnterKey() {
            if (!play) {
                play = true;
                ballPosX = 120;
                ballPosY = 350;
                ballDirX = -1;
                ballDirY = -2;
                playerX = 310;
                score = 0;
                totalBricks = 21;
                paddleWidth = 100;
                ballSize = 20;
                powerUps.clear();
                map = new MapGenerator(3, 7);
                repaint();
            }
        }
    }

    public void moveRight() {
        play = true;
        playerX += 20;
    }

    public void moveLeft() {
        play = true;
        playerX -= 20;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        BrickBreakerGpt4 gamePlay = new BrickBreakerGpt4();
        frame.setBounds(10, 10, 700, 600);
        frame.setTitle("Brick Breaker");
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(gamePlay);
    }
}

class MapGenerator {
    public int map[][];
    public int brickWidth;
    public int brickHeight;

    public MapGenerator(int row, int col) {
        map = new int[row][col];
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                map[i][j] = 1;
            }
        }
        brickWidth = 540 / col;
        brickHeight = 150 / row;
    }

    public void draw(Graphics2D g) {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (map[i][j] > 0) {
                    g.setColor(Color.white);
                    g.fillRect(j * brickWidth + 80, i * brickHeight + 50, brickWidth, brickHeight);

                    g.setStroke(new BasicStroke(3));
                    g.setColor(Color.black);
                    g.drawRect(j * brickWidth + 80, i * brickHeight + 50, brickWidth, brickHeight);
                }
            }
        }
    }

    public void setBrickValue(int value, int row, int col) {
        map[row][col] = value;
    }
}

class PowerUp {
    private int x, y, type;
    private int width = 20;
    private int height = 20;
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
                g.setColor(Color.blue);
                break;
            case 1:
                g.setColor(Color.red);
                break;
            case 2:
                g.setColor(Color.green);
                break;
            case 3:
                g.setColor(Color.orange);
                break;
        }
        g.fillRect(x, y, width, height);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getType() {
        return type;
    }
}