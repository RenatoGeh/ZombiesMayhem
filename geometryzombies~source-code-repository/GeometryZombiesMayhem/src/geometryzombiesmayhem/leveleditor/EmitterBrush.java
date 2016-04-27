/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package geometryzombiesmayhem.leveleditor;

import geometryzombiesmayhem.*;
import geometryzombiesmayhem.circuitry.Emitter;
import java.awt.event.MouseEvent;

/**
 *
 * @author Yan
 */
public class EmitterBrush extends Brush{

    private static EmitterBrush BRUSH;
    private EmitterBrush() {};
    public static EmitterBrush getBrush() {
        if(BRUSH == null) BRUSH = new EmitterBrush();
        return BRUSH;
    }
    
    private Emitter latest = null;
    private boolean hasUndone = false;
    
    @Override
    public void undo() {
        if(latest == null) return;
        Body.remove(latest);
        hasUndone = true;
    }
    @Override
    public void redo() {
        if(latest == null) return;
        if(!hasUndone) return;
        Body.register(latest);
        hasUndone = false;
    }
    
    @Override
    public void start(MouseEvent e) {
        if(e.getButton()==MouseEvent.BUTTON1) {
            Emitter em = new Emitter(new Zombie("z",Vector2D.ZERO.copy(),Zombie.getDefaultSprite(),100),new Vector2D(e.getX(),e.getY()),false);
            Body.register(em);
            latest = em;
        }
    }
    
}
