package command;


import java.util.Stack;

public class CommandManager {

    private static CommandManager instance;
    private final Stack<Command> commandStack;
    private final Stack<Command> redoStack;

    private CommandManager() {
        commandStack = new Stack<>();
        redoStack = new Stack<>();
    }

    public static synchronized CommandManager getInstance() {
        if (instance == null) {
            instance = new CommandManager();
        }
        return instance;
    }

    public void doCommand(Command command) {
        command.execute();
        commandStack.push(command);
        redoStack.clear();
    }

    public boolean canUndo(){
        return !commandStack.isEmpty();
    }

    public void undoCommand(){
        if(canUndo()){
            Command command = commandStack.pop();
            command.undo();
            redoStack.push(command);
        }
    }

    public boolean canRedo(){
        return !redoStack.isEmpty();
    }

    public void redoCommand(){
        if(canRedo()){
            Command command = redoStack.pop();
            command.execute();
            commandStack.push(command);
        }
    }

    public void forceClearState() {
        commandStack.clear();
        redoStack.clear();
    }
}
