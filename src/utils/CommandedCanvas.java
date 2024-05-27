package utils;
import command.*;
import canvas.Canvas;

public class CommandedCanvas {

    private final Canvas canvas;
    private final CommandManager commandManager;

    public CommandedCanvas(Canvas canvas) {
        this.canvas = canvas;
        this.commandManager = CommandManager.getInstance();
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public void doCommand(Command command){
        commandManager.doCommand(command);
        canvas.forceClearState();
        canvas.repaint();
    }

    public void undoCommand(){
        commandManager.undoCommand();
        canvas.forceClearState();
        canvas.repaint();
    }

    public void redoCommand(){
        commandManager.redoCommand();
        canvas.forceClearState();
        canvas.repaint();
    }

}
