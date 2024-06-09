package shape;

import config.Config;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SpTriangle implements Shape {
    private Point vertex1, vertex2, vertex3;
    private final CriticalPoint cp1, cp2, cp3, centerCP; // type 0, 1, 2
    private Color color;

    public SpTriangle() {
        vertex1 = new Point(0, 0);
        vertex2 = new Point(50, 0);
        vertex3 = new Point(25, 50);
        color = Color.BLACK;
        cp1 = new CriticalPoint(vertex1.x, vertex1.y, 0, this);
        cp2 = new CriticalPoint(vertex2.x, vertex2.y, 1, this);
        cp3 = new CriticalPoint(vertex3.x, vertex3.y, 2, this);
        centerCP = new CriticalPoint((vertex1.x + vertex2.x + vertex3.x) / 3,
                (vertex1.y + vertex2.y + vertex3.y) / 3,
                1, this);
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        Color old = g2d.getColor();
        g2d.setColor(color);

        int[] xPoints = {vertex1.x, vertex2.x, vertex3.x};
        int[] yPoints = {vertex1.y, vertex2.y, vertex3.y};
        g2d.fillPolygon(xPoints, yPoints, 3);

        g2d.setColor(old);
    }

    @Override
    public void setPosition(int x, int y) {
        int deltaX = x - getPosition().x;
        int deltaY = y - getPosition().y;
        vertex1.translate(deltaX, deltaY);
        vertex2.translate(deltaX, deltaY);
        vertex3.translate(deltaX, deltaY);
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
        int centerX = (vertex1.x + vertex2.x + vertex3.x) / 3;
        int centerY = (vertex1.y + vertex2.y + vertex3.y) / 3;
        return new Point(centerX, centerY);
    }

    @Override
    public List<CriticalPoint> getCriticalPoints() {
        List<CriticalPoint> points = new ArrayList<>();
        points.add(cp1);
        points.add(cp2);
        points.add(cp3);
        points.add(centerCP);
        return points;
    }

    @Override
    public void setCriticalPoint(CriticalPoint criticalPoint) {
        if (criticalPoint == cp1) {
            vertex1.setLocation(criticalPoint.x, criticalPoint.y);
        } else if (criticalPoint == cp2) {
            vertex2.setLocation(criticalPoint.x, criticalPoint.y);
        } else if (criticalPoint == cp3) {
            vertex3.setLocation(criticalPoint.x, criticalPoint.y);
        } else if (criticalPoint == centerCP) {
            setPosition(centerCP.x, centerCP.y);
        }
        setCPs();
    }

    @Override
    public boolean canSelect(Point point) {
        Polygon polygon = new Polygon(
                new int[] {vertex1.x, vertex2.x, vertex3.x},
                new int[] {vertex1.y, vertex2.y, vertex3.y},
                3
        );
        return polygon.contains(point);
    }

    @Override
    public Shape copy() {
        SpTriangle sp = new SpTriangle();
        sp.vertex1.setLocation(this.vertex1.x + Config.COPY_BIAS, this.vertex1.y + Config.COPY_BIAS);
        sp.vertex2.setLocation(this.vertex2.x + Config.COPY_BIAS, this.vertex2.y + Config.COPY_BIAS);
        sp.vertex3.setLocation(this.vertex3.x + Config.COPY_BIAS, this.vertex3.y + Config.COPY_BIAS);
        sp.setColor(this.color);
        sp.setCPs();
        return sp;
    }

    @Override
    public Rectangle getBounds() {
        int minX = Math.min(vertex1.x, Math.min(vertex2.x, vertex3.x));
        int minY = Math.min(vertex1.y, Math.min(vertex2.y, vertex3.y));
        int maxX = Math.max(vertex1.x, Math.max(vertex2.x, vertex3.x));
        int maxY = Math.max(vertex1.y, Math.max(vertex2.y, vertex3.y));
        return new Rectangle(minX, minY, maxX - minX, maxY - minY);
    }

    private void setCPs() {
        cp1.setLocation(vertex1);
        cp2.setLocation(vertex2);
        cp3.setLocation(vertex3);
        centerCP.setLocation(getPosition());
    }
}