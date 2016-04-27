package geometryzombiesmayhem;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;

/**
 *
 * @author Yan
 */
public class PanelP extends ComponentP{
    Vector2D p;
    List<ComponentP> components = new ArrayList<>();
    List<Paintable> paintables = new ArrayList<>();
    
    public PanelP(Vector2D position) {
        p = position;
    }
    
    public PanelP() {
        this(Vector2D.ZERO.copy());
    }
    
    public void add(Paintable p) {
        if(p instanceof ComponentP) { add((ComponentP)p); return; }
        paintables.add(p);
        if(window!=null) window.add(p);
    }
    
    public void add(ComponentP c) {add(c, false);}
    public void add(ComponentP c, boolean autoRegisters) {
        components.add(c);
        if(!p.equals(Vector2D.ZERO)) {
            AffineTransform at = new AffineTransform();
            at.translate(p.getX(), p.getY());
            c.setBounds(at.createTransformedShape(c.bounds));
        }
        if(window!=null)
            window.add(c, autoRegisters);
    }
    
    public void addRestrict(ComponentP c) {
        components.add(c);
        if(window!=null) window.add(c);
    }
    
    public void remove(Paintable p) {
        if(p instanceof ComponentP) { remove((ComponentP)p); return; }
        paintables.remove(p);
        if(window!=null) window.remove(p);
    }
    
    public void remove(ComponentP c) { //shouldn't be used
        components.remove(c);
        if(!p.equals(Vector2D.ZERO)) {
        AffineTransform at = new AffineTransform();
        at.translate(-p.getX(), -p.getY());
        c.setBounds(at.createTransformedShape(c.bounds));
        }
        if(window!=null)
            window.remove(c);
    }
    
    public void removeRestrict(ComponentP c) {
        components.remove(c);
        if(window!=null) window.remove(c);
    }
    
    public void add(ComponentP... e) {this.add(false, e);}
    
    public void add(boolean autoRegisters, ComponentP... e) {;
        for(ComponentP c : e) 
            this.add(c,autoRegisters);
    }
    
    public void add(Paintable... p) {
        for(Paintable pa:p)add(pa);
    }
    
    public void remove(ComponentP... e) {
        for(ComponentP c : e) 
            this.remove(c);
    }
    public void remove(Paintable... p) {
        for(Paintable pa:p)remove(pa);
    }
    
    public void addTo(WindowP window, boolean autoRegisters) {
        this.window = window;
        for(Paintable pa:paintables)
            window.add(pa);
        for(ComponentP c:components) 
            window.add(c, autoRegisters);
    }    
    public void addTo(WindowP window) {this.addTo(window, true);}
    
    public void addTo(PanelP pa) {
        for(Paintable p:paintables)
            pa.add(p);
        for(ComponentP c:components) 
            pa.add(c);
    }
    
    public void removeFromWindow() {
        if(window==null) return;
        for(ComponentP c:components)
            window.remove(c);      
        for(Paintable p:paintables)
            window.remove(p);
        window.clear();
        this.window = null;
    }
    
    public void removeFromPanel(PanelP pa) {
        for(ComponentP c:components)
            pa.remove(c);  
        for(Paintable p:paintables)
            pa.remove(p);
    }
    
    public WindowP toWindowP(JFrame frame) {
        WindowP w = new WindowP(frame);
        addTo(w);
        return w;
    }

    @Override
    public void updateWindowDefaultPosition() {}
    
    protected Vector2D getPosition() {return this.p;}
    protected List<ComponentP> getComponentPs() {return this.components;}

    @Override
    public boolean isActive() {return true;}

    @Override
    public void setActive(boolean active) {}

    @Override
    public void paint(Graphics2D g2, Component c) {}

    @Override
    public boolean showOnlyOnEditor() {
        return false;
    }
}
