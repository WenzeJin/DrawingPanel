package shape;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ShapeManager implements Serializable {

    private static ShapeManager instance;

    private List<Shape> shapes;

    private ShapeManager() {
        shapes = new ArrayList<>();
    }

    public static synchronized ShapeManager getInstance() {
        if (instance == null) {
            instance = new ShapeManager();
        }
        return instance;
    }

    public static synchronized void deleteInstance(){
        instance = null;
    }

    public List<Shape> getShapes() {
        return shapes;
    }

    public void addShape(Shape shape) {
        shapes.add(shape);
    }

    public void removeShape(Shape shape) {
        shapes.remove(shape);
    }

    public void clear() {
        shapes.clear();
    }

    public static synchronized void saveToFile(File file) throws IOException {
        if (instance == null) {
            throw new IOException("Shape Manager not initialized");
        }
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(instance);
        }
    }

    public static synchronized void loadFromFile(File file) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            instance = (ShapeManager) ois.readObject();
        }
    }
}
