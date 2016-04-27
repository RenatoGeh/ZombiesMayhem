package geometryzombiesmayhem;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * <p> Input box </p>
 * 
 * @author Renato Lui Geh
 * @author Yan
 */
public class InputBox extends ComponentP implements Paintable{
    private int maxChars;
    private String text = "";
    private int style;
    private float size;
    private boolean usesFont = false;
    private boolean usesColor = false;
    private Font font;
    private Color c;
    Vector2D position;
    boolean isActive = false;
    FontMetrics fm;
    
    private static Graphics g = new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB).getGraphics();
    
    public InputBox(Vector2D position, int maxChars, int style, float size) {
        //TODO: create a shape somehow
        fm = g.getFontMetrics(g.getFont().deriveFont(style, size));
        bounds = new Rectangle2D.Float(position.getX(), position.getY() - 50, fm.charWidth('d') *maxChars, 70);
        
        this.position = position;
        this.maxChars = maxChars;
        this.style = style;
        this.size = size;
    }
    
    public InputBox(Vector2D position, int maxChars, Font f) {
        bounds = new Rectangle2D.Float(position.getX() - 20, position.getY() - 50, 150, 70);
        fm = g.getFontMetrics(f);
        this.position = position;
        
        this.maxChars = maxChars;
        this.usesFont = true;
        
        this.font = f;
    }

    
    public void setColor(Color c) {
        this.usesColor = true;
        this.c = c;
    }
    
    public Vector2D getPosition() { return position; }
    
    @Override
    protected void processKeyEvent(KeyEvent event) {
        super.processKeyEvent(event);
        if(event.getID()==KeyEvent.KEY_TYPED) handleText(event);
    }
    
    public void handleText(KeyEvent event) {        
        if(event.getKeyChar() == ChatFrame.CHAR_BACKSPACE) {
            if(text.length() > 0)
                text = text.substring(0, text.length()-1);           
        } else if(text.length() < maxChars)
            text += event.getKeyChar();
    }
    
    public void resetText() {
        text = "";
    }
    
    public String getText() {
        return text;
    }
    
    private void setFont(Graphics2D g2, int style, float size) {
        g2.setFont(g2.getFont().deriveFont(style, size));
    }
    
    @Override
    public void paint(Graphics2D g2, Component c) {
        Font defaultFont = g2.getFont();
        Color defaultColor = g2.getColor();
        
        if(!usesFont)
            this.setFont(g2, style, size);
        else
            g2.setFont(font);
        
        if(usesColor)
            g2.setColor(this.c);
        
        if(!text.isEmpty())
            g2.drawString(text, (int)position.getX(), (int)position.getY());
        
        g2.setFont(defaultFont);
        g2.setColor(defaultColor);
        g2.draw(bounds);
        if(tooltip!=null) tooltip.paint(g2, c);
    }

    @Override
    public boolean isActive() {
        return isActive;
    }

    @Override
    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public void updateWindowDefaultPosition() {
        position.add(window.x, window.y);
    }
}


//package geometryzombiesmayhem;
//
//import java.awt.Component;
//import java.awt.Font;
//import java.awt.Graphics2D;
//import java.awt.geom.Rectangle2D;
//
//public class InputBox extends ComponentP implements Paintable {
//    private Font textFont;
//    private String[] text;
//    private int maxChars;
//    
//    public InputBox(Vector2D position, Vector2D size) {this(position, InputBox.DEFAULT_FONT, size);}
//    
//    public InputBox(Vector2D position, Font f, Vector2D size) {
//        this.textFont = f;
//        
//        this.bounds = new Rectangle2D.Float(position.getX(), position.getY(),
//                size.getX(), size.getY());
//        
//        
//        
//    }
//    
//    private int convertPixels(float pixels) {
//        return (int)(pixels*this.textFont.getSize()/ZM.GRAPHICS.getFontMetrics(this.textFont).getHeight());
//    }
//    
//    private float convertPoints(int points) {
//        return ZM.GRAPHICS.getFontMetrics(this.textFont).getHeight()*points/this.textFont.getSize();
//    }
//
//    @Override
//    public void updateWindowDefaultPosition() {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public boolean isActive() {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public void setActive(boolean active) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//
//    @Override
//    public void paint(Graphics2D g2, Component c) {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }
//    
//    private static final Font DEFAULT_FONT = new Font(Font.DIALOG, Font.PLAIN, 10);
//}