package geometryzombiesmayhem;

import java.awt.Component;
import java.awt.Graphics2D;

/**
 * <p>Base menu for menus.</p>
 * 
 * @author Renato Lui Geh
 */
public abstract class Menu extends PanelP {
    protected boolean loaded = false;
    
    public void load(WindowP window) {
        loaded = true;
        addTo(window);
    }
    
    public void close(WindowP window) {
        loaded = false;
        removeFromWindow();
    }
    
    @Override
    public boolean isActive() {return true;}
    @Override
    public void setActive(boolean isActive) {}

    @Override
    public void paint(Graphics2D g2, Component c) {}
}