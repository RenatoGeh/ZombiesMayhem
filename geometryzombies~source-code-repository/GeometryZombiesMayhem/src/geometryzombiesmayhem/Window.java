package geometryzombiesmayhem;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.RoundRectangle2D;
import java.util.HashSet;
import java.util.Set;

public class Window extends PanelP implements Paintable {
    private EventTrigger trigger;
    private Set<Conflictable> conflictables = new HashSet<>();
    private RoundRectangle2D.Float frame = new RoundRectangle2D.Float();
    private Color frameColor = ColorFactory.getColor(Color.LIGHT_GRAY, 200);
    
    private boolean isActive = true;
    private boolean isVisible = false;
    private boolean fixedSize = false;
    private boolean isDraggeable = true;
    
    private Vector2D mousePos = Vector2D.NEGATIVE.copy();

    public Window(Vector2D position) {
        super(position);
        
        frame.setRoundRect(this.getPosition().getX(), this.getPosition().getY(),
                0, 0, 10, 10);
        
        this.setBounds(frame);
        
        super.add(trigger = new EventTrigger(this) {
            @Override
            public void mouseTriggered(MouseEvent event) {
                if(!isDraggeable) return;
                
                if(event.getID() == MouseEvent.MOUSE_DRAGGED)
                    if(((event.getModifiersEx() & ~MouseEvent.BUTTON1_DOWN_MASK) == 0)) {
                        if(mousePos.equals(Vector2D.NEGATIVE))
                            mousePos.set(event.getX() - Window.this.getX(), event.getY() - Window.this.getY());
                        
                        Window.this.setPosition(event.getX() - mousePos.getX(), event.getY() - mousePos.getY());
                    }
                
                if(event.getID() == MouseEvent.MOUSE_RELEASED)
                    if(!mousePos.equals(Vector2D.NEGATIVE))
                        mousePos.set(Vector2D.NEGATIVE.getX(), Vector2D.NEGATIVE.getY());
            }
        });
        this.conflictables.add(trigger);
    }
    public Window(Vector2D position, ComponentP... c) {this(position); this.add(c);}
    
    public final void setDraggeable(boolean draggeable) {this.isDraggeable = draggeable;}
    public final boolean isDraggeable() {return isDraggeable;}
    
    @Override
    public final void setVisible(boolean visible) {
        if(visible && !isVisible){
            Paintables.registerInterface(this); 
            
            for(ComponentP c : components)
                if(c instanceof Paintable)
                    Paintables.registerInterface((Paintable)c);
            
            this.isVisible = visible; 
            
            for(ComponentP c : components)
                c.setVisible(visible);
            
            return;
        }
        if(!visible && isVisible) {
            Paintables.removeInterface(this);
            
            for(ComponentP c : components)
                if(c instanceof Paintable)
                    Paintables.removeInterface((Paintable)c);
            
            this.isVisible = visible;
            
            for(ComponentP c : components)
                c.setVisible(visible);
        } 
    }
    
    public void setSize(Vector2D size) {
        this.fixedSize = true;
        this.frame.setRoundRect(frame.getX(), frame.getY(), size.getX(), size.getY(), 
                frame.getArcWidth(), frame.getArcHeight());
        
        for(Conflictable t : conflictables)
            if(t instanceof EventTrigger)
                ((EventTrigger)t).updateBounds(this);
    }
    
    public final void add(Triggerable t) {trigger.add(t);}
    
    public void remove(Triggerable t) {trigger.remove(t);}
    
    @Override
    public void addRestrict(ComponentP e) {
        super.addRestrict(e);
        
        if(e instanceof Conflictable)
            this.conflictables.add((Conflictable)e);
        
        if(e instanceof Paintable)
            if(isVisible)
                if(!Paintables.containsInterface((Paintable)e))
                    Paintables.registerInterface((Paintable)e);
        
        if(conflictables.isEmpty()) {
            java.util.List<ComponentP> list = window==null? components : window.components;
            for(int i=list.size()-1;i>-1;i--)
                if(list.get(i) instanceof Conflictable) {
                    if(i==list.size()-1)
                        continue;
                    
                    ComponentP foo = list.get(i);
                    list.remove(foo);
                    list.add(foo);
                }
        }
    }
    
    @Override
    public void add(ComponentP e) {
        super.add(e);
        
        if(e instanceof Conflictable)
            this.conflictables.add((Conflictable)e);
        
        if(e instanceof Paintable)
            if(isVisible)
                if(!Paintables.containsInterface((Paintable)e))
                    Paintables.registerInterface((Paintable)e);
        
        if(!conflictables.isEmpty()) {
            java.util.List<ComponentP> list = window==null? components : window.components;
            for(int i=list.size()-1;i>-1;i--)
                if(list.get(i) instanceof Conflictable) {
                    if(i==list.size()-1)
                        continue;
                    
                    ComponentP foo = list.get(i);
                    list.remove(foo);
                    list.add(foo);
                }
        }
    }
    
    @Override
    public void addTo(WindowP window) {this.addTo(window, false);}
    
    @Override
    public void remove(ComponentP e) {
        super.remove(e);
        
        if(e instanceof Paintable)
            Paintables.removeInterface((Paintable)e);
        
        if(e instanceof Conflictable)
            conflictables.remove((Conflictable)e);
    }
    
    @Override
    public void removeRestrict(ComponentP e) {
        super.removeRestrict(e);
        
        if(e instanceof Paintable)
            Paintables.removeInterface((Paintable)e);
        
        if(e instanceof Conflictable)
            conflictables.remove((Conflictable)e);
    }
    
    @Override
    public boolean isVisible() {return isVisible;}
    
    public final void setPosition(float x, float y) {
        this.frame.setRoundRect(x, y, 
                frame.getWidth(), frame.getHeight(), frame.getArcWidth(), frame.getArcHeight());
        
        AffineTransform at = new AffineTransform();
        at.translate(x - this.p.getX(), y - this.p.getY());
        
        for(ComponentP c : components)
            c.bounds = at.createTransformedShape(c.bounds);
        
        this.p.set(x, y);
    }
    
    public final void setPosition(Vector2D pos) {this.setPosition(pos.getX(), pos.getY());}
    
    public void setColor(Color c) {this.frameColor = c;}

    @Override
    public int getWidth() {return (int)frame.getWidth();}
    @Override
    public int getHeight() {return (int)frame.getHeight();}
    @Override
    public int getX() {return (int)frame.getX();}
    @Override
    public int getY() {return (int)frame.getY();}
    
    @Override
    public boolean contains(java.awt.Point e) {return this.getShape().contains(e);}
    
    @Override
    public boolean isActive() {return isActive;}
    @Override
    public void setActive(boolean active) {this.isActive = active;}

    @Override
    public void paint(Graphics2D g2, Component c) {
        Color def = g2.getColor();
        Stroke s = g2.getStroke();
        
        g2.setColor(Color.DARK_GRAY);
        g2.setStroke(DEFAULT_BORDER_STROKE);
        
        g2.draw(frame);
        g2.setColor(frameColor);
        g2.fill(frame);
        
        g2.setColor(def);
        g2.setStroke(s);
    }
    
    @Override
    public void updateWindowDefaultPosition() {
        this.setPosition(p.add(window.x, window.y));
    }
    
    private static final Stroke DEFAULT_BORDER_STROKE = new java.awt.BasicStroke(6, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);

    public static interface Conflictable {};
}
