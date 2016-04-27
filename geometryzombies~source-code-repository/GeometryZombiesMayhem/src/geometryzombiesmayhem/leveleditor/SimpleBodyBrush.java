/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package geometryzombiesmayhem.leveleditor;

import geometryzombiesmayhem.Body;
import java.awt.event.MouseEvent;

/**
 *
 * @author Yan
 */
public class SimpleBodyBrush extends Brush{
    
    private Body b;
    private Body latest = null;
    private boolean hasUndone = false;
    
    public SimpleBodyBrush(Body body) {
        b = body;
    }
    
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
    public void start(MouseEvent startEvent) {
        Body newBody = b.copy();
        newBody.getPosition().set(startEvent.getPoint());
//        LevelEditor.level.bodies.add(newBody);
        Body.register(latest=newBody);
    }
    
}
