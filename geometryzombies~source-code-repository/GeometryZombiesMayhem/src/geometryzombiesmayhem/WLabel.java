package geometryzombiesmayhem;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

/**
 * <p>A <code>Window</code> label.</p>
 * 
 * @author Renato Lui Geh
 */
public class WLabel extends ComponentP implements Paintable {
    private String label = "";
    private Font textFont = new Font(Font.DIALOG, Font.PLAIN, 10);
    private Color textColor = Color.BLACK;
    
    public WLabel(Vector2D position, String label) {this(position, WLabel.DEFAULT_FONT, label);}
    
    public WLabel(Vector2D position, Font f, String label) {
        this.textFont = f;
        this.label = label;
        
        FontMetrics metrics = ZM.GRAPHICS.getFontMetrics(textFont);
        
        float width = metrics.stringWidth(this.label);
        float height = metrics.getHeight();
        
        this.bounds = new Rectangle2D.Float(position.getX(), position.getY(), width, height);
    }
    
    public void setFontSize(int points) {
        textFont = new Font(textFont.getName(), textFont.getStyle(), points);
    }
    
    public void setFontSize(float pixels) {
        this.setFontSize(textFont.getSize()*pixels/ZM.GRAPHICS.getFontMetrics(textFont).getHeight());
    }
    
    public void setText(String s) {this.label = s;}
    
    @Override
    public void updateWindowDefaultPosition() {}

    @Override
    public boolean isActive() {return true;}
    @Override
    public void setActive(boolean active) {}
    
    @Override
    public int getX() {return (int)this.bounds.getBounds().getX();}
    @Override
    public int getY() {return (int)this.bounds.getBounds().getY();}
    
    @Override
    public int getWidth() {return (int)this.bounds.getBounds().getWidth();}
    @Override
    public int getHeight() {return (int)this.bounds.getBounds().getHeight();}

    @Override
    public void paint(Graphics2D g2, Component c) {
        Color defColor = g2.getColor();
        Font defFont = g2.getFont();
        
        g2.setColor(textColor);
        g2.setFont(textFont);
        
        g2.drawString(label, this.getX(), this.getY() + this.getHeight());
        
        g2.setColor(defColor);
        g2.setFont(defFont);
    }

    private static final Font DEFAULT_FONT = new Font(Font.DIALOG, Font.PLAIN, 10);
}
