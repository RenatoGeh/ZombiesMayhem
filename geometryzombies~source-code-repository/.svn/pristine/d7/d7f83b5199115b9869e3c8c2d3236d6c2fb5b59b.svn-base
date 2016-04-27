package geometryzombiesmayhem;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.util.HashSet;
import java.util.Set;

/**
 * <code>Image</code>, <code>Rectangle2D</code> and <code>Vector2D</code> working together.
 * 
 * @author Renato Lui Geh
 * @author Yan
 * 
 * @see java.awt.Image
 * @see java.awt.geom.Rectangle2D
 * @see geometryzombiesmayhem.Vector2D
 */
public class ImageButton extends ComponentP implements IDable{
    private Rectangle2D buttonBounds;
    private Vector2D buttonPosition;
    
    private MutableImage hoverImage;
    private MutableImage unselectImage;
    protected MutableImage currentImage;
    
    private int BUTTON_ID;
    public static final int BUTTON_NONE = -1;
    
    private Set<ActionListener> actionL = new HashSet<>();
    
    private boolean hasHover = false;
    protected boolean isActive = true;
    private boolean isEnabled = true;
    
    public ImageButton(MutableImage hoverImage, MutableImage unselectImage, Vector2D buttonPosition, int id) {
        this(unselectImage,buttonPosition,id);
        this.hoverImage = hoverImage;
        this.unselectImage = unselectImage;
        
        this.hasHover = true;
    }
    
    public ImageButton(MutableImage buttonImage, Vector2D buttonPosition, int id) {
        this.buttonPosition = buttonPosition;
        this.currentImage = buttonImage;
        this.unselectImage = this.currentImage;
        
        this.buttonBounds = new Rectangle2D.Float(buttonPosition.getX(), buttonPosition.getY(), 
                buttonImage.getWidth(null), buttonImage.getHeight(null));
        bounds = buttonBounds;
        
        this.BUTTON_ID = id;
    }
    
    @Deprecated
    private void refreshRectangles() {
        buttonBounds = new Rectangle2D.Float(buttonPosition.getX(), buttonPosition.getY(),
                currentImage.getWidth(null), currentImage.getHeight(null));
    }
    
    private void refreshPositions(Vector2D position) {
        this.buttonPosition = position;
    }
    
    public void refreshButtons(Vector2D position) {
        refreshPositions(position);
    }
    
    public Vector2D getPosition() {return buttonPosition.set((float)bounds.getBounds().getX(), (float)bounds.getBounds().getY());}
    
    @Override
    public void paint(Graphics2D g2, Component c) {  
        if(!isEnabled)
            return;
        g2.drawImage(currentImage, (int)bounds.getBounds().getX(), (int)bounds.getBounds().getY(), c);
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
    public int getID() { return BUTTON_ID; }

    public void addActionListener(ActionListener listener) {
        this.actionL.add(listener);
    }
    
    public void removeActionListener(ActionListener listener) {
        this.actionL.remove(listener);
    }
    
    @Override
    public boolean isEnabled() {return isEnabled;}
    @Override
    public void setEnabled(boolean isEnabled) {this.isEnabled = isEnabled;}
    
    /**
     * Override this to change the clicking sound.
     */
    public void clickSound() {
        AudioManager.doInterfaceClick(true);
    }
    
    public void setPosition(Vector2D pos) {
        this.buttonPosition = pos;
        
        this.buttonBounds.setFrame(buttonPosition.getX(), buttonPosition.getY(), 
                this.buttonBounds.getWidth(), this.buttonBounds.getHeight());
        this.bounds = buttonBounds;
    }
    
    @Override
    protected void processMouseEvent(MouseEvent event) {
        if(!isEnabled)
            return;
        
        if(isActive) {
            super.processMouseEvent(event);
            switch(event.getID()) {
                case MouseEvent.MOUSE_CLICKED:
                    ActionEvent e = new ActionEvent(this,ActionEvent.ACTION_PERFORMED,BUTTON_ID + "clicked",event.getWhen(),event.getModifiers());
                    for(ActionListener l : actionL)
                        l.actionPerformed(e);
                    clickSound();
                    break;
                case MouseEvent.MOUSE_ENTERED:
                    if(hasHover)
                        currentImage = hoverImage;
                    break;
                case MouseEvent.MOUSE_EXITED:
                    currentImage = unselectImage;
                    break;
            }
        }
    }

    @Override
    public void updateWindowDefaultPosition() {
        buttonPosition.addLocal(window.x, window.y);
    }
    
    @Override
    public int getWidth() {return this.currentImage.getWidth();}
    @Override
    public int getHeight() {return this.currentImage.getHeight();}
}