package geometryzombiesmayhem;

import java.awt.Component;
import java.awt.Graphics2D;

/**
 * <p>A <code>Stack</code> of <code>Item</code>s.</p>
 * 
 * @author Renato Lui Geh
 */
public class Stack<E extends Item> implements Paintable {
    private Vector2D position;
    private E item;
    private byte q;
    private byte MAX;
    
    private boolean isActive = true;
    private boolean isSelected = false;
    
    public Stack(Vector2D position, E item, byte quantity) {
        this(position,item,quantity,(byte)16);
    }
    
    public Stack(Vector2D position, E item, byte quantity,byte maxStacking) {
        this.position = position;
        this.item = item;
        q = quantity;
        MAX = maxStacking;
            }
        
    /**
     * Splits the slot items in half, removing them and returning the result.
     * 
     * @return The other half of the split stack.
     */
    public Stack<E> split() {
        if(q==1) return this;
        byte n = (byte) (q/2);
        Stack s = new Stack(position,item,n,MAX);
        q-=n;
        return s;
    }
    
    /**
     * Adds <code>n</code> <code>Item</code>s to the <code>Stack</code>.
     * 
     * @param items 
     */
    public void add(byte quantity) {
        q+=quantity;
    }
    
    public Stack join(Stack<E> e) {
       if(q+e.q<MAX) {
           q+=e.q;
           e.destroy();
           return null;
       }//else
       e.q = (byte)(e.q - (MAX - q));
       q = MAX;
       return e;
    }
    
    public synchronized void destroy() {
        this.isActive = false;
        this.isSelected = false;
        item = null; q = 0;
    }
    
    public boolean isSelected() {return isSelected;}
    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
        if(isSelected)
            this.position = GameFrame.mousePosition;
    }
    
    public void toggleSelected() {this.setSelected(!this.isSelected);}

    public int size() {return q;}
    
    public boolean isEmpty() {return q==0;}
    
    @Override
    public boolean isActive() {return isActive;}

    @Override
    public void setActive(boolean active) {this.isActive = active;}

    public MutableImage getIcon() {return item.getIcon().resize(Slot.SLOT_PIXELS_WIDTH, Slot.SLOT_PIXELS_HEIGHT);}
    
    @Override
    public void paint(Graphics2D g2, Component c) {
        if(this.isSelected)
            forcePaint(g2, c);
    }
    
    public void forcePaint(Graphics2D g2, Component c) {
        g2.drawImage(this.getIcon(), null, (int)position.getX(), (int)position.getY());
    }

    @Override
    public boolean showOnlyOnEditor() {
        return false;
    }
}
