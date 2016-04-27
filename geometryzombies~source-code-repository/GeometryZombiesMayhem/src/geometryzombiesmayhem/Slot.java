package geometryzombiesmayhem;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

/**
 * <p>A <code>Slot</code> for a <code>Stack</code>.</p>
 * 
 * @author Renato Lui Geh
 */
public class Slot extends ComponentP implements Paintable {
    private Vector2D position;
    private Stack queue;
    private Rectangle2D frame;
    
    public Slot(Vector2D position) {
        this.position = position;
//        this.queue = new Stack(this.position);
        
        this.frame = new Rectangle2D.Float(position.getX()-1, position.getY()-1, 
                Slot.SLOT_PIXELS_WIDTH+2, Slot.SLOT_PIXELS_HEIGHT+2);
        
        this.bounds = frame;
    }
    
    @Override
    public void paint(Graphics2D g2, Component c) {
        g2.setColor(Color.GRAY);
        g2.draw(frame);
        
        if(queue.isEmpty()) return;
        
        if(!queue.isSelected()) {
            g2.drawImage(queue.getIcon(), null, (int)this.position.getX(), (int)this.position.getY());
            g2.drawString(Integer.toString(queue.size()), this.position.getX() + 2, this.position.getY() + 2);
        } else {
            queue.paint(g2, c);
        }
    }
    
    @Override
    protected void processMouseEvent(MouseEvent event) {
        
    }

    @Override
    public void updateWindowDefaultPosition() {
        position.addLocal(window.x, window.y);
    }

    @Override
    public boolean isActive() {return true;}
    @Override
    public void setActive(boolean active) {}
    
    @Override
    public int getWidth() {return (int)frame.getWidth();}
    @Override
    public int getHeight() {return (int)frame.getHeight();}
    
    public static final int SLOT_PIXELS_WIDTH = 45;
    public static final int SLOT_PIXELS_HEIGHT = 45;
}
