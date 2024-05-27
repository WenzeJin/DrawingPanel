package utils;

import java.awt.Point;

public class CriticalPoint extends Point{
    private static int idCounter;
    private final int id;
    private final int type;

    public CriticalPoint(int x, int y, int type) {
        super(x, y);
        this.type = type;
        this.id = idCounter;
        idCounter++;
    }

    public CriticalPoint(CriticalPoint criticalPoint) {
        super(criticalPoint.x, criticalPoint.y);
        this.id = criticalPoint.id;
        this.type = criticalPoint.type;
    }

    public int getId() {
        return id;
    }

    public int getType() {
        return type;
    }

    public boolean isSamePoint(CriticalPoint point) {
        return point.getType() == type && point.getId() == id;
    }
}
