package canvas;

import command.*;
import config.Config;
import shape.*;
import utils.*;
import shape.Shape;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;


public class Canvas extends JPanel {
    // State vars
    private final List<Shape> selectedShapes;
    private CriticalPoint selectedCP;
    private String selectedNewShape;
    private Color selectedColor;
    private boolean isCtrlPressed;

    // Other vars
    private Point movingCP;

    public Canvas() {
        selectedShapes = new LinkedList<>();        // use LinkedList to support add and remove better
        isCtrlPressed = false;                      // Actually command in macOS
        selectedNewShape = null;
        selectedColor = Color.BLACK;

        movingCP = new Point();

        setBackground(Color.WHITE);

        /*
        Add Mouse and Keyboard Listener.
         */
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                requestFocusInWindow();
                Point p = e.getPoint();

                // first check if this action is to add Shape
                if (selectedNewShape != null) {
                    addShape(p);
                    selectedShapes.clear();
                } else {
                    // TODO: add more mouse event cases
                    for (Shape s : selectedShapes) {
                        for (CriticalPoint cp: s.getCriticalPoints()) {
                            if (cp.canSelect(p)) {
                                selectedCP = cp;
                                movingCP.x = p.x;
                                movingCP.y = p.y;
                            }
                        }
                    }
                    if (selectedCP != null) {
                        DPLogger.success("Selected CP");
                        return;
                    }

                    // if No CP is selected
                    if (!isCtrlPressed) {
                        selectedShapes.clear();
                    }

                    int select = selectShape(p);
                    if (select == 0) {
                        // this is where nothing is clicked
                        selectedShapes.clear();
                    }

                }

                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (selectedCP != null) {
                    DPLogger.success("Released CP");
                    // use movingCP to change cp
                    ChangeCPCmd cmd = new ChangeCPCmd(selectedCP, movingCP);

                    CommandManager.getInstance().doCommand(cmd);

                    selectedCP = null;
                }
                repaint();
            }
        });


        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                movingCP = e.getPoint();
                repaint();
            }
        });

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_CONTROL && !Config.OS.contains("mac")
                        || e.getKeyCode() == KeyEvent.VK_META && Config.OS.contains("mac") ) {
                    DPLogger.success("Key Control Pressed");
                    isCtrlPressed = true;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_CONTROL && !Config.OS.contains("mac")
                        || e.getKeyCode() == KeyEvent.VK_META && Config.OS.contains("mac") ) {
                    DPLogger.success("Key Control Released");
                    isCtrlPressed = false;
                }
            }
        });

        setFocusable(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (Shape shape : ShapeManager.getInstance().getShapes()) {
            shape.draw(g);
        }

        for (Shape shape : selectedShapes) {
            for (CriticalPoint point : shape.getCriticalPoints()) {
                // draw every CP for this shape
                paintCP(g, point);
            }

            paintBounds(g, shape);
        }

        if (selectedCP != null) {
            paintCP(g, movingCP);
        }
    }

    private void paintCP(Graphics g, Point p) {
        Color ori = g.getColor();
        g.setColor(Color.BLACK);
        g.fillRect(p.x - (Config.CP_SIZE / 2), p.y - (Config.CP_SIZE / 2),
                Config.CP_SIZE, Config.CP_SIZE);
        g.setColor(Color.WHITE);
        g.fillRect(p.x - (Config.CP_SIZE / 2 - 1), p.y - (Config.CP_SIZE / 2 - 1),
                Config.CP_SIZE - 2, Config.CP_SIZE - 2);
        g.setColor(ori);
    }

    private void paintBounds(Graphics g, Shape shape) {
        Rectangle bounds = shape.getBounds();
        Color ori = g.getColor();
        g.setColor(Color.MAGENTA);
        g.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);
        g.setColor(ori);
    }

    private void addShape(Point p) {
        int x = p.x, y = p.y;
        Shape shape = ShapeFactory.createShape(selectedNewShape);
        shape.setPosition(x,y);
        shape.setColor(selectedColor);
        AddShapeCmd cmd = new AddShapeCmd(shape);
        CommandManager.getInstance().doCommand(cmd);
        DPLogger.success("Added Shape: " + selectedNewShape + " at " + x + ", " + y);
        repaint();
        if (!isCtrlPressed) {
            selectedNewShape = null;
        }
    }

    public int selectShape(Point point) {
        /*
        Select all shapes that can catch this point.
        Return: number of this selection
         */
        int cnt = 0;
        List<Shape> shapes = ShapeManager.getInstance().getShapes();
        for (Shape shape : shapes) {
            if (shape.canSelect(point) && !selectedShapes.contains(shape)) {
                DPLogger.success("Selected Shape.");
                selectedShapes.add(shape);
                cnt++;
            }
        }
        return cnt;
    }

    public void setNewShape(String newShape) {
        selectedNewShape = newShape;
    }

    public void copySelectedShape() {
        if (!selectedShapes.isEmpty()) {
            CopyShapesCmd cmd = new CopyShapesCmd();
            for (Shape shape : selectedShapes) {
                cmd.addShape(shape);
            }
            CommandManager.getInstance().doCommand(cmd);
            DPLogger.success("Copy Selected Shape: " + selectedShapes.size());
        }
        repaint();
    }

    public void changeSelectedShapeColor() {
        if (!selectedShapes.isEmpty()) {
            ChangeColorCmd cmd = new ChangeColorCmd(selectedColor);
            for (Shape shape : selectedShapes) {
                cmd.addShape(shape);
            }
            CommandManager.getInstance().doCommand(cmd);
            DPLogger.success("Colored Selected Shape: " + selectedShapes.size() + "With Color: " + selectedColor);
        }
        repaint();
    }

    public void forceClearState() {
        selectedShapes.clear();
        selectedNewShape = null;
        selectedColor = Color.BLACK;
    }

    public void exportToImage(File file) throws IOException {
        BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();
        paint(g2d);
        g2d.dispose();
        ImageIO.write(image, "jpg", file);
    }

    public Color getSelectedColor() {
        return selectedColor;
    }

    public void setSelectedColor(Color color) {
        selectedColor = color;
    }

    public void decorateSelectedShape(String content) {
        if (!selectedShapes.isEmpty()) {
            StringDecorateCmd cmd = new StringDecorateCmd();
            for (Shape shape : selectedShapes) {
                cmd.addShape(shape, content);
            }
            selectedShapes.clear();
            CommandManager.getInstance().doCommand(cmd);
            DPLogger.success("Decorated Selected Shape: " + selectedShapes.size() + "With String: " + content);
        }
        repaint();
    }

    public void removeSelectedShape() {
        if (!selectedShapes.isEmpty()) {
            RemoveCmd cmd = new RemoveCmd();
            for (Shape shape : selectedShapes) {
                cmd.addShape(shape);
            }
            selectedShapes.clear();
            CommandManager.getInstance().doCommand(cmd);
        }
        repaint();
    }

    public void compositeSelectedShape() {
        if (!selectedShapes.isEmpty()) {
            CompositeCmd cmd = new CompositeCmd();
            for (Shape shape : selectedShapes) {
                cmd.addShape(shape);
            }
            selectedShapes.clear();
            CommandManager.getInstance().doCommand(cmd);
        }
        repaint();
    }

}
