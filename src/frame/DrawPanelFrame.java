package frame;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

import canvas.Canvas;
import config.*;
import shape.ShapeManager;
import utils.*;

public class DrawPanelFrame extends JFrame {

    private final Canvas canvas;
    private final CommandedCanvas cmdCanvas;

    private JPanel selectedColorPanel;


    public DrawPanelFrame() {

        setTitle("Draw Panel" + " -v" + Config.VERSION);    //设置显示窗口标题
        setSize(1000,600);    //设置窗口显示尺寸

        canvas = new Canvas();      //初始化画布
        cmdCanvas = new CommandedCanvas(canvas);    //将其绑定到受命令控制的画布

        initUI();

        canvas.requestFocusInWindow();
    }

    public void canvasRequestFocusInWindow(){
        canvas.requestFocusInWindow();
    }

    private void initUI() {



        JPanel editPanel = getEditPanel();
        JPanel colorPanel = getColorPanel();

        add(editPanel, BorderLayout.NORTH);
        add(colorPanel, BorderLayout.EAST);

        add(new JScrollPane(canvas), BorderLayout.CENTER);
    }

    private JPanel getEditPanel() {
        JPanel editPanel = new JPanel();

        JButton undoButton = new JButton("撤销");
        undoButton.addActionListener(e -> cmdCanvas.undoCommand());

        JButton redoButton = new JButton("重做");
        redoButton.addActionListener(e -> cmdCanvas.redoCommand());

        JButton addCircleBt = new JButton("圆形");
        addCircleBt.addActionListener(e -> canvas.setNewShape("Circle"));

        JButton copyButton = new JButton("复制选中");
        copyButton.addActionListener(e -> canvas.copySelectedShape());

        JButton colorButton = new JButton("重新着色");
        colorButton.addActionListener(e -> canvas.changeSelectedShapeColor());

        JButton exportButton = new JButton("导出图片");
        exportButton.addActionListener(e -> handleExportToImage());

        JButton saveButton = new JButton("保存");
        saveButton.addActionListener(e -> handleSave());

        JButton loadButton = new JButton("加载");
        loadButton.addActionListener(e -> handleLoad());

        editPanel.add(undoButton);
        editPanel.add(redoButton);
        editPanel.add(addCircleBt);
        editPanel.add(copyButton);
        editPanel.add(colorButton);
        editPanel.add(exportButton);
        editPanel.add(saveButton);
        editPanel.add(loadButton);
        return editPanel;
    }

    private JPanel getColorPanel() {
        JPanel colorPanel = new JPanel();
        colorPanel.setLayout(new BoxLayout(colorPanel, BoxLayout.Y_AXIS));
        colorPanel.setBorder(BorderFactory.createTitledBorder("选取"));

        for (Color color : Config.COLORS) {
            JButton colorButton = new JButton();
            colorButton.addActionListener(e -> handleColorSelect(color));
            colorButton.setBackground(color);
            colorButton.setPreferredSize(new Dimension(50, 50));
            colorButton.setOpaque(true);
            colorButton.setBorderPainted(false);
            colorPanel.add(colorButton);
        }


        JLabel selectedColorLabel = new JLabel("选中：");

        selectedColorPanel = new JPanel();
        selectedColorPanel.setBackground(Color.BLACK);
        selectedColorPanel.setPreferredSize(new Dimension(50, 50));


        colorPanel.add(selectedColorLabel);
        colorPanel.add(selectedColorPanel);
        return colorPanel;
    }

    private void handleExportToImage(){
        JFileChooser chooser = new JFileChooser(new File(Config.USER_DIR));
        int option = chooser.showSaveDialog(this);
        if(option == JFileChooser.APPROVE_OPTION){
            File file = chooser.getSelectedFile();
            try{
                canvas.exportToImage(file);
                DPLogger.success("Export successful to " + file.getAbsolutePath());
            } catch(IOException e){
                DPLogger.error("Export failed");
            }
        }
    }

    private void handleColorSelect(Color color) {
        canvas.setSelectedColor(color);
        selectedColorPanel.setBackground(color);
    }

    private void handleSave(){
        JFileChooser chooser = new JFileChooser(new File(Config.USER_DIR));
        int option = chooser.showSaveDialog(this);
        if(option == JFileChooser.APPROVE_OPTION){
            File file = chooser.getSelectedFile();
            try{
                ShapeManager.saveToFile(file);
                DPLogger.success("Save successful to " + file.getAbsolutePath());
            } catch(IOException e){
                DPLogger.error("Save failed");
            }
        }
    }

    private void handleLoad(){
        JFileChooser chooser = new JFileChooser(new File(Config.USER_DIR));
        int option = chooser.showSaveDialog(this);
        if(option == JFileChooser.APPROVE_OPTION){
            File file = chooser.getSelectedFile();
            try{
                ShapeManager.loadFromFile(file);
                DPLogger.success("Load successful from " + file.getAbsolutePath());
            } catch(IOException e){
                DPLogger.error("Load failed");
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            cmdCanvas.forceClearState();
        }
    }


    public static void main(String[] args) {
        new DrawPanelFrame();
    }
}
