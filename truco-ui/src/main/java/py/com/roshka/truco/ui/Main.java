package py.com.roshka.truco.ui;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.lf5.LogLevel;

import javax.swing.*;

public class Main {
    static Logger logger = Logger.getLogger(Main.class);

    public static void main(String[] args) {
        BasicConfigurator.configure();
        Logger.getRootLogger().setLevel(Level.DEBUG);
        logger.debug("Starting JFrame");

        Runnable r = new Runnable() {
            public void run() {
                JFrame f = new JFrame("Truco");
                f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                f.add(new TrucoFrame(f).getContentPane());
                f.pack();
                f.setLocationByPlatform(true);
                f.setVisible(true);
            }
        };
        SwingUtilities.invokeLater(r);
    }
}
