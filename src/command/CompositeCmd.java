package command;

import shape.CompositeShape;
import shape.Shape;

import java.util.List;

public class CompositeCmd implements Command {
    private List<Shape> content;
    private CompositeShape composite;

    public CompositeCmd() {
        composite = new CompositeShape();
    }

    @Override
    public void execute() {

    }

    @Override
    public void undo() {

    }
}
