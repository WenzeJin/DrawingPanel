package shape;

import config.Config;

import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;

public class SpLine implements Shape {
    private Point start, end;
    private final CriticalPoint startCP, endCP, centerCP; // start and end points, center for moving the line
    private Color color;
    private static final int TOLERANCE = 5;

    public SpLine() {
        start = new Point(0, 0);
        end = new Point(100, 100);
        color = Color.BLACK;
        startCP = new CriticalPoint(start.x, start.y, 0, this);
        endCP = new CriticalPoint(end.x, end.y, 1, this);
        centerCP = new CriticalPoint((start.x + end.x) / 2, (start.y + end.y) / 2, 2, this);
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        Color old = g2d.getColor();
        g2d.setColor(color);
        g2d.setStroke(new BasicStroke(3));
        g2d.drawLine(start.x, start.y, end.x, end.y);
        g2d.setStroke(new BasicStroke(1));
        g2d.setColor(old);
    }

    @Override
    public void setPosition(int x, int y) {
        int deltaX = x - getPosition().x;
        int deltaY = y - getPosition().y;
        start.translate(deltaX, deltaY);
        end.translate(deltaX, deltaY);
        setCPs();
    }

    @Override
    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public Point getPosition() {
        int centerX = (start.x + end.x) / 2;
        int centerY = (start.y + end.y) / 2;
        return new Point(centerX, centerY);
    }

    @Override
    public List<CriticalPoint> getCriticalPoints() {
        List<CriticalPoint> points = new ArrayList<>();
        points.add(startCP);
        points.add(endCP);
        points.add(centerCP);
        return points;
    }

    @Override
    public void setCriticalPoint(CriticalPoint criticalPoint) {
        if (criticalPoint == startCP) {
            start.setLocation(criticalPoint.x, criticalPoint.y);
        } else if (criticalPoint == endCP) {
            end.setLocation(criticalPoint.x, criticalPoint.y);
        } else if (criticalPoint == centerCP) {
            setPosition(criticalPoint.x, criticalPoint.y);
        }
        setCPs();
    }

    @Override
    public boolean canSelect(Point point) {
        Line2D line = new Line2D.Float(start, end);
        return line.ptSegDist(point) <= TOLERANCE;
    }

    @Override
    public Shape copy() {
        SpLine sp = new SpLine();
        sp.start.setLocation(this.start.x + Config.COPY_BIAS, this.start.y + Config.COPY_BIAS);
        sp.end.setLocation(this.end.x + Config.COPY_BIAS, this.end.y + Config.COPY_BIAS);
        sp.setColor(this.color);
        sp.setCPs();
        return sp;
    }

    @Override
    public Rectangle getBounds() {
        int minX = Math.min(start.x, end.x);
        int minY = Math.min(start.y, end.y);
        int maxX = Math.max(start.x, end.x);
        int maxY = Math.max(start.y, end.y);
        return new Rectangle(minX, minY, maxX - minX, maxY - minY);
    }

    private void setCPs() {
        startCP.setLocation(start);
        endCP.setLocation(end);
        centerCP.setLocation((start.x + end.x) / 2, (start.y + end.y) / 2);
    }
}
