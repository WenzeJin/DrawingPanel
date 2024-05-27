import config.Config;
import frame.*;
import utils.DPLogger;

import javax.swing.*;


public class Main {
    public static void main(String[] args) {
        DPLogger.success("Executing DrawPanel Program...");


        if (Config.OS.contains("win")) {
            DPLogger.success("You are running Windows.");
        } else if (Config.OS.contains("mac")) {
            DPLogger.success("You are running macOS.");
        } else if (Config.OS.contains("nix") || Config.OS.contains("nux") || Config.OS.contains("aix")) {
            DPLogger.success("You are running Unix or Linux.");
        } else {
            DPLogger.success("Unknown operating system.");
        }

        // show DrawPanelFrame

        SwingUtilities.invokeLater(() -> {
            DrawPanelFrame frame = new DrawPanelFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
            frame.canvasRequestFocusInWindow();
        });

    }
}