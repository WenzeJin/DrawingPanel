package shape;

import java.awt.*;
import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import config.Config;
import utils.PosDelta;
import utils.CriticalPoint;

public class CompositeShape implements Shape {
    @Serial
    private static final long serialVersionUID = 1L;

    private List<Shape> children;
    private Map<Shape, PosDelta> deltaMap;
    private int size;

    private Point pos;

    @Override
    public void draw(Graphics g) {
        for (Shape child : children) {
            child.draw(g);
        }
    }

    @Override
    public void setPosition(int x, int y) {
        pos = new Point(x, y);
        for (Shape child : children) {
            assert(deltaMap.containsKey(child));
            PosDelta delta = deltaMap.get(child);
            child.setPosition(x + delta.dX, y + delta.dY);
        }
    }

    @Override
    public List<CriticalPoint> getCriticalPoints() {
        List<CriticalPoint> criticalPoints = new ArrayList<>();
        for (Shape child : children) {
            criticalPoints.addAll(child.getCriticalPoints());
        }
        return criticalPoints;
    }

    @Override
    public void setCriticalPoint(CriticalPoint criticalPoint) {
        for (Shape child : children) {
            List<CriticalPoint> criticalPoints = child.getCriticalPoints();
            if (criticalPoints.contains(criticalPoint)) {
                child.setCriticalPoint(criticalPoint);
                break;
            }
        }
    }

    @Override
    public Point getPosition() {
        return pos;
    }

    @Override
    public boolean canSelect(Point point) {
        boolean flag = false;
        for (Shape child : children) {
            if (child.canSelect(point)) {
                flag = true;
            }
        }
        return flag;
    }

    @Override
    public Shape copy() {
        CompositeShape cp = new CompositeShape();
        cp.setPosition(pos.x + Config.COPY_BIAS, pos.y + Config.COPY_BIAS);
        for (Shape child : children) {
            cp.addChild(child.copy());
        }
        return cp;
    }

    @Override
    public void setColor(Color color){
        for (Shape child : children) {
            child.setColor(color);
        }
    }

    public void addChild(Shape child) {
        children.add(child);
        PosDelta delta = new PosDelta();
        Point childPos = child.getPosition();
        delta.dX = childPos.x - pos.x;
        delta.dY = childPos.y - pos.y;
        deltaMap.put(child, delta);
        size++;
    }

    public void removeChild(Shape child) {
        if (children.contains(child)) {
            children.remove(child);
            deltaMap.remove(child);
            size--;
        }
    }
}
