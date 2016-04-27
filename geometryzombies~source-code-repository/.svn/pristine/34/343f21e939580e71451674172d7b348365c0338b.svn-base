package geometryzombiesmayhem.leveleditor;

import geometryzombiesmayhem.*;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.SwingUtilities;

/**
 *
 * @author Yan
 */
public class SimpleBoundBrush extends Brush {
    private static SimpleBoundBrush BRUSH;
    private SimpleBoundBrush() {};
    public static SimpleBoundBrush getBrush() {
        if(BRUSH == null) BRUSH = new SimpleBoundBrush();
        return BRUSH;
    }

    PaintableLine currentLine;
    boolean drawingLine = false;
    private Bound latest = null;
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
        System.err.println("redoing");
        Body.register(latest);
        hasUndone = false;
    }

    {
        gA = new GeneralAdapter() {

            @Override
            public void keyPressed(KeyEvent event) {
                super.keyPressed(event);
                
                if(event.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    Paintables.removeInterface(currentLine);

                    currentLine = null;
                    drawingLine = false;
                    
                    SwingUtilities.invokeLater(new Runnable(){
                        @Override
                        public void run() {
                            LevelEditor.gA.remove((MouseAdapter) gA);
                            LevelEditor.gA.remove((KeyListener)gA);
                        }
                    });
                }
            }
            
            @Override
            public void mouseDragged(MouseEvent event) {
                super.mouseDragged(event);
                if (drawingLine && ((event.getModifiersEx() & ~MouseEvent.BUTTON1_DOWN_MASK) == 0)) {
                    Point e = event.getPoint();
                    currentLine.updateEnd(e);
                }
            }

            @Override
            public void mouseReleased(MouseEvent event) {
                super.mouseReleased(event);
                if (event.getButton() == MouseEvent.BUTTON1) {
                    Bound.Simple la = new Bound.Simple(currentLine.line, null, !event.isControlDown());
                    Paintables.removeInterface(currentLine);
                    
                    if(currentLine.line.getP1().distance(currentLine.line.getP2())>1)
                        Body.register(latest = la);
                    
                    currentLine = null;
                    drawingLine = false;
                    SwingUtilities.invokeLater(new Runnable(){
                        @Override
                        public void run() {
                            LevelEditor.gA.remove((MouseAdapter) gA);
                            LevelEditor.gA.remove((KeyListener)gA);
                        }
                    });
                    
                }
            }
        };
    }

    @Override
    public void start(MouseEvent event) {
        if (event.getButton() == MouseEvent.BUTTON1) {
            Point startPoint = event.getPoint();
            currentLine = new PaintableLine(startPoint, startPoint);
            currentLine.setInterface(true);
            drawingLine = true;
            Paintables.registerInterface(currentLine);
            LevelEditor.gA.add((MouseAdapter)gA);
            LevelEditor.gA.add((KeyListener)gA);
        }
    }
}
