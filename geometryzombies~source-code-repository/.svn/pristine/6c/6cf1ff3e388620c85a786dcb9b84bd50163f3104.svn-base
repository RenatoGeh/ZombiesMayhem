package geometryzombiesmayhem;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import javax.swing.JComponent;

/**
 * Awesome component that supports events and even has its own Tooltip (if you want to). Cool.
 * @author Yan
 */
public abstract class ComponentP extends JComponent implements Paintable {
    Shape bounds;
    protected Tooltip tooltip = null;
    protected WindowP window = null;
    public void setTooltip(Tooltip t) {
        tooltip = t;
    }
    
    protected void setWindow(WindowP p) {
        window = p;
        if(p==null) return;
//        AffineTransform at = new AffineTransform();
//        at.translate(p.x, p.y);
//        bounds = at.createTransformedShape(bounds);
        updateWindowDefaultPosition();
    }

    protected void setDefShape() {
        bounds = getDefShape();
    }
    
    static protected Rectangle2D getDefShape() { return new Rectangle2D.Float(0, 0, GameFrame.FRAME_WIDTH, GameFrame.FRAME_HEIGHT); }

    public Shape getShape() {return bounds;}
    
    public void setBounds(Shape s) { this.bounds = s; }
    @Override
    public java.awt.Rectangle getBounds() {return bounds.getBounds();}
        
    public java.awt.Rectangle getRelativeBounds(PanelP p) {
        System.err.println(bounds.getBounds2D());
        AffineTransform at = new AffineTransform();
        at.translate(-p.getPosition().getX(), -p.getPosition().getY());
        
        return at.createTransformedShape(bounds).getBounds();
    }
    
    @Override
    protected void processMouseWheelEvent(MouseWheelEvent event){ super.processMouseWheelEvent(event);    }
    @Override
    protected void processMouseEvent(MouseEvent event) {
        super.processMouseEvent(event);
        if(tooltip==null) return;
        if(event.getID()==MouseEvent.MOUSE_MOVED) {
            float x = GameFrame.isInitialized? UserInterface.getUI().getCursor().getWidth() : ZM.menuCursor.getWidth(null);
            float y = GameFrame.isInitialized? UserInterface.getUI().getCursor().getHeight() : ZM.menuCursor.getHeight(null);
            
            tooltip.refreshPosition((float)event.getPoint().getX() + x, (float)event.getPoint().getY() + y);
        } if(event.getID()==MouseEvent.MOUSE_ENTERED)
            tooltip.startTooltip();
        if(event.getID()==MouseEvent.MOUSE_EXITED)
            tooltip.stopTooltip();
    }
    @Override
    protected void processKeyEvent(KeyEvent event){super.processKeyEvent(event);}
    protected void manageFocus(){}
    protected void manageUnFocus(){}
    
    protected void reset(){}

    @Override
    public void setVisible(boolean aFlag) {};
    
    @Override
    public int getX() {
        if(bounds==null) return 0;
        return (int)this.bounds.getBounds().getX();}
    @Override
    public int getY() {
        if(bounds==null) return 0;
        return (int)this.bounds.getBounds().getY();}
    
    @Override
    public int getWidth() {
//        System.out.println(getClass().getName());
        return (int)this.bounds.getBounds().getWidth();}
    @Override
    public int getHeight() {return (int)this.bounds.getBounds().getHeight();}
    
    public void updateWindowDefaultPosition(){};

    @Override
    public void paint(Graphics2D g2, Component c) {}

    @Override
    public void setActive(boolean active) {}

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public boolean showOnlyOnEditor() {
        return false;
    }
    
}
