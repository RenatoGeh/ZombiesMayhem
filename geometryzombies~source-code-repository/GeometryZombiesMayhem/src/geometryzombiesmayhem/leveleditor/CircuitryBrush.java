package geometryzombiesmayhem.leveleditor;

import geometryzombiesmayhem.GameFrame;
import geometryzombiesmayhem.GeneralAdapter;
import geometryzombiesmayhem.Paintables;
import geometryzombiesmayhem.circuitry.CircuitryBody;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.SwingUtilities;

/**
 * <p>A <code>Brush</code> that adds <code>CircuitryBody</code> components.
 * 
 * @author Renato Lui Geh
 */
public class CircuitryBrush extends Brush implements Typeable<CircuitryBody>{
    private static CircuitryBrush BRUSH = new CircuitryBrush();
    private CircuitryBrush() {};
    public static CircuitryBrush getBrush() {
        if(BRUSH == null) BRUSH = new CircuitryBrush();
        return BRUSH;
    }
    
    public static void setComponentType(CircuitryBody e) {BRUSH.setType(e);} 
    public static void open() {BRUSH.openStream();}
    public static void close() {BRUSH.closeStream();}
    public static CircuitryBody getSelectedComponent() {return BRUSH.getSelected();}
    
    public static void setEnabled(boolean isEnabled) {BRUSH.isEnabled = isEnabled;}
    public static boolean isEnabled() {return BRUSH.isEnabled;}
    
    private CircuitryBody component;
    private PaintableShape layout;
    private boolean ready = false;
    private boolean isEnabled = false;
    
    private CircuitryBody latest = null;
    private boolean hasUndone = false;
    
    @Override
    public void undo() {
        if(latest == null) return;
        CircuitryBody.remove(latest);
        hasUndone = true;
    }
    @Override
    public void redo() {
        if(latest == null) return;
        if(!hasUndone) return;
        CircuitryBody.register(latest);
        hasUndone = true;
    }
    
    {
        gA = new GeneralAdapter() {
            @Override
            public void keyPressed(KeyEvent event) {
                if(!ready) return;
                super.keyPressed(event);
                if(event.getKeyCode() == KeyEvent.VK_ESCAPE)
                    CircuitryBrush.this.closeStream();
            }
            
            @Override
            public void mouseReleased(MouseEvent event) {
                if(!ready) return;
                if(GameFrame.pauseInterference) return;
                if(event.getButton()!=MouseEvent.BUTTON1);
                super.mouseReleased(event);
                CircuitryBody copy = (CircuitryBody)component.copy();
                copy.setPosition(GameFrame.mousePosition.copy());
                CircuitryBody.register(copy);
                latest = copy;
            } 
            
            @Override
            public void mouseDragged(MouseEvent event) {
                if(!ready) return;
                super.mouseDragged(event);
            }
        };
    }

    @Override
    public void start(MouseEvent event) {}

    @Override
    public void openStream() {
        Paintables.registerInterface(layout);
        ready = true;
        LevelEditor.gA.add((MouseAdapter)gA);
        LevelEditor.gA.add((KeyListener)gA);
    }

    @Override
    public void closeStream() {
        if(ready == false) return;
        
        Paintables.removeInterface(layout);
        ready = false;
        component = null;
        layout = null;
        
        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run() {
                LevelEditor.gA.remove((MouseAdapter)gA);
                LevelEditor.gA.remove((KeyListener)gA);
            }        
        });
    }

    @Override
    public void setType(CircuitryBody e) {
        component = e;
        layout = new PaintableShape(e.getBounds(), GameFrame.mousePosition);
        layout.setInterface(true);
    }

    @Override
    public CircuitryBody getSelected() {return component;}
}
