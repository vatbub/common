package undo;

import java.util.Stack;

/**
 * A simple implementation of a undo/redo stack
 */
public class UndoRedoStack {
    private Stack<Command> undoStack = new Stack<>();
    private Stack<Command> redoStack = new Stack<>();

    /**
     * Adds a new command to the undo stack and reinitializes the redo stack. To be called when the user executes something new.
     *
     * @param command The command to be added to the undo stack
     */
    public void push(Command command) {
        redoStack = new Stack<>();
        undoStack.push(command);
    }

    /**
     * Pops the last command from the undo stack, puts it on the redo stack and calls its {@link Command#undo()}-method
     *
     * @return The command that was undone. <b>Its undo method was already called!</b>
     */
    public Command undoLast() {
        Command lastCommand = undoStack.pop();
        redoStack.push(lastCommand);
        lastCommand.undo();
        return lastCommand;
    }

    /**
     * Pops the last command from the redo stack, puts it on the undo stack and calls its {@link Command#redo()}-method. If no commandisfound on the redo stack,the last command from thundo stack is redone.
     *
     * @return The command that was redone. <b>Its redo method was already called!</b>
     */
    public Command redoNext() {
        Command nextCommand;
        if (redoStack.size() > 0) {
            nextCommand = redoStack.pop();
        } else {
            // redo the last command that was done
            nextCommand = undoStack.pop();
            // put it again on the stack
            undoStack.push(nextCommand);
        }
        undoStack.push(nextCommand);
        nextCommand.redo();
        return nextCommand;
    }
}
