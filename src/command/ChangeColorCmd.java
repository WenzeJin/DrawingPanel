package command;

import shape.Shape;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChangeColorCmd implements Command {
    private List<Shape> shapes;
    private Color newColor;
    private Map<Shape, Color> oldColor;
    private boolean executed;

    public ChangeColorCmd(Color color) {
        shapes = new ArrayList<>();
        oldColor = new HashMap<>();
        executed = false;
        newColor = color;
    }

    public void addShape(Shape shape) {
        if (!executed) {
            shapes.add(shape);
            oldColor.put(shape, shape.getColor());
        }
    }

    @Override
    public void execute() {
        executed = true;
        for (Shape shape : shapes) {
            shape.setColor(newColor);
        }
    }

    @Override
    public void undo() {
        for (Shape shape : shapes) {
            if (oldColor.get(shape) != null) {
                shape.setColor(oldColor.get(shape));
            }
        }
    }
}
