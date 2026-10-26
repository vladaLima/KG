package ru.vsu.cs.nazarova;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DrawingPanel extends JPanel implements ActionListener {
    private final int panelWidth;
    private final int panelHeight;
    private final int timerDelay;
    private Timer timerCats;
    private Timer timerHearts;

    private Cat leftCat;
    private Cat rightCat;

    private final Heart[] hearts = new Heart[11];

    public DrawingPanel(final int panelWidth, final int panelHeight, final int timerDelay) {
        this.panelWidth = panelWidth;
        this.panelHeight = panelHeight;
        this.timerDelay = timerDelay;
        timerCats = new Timer(timerDelay, this);
        timerCats.start();
        timerHearts = new Timer(timerDelay, this);
        timerHearts.start();

        this.leftCat = new Cat(100, 600, 177, 300, Color.WHITE, true);
        this.rightCat = new Cat(250, 600, 177, 300, Color.BLACK, false);

        this.hearts[0] = new Heart(250, 410, 30, 40, new Color(228, 33, 255));
        this.hearts[1] = new Heart(90, 300, 30, 40, new Color(255, 20, 112));
        this.hearts[2] = new Heart(407, 300, 30, 40, new Color(255, 20, 112));
        this.hearts[3] = new Heart(200, 150, 30, 40, new Color(228, 33, 255));
        this.hearts[4] = new Heart(300, 150, 30, 40, new Color(255, 20, 112));
        this.hearts[5] = new Heart(50, 50, 40, 50, new Color(90, 29, 255));
        this.hearts[6] = new Heart(450, 50, 40, 50, new Color(90, 29, 255));
        this.hearts[7] = new Heart(85, 220, 15, 20, new Color(125, 23, 181));
        this.hearts[8] = new Heart(430, 220, 15, 20, new Color(125, 23, 181));
        this.hearts[9] = new Heart(260, 120, 15, 20, new Color(168, 0, 113));
        this.hearts[10] = new Heart(260, 210, 15, 20, new Color(168, 0, 113));

    }

    @Override
    public void paint(final Graphics g) {
        super.paint(g);
        final Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.fillRect(0, 0, panelWidth, panelHeight);

        leftCat.draw(g2d);
        rightCat.draw(g2d);

        for (int i = 0; i < hearts.length; i++) {
            hearts[i].draw(g2d);
        }
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        if (e.getSource() == timerCats) {
            leftCat.updateEyes();
            rightCat.updateEyes();
            repaint();
        } else if (e.getSource() == timerHearts) {
            for (int i = 0; i < hearts.length; i++) {
                hearts[i].updateHeart();
            }
        }
    }
}
