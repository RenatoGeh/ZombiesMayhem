package geometryzombiesmayhem;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

/**
 * <p>Able to trigger events.</p>
 * 
 * @author Renato Lui Geh
 */
public interface Triggerable extends Window.Conflictable {    
    public void mouseTriggered(MouseEvent event);
    public void mouseWheelTriggered(MouseWheelEvent event);
    public void keyTriggered(KeyEvent event);
}
