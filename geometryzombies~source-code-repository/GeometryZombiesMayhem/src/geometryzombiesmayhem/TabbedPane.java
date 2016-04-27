package geometryzombiesmayhem;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>A tab-capable <code>ComponentP</code>.</p>
 * 
 * @author Renato Lui Geh
 */
public class TabbedPane extends ComponentP implements Paintable, Window.Conflictable {
    private Window parent;
    private Color frameColor = Color.GRAY;
    private Rectangle2D mainFrame;
    private Tab currentTab = null;
    private List<Tab> tabs = new ArrayList<>();
    private Map<Tab, Set<ComponentP>> sections = new HashMap<>();
    
    public TabbedPane(Window parent, Vector2D position, Vector2D size) {
        this.mainFrame = new Rectangle2D.Float(position.getX(), position.getY(),
                size.getX(), size.getY());
        
        this.bounds = mainFrame;
        this.parent = parent;
    }
    
    public void register(Tab t, ComponentP... c) {
        if(currentTab == null)
            currentTab = t;
        
        if(!tabs.contains(t)) {
            tabs.add(t);
            sections.put(t, new HashSet<ComponentP>());
            
            double width = 0;
            for(Tab aux : tabs) {
                if(aux == t) break;
                width += aux.getWidth();
            }

            t.setPosition(t.getX() + this.mainFrame.getX() + width, 
                    t.getY() + this.mainFrame.getY() - t.getHeight());
            
            t.addActionListener(buttonListener);
            
            parent.add(t);
        }
        
        sections.get(t).addAll(Arrays.asList(c));

        //Note to self: Add Tabs and ComponentPs first, then add the TabbedPane to Window.
        
        AffineTransform at = new AffineTransform();
        at.translate(this.mainFrame.getX(), this.mainFrame.getY());
        
        if(currentTab != t)
            at.translate(parent.getX(), parent.getY());
        
        for(ComponentP comp : c)
            comp.setBounds(at.createTransformedShape(comp.bounds));
        
        if(currentTab != t) return;
        
        currentTab.setColor(Color.DARK_GRAY);
        
        for(ComponentP foo : sections.get(currentTab)) {
            parent.add(foo);
            
            if(!parent.isVisible())
                if(foo instanceof Paintable)
                    Paintables.removeInterface((Paintable)foo);
        }
    }
    
    public void setFrameColor(Color frameColor) {this.frameColor = frameColor;}

    @Override
    public void setVisible(boolean aFlag) {
        if(!aFlag) {
            for(ComponentP c : sections.get(currentTab))
                if(c instanceof Paintable)
                    Paintables.removeInterface((Paintable)c);
        } else {
            for(ComponentP c : sections.get(currentTab))
                if(c instanceof Paintable)
                    if(!Paintables.contains((Paintable)c))
                        Paintables.registerInterface((Paintable)c);
        }
    }
    
    @Override
    public void updateWindowDefaultPosition() {}

    @Override
    public boolean isActive() {return true;}
    @Override
    public void setActive(boolean active) {}

    @Override
    public void paint(Graphics2D g2, Component c) {
        Color defColor = g2.getColor();
        Stroke defStroke = g2.getStroke();
        
        g2.setColor(frameColor);
        g2.setStroke(DEFAULT_FRAME_STROKE);
        
        g2.draw(bounds);
        
        g2.setColor(Color.LIGHT_GRAY);
        g2.fill(bounds);
        
        g2.setColor(defColor);
        g2.setStroke(defStroke);
    }
    
    private ActionListener buttonListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent event) {
            Tab e = (Tab)event.getSource();
            if(!e.isEnabled()) return;
            
            Set<ComponentP> last = sections.get(currentTab);
            
            for(ComponentP c : last) {
                parent.removeRestrict(c);
                if(c instanceof Paintable)
                    Paintables.removeInterface((Paintable)c);
            }
            
            TabbedPane.this.currentTab.setColor(TabbedPane.this.currentTab.getUnselectedColor());
            TabbedPane.this.currentTab = e;
            
            Set<ComponentP> components = sections.get(tabs.get(e.getID()));
            
//            AffineTransform at = new AffineTransform();
//            at.translate(TabbedPane.this.mainFrame.getX(), TabbedPane.this.mainFrame.getY());
            
            for(ComponentP c : components)
                parent.addRestrict(c);
            
            TabbedPane.this.currentTab.setColor(Color.DARK_GRAY);
        }
    };
    
    public static class Tab extends TextButton {
        private Polygon frame;
        
        public Tab(TabbedPane parent, float height, String text) {
            super(new Vector2D(), height, text, parent.tabs.size());
            
            this.bounds = frame = new Polygon(new int[] {0, (7*this.getWidth()/8), this.getWidth(), 0},
                                new int[] {0, 0, (int)height, (int)height}, 4);
        }
        
        @Override
        public void paint(Graphics2D g2, Component c) {
            super.paint(g2, c);
            
            Stroke s = g2.getStroke();
            Color defColor = g2.getColor();
            
            g2.setStroke(DEFAULT_TAB_STROKE);
            g2.setColor(Color.BLACK);
            g2.draw(bounds);
            
            g2.setColor(defColor);
            g2.setStroke(s);
        }
        
        private static final Stroke DEFAULT_TAB_STROKE = new BasicStroke(1, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER);
    }
    
    private Stroke DEFAULT_FRAME_STROKE = new BasicStroke(5, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER);
}
