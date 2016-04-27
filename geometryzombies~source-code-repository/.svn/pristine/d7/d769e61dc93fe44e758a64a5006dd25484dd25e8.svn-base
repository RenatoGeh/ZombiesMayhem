/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package geometryzombiesmayhem.leveleditor;

import geometryzombiesmayhem.*;
import geometryzombiesmayhem.ImageFader.Text;
import geometryzombiesmayhem.circuitry.Input;
import geometryzombiesmayhem.circuitry.Output;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.SwingUtilities;

/**
 *
 * @author Yan
 */
public class LinkBrush extends Brush{
    private static LinkBrush BRUSH = null;
    private LinkBrush(){};
    public static LinkBrush getBrush() {
        if(BRUSH==null) BRUSH = new LinkBrush();
        return BRUSH;
    }
    
    private Output lateOut = null;
    private Input lateIn = null;
    private boolean hasUndone = false;
    
    @Override
    public void undo() {
        if(lateOut == null) return;
        lateOut.remove(lateIn);
        hasUndone = true;
    }
    @Override
    public void redo() {
        if(lateOut == null) return;
        if(!hasUndone) return;
        lateOut.add(lateIn);
        hasUndone = false;
    }
    
    PaintableLine wire;
    boolean drawingWire = false;
    Output o;
    
    {
        gA = new GeneralAdapter() {
            
            @Override
            public void mouseDragged(MouseEvent event) {
                super.mouseDragged(event);
                if (drawingWire && ((event.getModifiersEx() & ~MouseEvent.BUTTON1_DOWN_MASK) == 0)) {
                    Point e = event.getPoint();
                    wire.updateEnd(e);
                }
            }

            @Override
            public void mouseReleased(MouseEvent event) {
                super.mouseReleased(event);
                if (event.getButton() == MouseEvent.BUTTON1) {
                    Paintables.removeInterface(wire);
                    boolean found = false;
                    for(int i=Body.getBodies().size()-1;i>-1;i--) {
                        Body b = Body.getBodies().get(i);
                        if(b instanceof Input && b.contains(event.getPoint())) {
                            lateIn = (Input)b;
                            o.add(lateIn);
                            found = true;
                            break;
                        }
                    }
                    if(!found) Paintables.register(new ImageFader(). new Text(Vector2D.convertFrom(event.getPoint()).subtract(0, 20), "No Input hete", FontFactory.ZOMBIE_FONT, ColorFactory.getPDRandomColor(), .05f));
                    else Paintables.register(new ImageFader(). new Text(Vector2D.convertFrom(event.getPoint()).subtract(0, 20), "Connected!", FontFactory.ZOMBIE_FONT, ColorFactory.getPDRandomColor(), .05f));
                    wire = null;
                    drawingWire = false;
                    SwingUtilities.invokeLater(new Runnable(){

                        @Override
                        public void run() {
                            LevelEditor.gA.remove((MouseAdapter) gA);
                        }
                        
                    });
                    
                }
            }
        };
    }

    @Override
    public void start(MouseEvent event) {
        for(int i=Body.getBodies().size()-1;i>-1;i--) {
                Body b = Body.getBodies().get(i);
                if(b instanceof Output && b.contains(event.getPoint())) {
                    lateOut = o = (Output) b;
                    wire = new PaintableLine(event.getPoint(),event.getPoint());
                    wire.setInterface(true);
                    drawingWire = true;
                    Paintables.registerInterface(wire);
                    LevelEditor.gA.add((MouseAdapter)gA);
                    return;
                }
        }
        Paintables.register(new ImageFader(). new Text(Vector2D.convertFrom(event.getPoint()).subtract(0, 20), "No Output hete", FontFactory.ZOMBIE_FONT, ColorFactory.getPDRandomColor(), .05f));
    }
    
}
