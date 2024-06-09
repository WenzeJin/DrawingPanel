package shape;

import config.Config;

import java.awt.*;
import java.util.List;

public class SpRectangle implements Shape {
    private Point center;
    private int width, height;
    private final CriticalPoint centerCP;     // type 0
    private final CriticalPoint LTCP, LBCP, RTCP, RBCP;// type 1
    private Color color;


    public SpRectangle() {
        width = 100;
        height = 60;
        center = new Point();
        centerCP = new CriticalPoint(center.x, center.y, 0, this);
        LTCP = new CriticalPoint(center.x - width / 2, center.y - height / 2, 1, this);
        LBCP = new CriticalPoint(center.x - width / 2, center.y + height / 2, 2, this);
        RTCP = new CriticalPoint(center.x + width / 2, center.y - height / 2, 3, this);
        RBCP = new CriticalPoint(center.x + width / 2, center.y + height / 2, 4, this);
        color = Color.BLACK;
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        Color old = g2d.getColor();
        g2d.setColor(color);
        g2d.fillRect(center.x - width / 2, center.y - height / 2, width, height);
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
        return List.of(centerCP, LTCP, LBCP, RTCP, RBCP);
    }

    @Override
    public void setCriticalPoint(CriticalPoint criticalPoint) {
        if (criticalPoint == centerCP) {
            setPosition(centerCP.x, centerCP.y);
        } else if (criticalPoint == LTCP || criticalPoint == RBCP) {
            if (criticalPoint == RBCP) {
                if (RBCP.x < LTCP.x) {
                    RBCP.x = LTCP.x + 5;
                } else if (RBCP.y < LTCP.y) {
                    RBCP.y = LTCP.y + 5;
                }
            } else {
                if (RBCP.x < LTCP.x) {
                    LTCP.x = RBCP.x - 5;
                } else if (RBCP.y < LTCP.y) {
                    LTCP.y = RBCP.y - 5;
                }
            }
            width = RBCP.x - LTCP.x;
            height = RBCP.y - LTCP.y;
            center.setLocation((RBCP.x + LTCP.x) / 2, (RBCP.y + LTCP.y) / 2);
            setCPs();
        } else if (criticalPoint == LBCP || criticalPoint == RTCP) {
            if (criticalPoint == RTCP) {
                if (RTCP.x < LBCP.x) {
                    RTCP.x = LBCP.x + 5;
                } else if (RTCP.y > LBCP.y) {
                    RTCP.y = LBCP.y - 5;
                }
            } else {
                if (RTCP.x < LBCP.x) {
                    LBCP.x = RTCP.x - 5;
                } else if (RTCP.y > LBCP.y) {
                    LBCP.y = RTCP.y + 5;
                }
            }
            width = RTCP.x - LBCP.x;
            height = LBCP.y - RTCP.y;
            center.setLocation((RTCP.x + LBCP.x) / 2, (RTCP.y + LBCP.y) / 2);
            setCPs();
        }
    }

    @Override
    public boolean canSelect(Point point) {
        Rectangle bounds = getBounds();
        return bounds.contains(point);
    }

    @Override
    public Shape copy() {
        SpRectangle sp = new SpRectangle();
        sp.setPosition(center.x + Config.COPY_BIAS, center.y + Config.COPY_BIAS);
        sp.setColor(color);
        sp.setWidth(width);
        sp.setHeight(height);
        return sp;
    }

    @Override
    public Rectangle getBounds() {
        Rectangle bounds = new Rectangle();
        bounds.setBounds(center.x - width / 2, center.y - height / 2, width, height);
        return bounds;
    }

    public void setWidth(int width) {
        this.width = width;
        setCPs();
    }

    public void setHeight(int height) {
        this.height = height;
        setCPs();
    }

    private void setCPs(){
        centerCP.setLocation(center);
        LTCP.setLocation(center.x - width / 2, center.y - height / 2);
        LBCP.setLocation(center.x - width / 2, center.y + height / 2);
        RTCP.setLocation(center.x + width / 2, center.y - height / 2);
        RBCP.setLocation(center.x + width / 2, center.y + height / 2);
    }
}
