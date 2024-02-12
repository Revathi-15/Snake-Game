import java.awt.*;
//import java.awt.Color;
// import java.awt.Dimension;
// import java.awt.Font;
// import java.awt.FontMetrics;
// import java.awt.Graphics;
// import java.awt.Image;
// import java.awt.Toolkit;
import java.awt.event.*;
import javax.swing.*;

class Board extends JPanel implements ActionListener {
    public Image apple;
    private Image dot;
    private Image head;

    private int dots;
    private final int all_dots = 900;
    private int dot_size = 10;

    private boolean leftdirection = false;
    private boolean rightdirection = true;
    private boolean updirection = false;
    private boolean downdirection = false;
    private boolean inGame = true;

    private int random_pos = 29;
    private int apple_x;
    private int apple_y;
    private final int x[] = new int[all_dots];
    private final int y[] = new int[all_dots];
    private Timer timer;

    Board() {
        addKeyListener(new TAdapter());

        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(500, 500));
        setFocusable(true); //
        initGame();
        LoadImage();
    }

    public void LoadImage() {
        ImageIcon i1 = new ImageIcon("icons/apple.png");
        apple = i1.getImage();

        ImageIcon i2 = new ImageIcon("icons/dot.png");
        dot = i2.getImage();

        ImageIcon i3 = new ImageIcon("icons/head.png");
        head = i3.getImage();
    }

    public void initGame() {
        dots = 3;

        for (int i = 0; i < dots; i++) {
            y[i] = 50;
            x[i] = 50 - i * dot_size;
        }
        LocateApple();
        Timer timer = new Timer(140, this);
        timer.start();
    }

    public void LocateApple() {
        int r = (int) (Math.random() * random_pos);
        apple_x = r * dot_size;

        r = (int) (Math.random() * random_pos);
        apple_y = r * dot_size;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        if (inGame) {
            g.drawImage(apple, apple_x, apple_y, this);
            for (int i = 0; i < dots; i++) {
                if (i == 0) {
                    g.drawImage(head, x[i], y[i], this);
                } else {
                    g.drawImage(dot, x[i], y[i], this);
                }
            }
            Toolkit.getDefaultToolkit().sync();
        } else {
            gameover(g);
        }
    }

    public void gameover(Graphics g) {
        String str = "GAME OVER!";
        Font font = new Font("SAN_SERIF", Font.BOLD, 14);
        FontMetrics metrics = getFontMetrics(font);

        g.setColor(Color.RED);
        g.setFont(font);
        g.drawString(str, (500 - metrics.stringWidth(str)) / 2, 500 / 2);
    }

    public void move() {
        for (int i = dots; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        if (leftdirection) {
            x[0] = x[0] - dot_size;
        }
        if (rightdirection) {
            x[0] = x[0] + dot_size;
        }
        if (updirection) {
            y[0] = y[0] - dot_size;
        }
        if (downdirection) {
            y[0] = y[0] + dot_size;
        }
        // x[0] += dot_size;
        // y[0] += dot_size;
    }

    public void incrlen() {
        if (x[0] == apple_x && y[0] == apple_y) {
            dots++;
            LocateApple();
        }
    }

    public void collision() {
        for (int i = dots; i >= 0; i--) {
            if ((i > 4) && (x[0] == x[i]) && (y[0] == y[i])) {
                inGame = false;
            }
            if (y[0] >= 500) {
                inGame = false;
            }
            if (x[0] >= 500) {
                inGame = false;
            }
            if (x[0] < 0) {
                inGame = false;
            }
            if (y[0] < 0) {
                inGame = false;
            }
            if (!inGame) {
                timer.stop();
            }
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (inGame) {
            move();
            incrlen();
            collision();
        }
        repaint();
    }

    public class TAdapter extends KeyAdapter {
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();

            if (key == KeyEvent.VK_LEFT && !(rightdirection)) {
                leftdirection = true;
                updirection = false;
                downdirection = false;
            }

            if (key == KeyEvent.VK_RIGHT && !(leftdirection)) {
                rightdirection = true;
                updirection = false;
                downdirection = false;
            }

            if (key == KeyEvent.VK_UP && !(downdirection)) {
                updirection = true;
                leftdirection = false;
                rightdirection = false;
            }

            if (key == KeyEvent.VK_DOWN && !(updirection)) {
                downdirection = true;
                leftdirection = false;
                rightdirection = false;
            }
        }
    }
}

public class SnakeGame extends JFrame {
    SnakeGame() {
        super("Snake Game");
        add(new Board());
        pack();// setVisible() , pack() , repaint() all methods do same function

        setLocationRelativeTo(null);
        setResizable(false);
    }

    public static void main(String args[]) {
        new SnakeGame().setVisible(true);
    }
}