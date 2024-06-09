package command;

import shape.Shape;
import shape.ShapeManager;

import java.util.ArrayList;
import java.util.List;


public class CopyShapesCmd implements Command {
    private List<Shape> copy;
    private boolean executed = false;

    public CopyShapesCmd() {
        copy = new ArrayList<>();
    }

    public void addShape(Shape shape) {
        if(!executed){
            copy.add(shape.copy());
        }

    }

    @Override
    public void execute() {
        executed = true;
        ShapeManager shapeManager = ShapeManager.getInstance();
        for(Shape shape : copy){
            shapeManager.addShape(shape);
        }
    }

    @Override
    public void undo() {
        ShapeManager shapeManager = ShapeManager.getInstance();
        for(Shape shape : copy){
            shapeManager.removeShape(shape);
        }
    }
}
