package frame;

import javax.swing.*;
import java.awt.*;

import canvas.Canvas;
import config.*;
import utils.*;

public class DrawPanelFrame extends JFrame {

    private Canvas canvas;
    private CommandedCanvas cmdCanvas;


    public DrawPanelFrame() {

        setTitle("Draw Panel" + " -v" + Config.VERSION);    //设置显示窗口标题
        setSize(1000,600);    //设置窗口显示尺寸

        canvas = new Canvas();      //初始化画布
        cmdCanvas = new CommandedCanvas(canvas);    //将其绑定到受命令控制的画布

        initUI();
    }

    public void canvasRequestFocusInWindow(){
        canvas.requestFocusInWindow();
    }

    private void initUI() {//创建一个标签

        JPanel jp = new JPanel();

        JButton undoButton = new JButton("撤销");
        undoButton.addActionListener(e -> cmdCanvas.undoCommand());

        JButton redoButton = new JButton("重做");
        redoButton.addActionListener(e -> cmdCanvas.redoCommand());

        JButton addCircleBt = new JButton("圆形");
        addCircleBt.addActionListener(e -> canvas.setNewShape("Circle"));

        jp.add(undoButton);
        jp.add(redoButton);
        jp.add(addCircleBt);

        add(jp, BorderLayout.NORTH);
        add(new JScrollPane(canvas), BorderLayout.CENTER);
    }


    public static void main(String[] args) {
        new DrawPanelFrame();
    }
}
