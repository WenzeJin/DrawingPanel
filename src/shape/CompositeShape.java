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
        resetBounds();
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
        int temp;
        if (size == 1) {
            bounds = new Rectangle(childBounds);
            changed = true;
        } else {
            if (childBounds.x < bounds.x) {
                changed = true;
                temp = bounds.x;
                bounds.x = childBounds.x;
                bounds.width += temp - bounds.x;
            }
            if (childBounds.y < bounds.y) {
                changed = true;
                temp = bounds.y;
                bounds.y = childBounds.y;
                bounds.height += temp - bounds.y;
            }
            if (childBounds.x + childBounds.width > bounds.x + bounds.width) {
                bounds.width += childBounds.x + childBounds.width - bounds.x - bounds.width;
            }
            if (childBounds.y + childBounds.height > bounds.y + bounds.height) {
                bounds.height += childBounds.y + childBounds.height - bounds.y - bounds.height;
            }
        }
        if (changed) {
            pos.setLocation(bounds.x, bounds.y);
            posCP.setLocation(pos);
            resetDelta();
        }
    }

    private void resetBounds() {
        if (size > 0) {
            int XMin, XMax, YMin, YMax;
            Rectangle childBounds = children.get(0).getBounds();
            XMin = childBounds.x;
            YMin = childBounds.y;
            XMax = XMin + childBounds.width;
            YMax = YMin + childBounds.height;
            for (Shape child : children) {
                Rectangle cb = child.getBounds();
                XMin = Math.min(cb.x, XMin);
                XMax = Math.max(cb.x + cb.width, XMax);
                YMin = Math.min(cb.y, YMin);
                YMax = Math.max(cb.y + cb.height, YMax);
            }
            bounds.setLocation(XMin, YMin);
            bounds.setSize(XMax - XMin, YMax - YMin);
            pos.setLocation(XMin, YMin);
            posCP.setLocation(pos);
            resetDelta();
        }
    }

    private void resetDelta() {
        System.out.println("resetDelta");
        for (Shape shape: children) {
            PosDelta deltaOld = deltaMap.get(shape);
            PosDelta delta = new PosDelta();
            Point childPos = shape.getPosition();
            delta.dX = childPos.x - pos.x;
            delta.dY = childPos.y - pos.y;
            deltaMap.put(shape, delta);
        }
    }
}
