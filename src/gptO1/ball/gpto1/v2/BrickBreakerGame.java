package gptO1.ball.gpto1.v2;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class BrickBreakerGame extends JPanel implements KeyListener, ActionListener {
    private boolean play = false;
    private int score = 0;
    private int totalBricks = 21;

    private Timer timer;
    private int delay = 8;

    private int playerX = 310; // Pozycja paletki
    private int playerWidth = 100; // Szerokość paletki

    private Ball mainBall;
    private ArrayList<Ball> balls = new ArrayList<>();

    private MapGenerator map;
    private ArrayList<PowerUp> powerUps = new ArrayList<>();
    private Random random = new Random();

    public BrickBreakerGame() {
        map = new MapGenerator(3, 7); // Tworzenie mapy cegiełek
        mainBall = new Ball(120, 350, -1, -2, 20);
        balls.add(mainBall);

        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay, this);
        timer.start();
    }

    public void paint(Graphics g) {
        // Tło
        g.setColor(Color.black);
        g.fillRect(1, 1, 692, 592);

        // Rysowanie cegiełek
        map.draw((Graphics2D) g);

        // Ramki
        g.setColor(Color.yellow);
        g.fillRect(0, 0, 3, 592); // Lewa
        g.fillRect(0, 0, 692, 3); // Górna
        g.fillRect(691, 0, 3, 592); // Prawa

        // Wynik
        g.setColor(Color.white);
        g.setFont(new Font("serif", Font.BOLD, 25));
        g.drawString("Wynik: " + score, 560, 30);

        // Paletka
        g.setColor(Color.green);
        g.fillRect(playerX, 550, playerWidth, 8);

        // Piłeczki
        for (Ball ball : balls) {
            g.setColor(Color.yellow);
            g.fillOval(ball.getX(), ball.getY(), ball.getSize(), ball.getSize());
        }

        // Power-upy
        for (PowerUp powerUp : powerUps) {
            powerUp.draw(g);
        }

        // Wygrana
        if (totalBricks <= 0) {
            play = false;
            for (Ball ball : balls) {
                ball.setDirX(0);
                ball.setDirY(0);
            }
            g.setColor(Color.red);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("Wygrana!", 260, 300);

            g.setFont(new Font("serif", Font.BOLD, 20));
            g.drawString("Naciśnij Enter, aby zagrać ponownie", 230, 350);
        }

        // Przegrana
        boolean allBallsLost = true;
        for (Ball ball : balls) {
            if (ball.getY() <= 570) {
                allBallsLost = false;
                break;
            }
        }
        if (allBallsLost) {
            play = false;
            for (Ball ball : balls) {
                ball.setDirX(0);
                ball.setDirY(0);
            }
            g.setColor(Color.red);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("Koniec Gry!", 240, 300);

            g.setFont(new Font("serif", Font.BOLD, 20));
            g.drawString("Naciśnij Enter, aby zagrać ponownie", 230, 350);
        }

        g.dispose();
    }

    public void actionPerformed(ActionEvent e) {
        timer.start();

        if (play) {
            // Ruch power-upów
            ArrayList<PowerUp> caughtPowerUps = new ArrayList<>();
            for (PowerUp powerUp : powerUps) {
                powerUp.move();
                if (powerUp.getY() > 570) {
                    caughtPowerUps.add(powerUp);
                } else if (new Rectangle(powerUp.getX(), powerUp.getY(), powerUp.getSize(), powerUp.getSize())
                        .intersects(new Rectangle(playerX, 550, playerWidth, 8))) {
                    applyPowerUp(powerUp.getType());
                    caughtPowerUps.add(powerUp);
                }
            }
            powerUps.removeAll(caughtPowerUps);

            // Ruch piłeczek
            ArrayList<Ball> lostBalls = new ArrayList<>();
            for (Ball ball : balls) {
                if (ball.move()) {
                    lostBalls.add(ball);
                }
                // Detekcja kolizji z paletką
                if (new Rectangle(ball.getX(), ball.getY(), ball.getSize(), ball.getSize())
                        .intersects(new Rectangle(playerX, 550, playerWidth, 8))) {
                    ball.setDirY(-ball.getDirY());
                }
                // Detekcja kolizji z cegiełkami
                A: for (int i = 0; i < map.map.length; i++) {
                    for (int j = 0; j < map.map[0].length; j++) {
                        if (map.map[i][j] > 0) {
                            int brickX = j * map.brickWidth + 80;
                            int brickY = i * map.brickHeight + 50;
                            int brickWidth = map.brickWidth;
                            int brickHeight = map.brickHeight;

                            Rectangle rect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
                            Rectangle ballRect = new Rectangle(ball.getX(), ball.getY(), ball.getSize(),
                                    ball.getSize());

                            if (ballRect.intersects(rect)) {
                                map.setBrickValue(0, i, j);
                                totalBricks--;
                                score += 5;

                                // Dodawanie power-upa
                                generatePowerUp(brickX + brickWidth / 2, brickY + brickHeight / 2);

                                // Zmiana kierunku piłeczki
                                if (ball.getX() + 19 <= rect.x || ball.getX() + 1 >= rect.x + rect.width) {
                                    ball.setDirX(-ball.getDirX());
                                } else {
                                    ball.setDirY(-ball.getDirY());
                                }

                                break A;
                            }
                        }
                    }
                }

                // Odbicia od ścian
                if (ball.getX() < 0) {
                    ball.setDirX(-ball.getDirX());
                }
                if (ball.getY() < 0) {
                    ball.setDirY(-ball.getDirY());
                }
                if (ball.getX() > 670) {
                    ball.setDirX(-ball.getDirX());
                }
            }
            balls.removeAll(lostBalls);
        }

        repaint();
    }

    // Generowanie losowego power-upa
    private void generatePowerUp(int x, int y) {
        int type = random.nextInt(4); // 0 - rozmnożenie piłek, 1 - większa paletka, 2 - większa piłka, 3 - szybsza piłka
        powerUps.add(new PowerUp(x, y, type));
    }

    // Aplikowanie efektu power-upa
    private void applyPowerUp(int type) {
        switch (type) {
            case 0: // Rozmnożenie piłek
                ArrayList<Ball> newBalls = new ArrayList<>();
                for (Ball ball : balls) {
                    Ball newBall = new Ball(ball.getX(), ball.getY(), -ball.getDirX(), ball.getDirY(), ball.getSize());
                    newBalls.add(newBall);
                }
                balls.addAll(newBalls);
                break;
            case 1: // Większa paletka
                playerWidth += 30;
                if (playerWidth > 200) {
                    playerWidth = 200;
                }
                break;
            case 2: // Większa piłka
                for (Ball ball : balls) {
                    ball.setSize(ball.getSize() + 5);
                }
                break;
            case 3: // Szybsza piłka
                for (Ball ball : balls) {
                    ball.setDirX(ball.getDirX() * 2);
                    ball.setDirY(ball.getDirY() * 2);
                }
                break;
        }
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
        // Ruch paletki w prawo
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if (playerX >= 700 - playerWidth) {
                playerX = 700 - playerWidth;
            } else {
                moveRight();
            }
        }
        // Ruch paletki w lewo
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            if (playerX <= 10) {
                playerX = 10;
            } else {
                moveLeft();
            }
        }
        // Restart gry
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (!play) {
                restartGame();
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

    private void restartGame() {
        play = true;
        playerX = 310;
        playerWidth = 100;
        score = 0;
        totalBricks = 21;
        map = new MapGenerator(3, 7);
        balls.clear();
        mainBall = new Ball(120, 350, -1, -2, 20);
        balls.add(mainBall);
        powerUps.clear();
        repaint();
    }

    public static void main(String[] args) {
        JFrame obj = new JFrame();
        BrickBreakerGame gamePlay = new BrickBreakerGame();
        obj.setBounds(10, 10, 700, 600);
        obj.setTitle("Brick Breaker");
        obj.setResizable(false);
        obj.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        obj.add(gamePlay);
        obj.setVisible(true);
    }
}
