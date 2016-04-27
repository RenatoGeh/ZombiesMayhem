package geometryzombiesmayhem;

import java.awt.BasicStroke;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Abstract <code>ComponentP</code>. It does not exist until you add to it.</p>
 * 
 * @author Renato Lui Geh
 */
public abstract class TickGroup extends ComponentP implements IDable {    
    private List<Tick> ticks = new ArrayList<>();
    private List<ActionListener> listeners = new ArrayList<>();
    private int id;
    private Vector2D relative;
    
    private TickGroup(Window parent, int id) {this.id = id; relative = parent.getPosition();};
    
    public abstract void add(Vector2D position, String label);
    public abstract void add(Vector2D position, String label, boolean isSelected);
    
    private void updateBounds(Tick e) {
        AffineTransform at = new AffineTransform();
        at.translate(-relative.getX(), -relative.getY());
        
        if(bounds==null) bounds = at.createTransformedShape(e.box);
        else {
            Area prev = new Area(bounds);
            for(int i=0;i<ticks.size();i++)
                prev.add(new Area(at.createTransformedShape(ticks.get(i).box)));
            this.bounds = prev;
        }
//        else {
//            float minX, minY = minX = 0;
//            float maxX, maxY = maxX = 0;
//            for(int i=0;i<ticks.size();i++) {
//                float x = (float)ticks.get(i).getBounds().getX();
//                float y = (float)ticks.get(i).getBounds().getY();
//                float width = (float)ticks.get(i).getBounds().getWidth();
//                float height = (float)ticks.get(i).getBounds().getHeight();
//
//                minX = x<minX? x : minX;
//                minY = y<minY? y : minY;
//                maxX = x>minX? x+width : minX;
//                maxY = y>minY? y+height : minY;
//            }
//
//            bounds = new Rectangle2D.Float(minX, minY, maxX, maxY);
//        }
    }
    
    public String[] getSelected() {
        int count=0;
        for(int i=0;i<ticks.size();i++) if(ticks.get(i).isSelected()) count++;
        String[] selected = new String[count];
        int n=0;
        for(int i=0;i<ticks.size();i++) if(ticks.get(i).isSelected()) selected[n++]=ticks.get(i).label;
        return selected;
    }
    
    protected abstract boolean allowsMultiples();
    
    @Override
    public void processMouseEvent(MouseEvent event) {
        if(event.getID() != MouseEvent.MOUSE_CLICKED) return;
        if(event.getButton() != MouseEvent.BUTTON1) return;
        super.processMouseEvent(event);

        Tick selectedTick = null;
        if(!this.allowsMultiples()) for(Tick e : ticks) if(e.isSelected()) selectedTick = e;
        
        for(Tick e : ticks)
            if(e.getBounds().contains(event.getX(), event.getY())) {
                if(selectedTick != null)
                    selectedTick.deselect();
                e.toggleSelected();
            }
        
        String[] selected = getSelected();
        String s = selected.length!=0?selected[0]:"";
        for(int i=1;i<selected.length;i++)
            s += " " + selected[i];
        for(ActionListener al : listeners)
            al.actionPerformed(new ActionEvent(this, this.id, s));
    }
    
    public void addActionListener(ActionListener list) {listeners.add(list);}
    public void removeActionListener(ActionListener list) {listeners.remove(list);}
    
    @Override
    public void paint(Graphics2D g2, Component c) {
        for(Tick e : ticks)
            e.paint(g2, c);
    }

    @Override
    public int getID() {return this.id;}
    
    private static class Tick implements Paintable {
        private Shape box;
        private String label;
        private boolean selected;
        
        public Tick(Shape s, String label) {
            this.box = s;
            this.label = label;
            this.TEXT_WIDTH = ZM.GRAPHICS.getFontMetrics(TEXT_FONT).stringWidth(label);
            this.TEXT_HEIGHT = ZM.GRAPHICS.getFontMetrics(TEXT_FONT).getHeight();
            
            AffineTransform trans = new AffineTransform();
            trans.translate(TEXT_WIDTH + OFFSET_X, 0);
            this.box = trans.createTransformedShape(box);
        }
        
        @Override
        public void paint(Graphics2D g2, Component c) {
            java.awt.Stroke defStroke = g2.getStroke();
            java.awt.Color defColor = g2.getColor();
            java.awt.Font defFont = g2.getFont();
            
            g2.setColor(java.awt.Color.BLACK);
            g2.setStroke(Tick.BOX_STROKE);
            g2.setFont(TEXT_FONT);
            
            g2.draw(box);
            g2.drawString(label, (float)box.getBounds2D().getX() - (TEXT_WIDTH + OFFSET_X),
                    (float)box.getBounds2D().getY() + TEXT_HEIGHT/2);
            
            if(selected)
                g2.drawImage(TICK, null, (int)this.box.getBounds2D().getX() + (int)this.box.getBounds2D().getWidth()/6, (int)this.box.getBounds2D().getY() - TICK.getHeight()/2);
        
            g2.setFont(defFont);
            g2.setColor(defColor);
            g2.setStroke(defStroke);
        }
        
        public float getWidth() {return (float)this.box.getBounds2D().getWidth() + OFFSET_X + TEXT_WIDTH;}
        public float getHeight() {return (float)box.getBounds2D().getHeight();}
        
        public Rectangle2D getBounds() {return this.box.getBounds2D();}
        
        public boolean isSelected() {return this.selected;}
        public void setSelected(boolean selected) {this.selected = selected;}
        public void select() {this.selected = true;}
        public void deselect() {this.selected = false;}
        public void toggleSelected() {this.setSelected(!this.isSelected());}

        @Override
        public boolean isActive() {return true;}
        @Override
        public void setActive(boolean active) {}
        
        private final float TEXT_HEIGHT;
        private final float TEXT_WIDTH;
        private static final float OFFSET_X = 10;
        private static final MutableImage TICK = AssetManager.loadImage("resources/interface/tickgroup/tick.png");
        private static final Font TEXT_FONT = new Font(Font.DIALOG, Font.PLAIN, 15);
        private static final java.awt.Stroke BOX_STROKE = new java.awt.BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);

        @Override
        public boolean showOnlyOnEditor() {
            return false;
        }
    }
    
    public static class Single extends TickGroup {
        // Elliptic
        public Single(Window parent, int id) {super(parent, id);}
        
        @Override
        public void add(Vector2D position, String label) {this.add(position, label, false);}
        @Override
        public void add(Vector2D position, String label, boolean isSelected) {
            Tick e = new Tick(new Ellipse2D.Float(position.getX() + super.relative.getX(), 
                    position.getY() + super.relative.getY(), 15, 15), label);
            e.setSelected(isSelected);
            super.ticks.add(e);
            super.updateBounds(e);
        }
            
        
        @Override
        public boolean allowsMultiples() {return false;}
    }
    
    public static class Multiple extends TickGroup {
        // Rectangular
        public Multiple(Window parent, int id) {super(parent, id);}
        
        @Override
        public void add(Vector2D position, String label) {this.add(position, label, false);}
        @Override
        public void add(Vector2D position, String label, boolean isSelected) {
            Tick e = new Tick(new Rectangle2D.Float(position.getX() + super.relative.getX(), 
                    position.getY() + super.relative.getY(), 10, 10), label);
            e.setSelected(isSelected);
            super.ticks.add(e);
            super.updateBounds(e);
        }
        
        @Override
        public boolean allowsMultiples() {return true;}
    }
}
