package ru.vsu.cs.nazarova;

import javax.swing.*;

public class FrameMain extends JFrame {
    private final DrawingPanel panel;

    public FrameMain() {
        this.setTitle("Cats in love");
        this.panel = new DrawingPanel(550, 800, 50);
        this.add(panel);
    }

}
