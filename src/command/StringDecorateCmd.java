package command;

import shape.Shape;
import shape.ShapeManager;
import shape.StringDecorator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StringDecorateCmd implements Command {
    private List<StringDecorator> decorated;
    private List<Shape> original;
    private Map<Shape, StringDecorator> o2dMap;
    boolean executed = false;

    public StringDecorateCmd() {
        decorated = new ArrayList<>();
        original = new ArrayList<>();
        o2dMap = new HashMap<>();
    }

    public void addShape(Shape sp, String content) {
        if (!executed) {
            StringDecorator decorator = new StringDecorator(sp, content);
            original.add(sp);
            decorated.add(decorator);
            o2dMap.put(sp, decorator);
        }
    }

    @Override
    public void execute() {
        executed = true;
        for(Shape sp: original) {
            StringDecorator decorator = o2dMap.get(sp);
            ShapeManager.getInstance().removeShape(sp);
            ShapeManager.getInstance().addShape(decorator);
        }
    }

    @Override
    public void undo() {
        for(Shape sp: original) {
            StringDecorator decorator = o2dMap.get(sp);
            ShapeManager.getInstance().removeShape(decorator);
            ShapeManager.getInstance().addShape(sp);
        }
    }
}
