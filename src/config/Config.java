package config;


import java.awt.*;

public class Config {

    public static final String  VERSION = "0.3";
    public static final int     COPY_BIAS = 20;
    public static final int     CP_SIZE = 10;

    // Below will be initialized after running.
    public static final String  OS = System.getProperty("os.name").toLowerCase();
    public static final String  USER_DIR = System.getProperty("user.dir");
    public static final Color[] COLORS = new Color[] {
            Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.ORANGE,
            Color.CYAN, Color.PINK, Color.GRAY, Color.BLACK
    };
}
