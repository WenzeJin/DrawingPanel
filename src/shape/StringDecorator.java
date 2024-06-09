package shape;

import utils.DPLogger;

import java.awt.*;
import java.util.List;

public class StringDecorator implements Shape {

    private final Shape child;
    private final String content;

    public StringDecorator(Shape child, String content) {
        this.child = child;
        this.content = content;
    }


    @Override
    public void draw(Graphics g) {
        child.draw(g);
        Color ori = g.getColor();
        g.setColor(Color.black);
        int x = child.getBounds().x;
        int y = child.getBounds().y + child.getBounds().height + 10;
        g.drawString(content, x, y);
        g.setColor(ori);
    }

    @Override
    public void setPosition(int x, int y) {
        DPLogger.error("Should not use this method in StringDecorator");
        child.setPosition(x, y);
    }

    @Override
    public void setColor(Color color) {
        child.setColor(color);
    }

    @Override
    public Color getColor() {
        return child.getColor();
    }

    @Override
    public Point getPosition() {
        return child.getPosition();
    }

    @Override
    public List<CriticalPoint> getCriticalPoints() {
        return child.getCriticalPoints();
    }

    @Override
    public void setCriticalPoint(CriticalPoint criticalPoint) {
        child.setCriticalPoint(criticalPoint);
    }

    @Override
    public boolean canSelect(Point point) {
        return child.canSelect(point);
    }

    @Override
    public Shape copy() {
        Shape childCopy = child.copy();
        return new StringDecorator(childCopy, content);
    }

    @Override
    public Rectangle getBounds() {
        Rectangle childBounds = new Rectangle(child.getBounds());
        childBounds.setSize(child.getBounds().width, child.getBounds().height + 10);
        return childBounds;
    }
}
