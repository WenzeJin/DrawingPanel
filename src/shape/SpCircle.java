package shape;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import config.Config;

public class SpCircle implements Shape {
    private int radius;
    private Point center;
    private final CriticalPoint centerCP;     // type 0
    private final CriticalPoint edgeCP;       // type 1
    private Color color;

    public SpCircle() {
        this.radius = 50;
        this.center = new Point();
        this.color = Color.BLACK;
        centerCP = new CriticalPoint(center.x, center.y, 0, this);
        edgeCP = new CriticalPoint(center.x + radius, center.y, 1, this);
    }

    public void setRadius(int radius) {
        this.radius = radius;
        edgeCP.x = center.x + radius;
    }

    @Override
    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        Color old = g2d.getColor();
        g2d.setColor(color);
        g2d.fillOval(center.x - radius, center.y - radius, 2 * radius, 2 * radius);
        g2d.setColor(old);
    }

    @Override
    public void setPosition(int x, int y) {
        center = new Point(x, y);
        centerCP.x = center.x; centerCP.y = center.y;
        edgeCP.x = center.x + radius; edgeCP.y = center.y;
    }

    @Override
    public Point getPosition() {
        return new Point(center.x, center.y);
    }

    @Override
    public List<CriticalPoint> getCriticalPoints() {
        List<CriticalPoint> points = new ArrayList<>();
        points.add(centerCP);
        points.add(edgeCP);
        return points;
    }

    @Override
    public void setCriticalPoint(CriticalPoint criticalPoint) {
        if (criticalPoint != null) {
            if (criticalPoint == centerCP) {
                // if the moving point is centerCP then change center with fixed radius
                center.x = criticalPoint.x;
                center.y = criticalPoint.y;
                edgeCP.x = criticalPoint.x + radius;
                edgeCP.y = criticalPoint.y;
            } else if (criticalPoint == edgeCP) {
                //  if the moving point is edgeCP then change radius, and move center y to edgeCP y
                center.y = criticalPoint.y;
                centerCP.y = criticalPoint.y;
                radius = criticalPoint.x - center.x;
                if (radius < 0) {
                    radius = 5;
                    edgeCP.x = center.x + radius;
                }
            }
        }
    }

    @Override
    public boolean canSelect(Point point) {
        double dist = center.distance(point);
        return dist < (double) radius;
    }

    @Override
    public Shape copy() {
        SpCircle sp = new SpCircle();
        sp.setPosition(center.x + Config.COPY_BIAS, center.y + Config.COPY_BIAS);
        sp.setColor(this.color);
        sp.setRadius(radius);
        return sp;
    }

    @Override
    public Color getColor() {
        return color;
    }

    public Rectangle getBounds() {
        return new Rectangle(center.x - radius, center.y - radius, 2 * radius, 2 * radius);
    }
}
