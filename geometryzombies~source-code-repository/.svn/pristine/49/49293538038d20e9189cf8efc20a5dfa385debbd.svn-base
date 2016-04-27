package geometryzombiesmayhem;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;

/**
 *
 * @author Yan
 */
public class WindowP extends ComponentP{
    public List<ComponentP> components = new ArrayList<>();
    protected List<Paintable> paintables = new ArrayList<>();
    protected ComponentP focusComponent = null;
    protected ComponentP cComp = null;
    public int x ,y;
    public int deltaY  = 0;
        
    protected Scroll scroll;
    
    protected boolean active = true;
    protected BufferedImage im;
    protected Graphics2D g;
    protected Rectangle2D r;
    
    public WindowP(JFrame f) {
        this(f,getDefShape(),GameFrame.FRAME_HEIGHT);
    }
        
    public WindowP(JFrame f , Rectangle2D d,int height) { 
        r=d; bounds = d; x= (int)r.getMinX(); y = (int)r.getMinY();
        im = new BufferedImage((int) (r.getWidth()), height,BufferedImage.TYPE_INT_ARGB_PRE);
        g = im.createGraphics();
        g.setBackground(new Color(255,255,255,0));
        Paintables.register(this);
        this.addMouseListener(gA);
        this.addMouseMotionListener(gA);
        this.addMouseWheelListener(gA);
        if(f!=null) f.addKeyListener(gA);
        //asass
        if(height== d.getHeight()) return;
        scroll = new Scroll(new Vector2D(x+r.getWidth(),y),(int)r.getHeight(),height);
        add(scroll, true);
    }

    public void removeMouseListeners() {
        super.removeMouseListener(gA);
        super.removeMouseMotionListener(gA);
    }
    
    public PanelP toPanelP() {
        PanelP p = new PanelP(new Vector2D(x,y));
        for(ComponentP c:components)
            p.add(c);
        return p;
    }

    @Override
    public synchronized void addKeyListener(KeyListener l) {
        gA.add(l);
    }
    
    public void addMouseAdapter(MouseAdapter m) {
        gA.add(m);
    }
    
    public void add(ComponentP comp) {this.add(comp, false);}
    public void add(ComponentP comp, boolean autoRegister) {
        if(comp instanceof PanelP) { add((PanelP)comp); return; }
        if(comp instanceof WindowP) { add((WindowP)comp); return; }
        components.add(comp);
        if(autoRegister)
            paintables.add(comp);
        comp.setWindow(this);
        setFocus(comp);
    }
    
    public void add(PanelP panel) {
        panel.addTo(this);
    }
    
    public void add(WindowP window) {
        add(window.toPanelP());
    }
    
    public void add(Paintable p) {
        if(p instanceof ComponentP) { add((ComponentP) p); return; }
        paintables.add(p);
    }
    
    public void add(boolean autoRegisters, ComponentP... comps) {for(int i=0;i<comps.length;i++) add(comps[i], autoRegisters);}
    public void add(ComponentP... comps) {this.add(false, comps);}
    
    public void remove(ComponentP comp) {
        if(comp instanceof PanelP) { remove((PanelP)comp); return; }
        if(comp instanceof WindowP) { remove((WindowP)comp); return; }
        components.remove(comp);
        paintables.remove(comp);
        comp.setWindow(null);        
        if(focusComponent == comp) setFocus(null);
    }
    
    public void remove(PanelP panel){
        panel.removeFromWindow();
    }
    
    public void remove(WindowP window) {
        remove(window.toPanelP());
    }
    
    public void remove(Paintable p) {
        if(p instanceof ComponentP) { remove((ComponentP) p); return; }
        paintables.remove(p);
    }
    
    public void remove(ComponentP... comps) {for(int i=0;i<comps.length;i++) remove(comps[i]);}
    
    private ComponentP getComp(int x,int y) {
        if(!r.contains(new Point2D.Float(x + this.x, y  + this.y - deltaY))) return null;
        Point2D p = new Point2D.Float(x, y);
        for(ComponentP c : components)
            if(c.bounds!=null && c.bounds.contains(p))
                return c;
        return null;
    }
    
    public void forceUnfocus() {this.setFocus(null);}
    
    private void setFocus(ComponentP comp) {
        if(comp==focusComponent) return;
        if(focusComponent!=null) focusComponent.manageUnFocus();
        focusComponent = comp;
        if(comp!=null) comp.manageFocus();
    }
    
    private void setCur(ComponentP comp,MouseEvent e) {
        if(cComp==comp) return;
        if(cComp!=null) { //send mouseExited
            MouseEvent event = new MouseEvent(cComp, MouseEvent.MOUSE_EXITED, e.getWhen(), e.getModifiers(),
                      e.getX(), e.getY(), 0, false,
                      MouseEvent.NOBUTTON);
            cComp.processMouseEvent(event);
        }
        if(comp!=null) { //send mouseEntered
            MouseEvent event = new MouseEvent(comp, MouseEvent.MOUSE_ENTERED, e.getWhen(), e.getModifiers(),
                      e.getX(), e.getY(), 0, false,
                      MouseEvent.NOBUTTON);
            comp.processMouseEvent(event);
        }
        cComp = comp;
    }
    
    protected MouseEvent fix(MouseEvent ev) {
        return new MouseEvent((Component) ev.getSource(),ev.getID(),ev.getWhen(),ev.getModifiers(),ev.getX() - x ,(ev.getY() -y + deltaY),
                ev.getClickCount(),ev.isPopupTrigger(),ev.getButton());
    }
    
    protected MouseWheelEvent fix(MouseWheelEvent ev) {
        return new MouseWheelEvent((Component) ev.getSource(),ev.getID(),ev.getWhen(),ev.getModifiers(),ev.getX() -x,ev.getY() -y + deltaY,
                ev.getClickCount(),ev.isPopupTrigger(),ev.getScrollType(),ev.getScrollAmount(),ev.getWheelRotation());
    }
    
    public GeneralAdapter gA = new GeneralAdapter() {
        

        @Override
        public void mouseMoved(MouseEvent event) {
            super.mouseMoved(event);
            event = fix(event);
            ComponentP p = getComp(event.getX(),event.getY());
            setCur(p,event);
            event.setSource(p);
            if(p!=null) p.processMouseEvent(event);
        }
        
        @Override
        public void mouseDragged(MouseEvent event) {
            super.mouseDragged(event);
            event = fix(event);
            ComponentP p = getComp(event.getX(), event.getY());
            setCur(p, event);
            event.setSource(p);
            if(p!=null) p.processMouseEvent(event);
        }
        
        @Override
        public void mouseReleased(MouseEvent event) {
            super.mouseReleased(event);
            event = fix(event);
            ComponentP p = getComp(event.getX(),event.getY());
            event.setSource(p);
            if(p!=null) p.processMouseEvent(event);
        }

        @Override
        public void mousePressed(MouseEvent event) {
            super.mousePressed(event);
            event = fix(event);
            ComponentP p = getComp(event.getX(),event.getY());
            event.setSource(p);
            if(p!=null) p.processMouseEvent(event);
            setFocus(p);
        }

        @Override
        public void mouseClicked(MouseEvent event) {
            super.mouseClicked(event);
            event = fix(event);
            ComponentP p = getComp(event.getX(),event.getY());
            event.setSource(p);
            if(p!=null) 
               p.processMouseEvent(event);
            setFocus(p);
        }

        @Override
        public void mouseWheelMoved(MouseWheelEvent event) {
            super.mouseWheelMoved(event);
            event = fix(event);
            ComponentP p = getComp(event.getX(),event.getY());
            if(scroll!=null && scroll!=p) {
                scroll.processMouseWheelEvent(event);
            }
            event.setSource(p);
            if(p!=null) p.processMouseWheelEvent(event);
        }
        
        @Override
        public void keyPressed(KeyEvent event) {
            if(focusComponent==null) return;
            super.keyPressed(event);
            event.setSource(focusComponent);
            if(focusComponent!=null) focusComponent.processKeyEvent(event);
        }

        @Override
        public void keyReleased(KeyEvent event) {
            if(focusComponent==null) return;
            super.keyReleased(event);
            event.setSource(focusComponent);
            if(focusComponent!=null) focusComponent.processKeyEvent(event);
        }

        @Override
        public void keyTyped(KeyEvent event) {
            if(focusComponent==null) return;
            super.keyTyped(event);
            event.setSource(focusComponent);
            if(focusComponent!=null) focusComponent.processKeyEvent(event);
        }

    };

    @Override
    public void updateWindowDefaultPosition() {
        throw new UnsupportedOperationException("Not supported yet.");
}

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public void paint(Graphics2D g2, Component c) {
       clear(); //should this be here?
        for(Paintable p : paintables)
            p.paint(g, c);
        g2.drawImage(im.getSubimage(0, deltaY, (int) r.getWidth(), (int) r.getHeight()), (int)r.getMinX(), (int)r.getMinY(), c);
    }

    void clear() {
        //clears the image
        g.clearRect(0, 0, im.getWidth(), im.getHeight());
    }
}
