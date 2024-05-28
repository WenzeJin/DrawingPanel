package shape;

import java.awt.*;
import java.io.Serial;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import config.Config;
import utils.PosDelta;

public class CompositeShape implements Shape {
    @Serial
    private static final long serialVersionUID = 1L;

    private List<Shape> children;
    private Map<Shape, PosDelta> deltaMap;
    private int size;

    private Point pos;
    private final CriticalPoint posCP;
    private Rectangle bounds;

    public CompositeShape() {
        children = new ArrayList<>();
        deltaMap = new HashMap<>();
        size = 0;
        pos = new Point(0, 0);
        posCP = new CriticalPoint(0 ,0 ,0, this);
    }

    @Override
    public void draw(Graphics g) {
        for (Shape child : children) {
            child.draw(g);
        }
    }

    @Override
    public void setPosition(int x, int y) {
        pos = new Point(x, y);
        posCP.setLocation(pos);
        bounds.setLocation(pos);
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
        criticalPoints.add(posCP);
        return criticalPoints;
    }

    @Override
    public void setCriticalPoint(CriticalPoint criticalPoint) {
        if (criticalPoint == posCP) {
            setPosition(posCP.x, posCP.y);
        } else {
            for (Shape child : children) {
                List<CriticalPoint> criticalPoints = child.getCriticalPoints();
                if (criticalPoints.contains(criticalPoint)) {
                    child.setCriticalPoint(criticalPoint);
                    break;
                }
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
                break;
            }
        }
        return flag;
    }

    @Override
    public Shape copy() {
        CompositeShape cp = new CompositeShape();
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

    @Override
    public Color getColor() {
        return null;
    }

    @Override
    public Rectangle getBounds(){
        return bounds;
    }

    public void addChild(Shape child) {
        children.add(child);
        PosDelta delta = new PosDelta();
        Point childPos = child.getPosition();
        delta.dX = childPos.x - pos.x;
        delta.dY = childPos.y - pos.y;
        deltaMap.put(child, delta);
        size++;

        refreshBounds(child);
    }

    private void refreshBounds(Shape child) {
        Rectangle childBounds = child.getBounds();
        boolean changed = false;
        int temp = 0;
        if (childBounds.x < bounds.x || size == 0) {
            changed = true;
            temp = bounds.x;
            bounds.x = childBounds.x;
            bounds.width += temp - bounds.x;
        }
        if (childBounds.y < bounds.y || size == 0) {
            changed = true;
            temp = bounds.y;
            bounds.y = childBounds.y;
            bounds.height += temp - bounds.y;
        }
        if (childBounds.x + childBounds.width > bounds.x + bounds.width|| size == 0) {
            bounds.width += childBounds.x + childBounds.width - bounds.x - bounds.width;
        }
        if (childBounds.y + childBounds.height > bounds.y + bounds.height|| size == 0) {
            bounds.height += childBounds.y + childBounds.height - bounds.y - bounds.height;
        }
        if (changed) {
            for (Shape shape: children) {
                PosDelta delta = new PosDelta();
                Point childPos = shape.getPosition();
                delta.dX = childPos.x - pos.x;
                delta.dY = childPos.y - pos.y;
                deltaMap.put(shape, delta);
            }
        }
    }
}
