package geometryzombiesmayhem.leveleditor;

import geometryzombiesmayhem.AwesomeAction;
import geometryzombiesmayhem.AwesomeTimer;
import geometryzombiesmayhem.Body;
import geometryzombiesmayhem.Bound;
import geometryzombiesmayhem.GameFrame;
import geometryzombiesmayhem.GameSettings;
import geometryzombiesmayhem.GeneralAdapter;
import geometryzombiesmayhem.InputField;
import geometryzombiesmayhem.MutableInputField;
import geometryzombiesmayhem.Paintable;
import geometryzombiesmayhem.PaintableImage;
import geometryzombiesmayhem.Paintables;
import geometryzombiesmayhem.Person;
import geometryzombiesmayhem.TabbedPane;
import geometryzombiesmayhem.Vector2D;
import geometryzombiesmayhem.WLabel;
import geometryzombiesmayhem.Window;
import geometryzombiesmayhem.circuitry.Detector;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.swing.SwingUtilities;

/**
 *
 * @author Renato Lui Geh
 */
public class Selector extends Brush {
    private static Selector SELECTOR = new Selector();
    private Selector() {}
    public static Selector getBrush() {
        if(SELECTOR == null) SELECTOR = new Selector();
        return SELECTOR;
    }
    
    private Vector2D latePos = null;
    
    @Override
    public void undo() {
        //TODO: specific undoing according to each Selector action
    }
    @Override
    public void redo() {
        //TODO: specific redoing according to each Selector action
    }
    
    private Paintable selected;
    private boolean selecting = false;
    private Body selectedBody;
    private Vector2D position;
    
    private InfoWindow infoWindow;
    
    private Vector2D relativePos;
    private float BASE_ANGLE = 0;
    
    {
        gA = new GeneralAdapter() {
            @Override
            public void mouseDragged(MouseEvent event) {
                super.mouseClicked(event);
                
                if(selecting && ((event.getModifiersEx() & ~MouseEvent.BUTTON1_DOWN_MASK) == 0))
                    position.set(event.getX() - relativePos.getX(), event.getY() + relativePos.getY());
            }
            
            @Override
            public void keyPressed(KeyEvent event) {
                super.keyPressed(event);
                
                if(!selecting) return;
                if(selectedBody.getID() == Body.ID_PLAYER) return;
                
                if(event.getKeyCode() == KeyEvent.VK_DELETE) {
                    Paintables.remove(selected);
                    Body.remove(selectedBody);

                    selecting = false;
                    selectedBody = null;
                    selected = null;
                    position = null;
                    relativePos = null;

                    SwingUtilities.invokeLater(new Runnable(){
                        @Override
                        public void run() {
                            LevelEditor.gA.remove((MouseAdapter) gA);
                        }        
                    });
                } else if(event.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    Paintables.remove(selected);
                    
                    selecting = false;
                    selectedBody = null;
                    selected = null;
                    position = null;
                    relativePos = null;
                    
                    AwesomeTimer.addAction(new AwesomeAction() {
                        @Override
                        public void run() {GameFrame.pauseInterference = false;}
                    }, 250, false, TimeUnit.MILLISECONDS);
                    
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
            public void mouseWheelMoved(MouseWheelEvent event) {
                super.mouseWheelMoved(event);
                
                float angle = event.getWheelRotation()*(BASE_ANGLE>Math.PI? (float)-Math.PI/24 : (float)Math.PI);
                BASE_ANGLE += angle;

//                Selector.this.selectedBody.lookAt(angle);
                if(Selector.this.selected instanceof PaintableShape)
                    ((PaintableShape)Selector.this.selected).rotate(angle);
                else
                    ((PaintableImage)Selector.this.selected).rotate(angle);
                
                System.err.println(angle);
                
                System.err.println("Units " + event.getUnitsToScroll());
                System.err.println("Wheel " + event.getWheelRotation());
            }
            
            @Override
            public void mouseReleased(MouseEvent event) {
                super.mouseReleased(event);
                
                if(event.getButton() == MouseEvent.BUTTON1) {
                    Paintables.remove(selected);

                    selectedBody.setPosition(position);
                    selectedBody.lookAt(BASE_ANGLE);
                    
                    selecting = false;
                    selectedBody = null;
                    selected = null;
                    position = null;
                    relativePos = null;
                    
                    AwesomeTimer.addAction(new AwesomeAction() {
                        @Override
                        public void run() {GameFrame.pauseInterference = false;}
                    }, 250, false, TimeUnit.MILLISECONDS);
                    
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
        if(event.getButton() == MouseEvent.BUTTON1) {
            if(infoWindow != null)
                if(infoWindow.isVisible()) {
                    Point relative = event.getPoint();
                    relative.translate(-GameFrame.translateX, GameFrame.translateY);
                    if(!infoWindow.contains(relative)) {
                        GameFrame.paintArea.remove(infoWindow);
                        infoWindow.setVisible(false);
                        infoWindow = null;
                    }
                }
            
            for(int i=Body.getBodies().size()-1;i>-1;i--) {
                Body b = Body.getBodies().get(i);
                if(b.contains(event.getPoint())) {                   
                    position = GameFrame.mousePosition.copy();
                    
                    if(position.getX() < b.getPosition().getX())
                        continue;
                    
                    if(b instanceof Bound || b instanceof Detector) {
                        selected = new PaintableShape((b instanceof Bound)? b.gBA() : b.getBounds(), position); 
                        Paintables.register(selected);
                    } else if(b instanceof Person) { 
                        selected = new PaintableImage(((Person)b).getSprite().getCurrentFrame(),"", position, false);
                        ((PaintableImage)selected).filter(GameSettings.Graphics.Filter.Gray);
                        Paintables.register(selected);
                    } else {
                        System.err.println("Body not supported by Selector yet!");
                        return;
                    }
                    
                    selectedBody = b;
                    selecting = true;

                    float y = selectedBody.getPosition().getY() - position.getY();
                    y += y<0? selectedBody.getHeight() : 0;
                    
                    relativePos = new Vector2D(position.getX() - selectedBody.getPosition().getX(), y);
                    
                    position.set(event.getX() - relativePos.getX(), event.getY() + relativePos.getY());
                    
                    LevelEditor.gA.add((MouseAdapter)gA);
                    LevelEditor.gA.add((KeyListener)gA);
                    
                    GameFrame.pauseInterference = true;
                    
                    return;
                }
            }
        } else if(event.getButton() == MouseEvent.BUTTON3)
            for(int i=Body.getBodies().size()-1;i>-1;i--) {
                Body b = Body.getBodies().get(i);
                if(b.contains(event.getPoint()))
                    if(infoWindow == null || !infoWindow.isVisible()) {
                        infoWindow = new InfoWindow(new Vector2D(200, 100), b);
                        GameFrame.paintArea.add(infoWindow);
                        infoWindow.setVisible(true);
                        
                        return;
                    }
            }
    }
    
    private class InfoWindow extends Window {
        private List<WLabel> labels = new ArrayList<>();
        private TabbedPane infoPane;
        private Body b = null;
        
        private InfoWindow(Vector2D position, Body b) {
            super(position);
            
            this.setSize(new Vector2D(200, 250));
            
            this.b = b;
            
            float x = 0;
            
            WLabel cName = new WLabel(new Vector2D(10, 0), "Class: " + b.getClassName() + " Class");
            WLabel name = new WLabel(new Vector2D(10, x += cName.getHeight()), "Name: " + b.getName());
            InputField foo = new MutableInputField(new Vector2D(10, x += name.getHeight()), new Vector2D(120, 15), b.getPosition());
            foo.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(InfoWindow.this.b != null)
                        if(Vector2D.isVector(e.getActionCommand())) {
                            System.err.println(e.getActionCommand());
                            InfoWindow.this.b.setPosition(Vector2D.parseVector(e.getActionCommand()));
                        }
                }
            });
            
            infoPane = new TabbedPane(this, new Vector2D(10, 100), 
                    new Vector2D(this.getWidth()-20, this.getHeight() - 120));
            
            infoPane.register(new TabbedPane.Tab(infoPane, 20, "Info"), cName, name, foo);
            infoPane.register(new TabbedPane.Tab(infoPane, 20, "Physics"));
            infoPane.register(new TabbedPane.Tab(infoPane, 20, "Storage"));
            infoPane.register(new TabbedPane.Tab(infoPane, 20, "Class"));
            
            this.add(infoPane);
        }
        
        private void setBody(Body b) {this.b = b;}
    }
}
