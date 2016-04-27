package geometryzombiesmayhem;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Processes events for a <code>WindowP</code>.
 * 
 * @author Renato Lui Geh
 */
public abstract class EventTrigger extends ComponentP implements Triggerable {
    private List<Triggerable> triggers = new ArrayList<>();
    
    public EventTrigger(Window w) {
        this.bounds = new Rectangle2D.Float(0, 0, w.getWidth(), w.getHeight());
    }

    public void updateBounds(Window w) {
        if(!(bounds instanceof Rectangle2D))
            bounds = new Rectangle2D.Float(w.getX(), w.getY(), w.getWidth(), w.getHeight());
        
        Rectangle2D foo = (Rectangle2D)bounds;
        ((Rectangle2D.Float)bounds).setRect(foo.getX(), foo.getY(), w.getWidth(), w.getHeight());
    }
    
    @Override
    protected void processMouseEvent(MouseEvent event) {
        super.processMouseEvent(event);
        this.mouseTriggered(event);
        for(Triggerable t : triggers) t.mouseTriggered(event);
    }
    @Override
    protected void processKeyEvent(KeyEvent event) {
        super.processKeyEvent(event);
        this.keyTriggered(event);
        for(Triggerable t : triggers) t.keyTriggered(event);
    }
    @Override
    protected void processMouseWheelEvent(MouseWheelEvent event) {
        super.processMouseWheelEvent(event);
        this.mouseWheelTriggered(event);
        for(Triggerable t : triggers) t.mouseWheelTriggered(event);
    }
    
    @Override
    public void mouseTriggered(MouseEvent event) {};
    @Override
    public void mouseWheelTriggered(MouseWheelEvent event) {};
    @Override
    public void keyTriggered(KeyEvent event) {};
    
    public void add(Triggerable t) {this.triggers.add(t);}
    public void remove(Triggerable t) {this.triggers.remove(t);}
    
    @Override
    public void updateWindowDefaultPosition() {}
}
