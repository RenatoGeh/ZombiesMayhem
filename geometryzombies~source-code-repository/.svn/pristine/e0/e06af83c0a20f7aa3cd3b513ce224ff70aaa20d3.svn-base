/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package geometryzombiesmayhem.leveleditor;

import geometryzombiesmayhem.circuitry.Detector;
import geometryzombiesmayhem.*;
import geometryzombiesmayhem.circuitry.Detector.Type;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import javax.swing.SwingUtilities;

/**
 *
 * @author Yan
 */
public class DetectorBrush extends Brush {
    private static DetectorBrush BRUSH;
    private DetectorBrush() {};
    public static DetectorBrush getBrush() {
        if(BRUSH == null) BRUSH = new DetectorBrush();
        return BRUSH;
    }

    PaintableRectangle currentRec;
    boolean drawingRec = false;
    Type t = Detector.Type.PLAYER;

    private Detector latest = null;
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
    
    {
        gA = new GeneralAdapter() {

            @Override
            public void keyPressed(KeyEvent event) {
                super.keyPressed(event);
                
                if(event.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    Paintables.removeInterface(currentRec);

                    currentRec = null;
                    drawingRec = false;
                    
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            LevelEditor.gA.remove((MouseAdapter) gA);
                            LevelEditor.gA.remove((KeyListener)gA);
                        }
                    });
                }
                else if(event.getKeyCode()==KeyEvent.VK_Z) t = Type.ZOMBIE;
                else if(event.getKeyCode()==KeyEvent.VK_P) t = Type.PLAYER;
                else if(event.getKeyCode()==KeyEvent.VK_S) t = Type.PROJECTILE;
            }
            
            @Override
            public void mouseDragged(MouseEvent event) {
                super.mouseDragged(event);
                if (drawingRec && ((event.getModifiersEx() & ~MouseEvent.BUTTON1_DOWN_MASK) == 0)) {
                    Point e = event.getPoint();
                    currentRec.updateEnd(e);
                }
            }

            @Override
            public void mouseReleased(MouseEvent event) {
                super.mouseReleased(event);
                if (event.getButton() == MouseEvent.BUTTON1 && currentRec!=null) {
                    //TEMPORARY
                    Paintables.removeInterface(currentRec);
                    Rectangle2D rec = currentRec.getRectangle();
                    if(rec.getHeight()<10 || rec.getWidth() <10) return;
                    Detector d = new Detector(rec, t);
                    Body.register(d);
                    latest = d;
                    currentRec = null;
                    drawingRec = false;
                    SwingUtilities.invokeLater(new Runnable() {

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
            currentRec = new PaintableRectangle(startPoint, startPoint);
            currentRec.setInterface(true);
            drawingRec = true;
            Paintables.registerInterface(currentRec);
            LevelEditor.gA.add((MouseAdapter)gA);
            LevelEditor.gA.add((KeyListener)gA);
        }
    }
}
