package utils;
import command.*;
import canvas.Canvas;

public class CommandedCanvas {

    private final Canvas canvas;

    public CommandedCanvas(Canvas canvas) {
        this.canvas = canvas;
    }

    public Canvas getCanvas() {
        return canvas;
    }


    public void doCommand(Command command){
        CommandManager.getInstance().doCommand(command);
        canvas.forceClearState();
        canvas.repaint();
    }

    public void undoCommand(){
        CommandManager.getInstance().undoCommand();
        canvas.forceClearState();
        canvas.repaint();
    }

    public void redoCommand(){
        CommandManager.getInstance().redoCommand();
        canvas.forceClearState();
        canvas.repaint();
    }

    public void forceClearState(){
        canvas.forceClearState();
        CommandManager.getInstance().forceClearState();
        canvas.repaint();
    }

}
