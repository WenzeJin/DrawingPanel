package command;

import shape.CriticalPoint;
import shape.Shape;
import shape.ShapeManager;

import java.awt.*;

public class ChangeCPCmd implements Command {
    private final CriticalPoint selectedCP;
    private final Point oldPos;
    private final Point newPos;

    public ChangeCPCmd(CriticalPoint selectedCP, Point p) {
        this.selectedCP = selectedCP;
        this.oldPos = new Point(p);
        this.newPos = new Point(p);
        this.oldPos.setLocation(selectedCP);
        this.newPos.setLocation(p);
    }

    public void execute() {
        selectedCP.setLocation(newPos);
        for (Shape shape: ShapeManager.getInstance().getShapes()) {
            shape.setCriticalPoint(selectedCP);
        }
    }

    public void undo() {
        selectedCP.setLocation(oldPos);
        for (Shape shape: ShapeManager.getInstance().getShapes()) {
            shape.setCriticalPoint(selectedCP);
        }
    }
}
