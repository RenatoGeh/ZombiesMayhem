package geometryzombiesmayhem;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>A text variant of <code>ImageButton</code>.</p>
 * 
 * @author Renato Lui Geh
 */
public class TextButton extends ComponentP implements Paintable, IDable {
    private Rectangle2D buttonBounds;
    private int BUTTON_ID;
    
    private Set<ActionListener> listeners = new HashSet<>();
    
    private String label;
    
    private Color hoverColor = Color.BLUE, unselectedColor = ColorFactory.getColor(Color.LIGHT_GRAY, 0);
    private Color textColor = Color.BLACK, baseColor = unselectedColor;
    private Font textFont = new Font(Font.DIALOG, Font.PLAIN, 20);
    
    private boolean hasHover = false;
    private boolean isActive = true;
    private boolean isEnabled = true;
    
    public TextButton(Vector2D position, float height, String text, Color hoverColor, int id) {
        this(position, height, text, id);
        
        this.hoverColor = hoverColor;
        this.hasHover = true;
    }
    
    public TextButton(Vector2D position, float height, String text, int id) {
        this.label = text;
        this.BUTTON_ID = id;
        
        FontMetrics metrics = ZM.GRAPHICS.getFontMetrics(textFont);
        float n = 20*(height-6)/metrics.getHeight();
        textFont = new Font(Font.DIALOG, Font.PLAIN, (int)n);
        
        this.buttonBounds = new Rectangle2D.Float(position.getX(), position.getY(),
                ZM.GRAPHICS.getFontMetrics(textFont).stringWidth(label) + 10, height);
        
        this.bounds = this.buttonBounds;
    }

    public void addActionListener(ActionListener al) {this.listeners.add(al);}
    public void removeActionListener(ActionListener al) {this.listeners.remove(al);}
    
    @Override
    public void processMouseEvent(MouseEvent event) {
        if(!this.isEnabled) return;
        if(!this.isActive) return;
        
        super.processMouseEvent(event);
        
        switch(event.getID()) {
            case MouseEvent.MOUSE_CLICKED:
                ActionEvent e = new ActionEvent(this, ActionEvent.ACTION_PERFORMED,
                        BUTTON_ID + "clicked", event.getWhen(), event.getModifiers());
                for(ActionListener a : listeners)
                    a.actionPerformed(e);
            break;
            case MouseEvent.MOUSE_ENTERED:
                if(hasHover) {
                    this.baseColor = this.hoverColor;
                    this.textColor = ColorFactory.invert(this.textColor);
                }
            break;
            case MouseEvent.MOUSE_EXITED:
                if(hasHover) {
                    this.baseColor = this.unselectedColor;
                    this.textColor = ColorFactory.invert(this.textColor);
                }
            break;
        }
    }
    
    @Override
    public void updateWindowDefaultPosition() {this.buttonBounds = this.bounds.getBounds2D();}
    
    @Override
    public int getWidth() {return (int)this.bounds.getBounds().getWidth();}
    @Override
    public int getHeight() {return (int)this.bounds.getBounds().getHeight();}
    @Override
    public int getX() {return (int)this.bounds.getBounds().getX();}
    @Override
    public int getY() {return (int)this.bounds.getBounds().getY();}
    
    public void setColor(Color c) {this.baseColor = c;}
    public Color getColor() {return this.baseColor;}
    
    public Color getUnselectedColor() {return this.unselectedColor;}
    
    public void setPosition(double x, double y) {
        AffineTransform at = new AffineTransform();
        at.translate(x, y);
        this.setBounds(at.createTransformedShape(this.bounds));
    }
    
    @Override
    public void setEnabled(boolean isEnabled) {this.isEnabled = isEnabled;}
    @Override
    public boolean isEnabled() {return isEnabled;}
    
    @Override
    public boolean isActive() {return isActive;}
    @Override
    public void setActive(boolean active) {this.isActive = active;}

    @Override
    public void paint(Graphics2D g2, Component c) {
        if(!isEnabled) return;
        
        Color defaultColor = g2.getColor();
        Font defaultFont = g2.getFont();
        
        g2.setColor(baseColor);
        g2.fill(bounds);
        g2.setColor(textColor);
        g2.setFont(textFont);
        g2.drawString(label, this.getX() + 5, this.getY() + 3*this.getHeight()/4);
        
        g2.setColor(defaultColor);
        g2.setFont(defaultFont);
        
        if(tooltip != null) tooltip.paint(g2, c);
    }

    @Override
    public int getID() {return BUTTON_ID;}
    
    public static final int BUTTON_NONE = -1;
}
