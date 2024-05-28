package canvas;

import javax.swing.*;
import command.*;
import config.Config;
import shape.*;
import utils.*;
import shape.Shape;

import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.List;


public class Canvas extends JPanel {
    CommandManager commandManager;
    ShapeManager shapeManager;
    List<Shape> selectedShapes;
    String selectedNewShape;
    Color selectedColor;
    boolean isCtrlPressed;

    public Canvas() {
        shapeManager = ShapeManager.getInstance();
        commandManager = CommandManager.getInstance();
        selectedShapes = new LinkedList<>();        // use LinkedList to support add and remove better
        isCtrlPressed = false;      // Actually command in macOS
        selectedNewShape = null;
        selectedColor = null;

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

    public int selectShape(Point point) {
        /*
        Select all shapes that can catch this point.
        Return: number of this selection
         */
        int cnt = 0;
        List<Shape> shapes = shapeManager.getShapes();
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
            commandManager.doCommand(cmd);
            DPLogger.success("Copy Selected Shape: " + selectedShapes.size());
        }
        repaint();
    }

    public void forceClearState() {
        selectedShapes.clear();
        selectedNewShape = null;
        selectedColor = null;
    }

    private void addShape(Point p) {
        int x = p.x, y = p.y;
        Shape shape = ShapeFactory.createShape(selectedNewShape);
        shape.setPosition(x,y);
        if (selectedColor != null) {
            shape.setColor(selectedColor);
        }
        AddShapeCmd cmd = new AddShapeCmd(shape);
        commandManager.doCommand(cmd);
        DPLogger.success("Added Shape: " + selectedNewShape + "at " + x + ", " + y);
        repaint();
        if (!isCtrlPressed) {
            selectedNewShape = null;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (Shape shape : shapeManager.getShapes()) {
            shape.draw(g);
        }

        for (Shape shape : selectedShapes) {
            for (CriticalPoint point : shape.getCriticalPoints()) {
                // draw every critical point for this shape
                Color ori = g.getColor();
                g.setColor(Color.BLACK);
                g.fillRect(point.x - 5, point.y - 5, 10, 10);
                g.setColor(Color.WHITE);
                g.fillRect(point.x - 4, point.y - 4, 8, 8);
                g.setColor(ori);
            }
        }
    }



}
