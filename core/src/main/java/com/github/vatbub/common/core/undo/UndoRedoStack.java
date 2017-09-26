package com.github.vatbub.common.core.undo;

/*-
 * #%L
 * FOKProjects Common
 * %%
 * Copyright (C) 2016 - 2017 Frederik Kammel
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


import java.util.Stack;

/**
 * A simple implementation of a com.github.vatbub.common.core.undo/redo stack
 */
public class UndoRedoStack {
    private Stack<Command> undoStack = new Stack<>();
    private Stack<Command> redoStack = new Stack<>();

    /**
     * Adds a new command to the com.github.vatbub.common.core.undo stack and reinitializes the redo stack. To be called when the user executes something new.
     *
     * @param command The command to be added to the com.github.vatbub.common.core.undo stack
     */
    public void push(Command command) {
        redoStack = new Stack<>();
        undoStack.push(command);
    }

    /**
     * Pops the last command from the com.github.vatbub.common.core.undo stack, puts it on the redo stack and calls its {@link Command#undo()}-method
     *
     * @return The command that was undone. <b>Its com.github.vatbub.common.core.undo method was already called!</b>
     */
    public Command undoLast() {
        Command lastCommand = undoStack.pop();
        redoStack.push(lastCommand);
        lastCommand.undo();
        return lastCommand;
    }

    /**
     * Pops the last command from the redo stack, puts it on the com.github.vatbub.common.core.undo stack and calls its {@link Command#redo()}-method. If no command is found on the redo stack,the last command from the undo stack is redone.
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
