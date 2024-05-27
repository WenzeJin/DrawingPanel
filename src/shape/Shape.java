package shape;

import utils.CriticalPoint;

import java.awt.*;
import java.io.Serializable;
import java.util.List;


public interface Shape extends Serializable {

    void draw(Graphics g); // draw this shape on awt graphics

    void setPosition(int x, int y); // set Position for this shape

    void setColor(Color color);

    Point getPosition();

    List<CriticalPoint> getCriticalPoints();  // get critical control points of this shape

    void setCriticalPoint(CriticalPoint criticalPoint); // use critical control points to resize this shape

    boolean canSelect(Point point);

    Shape copy();

}
