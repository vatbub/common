package undo;

/**
 * Created by Frederik on 07/03/2017.
 */
public interface Command {
    public void getText();

    public void undo();

    public void redo();
}
