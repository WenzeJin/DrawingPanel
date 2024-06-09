package shape;

import config.Config;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SpEllipse implements Shape {
    private Point center;
    private int width, height;
    private final CriticalPoint centerCP;     // type 0
    private final CriticalPoint widthCP, heightCP; // type 1, 2
    private Color color;

    public SpEllipse() {
        this.width = 100;
        this.height = 60;
        this.center = new Point();
        this.color = Color.BLACK;
        centerCP = new CriticalPoint(center.x, center.y, 0, this);
        widthCP = new CriticalPoint(center.x + width / 2, center.y, 1, this);
        heightCP = new CriticalPoint(center.x, center.y + height / 2, 2, this);
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        Color old = g2d.getColor();
        g2d.setColor(color);
        g2d.fillOval(center.x - width / 2, center.y - height / 2, width, height);
        g2d.setColor(old);
    }

    @Override
    public void setPosition(int x, int y) {
        center.setLocation(x, y);
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
        return new Point(center.x, center.y);
    }

    @Override
    public List<CriticalPoint> getCriticalPoints() {
        List<CriticalPoint> points = new ArrayList<>();
        points.add(centerCP);
        points.add(widthCP);
        points.add(heightCP);
        return points;
    }

    @Override
    public void setCriticalPoint(CriticalPoint criticalPoint) {
        if (criticalPoint == centerCP) {
            setPosition(centerCP.x, centerCP.y);
        } else if (criticalPoint == widthCP) {
            width = Math.abs(criticalPoint.x - center.x) * 2;
            setCPs();
        } else if (criticalPoint == heightCP) {
            height = Math.abs(criticalPoint.y - center.y) * 2;
            setCPs();
        }
    }

    @Override
    public boolean canSelect(Point point) {
        double dx = point.x - center.x;
        double dy = point.y - center.y;
        return (dx * dx) / ((width / 2.0) * (width / 2.0)) + (dy * dy) / ((height / 2.0) * (height / 2.0)) <= 1.0;
    }

    @Override
    public Shape copy() {
        SpEllipse sp = new SpEllipse();
        sp.setPosition(center.x + Config.COPY_BIAS, center.y + Config.COPY_BIAS);
        sp.setColor(this.color);
        sp.setWidth(this.width);
        sp.setHeight(this.height);
        return sp;
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(center.x - width / 2, center.y - height / 2, width, height);
    }

    public void setWidth(int width) {
        this.width = width;
        setCPs();
    }

    public void setHeight(int height) {
        this.height = height;
        setCPs();
    }

    private void setCPs() {
        centerCP.setLocation(center);
        widthCP.setLocation(center.x + width / 2, center.y);
        heightCP.setLocation(center.x, center.y + height / 2);
    }
}
