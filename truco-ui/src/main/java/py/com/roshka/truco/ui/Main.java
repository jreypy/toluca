package py.com.roshka.truco.ui;

import py.edu.uca.fcyt.toluca.guinicio.RoomUING;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        Runnable r = new Runnable() {
            public void run() {
                JFrame f = new JFrame("Truco");
                f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                f.add(new TrucoFrame().getContentPane());
                f.pack();
                f.setLocationByPlatform(true);
                f.setVisible(true);
            }
        };
        SwingUtilities.invokeLater(r);
    }
}
