package geometryzombiesmayhem.leveleditor;

import geometryzombiesmayhem.GeneralAdapter;
import java.awt.event.MouseEvent;

/**
 *
 * @author Yan
 */
public abstract class Brush {
    public GeneralAdapter gA;

    public abstract void start(MouseEvent starEvent);
    public abstract void undo();
    public abstract void redo();
}
