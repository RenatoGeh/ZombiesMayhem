package geometryzombiesmayhem;

import java.awt.Component;
import java.awt.Graphics2D;

/**
 * Indicates the implemented class can be painted.
 * 
 * @author Renato Lui Geh
 */
public interface Paintable {  
    public boolean isActive();
    public void setActive(boolean active);
    public void paint(Graphics2D g2, Component c);
    public boolean showOnlyOnEditor();
}
