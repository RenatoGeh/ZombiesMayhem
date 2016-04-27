package geometryzombiesmayhem.leveleditor;

import geometryzombiesmayhem.Body;
import geometryzombiesmayhem.Dialog;
import geometryzombiesmayhem.Vector2D;
import geometryzombiesmayhem.circuitry.DialogAdapter;
import java.awt.event.MouseEvent;

/**
 *
 * @author Renato Lui Geh
 */
public class DialogBrush extends Brush {
    private static DialogBrush BRUSH = new DialogBrush();
    public static DialogBrush getBrush() {
        if(BRUSH == null) return BRUSH = new DialogBrush();
        return BRUSH;
    }
 
    private Vector2D position;
    private DialogAdapter latest = null;
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
    
    public void submit(Dialog diag, DialogAdapter.Type type) {
        this.latest = new DialogAdapter(position, diag, type);
        Body.register(this.latest);
        position = null;
    }
    
    @Override
    public void start(MouseEvent event) {
        if(DialogCreator.inUse) return;
        
        DialogCreator.activate();
        this.position = new Vector2D(event.getX(), event.getY());
    }
}
