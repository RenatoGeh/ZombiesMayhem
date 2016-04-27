package geometryzombiesmayhem;

import geometryzombiesmayhem.StrokeFactory.NoiseStroke;
import geometryzombiesmayhem.StrokeFactory.StaticNoiseStroke;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Renato Lui Geh
 */
public class NoisedTextButton extends ComponentP implements Paintable, IDable {
    private Rectangle2D buttonBounds;
    private int BUTTON_ID;
    private boolean isActive = true;
    private boolean isEnabled = true;
    
    private Font textFont = new Font(Font.SERIF, Font.BOLD, 60);
    private String label;
    
    private Shape outline = null;
    
    private NoiseStroke stroke;
    private StaticNoiseStroke unStroke;
    private Stroke baseStroke;
    
    private Color color = ColorFactory.getPDRandomColor();
    
    private Set<ActionListener> listeners = new HashSet<>();
    
    private Vector2D offset = new Vector2D(50, 50);
    
    public NoisedTextButton(Vector2D position, float height, String text, int id, Color c, float width, float noise) {
        this.label = text;
        this.BUTTON_ID = id;
        
        FontMetrics metrics = ZM.GRAPHICS.getFontMetrics(textFont);
        float n = textFont.getSize()*(height)/metrics.getHeight();
        textFont = new Font(Font.DIALOG, Font.PLAIN, (int)n);
        
        this.buttonBounds = new Rectangle2D.Float(position.getX(), position.getY(),
                ZM.GRAPHICS.getFontMetrics(textFont).stringWidth(label) + 10, height);
        
        this.bounds = this.buttonBounds;
        
        this.color = c;
        this.stroke = new NoiseStroke(width, noise);
        this.unStroke = new StaticNoiseStroke(width, noise/2);
        this.baseStroke = unStroke;
    }
    
    @Override
    public void processMouseEvent(MouseEvent event) {
        if(!isActive) return;
        if(!isEnabled) return;
        
        super.processMouseEvent(event);
        
        switch(event.getID()) {
            case MouseEvent.MOUSE_ENTERED:
                this.baseStroke = this.stroke;
            break;
            case MouseEvent.MOUSE_EXITED:
                this.baseStroke = this.unStroke;
            break;
            case MouseEvent.MOUSE_CLICKED:
                System.err.println("Click");
                ActionEvent e = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, 
                        BUTTON_ID + "clicked", event.getWhen(), event.getModifiers());
                for(ActionListener o : listeners)
                    o.actionPerformed(e);
            break;
        }
    }
    
    public void addActionListener(ActionListener al) {this.listeners.add(al);}
    public void removeActionListener(ActionListener al) {this.listeners.remove(al);}
    
    @Override
    public void setEnabled(boolean isEnabled) {this.isEnabled = isEnabled;}
    @Override
    public boolean isEnabled() {return isEnabled;}
    
    @Override
    public boolean isActive() {return this.isActive;}
    @Override
    public void setActive(boolean active) {this.isActive = active;}

    @Override
    public void paint(Graphics2D g2, Component c) {
        Stroke defStroke = g2.getStroke();
        Color defColor = g2.getColor();
        
        g2.setColor(this.color);
        g2.setStroke(this.baseStroke);
        
        g2.draw(bounds);
        
        if(this.outline == null) {
            AffineTransform at = new AffineTransform();
            this.outline = this.textFont.createGlyphVector(g2.getFontRenderContext(), this.label).getOutline();
            at.translate(this.getX(), 
                    this.getY() + this.outline.getBounds().getHeight());
            this.outline = at.createTransformedShape(outline);
            this.buttonBounds.setFrame(this.getX() - this.offset.getX()/2, this.getY() - this.offset.getY()/2,
                    outline.getBounds().getWidth() + this.offset.getX(), outline.getBounds().getHeight() + this.offset.getY());
            this.bounds = this.buttonBounds;
        }
        
        g2.draw(outline);
        g2.fill(outline);
        
        g2.setStroke(defStroke);
        g2.setColor(defColor);
    }

    @Override
    public int getID() {return this.BUTTON_ID;}

    @Override
    public int getWidth() {return (int)this.bounds.getBounds().getWidth();}
    @Override
    public int getHeight() {return (int)this.bounds.getBounds().getHeight();}
    @Override
    public final int getX() {return (int)this.bounds.getBounds().getX();}
    @Override
    public final int getY() {return (int)this.bounds.getBounds().getY();}
    
    @Override
    public void updateWindowDefaultPosition() {this.buttonBounds = this.bounds.getBounds2D();}
}
