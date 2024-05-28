package shape;

public class ShapeFactory {
    public static Shape createShape(String shapeType) {
        Shape shape;
        switch (shapeType) {
            case "Circle":
                shape = new SpCircle();
                break;
            // TODO: add more shapes
            default:
                throw new IllegalArgumentException("Unknown shape type: " + shapeType);
        }
        return shape;
    }
}
