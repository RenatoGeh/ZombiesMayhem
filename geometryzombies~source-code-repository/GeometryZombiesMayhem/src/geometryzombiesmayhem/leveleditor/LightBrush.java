package geometryzombiesmayhem.leveleditor;

import geometryzombiesmayhem.Body;
import geometryzombiesmayhem.ColorFactory;
import geometryzombiesmayhem.FlareShape;
import geometryzombiesmayhem.GameFrame;
import geometryzombiesmayhem.GeneralAdapter;
import geometryzombiesmayhem.LightManager;
import geometryzombiesmayhem.LightSource;
import geometryzombiesmayhem.Paintables;
import geometryzombiesmayhem.Vector2D;
import geometryzombiesmayhem.circuitry.LightBulb;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

/**
 *
 * @author Renato Lui Geh
 */
public class LightBrush extends Brush implements Typeable<LightSource> {
    private static LightBrush BRUSH = new LightBrush();
    private LightBrush() {};
    public static LightBrush getBrush() {
        if(BRUSH == null) BRUSH = new LightBrush();
        return BRUSH;
    }
    
    public static void setCircuitEnabled(boolean isCircuit) {BRUSH.isCircuit = isCircuit;}
    public static void setLightType(LightSource e) {BRUSH.setType(e);}
    public static void open() {BRUSH.openStream();}
    public static void close() {BRUSH.closeStream();}
    public static void setEnabled(boolean enabled) {BRUSH.isEnabled = enabled;}
    public static boolean isEnabled() {return BRUSH.isEnabled;}
    public static LightSource getSelectedLight() {return BRUSH.getSelected();}
    
    private LightSource source;
    private LightSource copy;
    private boolean ready = false;
    private boolean isEnabled = false;
    private boolean isSelected = false;
    private boolean isCircuit = false;
    private Vector2D position;
    private Vector2D[] points; //If the light source is polygonal.
    private ArrayList<PaintableShape> pointMarkers = new ArrayList<>();
    private boolean polyReady = false;

    private LightSource latest = null;
    private boolean hasUndone = false;
    
    @Override
    public void undo() {
        if(latest == null) return;
        LightManager.remove(latest);
        hasUndone = true;
    }
    @Override
    public void redo() {
        if(latest == null) return;
        if(!hasUndone) return;
        LightManager.register(latest);
        hasUndone = false;
    }
    
    {
        gA = new GeneralAdapter() {
            @Override
            public void mouseDragged(MouseEvent event) {
                if(!LightBrush.this.isSelected) return;
                super.mouseDragged(event);
                
                if(LightBrush.this.copy instanceof FlareShape.Conical) {
                    FlareShape.Conical cone = (FlareShape.Conical)copy;
                    float dist = LightBrush.this.position.distance(GameFrame.mousePosition);
                    if(Math.round(dist)!=Math.round(cone.getWidth())) cone.setWidth(dist);
                } else if(LightBrush.this.copy instanceof FlareShape.Trapezoid) {
                    FlareShape.Trapezoid trap = (FlareShape.Trapezoid)copy;
                    float dist = LightBrush.this.position.distance(GameFrame.mousePosition);
                    if(Math.round(dist)!=Math.round(trap.getWidth())) trap.setWidth(dist);
                }
            }
            
            @Override
            public void mouseWheelMoved(MouseWheelEvent event) {
                if(!LightBrush.this.isSelected) return;
                if(!(LightBrush.this.copy instanceof FlareShape.Conical)) return;
                super.mouseWheelMoved(event);
                
                FlareShape.Conical cone = (FlareShape.Conical)copy;
                cone.setAngle(cone.getAngle()+event.getWheelRotation()*(float)Math.PI/72);
            }

            @Override
            public void keyReleased(java.awt.event.KeyEvent event) {
                if(!LightBrush.this.isSelected) return;
                super.keyReleased(event);
                
                if(event.getKeyCode() == java.awt.event.KeyEvent.VK_DELETE)
                    if(LightBrush.this.copy != null)
                        LightManager.remove(LightBrush.this.copy);
            }
            
            @Override
            public void mouseReleased(MouseEvent event) {
                if(!LightBrush.this.ready) return;
                super.mouseReleased(event);
                
                if(event.getButton()==MouseEvent.BUTTON2) return;
                
                if(LightBrush.this.source instanceof FlareShape.Polygonal && LightBrush.this.isSelected)
                    if(LightBrush.this.polyReady) {
//                        for(int i=0;i<points.length;i++) System.err.println("Point" + i + ": " + points[i]);
                        LightBrush.this.copy = new FlareShape.Polygonal(points, 50, 50, 0, LightManager.getColor());
                        for(PaintableShape p : pointMarkers) Paintables.remove(p);
                        pointMarkers.clear();
                        LightManager.register((FlareShape.Polygonal)copy);
                        latest = copy;
                        LightBrush.this.polyReady = false;
                    } else return;
                
                if(LightBrush.this.copy==null)
                    return;
                    
                LightBrush.this.copy.setDirection(LightBrush.this.copy.getDirection().copy());
                if(LightBrush.this.copy instanceof FlareShape)((FlareShape)copy).setBlinking(true);
                LightBrush.this.isSelected = false;
                
                if(LightBrush.this.isCircuit)
                    Body.register(new LightBulb(LightBrush.this.copy, 
                            LightBrush.this.copy.currentVoltage(), true, LightBrush.this.position));
            }
        };
    }
    
    @Override
    public void start(MouseEvent event) {
        if(!this.ready) return;
        
        if(this.isSelected && source instanceof FlareShape.Polygonal) {
            if(points.length > 2 && GameFrame.mousePosition.equalsRounded(points[0])) {
                this.polyReady = true;
                gA.mouseReleased(event);
                return;
            }
            
            Vector2D[] aux = new Vector2D[points.length+1];
            System.arraycopy(points, 0, aux, 0, points.length);
            aux[points.length] = GameFrame.mousePosition.copy();
            this.points = aux;
            
            PaintableShape<Ellipse2D.Float> marker = new PaintableShape<>(
                    new Ellipse2D.Float(GameFrame.mousePosition.getX()-5, GameFrame.mousePosition.getY()-5, 10, 10), 
                    GameFrame.mousePosition.copy(), ColorFactory.getPDRandomColor());
            pointMarkers.add(marker);
            Paintables.register(marker);
            
            return;
        }
        
        this.position = new Vector2D(event.getX(), event.getY());
        if(!(source instanceof FlareShape.Polygonal)) {
            this.copy = source.copy();
            copy.setPosition(LightBrush.this.position);
            copy.setDirection(GameFrame.mousePosition);
            copy.setEnabled(true);
            latest = copy;
        } else {
            points = new Vector2D[] {position}; 
            PaintableShape<Ellipse2D.Float> marker = new PaintableShape<>(
                    new Ellipse2D.Float(position.getX()-5, position.getY()-5, 10, 10), 
                    position.copy(), ColorFactory.getPDRandomColor());
            pointMarkers.add(marker);
            Paintables.register(marker);
        }
        this.isSelected = true;
    }

    @Override
    public void openStream() {
        this.ready = true;
        LevelEditor.gA.add((MouseAdapter)gA);
        LevelEditor.gA.add((KeyListener)gA);
    }

    @Override
    public void closeStream() {
        if(ready == false) return;
        
        ready = false;
        this.source = null;
        this.position = null;
        this.copy = null;

        LevelEditor.gA.remove((MouseAdapter)gA);
        LevelEditor.gA.remove((KeyListener)gA);
    }

    @Override
    public void setType(LightSource e) {
        this.source = e;
    }

    @Override
    public LightSource getSelected() {return source;}    
}
