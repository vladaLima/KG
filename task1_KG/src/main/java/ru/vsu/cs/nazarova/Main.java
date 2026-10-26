package ru.vsu.cs.nazarova;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        FrameMain window = new FrameMain();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setSize(550, 800);
        window.setVisible(true);
    }
}