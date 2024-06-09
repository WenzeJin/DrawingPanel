package shape;

public class ShapeFactory {
    public static Shape createShape(String shapeType) {
        return switch (shapeType) {
            case "Circle" -> new SpCircle();
            case "Rectangle" -> new SpRectangle();
            case "Triangle" -> new SpTriangle();
            case "Line" -> new SpLine();
            case "Ellipse" -> new SpEllipse();
            // TODO: add more shapes
            default -> throw new IllegalArgumentException("Unknown shape type: " + shapeType);
        };
    }
}
