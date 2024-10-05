package gptO1.ball.gpto1.v1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class BrickBreakerGameO1 extends JPanel implements KeyListener, ActionListener {
    private boolean play = false;
    private int score = 0;
    private int totalBricks = 21;

    private Timer timer;
    private int delay = 8;

    private int playerX = 310; // Pozycja paletki

    private int ballPosX = 120; // Pozycja piłeczki
    private int ballPosY = 350;
    private int ballDirX = -1; // Kierunek piłeczki
    private int ballDirY = -2;

    private MapGenerator map;

    public BrickBreakerGameO1() {
        map = new MapGenerator(3, 7); // Tworzenie mapy cegiełek
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
        g.fillRect(playerX, 550, 100, 8);

        // Piłeczka
        g.setColor(Color.yellow);
        g.fillOval(ballPosX, ballPosY, 20, 20);

        // Wygrana
        if (totalBricks <= 0) {
            play = false;
            ballDirX = 0;
            ballDirY = 0;
            g.setColor(Color.red);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("Wygrana!", 260, 300);

            g.setFont(new Font("serif", Font.BOLD, 20));
            g.drawString("Naciśnij Enter, aby zagrać ponownie", 230, 350);
        }

        // Przegrana
        if (ballPosY > 570) {
            play = false;
            ballDirX = 0;
            ballDirY = 0;
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
            // Detekcja kolizji z paletką
            if (new Rectangle(ballPosX, ballPosY, 20, 20).intersects(new Rectangle(playerX, 550, 100, 8))) {
                ballDirY = -ballDirY;
            }

            // Detekcja kolizji z cegiełkami
            A: for (int i = 0; i < map.map.length; i++) {
                for (int j = 0; j < map.map[0].length; j++) {
                    if (map.map[i][j] > 0) {
                        // Pozycje cegiełek
                        int brickX = j * map.brickWidth + 80;
                        int brickY = i * map.brickHeight + 50;
                        int brickWidth = map.brickWidth;
                        int brickHeight = map.brickHeight;

                        Rectangle rect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
                        Rectangle ballRect = new Rectangle(ballPosX, ballPosY, 20, 20);

                        if (ballRect.intersects(rect)) {
                            map.setBrickValue(0, i, j);
                            totalBricks--;
                            score += 5;

                            // Zmiana kierunku piłeczki
                            if (ballPosX + 19 <= rect.x || ballPosX + 1 >= rect.x + rect.width) {
                                ballDirX = -ballDirX;
                            } else {
                                ballDirY = -ballDirY;
                            }

                            break A;
                        }
                    }
                }
            }

            // Ruch piłeczki
            ballPosX += ballDirX;
            ballPosY += ballDirY;

            // Odbicia od ścian
            if (ballPosX < 0) {
                ballDirX = -ballDirX;
            }
            if (ballPosY < 0) {
                ballDirY = -ballDirY;
            }
            if (ballPosX > 670) {
                ballDirX = -ballDirX;
            }
        }

        repaint();
    }

    public void keyTyped(KeyEvent e) { }

    public void keyReleased(KeyEvent e) { }

    public void keyPressed(KeyEvent e) {
        // Ruch paletki w prawo
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if (playerX >= 600) {
                playerX = 600;
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
                play = true;
                ballPosX = 120;
                ballPosY = 350;
                ballDirX = -1;
                ballDirY = -2;
                playerX = 310;
                score = 0;
                totalBricks = 21;
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
        JFrame obj = new JFrame();
        BrickBreakerGameO1 gamePlay = new BrickBreakerGameO1();
        obj.setBounds(10, 10, 700, 600);
        obj.setTitle("Brick Breaker");
        obj.setResizable(false);
        obj.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        obj.add(gamePlay);
        obj.setVisible(true);
    }
}

