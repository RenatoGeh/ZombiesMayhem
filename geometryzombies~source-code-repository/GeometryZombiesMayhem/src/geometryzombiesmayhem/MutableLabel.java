package geometryzombiesmayhem;

import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics2D;

/**
 * <p>A <code>WLabel</code> that has the ability to mutate in painting time to maintain
 * the object's integrity throughout runtime.</p>
 * 
 * @author Renato Lui Geh
 */
public class MutableLabel extends WLabel {
    private Object e;
    private String initial;
    private int decimal = 2;
    
    public MutableLabel(Vector2D position, Font f, Object e) {this(position, f, "", e);}
    public MutableLabel(Vector2D position, Object e) {this(position, "", e);}
    public MutableLabel(Vector2D position, String initial, Object e) {this(position, DEFAULT_FONT, initial, e);}
    public MutableLabel(Vector2D position, Font f, String initial, Object e) {
        super(position, f, initial + e.toString());
        this.e = e;
        this.initial = initial;
    }
    
    public void setDecimalAccuracy(int decimal) {
        if(!(e instanceof Vector2D))
            throw new IllegalArgumentException("Inner object isn't a Vector2D!");
        
        this.decimal = decimal;
    }
    
    @Override
    public void paint(Graphics2D g2, Component c) {
        String s = e instanceof Vector2D? ((Vector2D)e).toFormattedString(decimal) : e.toString();
        this.setText(initial + s);
        super.paint(g2, c);
    }
    
    private static final Font DEFAULT_FONT = new Font(Font.DIALOG, Font.PLAIN, 10);
}
