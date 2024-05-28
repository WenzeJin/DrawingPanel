package shape;

import config.Config;

import java.awt.Point;
import java.math.*;

public class CriticalPoint extends Point{
    private static int idCounter;
    private final int type;
    private final Shape parent;

    public CriticalPoint(int x, int y, int type, Shape parent) {
        super(x, y);
        this.type = type;
        this.parent = parent;
    }

    public int getType() {
        return type;
    }

    public Shape getParent() {
        return parent;
    }

    public boolean canSelect(Point p) {
        return Math.abs(p.x - x) <= (Config.CP_SIZE / 2) && Math.abs(p.y - y) <= (Config.CP_SIZE / 2);
    }
}
