package org.CMVD.Softwork;

import org.CMVD.Softwork.Vistas.HomeUI;

public class Main {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            new HomeUI();
        });
    }
}