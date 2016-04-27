package geometryzombiesmayhem.leveleditor;

import geometryzombiesmayhem.Body;
import geometryzombiesmayhem.GameFrame;
import geometryzombiesmayhem.GeneralAdapter;
import geometryzombiesmayhem.Item;
import geometryzombiesmayhem.Paintables;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.SwingUtilities;

/**
 * <p>A <code>Brush</code> that adds <code>Body</code> units.
 * 
 * @author Renato Lui Geh
 */
public class UnitBrush extends Brush implements Typeable<Body>{
    private static UnitBrush BRUSH = new UnitBrush();
    private UnitBrush() {};
    public static UnitBrush getBrush() {
        if(BRUSH == null) BRUSH = new UnitBrush();
        return BRUSH;
    }
    
    public static void setBodyType(Body e) {BRUSH.setType(e);} 
    public static void open() {BRUSH.openStream();}
    public static void close() {BRUSH.closeStream();}
    public static Body getSelectedUnit() {return BRUSH.getSelected();}
    
    public static void setEnabled(boolean isEnabled) {BRUSH.isEnabled = isEnabled;}
    public static boolean isEnabled() {return BRUSH.isEnabled;}
    
    private Body unit;
    private PaintableShape currentRect;
    private boolean ready = false;
    private boolean isEnabled = false;
    
    private Body latest = null;
    private boolean hasUndone = false;
    
    @Override
    public void undo() {
        if(latest == null) return;
        Body.remove(latest);
        if(latest instanceof Item) Paintables.remove((Item)latest);
        hasUndone = true;
    }
    @Override
    public void redo() {
        if(latest == null) return;
        if(!hasUndone) return;
        Body.register(latest);
        if(latest instanceof Item) Paintables.register((Item)latest);
        hasUndone = false;
    }
    
    {
        gA = new GeneralAdapter() {
            @Override
            public void keyPressed(KeyEvent event) {
                if(!ready) return;
                super.keyPressed(event);
                if(event.getKeyCode() == KeyEvent.VK_ESCAPE)
                    UnitBrush.this.closeStream();
            }
            
            @Override
            public void mouseReleased(MouseEvent event) {
                if(!ready) return;
                if(GameFrame.pauseInterference) return;
                if(event.getButton()!=MouseEvent.BUTTON1) return;
                super.mouseReleased(event);
                Body copy = unit.copy();
                copy.setPosition(GameFrame.mousePosition.copy());
                Body.register(latest = copy);
            } 
            
            @Override
            public void mouseDragged(MouseEvent event) {
                if(!ready) return;
                super.mouseDragged(event);
//                UnitBrush.this.unit.lookAt(GameFrame.mousePosition);
            }
        };
    }

    @Override
    public void start(MouseEvent event) {
        //Nothing to see here. Move along, citizen.
    }

    @Override
    public void openStream() {
        Paintables.registerInterface(currentRect);
        ready = true;
        LevelEditor.gA.add((MouseAdapter)gA);
        LevelEditor.gA.add((KeyListener)gA);
    }

    @Override
    public void closeStream() {
        if(ready == false) return;
        
        Paintables.removeInterface(currentRect);
        ready = false;
        unit = null;
        currentRect = null;
        
        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run() {
                LevelEditor.gA.remove((MouseAdapter)gA);
                LevelEditor.gA.remove((KeyListener)gA);
            }        
        });
    }

    @Override
    public void setType(Body e) {
        unit = e;
        currentRect = new PaintableShape(e.getBounds(), GameFrame.mousePosition);
        currentRect.setInterface(true);
    }

    @Override
    public Body getSelected() {return unit;}
}
