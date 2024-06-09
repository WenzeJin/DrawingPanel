package command;

import shape.Shape;
import shape.ShapeManager;

import java.util.ArrayList;
import java.util.List;

public class RemoveCmd implements Command {
    List<Shape> removed;
    boolean executed = false;

    public RemoveCmd() {
        removed = new ArrayList<>();
    }

    public void addShape(Shape shape) {
        if (!executed) {
            removed.add(shape);
        }
    }


    @Override
    public void execute() {
        executed = true;
        for (Shape shape : removed) {
            ShapeManager.getInstance().removeShape(shape);
        }
    }

    @Override
    public void undo() {
        for (Shape shape : removed) {
            ShapeManager.getInstance().addShape(shape);
        }
    }
}
