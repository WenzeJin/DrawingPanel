package command;

import shape.*;

public class AddShapeCmd implements Command {
    private final Shape shape;
    public AddShapeCmd(Shape shape) {
        this.shape = shape;
    }

    @Override
    public void execute() {
        ShapeManager.getInstance().addShape(shape);
    }

    @Override
    public void undo() {
        ShapeManager.getInstance().removeShape(shape);
    }
}
