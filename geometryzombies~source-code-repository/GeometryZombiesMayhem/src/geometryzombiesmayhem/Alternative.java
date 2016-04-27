package geometryzombiesmayhem;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

/**
 * <p>Represents an <code>Alternative</code> to <code>InteractibleDialog</code>.</p>
 * 
 * @author Renato Lui Geh
 */
public abstract class Alternative extends TextBox implements IDable {     
    private final int id;
    private Color highlightColor = Color.YELLOW;
    private boolean isSelected = false;
    private boolean isDone = false;
    
    public Alternative(Vector2D position, String text, Font f, int maxChars, int id) {
        super(position, text, ColorFactory.getColor(Color.BLACK, 80), f, maxChars);  
        this.id = id;
        
        this.setText(id + ". " + this.text);
        
        this.addMouseListener(gAdapter);
        this.addKeyListener(gAdapter);
    }

    @Override
    public void paint(Graphics2D g2, Component c) {
        super.paint(g2, c);
        
        if(isSelected) {
            Color defColor = g2.getColor();
            java.awt.Stroke s = g2.getStroke();
            g2.setColor(highlightColor);
            g2.setStroke(new java.awt.BasicStroke(3));
            g2.draw(this.getBounds());
            g2.setStroke(s);
            g2.setColor(defColor);
        }
    }
    
    @Override
    public Rectangle getBounds() {
        Rectangle rect = super.getBounds();
        rect.setFrame(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight()/2);
        return rect;
    }
    
    public boolean isDone() {return isDone;}
    
    public boolean isSelected() {return isSelected;}
    public void setSelected(boolean isSelected) {this.isSelected = isSelected;}
    
    public void toggleSelected() {this.isSelected = !this.isSelected;}
    
    public abstract void run();
    
    public void load() {
        GameFrame.paintArea.addMouseListener(gAdapter);
        GameFrame.paintArea.addMouseMotionListener(gAdapter);
        GameFrame.paintArea.addKeyListener(gAdapter);
    }
    
    public void close() {
        GameFrame.paintArea.removeMouseListener(gAdapter);
        GameFrame.paintArea.removeMouseMotionListener(gAdapter);
        GameFrame.paintArea.removeKeyListener(gAdapter);
    }
    
    private char getChar(int number) {return (char)((int)'0' + number);}
    
    @Override
    public int getID() {return this.id;}
    
    private GeneralAdapter gAdapter = new GeneralAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            super.mouseClicked(e);
            if(isSelected()) {
                run();
                isDone = true;
            }
        }
        
        @Override
        public void mouseMoved(MouseEvent e) {
            super.mouseMoved(e);
            if(getBounds().contains(GameFrame.mousePosition.getX() - GameFrame.translateX, GameFrame.mousePosition.getY()))
                setSelected(true);
            else
                setSelected(false);
        }

        @Override
        public void keyTyped(KeyEvent e) {
            super.keyTyped(e);
            if(e.getKeyChar() == getChar(id)) {
                run();
                isDone = true;
            }
        }
    };
}