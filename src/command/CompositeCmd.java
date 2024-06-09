package command;

import shape.CompositeShape;
import shape.Shape;
import shape.ShapeManager;

import java.util.ArrayList;
import java.util.List;

public class CompositeCmd implements Command {
    private List<Shape> content;
    private CompositeShape composite;
    boolean executed = false;

    public CompositeCmd() {
        composite = new CompositeShape();
        content = new ArrayList<>();
    }

    public void addShape(Shape sp) {
        if (!executed) {
            content.add(sp);
            composite.addChild(sp);
        }
    }

    @Override
    public void execute() {
        executed = true;
        for (Shape shape : content) {
            ShapeManager.getInstance().removeShape(shape);
        }
        ShapeManager.getInstance().addShape(composite);
    }

    @Override
    public void undo() {
        ShapeManager.getInstance().removeShape(composite);
        for (Shape shape : content) {
            ShapeManager.getInstance().addShape(shape);
        }
    }
}
