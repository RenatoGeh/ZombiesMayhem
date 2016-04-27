/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package geometryzombiesmayhem.leveleditor;

import geometryzombiesmayhem.*;
import geometryzombiesmayhem.circuitry.Battery;
import geometryzombiesmayhem.circuitry.Dimmer;
import geometryzombiesmayhem.circuitry.Emitter;
import geometryzombiesmayhem.circuitry.Lever;
import geometryzombiesmayhem.circuitry.NotGate;
import geometryzombiesmayhem.io.StateManager;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author Yan
 */
public class LevelEditor {
    
    public static BaseLevel level;
    public static Brush currentBrush = Selector.getBrush();
    public static Level getLevel() {return level;}
    
    private static ImageButton sideToolbarButton;
    
    public static void start() {
        GameFrame.godMode = true;
        GameFrame.onEditor = true;
        level = new BaseLevel();
        GameFrame.paintArea.addMouseAdapter((MouseAdapter)gA);
        ZM.gameFrame.addKeyListener(gA);
        GameFrame.currentLevel = level;
        level.load();
        
        sideToolbarButton = new ImageButton(AssetManager.loadImage("resources/interface/leveleditor/sidetoolbar_push.png"),
                Vector2D.BOTTOM_RIGHT.subtract(30, 350), 0);
        
        TOOLBAR.setVisible(true);
        GameFrame.paintArea.add(TOOLBAR);
        GameFrame.onEditor = true;
    }
    
    public static MouseEvent fix(MouseEvent e) {
        return new MouseEvent(e.getComponent(),e.getID(),e.getWhen(),e.getModifiers(),e.getX() + GameFrame.translateX,e.getY() - GameFrame.translateY,
                            e.getClickCount(),e.isPopupTrigger(),e.getButton());
    }
    
    public static GeneralAdapter gA = new GeneralAdapter() {

        @Override
        public void keyPressed(KeyEvent event) {
            super.keyPressed(event);
            
            if(event.getKeyCode() == KeyEvent.VK_L)
                currentBrush = Selector.getBrush();
            else if(event.getKeyCode() == KeyEvent.VK_SEMICOLON)
                currentBrush = SimpleBoundBrush.getBrush();
            else if(event.getKeyCode() == KeyEvent.VK_K)
                currentBrush = DetectorBrush.getBrush();
            else if(event.getKeyCode() == KeyEvent.VK_E)
                currentBrush = EmitterBrush.getBrush();
            else if(event.getKeyCode() == KeyEvent.VK_B)
                currentBrush = new SimpleBodyBrush(new Lever());
            else if(event.getKeyCode() == KeyEvent.VK_C)
                currentBrush = LinkBrush.getBrush();
            else if(event.isControlDown() && event.getKeyCode() == KeyEvent.VK_Z)
                LevelEditor.currentBrush.undo();
            else if(event.isControlDown() && event.getKeyCode() == KeyEvent.VK_Y) {System.err.println("calling");
                LevelEditor.currentBrush.redo();}
            else if(event.getKeyCode() == KeyEvent.VK_P)
                GameFrame.velocity = GameFrame.velocity==0?1:0;
            else
                return;
            
            TOOLBAR.update();
        }
        
        @Override
        public void mousePressed(MouseEvent event) {
            event = fix(event);
            super.mousePressed(event);
            
            if (event.getButton() == MouseEvent.BUTTON2) {
                Player.getDefaultPlayer().Vx = Player.getDefaultPlayer().Vy = 0;
                Console.getConsole().interpretCmd("tp " + event.getX() +  " " + event.getY());
                return;
            }
            if(!GameFrame.pauseInterference && !GameFrame.isPaused) currentBrush.start(event);
        }
       

        @Override
        public void mouseDragged(MouseEvent event) {
            event = fix(event);
            super.mouseDragged(event);
        }

        @Override
        public void mouseReleased(MouseEvent event) {
            event = fix(event);
            super.mouseReleased(event);
            
            if(sideToolbarButton.isEnabled())
                if(sideToolbarButton.getShape().contains(Vector2D.convertToPoint(GameFrame.mousePosition.subtract(GameFrame.translateX, -GameFrame.translateY)))) {
                    SIDE_TOOLBAR.setVisible(true);
                    GameFrame.paintArea.add(SIDE_TOOLBAR);
                    Paintables.removeInterface(sideToolbarButton);
                    sideToolbarButton.setEnabled(false);
                    GameFrame.pauseInterference = true;
                }
        }
        
        @Override
        public void mouseMoved(MouseEvent event) {
            if(event.getPoint().getY() < TOOLBAR.getY() + TOOLBAR.getHeight() + 10) {
                if(!TOOLBAR.isVisible()) {
                    TOOLBAR.setVisible(true);
                    GameFrame.paintArea.add(TOOLBAR);
                    TOOLBAR.switchSection(LevelEditor.current);
                    GameFrame.pauseInterference = true;
                }
            } else if(TOOLBAR.isVisible()) {
                TOOLBAR.setVisible(false);
                GameFrame.paintArea.remove(TOOLBAR);
                GameFrame.pauseInterference = false;
            }
            
            if(!SIDE_TOOLBAR.contains(event.getPoint()))
                if(SIDE_TOOLBAR.isVisible()) {
                    SIDE_TOOLBAR.setVisible(false);
                    GameFrame.paintArea.remove(SIDE_TOOLBAR);
                    Paintables.registerInterface(sideToolbarButton);
                    sideToolbarButton.setEnabled(true);
                    if(!TOOLBAR.isVisible())
                        GameFrame.pauseInterference = false;
                }
            
            if(sideToolbarButton.getShape().contains(Vector2D.convertToPoint(GameFrame.mousePosition.subtract(GameFrame.translateX, -GameFrame.translateY))))
                GameFrame.pauseInterference = true;
        }
    };
    
    private static enum Section {BOUNDS, UNITS, LIGHTS, CIRCUITRY, PROPERTIES, DIALOGS};
    private static Section current = Section.BOUNDS;
    
   
    
    private static final ToolbarWindow TOOLBAR = new ToolbarWindow();
    private static class ToolbarWindow extends Window {
//    private static final Window TOOLBAR = new Window(new Vector2D(0, 0)) {
        private Map<Section, List<ImageButton>> section = new EnumMap<>(Section.class);
        private List<ImageButton> fixed = new ArrayList<>();
        private List<ImageButton> currentList;
        
        private RoundRectangle2D selectedFrame;
        private boolean isInteractible = true;
        
        public ToolbarWindow() {super(new Vector2D());}
        
        private void nextSection() {
            Section[] values = Section.values();
            Section res = null;
            
            for(int i=0;i<values.length;i++) {
                if(current == values[values.length-1]) {
                    res = values[0];
                    break;
                }
                if(current == values[i])
                    res = values[i+1];
            }
            
            this.switchSection(res);
        }
        
        private void previousSection() {
            Section[] values = Section.values();
            Section res = null;
            
            for(int i=0;i<values.length;i++) {
                if(current == values[0]) {
                    res = values[values.length-1];
                    break;
                }
                if(i != values.length-1) {
                    if(values[i+1] == current)
                        res = values[i];
                } else if(res == null)
                    res = values[0];
            }
            
            this.switchSection(res);
        }
        
        private void switchSection(Section s) {
            for(ImageButton ib : currentList)
                this.remove(ib);
            
            this.currentList = section.get(s);
            LevelEditor.current = s;
            
            for(ImageButton ib : currentList)
                this.add(ib);
            
            if(!s.equals(Section.BOUNDS))
                SIDE_TOOLBAR.switchSection(s);
        }
        
        private ActionListener buttonListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                if(!isInteractible) return;
                if(!((ImageButton)event.getSource()).isEnabled()) return;
               
                int index = ((ImageButton)event.getSource()).getID();
 
                if(!(index == ID_CATEGORY_UP || index == ID_CATEGORY_DOWN)) {
                    ImageButton e = currentList.get(index);
                    selectedFrame = new RoundRectangle2D.Float(getPosition().getX() + e.getPosition().getX(), 
                            getPosition().getY() + e.getPosition().getY(), e.getWidth(), e.getHeight(), 10, 10);
                } else
                    selectedFrame = null;
                
                
                if(index==ID_CATEGORY_UP)
                    nextSection();
                if(index==ID_CATEGORY_DOWN)
                    previousSection();

                if(currentBrush instanceof Typeable) ((Typeable)currentBrush).closeStream();
                if(current == Section.BOUNDS)
                    switch(index) {
                        case ID_BRUSH_SELECTOR:
                            currentBrush = Selector.getBrush();
                        break;
                        case ID_BRUSH_SIMPLE_BOUNDS:
                            currentBrush = SimpleBoundBrush.getBrush();
                        break;
                        case ID_BRUSH_DETECTOR:
                            currentBrush = DetectorBrush.getBrush();
                        break;
                        case ID_BRUSH_SIMPLE_BODY:
                            currentBrush = new SimpleBodyBrush(new Lever());
                        break;
                    }
                else if(current == Section.UNITS)
                    switch(index) {
                        case ID_UNIT_ADD:
                            currentBrush = UnitBrush.getBrush();
                        break;
                        case ID_UNIT_EDIT:
                        break;
                        case ID_UNIT_REMOVE:
                    }
                else if(current == Section.LIGHTS)
                    switch(index) {
                        case ID_LIGHT_ADD:
                            currentBrush = LightBrush.getBrush();
                        break;
                        case ID_LIGHT_EDIT:
                        break;
                        case ID_LIGHT_REMOVE:
                        break;
                    }
                else if(current == Section.CIRCUITRY)
                    switch(index) {
                        case ID_CIRCUITRY_ADD:
                            currentBrush = CircuitryBrush.getBrush();
                        break;
                        case ID_CIRCUITRY_EDIT:
                        break;
                        case ID_CIRCUITRY_REMOVE:
                        break;
                        case ID_CIRCUITRY_LINK:
                            currentBrush = LinkBrush.getBrush();
                        break;
                    }
                else if(current == Section.PROPERTIES)
                    switch(index) {
                        case ID_SAVE:
                            StateManager.saveLevel();
                        break;
                        case ID_IMPORT:
                            StateManager.loadLevel();
                        break;
                        case ID_SCENE:
                            if(!SceneFrame.inUse)
                                SceneFrame.activate();
                        break;
                        case ID_PLAYER:
                        break;
                        case ID_ADVANCED:
                        break;
                    }
                else if(current == Section.DIALOGS)
                    switch(index) {
                        case ID_DIALOG_ADD:
                            currentBrush = DialogBrush.getBrush();
                        break;
                        case ID_DIALOG_EDIT:
                        break;
                        case ID_DIALOG_REMOVE:
                        break;
                    }
                
                if(index == ID_CATEGORY_UP || index == ID_CATEGORY_DOWN)
                    if(current == Section.UNITS || current == Section.LIGHTS || current == Section.CIRCUITRY) {
                        Paintables.registerInterface(sideToolbarButton);
                        sideToolbarButton.setEnabled(true);
                    } else if(sideToolbarButton.isEnabled()) {
                        Paintables.removeInterface(sideToolbarButton);
                        sideToolbarButton.setEnabled(false);
                        UnitBrush.close();
                    }
            }
        };
        
        {
            for(Section s : Section.values())
                section.put(s, new ArrayList<ImageButton>());
            currentList = section.get(Section.BOUNDS);
            
            this.setSize(new Vector2D(GameFrame.FRAME_WIDTH, 50));
            this.setColor(Color.LIGHT_GRAY);
            init();
        }
        
        private void init() {            
            
            
            {   //BOUNDS
                float x = 50;
                
                ImageButton selectorButton = new ImageButton(AssetManager.loadImage("resources/interface/leveleditor/toolbar/selector_icon.png"), 
                        new Vector2D(x, 10), ID_BRUSH_SELECTOR);
                ImageButton simpleBoundsButton = new ImageButton(AssetManager.loadImage("resources/interface/leveleditor/toolbar/simple_bounds_icon.png"),
                        new Vector2D(x += selectorButton.getWidth() + 100, 10), ID_BRUSH_SIMPLE_BOUNDS);
                ImageButton detectorButton = new ImageButton(AssetManager.loadImage("resources/interface/leveleditor/toolbar/detector_icon.png"), 
                        new Vector2D(x += simpleBoundsButton.getWidth() + 100, 10), ID_BRUSH_DETECTOR);
                ImageButton simpleBodyButton = new ImageButton(AssetManager.loadImage("resources/interface/leveleditor/toolbar/simple_body.png"),
                        new Vector2D(x += detectorButton.getWidth() + 100, 10), ID_BRUSH_SIMPLE_BODY);
                
                selectorButton.setTooltip(new Tooltip("Selector: Allows the selected body" + "\n" + 
                    "to be moved or rotated.\nHotkey: 'L'.", Color.GRAY, 200, 0));
                simpleBoundsButton.setTooltip(new Tooltip("Simple Bounds Brush: Creates a " + "\n" + 
                        "single Bound class line.\nHotkey: ';'.", Color.GRAY, 200, 0));
                detectorButton.setTooltip(new Tooltip("Detector Brush: Creates a " + "\n" + 
                        "square-shaped Detector class zone.\nHotkey: 'K'.", Color.GRAY, 200, 0));
                simpleBodyButton.setTooltip(new Tooltip("Simple Body Brush: Instantaneously create a simple version of a pre-coded body",
                        Color.GRAY, 200, 0));
                
                currentList.add(selectorButton);
                currentList.add(simpleBoundsButton);
                currentList.add(detectorButton);
                currentList.add(simpleBodyButton);
                
                for(ImageButton ib : currentList) {
                    ib.addActionListener(buttonListener);
                    this.add(ib);
                }
            }
            
            {   //UNITS
                float x = 50;
                
                ImageButton add = new ImageButton(AssetManager.loadImage("resources/interface/leveleditor/toolbar/add_unit.png"),
                        new Vector2D(x, 10), ID_UNIT_ADD);
                ImageButton edit = new ImageButton(AssetManager.loadImage("resources/interface/leveleditor/toolbar/edit_unit.png"),
                        new Vector2D(x += add.getWidth() + 100, 10), ID_UNIT_EDIT);
                ImageButton remove = new ImageButton(AssetManager.loadImage("resources/interface/leveleditor/toolbar/remove_unit.png"),
                        new Vector2D(x += edit.getWidth() + 100, 10), ID_UNIT_REMOVE);
                
                add.setTooltip(new Tooltip("Add unit: Adds a selected type of unit to the scene world.\n" +
                        "Use the side toolbar to customize the preferred settings of the selected unit.", Color.GRAY, 200, 0));
                edit.setTooltip(new Tooltip("Edit unit: Edits an already existent unit from the game world.\n" +
                        "Use the side toolbar to customize the preferred settings of the selected unit.", Color.GRAY, 200, 0));
                remove.setTooltip(new Tooltip("Remove unit: Permanently removes an already existent unit from the game world.",
                        Color.GRAY, 200, 0));
                
                section.get(Section.UNITS).add(add);
                section.get(Section.UNITS).add(edit);
                section.get(Section.UNITS).add(remove);
                
                for(ImageButton ib : section.get(Section.UNITS))
                    ib.addActionListener(buttonListener);
            }
            
            {   //LIGHTS
                float x = 50;
                
                ImageButton add = new ImageButton(AssetManager.loadImage("resources/interface/leveleditor/toolbar/add_light.png"),
                        new Vector2D(x, 10), ID_LIGHT_ADD);
                ImageButton edit = new ImageButton(AssetManager.loadImage("resources/interface/leveleditor/toolbar/edit_light.png"),
                        new Vector2D(x+=add.getWidth() + 100, 10), ID_LIGHT_EDIT);
                ImageButton remove = new ImageButton(AssetManager.loadImage("resources/interface/leveleditor/toolbar/remove_light.png"),
                        new Vector2D(x+=edit.getWidth() + 100, 10), ID_LIGHT_REMOVE);
            
                add.setTooltip(new Tooltip("Add light source: Adds a selected type of light source to the scene world.\n" + 
                        "Use the side toolbar to customize the preferred settings of the selected light source.", Color.GRAY, 200, 0));
                edit.setTooltip(new Tooltip("Edit light source: Edits an already existent light source from the game world.\n" + 
                        "Use the side toolbar to customize the preferred settings of the selected light source.", Color.GRAY, 200, 0));
                remove.setTooltip(new Tooltip("Removes light source: permanently removes an already existent light source from the game world.",
                        Color.GRAY, 200, 0));
                
                section.get(Section.LIGHTS).add(add);
                section.get(Section.LIGHTS).add(edit);
                section.get(Section.LIGHTS).add(remove);
                
                for(ImageButton ib : section.get(Section.LIGHTS))
                    ib.addActionListener(buttonListener);
            }
            
            {   //CIRCUITRY
                float x = 50;
                
                ImageButton add = new ImageButton(AssetManager.loadImage("resources/interface/leveleditor/toolbar/add_circuitry.png"),
                        new Vector2D(x, 10), ID_CIRCUITRY_ADD);
                ImageButton edit = new ImageButton(AssetManager.loadImage("resources/interface/leveleditor/toolbar/edit_circuitry.png"),
                        new Vector2D(x+=add.getWidth() + 100, 10), ID_CIRCUITRY_EDIT);
                ImageButton remove = new ImageButton(AssetManager.loadImage("resources/interface/leveleditor/toolbar/remove_circuitry.png"),
                        new Vector2D(x+=edit.getWidth() + 100, 10), ID_CIRCUITRY_REMOVE);
                ImageButton link = new ImageButton(AssetManager.loadImage("resources/interface/leveleditor/toolbar/link_circuitry.png"),
                        new Vector2D(x+=remove.getWidth() + 100, 10), ID_CIRCUITRY_LINK);
                
                add.setTooltip(new Tooltip("Add circuitry component: Adds a selected type of circuitry component to the scene world.\n" + 
                        "Use the side toolbar to customize the preferred settings of the selected circuitry component.", Color.GRAY, 200, 0));
                edit.setTooltip(new Tooltip("Edit circuitry component: Edits an already existent circuitry component from the game world.\n" + 
                        "Use the side toolbar to customize the preferred settings of the selected circuitry component.", Color.GRAY, 200, 0));
                remove.setTooltip(new Tooltip("Removes circuitry component: permanently removes an already existent circuitry component from the game world.",
                        Color.GRAY, 200, 0));
                link.setTooltip(new Tooltip("Link circuitry components: Links one circuitry component to another.\n"
                        + "Drag mouse from Output component to Input. This will cause the Output to send voltage to the Input selected.", Color.GRAY, 200, 0));
                
                section.get(Section.CIRCUITRY).add(add);
                section.get(Section.CIRCUITRY).add(edit);
                section.get(Section.CIRCUITRY).add(remove);
                section.get(Section.CIRCUITRY).add(link);
                
                for(ImageButton ib : section.get(Section.CIRCUITRY))
                    ib.addActionListener(buttonListener);
            }
            
            {   //Properties
                float x = 50;
                
                ImageButton scene = new ImageButton(AssetManager.loadImage("resources/interface/leveleditor/toolbar/scene.png"), 
                        new Vector2D(x, 10), ID_SCENE);
                ImageButton save = new ImageButton(AssetManager.loadImage("resources/interface/leveleditor/toolbar/save.png"),
                        new Vector2D(x+=scene.getWidth()+100, 10), ID_SAVE);
                ImageButton imp = new ImageButton(AssetManager.loadImage("resources/interface/leveleditor/toolbar/import.png"),
                        new Vector2D(x+=save.getWidth()+100, 10), ID_IMPORT);
                ImageButton player = new ImageButton(AssetManager.loadImage("resources/interface/leveleditor/toolbar/player.png"),
                        new Vector2D(x+=imp.getWidth()+100, 10), ID_PLAYER);
                ImageButton advanced = new ImageButton(AssetManager.loadImage("resources/interface/leveleditor/toolbar/advanced.png"),
                        new Vector2D(x+=player.getWidth()+100, 10), ID_ADVANCED);
                
                scene.setTooltip(new Tooltip("Edit Scene properties: Add, edit, remove the Level's Scene Layers.\n"
                        + "A Scene is composed of Scene Layers, having each its own properties.", Color.GRAY, 200, 0));
                save.setTooltip(new Tooltip("Save Level: Save the current level to the given path.", Color.GRAY, 200, 0));
                imp.setTooltip(new Tooltip("Import Level: Import a level to the Level Editor.", Color.GRAY, 200, 0));
                player.setTooltip(new Tooltip("Player Advanced Options: Edit the player's options, such as inventory, name and sprite.", Color.GRAY, 200, 0));
                advanced.setTooltip(new Tooltip("Advanced Level Options:\nAccess advanced options \nfor the current Level.", Color.GRAY, 200, 0));
                
                section.get(Section.PROPERTIES).add(scene);
                section.get(Section.PROPERTIES).add(save);
                section.get(Section.PROPERTIES).add(imp);
                section.get(Section.PROPERTIES).add(player);
                section.get(Section.PROPERTIES).add(advanced);
                
                for(ImageButton ib : section.get(Section.PROPERTIES))
                    ib.addActionListener(buttonListener);
            }
            
            {   //DIALOGS
                float x = 50;
                
                ImageButton add = new ImageButton(AssetManager.loadImage("resources/interface/leveleditor/toolbar/add_dialog.png"),
                        new Vector2D(x, 10), ID_DIALOG_ADD);
                ImageButton edit = new ImageButton(AssetManager.loadImage("resources/interface/leveleditor/toolbar/edit_dialog.png"),
                        new Vector2D(x+=add.getWidth()+100, 10), ID_DIALOG_EDIT);
                ImageButton remove = new ImageButton(AssetManager.loadImage("resources/interface/leveleditor/toolbar/remove_dialog.png"),
                        new Vector2D(x+=edit.getWidth()+100, 10), ID_DIALOG_REMOVE);
                
                add.setTooltip(new Tooltip("Add a Dialog: create a single Dialog window.", Color.GRAY, 200, 0));
                edit.setTooltip(new Tooltip("Edit a Dialog: edit a Dialog.", Color.GRAY, 200, 0));
                remove.setTooltip(new Tooltip("Remove a Dialog: remove a Dialog.", Color.GRAY, 200, 0));
                
                section.get(Section.DIALOGS).add(add);
                section.get(Section.DIALOGS).add(edit);
                section.get(Section.DIALOGS).add(remove);
                
                for(ImageButton ib : section.get(Section.DIALOGS))
                    ib.addActionListener(buttonListener);
            }
            
            {   //FIXED
                ImageButton upButton = new ImageButton(AssetManager.loadImage("resources/interface/leveleditor/toolbar/up_category.png"),
                        new Vector2D(15, 10), ID_CATEGORY_UP);
                ImageButton downButton = new ImageButton(AssetManager.loadImage("resources/interface/leveleditor/toolbar/down_category.png"),
                        new Vector2D(15, 25), ID_CATEGORY_DOWN);
                
                fixed.add(upButton);
                fixed.add(downButton);

                for(ImageButton ib : fixed) {
                    ib.addActionListener(buttonListener);
                    this.add(ib);
                }
            }

            this.update();
            this.setDraggeable(false);
        }
        
        private final int ID_BRUSH_SELECTOR = 0;
        private final int ID_BRUSH_SIMPLE_BOUNDS = 1;
        private final int ID_BRUSH_DETECTOR = 2;
        private final int ID_BRUSH_SIMPLE_BODY = 3;
        
        private final int ID_UNIT_ADD = 0;
        private final int ID_UNIT_EDIT = 1;
        private final int ID_UNIT_REMOVE = 2;
        
        private final int ID_LIGHT_ADD = 0;
        private final int ID_LIGHT_EDIT = 1;
        private final int ID_LIGHT_REMOVE = 2;
        
        private final int ID_CIRCUITRY_ADD = 0;
        private final int ID_CIRCUITRY_EDIT = 1;
        private final int ID_CIRCUITRY_REMOVE = 2;
        private final int ID_CIRCUITRY_LINK = 3;
        
        private final int ID_SCENE = 0;
        private final int ID_SAVE = 1;
        private final int ID_IMPORT = 2;
        private final int ID_PLAYER = 3;
        private final int ID_ADVANCED = 4;
        
        private final int ID_DIALOG_ADD = 0;
        private final int ID_DIALOG_EDIT = 1;
        private final int ID_DIALOG_REMOVE = 2;
        
        private final int ID_CATEGORY_UP = 100;
        private final int ID_CATEGORY_DOWN = 101;
        
        @Override
        public void paint(Graphics2D g2, Component c) {
            super.paint(g2, c);
            
            Color color = g2.getColor();
            Stroke s = g2.getStroke();
            
            g2.setColor(Color.BLUE);
            g2.setStroke(DEFAULT_FRAME_STROKE);
            
            if(selectedFrame != null)
                g2.draw(selectedFrame);
            
            g2.setColor(color);
            g2.setStroke(s);
        }

        @Override
        public Rectangle getBounds() {return this.selectedFrame.getBounds();}
        
        private final Stroke DEFAULT_FRAME_STROKE = new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER);
        
        public void update() {
            Vector2D pos = currentBrush instanceof Selector? currentList.get(ID_BRUSH_SELECTOR).getPosition() :
                    currentBrush instanceof SimpleBoundBrush? currentList.get(ID_BRUSH_SIMPLE_BOUNDS).getPosition() :
                    currentBrush instanceof DetectorBrush? currentList.get(ID_BRUSH_DETECTOR).getPosition() : 
                    new Vector2D(0, 0);
            
            selectedFrame = new RoundRectangle2D.Float(this.getPosition().getX() + pos.getX(), 
                    this.getPosition().getY() + pos.getY(), currentList.get(ID_BRUSH_SELECTOR).getWidth(), 
                    currentList.get(ID_BRUSH_SELECTOR).getHeight(), 10, 10);
        }
    };
    
    private static SideWindow SIDE_TOOLBAR = new SideWindow();
    private static class SideWindow extends Window {
        private SideWindow() {
            super(new Vector2D(Vector2D.TOP_RIGHT).subtract(350, -TOOLBAR.getHeight() -10));
        }
        
        private static final int TYPE_SELECTOR_ID = 0;
        
        private static final int MASS_INPUT_ID = 1;
        private static final int RANGE_INPUT_ID = 2;
        private static final int BPERIODS_INPUT_ID = 3;
        private static final int BLINKS_TICK_ID = 4;
        private static final int CIRCUITRY_LIGHT_TICK_ID = 5;
        private static final int VOLTAGE_INPUT_ID = 6;
        private static final int AMBIENT_DARKNESS_INPUT_ID = 7;
        private static final int ADVANCED_PROPERTIES_ID = 1001;
        
        private float mass = 100;
        private float range = 100;
        private float voltage = 1;
        private float trapMinBase = 50, trapMaxBase = 100, trapFlareWidth = 50;
        PanelP batlev = new PanelP();
        PanelP emit = new PanelP();
        ComponentP p = null;
        
        private Map<Section, List<ComponentP>> components = new EnumMap<>(Section.class);
        private List<ComponentP> currentList;
        
        private ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {System.err.println(event.getID());
                if(LevelEditor.current == Section.UNITS)
                    switch(event.getID()) {
                        case MASS_INPUT_ID:
                            mass = Float.parseFloat(event.getActionCommand());
                            if(UnitBrush.isEnabled())
                                UnitBrush.getSelectedUnit().setMass(mass);       
                        break;
                        case TYPE_SELECTOR_ID:
                            UnitBrush.close();
                            switch(event.getActionCommand()) {
                                case "Zombie":
                                    UnitBrush.setBodyType(applySettings(new Zombie("Zombie", GameFrame.mousePosition, Zombie.getDefaultSprite(), mass)));
                                break;
                                case "Player":
                                    System.err.println("Nothing to see. Keep moving citizen.");
                                    return;
//                                break;
                                case "Health Pack":
                                    UnitBrush.setBodyType(applySettings(Item.createHealthItem(GameFrame.mousePosition)));
                                break;
                                case "Ammo Crate":
                                    UnitBrush.setBodyType(applySettings(Item.createAmmoItem(GameFrame.mousePosition, ProjectileType.BULLET)));
                                break;
                                case "Crawler":
                                    UnitBrush.setBodyType(applySettings(new Crawler("Crawler", GameFrame.mousePosition, Crawler.getDefaultIdleSprite(), mass)));
                                break;
                                case "Flashlight":
                                    UnitBrush.setBodyType(applySettings(Item.createFlashlightItem(GameFrame.mousePosition)));
                                break;
                                default: UnitBrush.close(); return;
                            }
                            UnitBrush.open();
                        break;
                    }
                else if(LevelEditor.current == Section.LIGHTS)
                    switch(event.getID()) {
                        case TYPE_SELECTOR_ID:
                            LightBrush.close();
                            switch(event.getActionCommand()) {
                                case "Conical":
                                    LightBrush.setLightType(new FlareShape.Conical(GameFrame.mousePosition, GameFrame.mousePosition.add(0, 100), range, (float)Math.PI/12, LightManager.getColor()));
                                break;
                                case "Elliptic":
                                    LightBrush.setLightType(new FlareShape.Elliptic(GameFrame.mousePosition, new Vector2D(100, 100), 100, 100, 0, ColorFactory.getColor(LightManager.getColor(), 0)));
                                break;
                                case "Polygonal":
                                    LightBrush.setLightType(new FlareShape.Polygonal(new Vector2D[] {new Vector2D(0, 0)}, 0, 0, 0, LightManager.getColor()));
                                break;
                                case "Trapezoid":
                                    LightBrush.setLightType(new FlareShape.Trapezoid(GameFrame.mousePosition, GameFrame.mousePosition.add(0, 100), trapMinBase, trapMaxBase, range, (int)trapFlareWidth, LightManager.getColor()));
                                break;
                                default: LightBrush.close(); return;
                            }
                            LightBrush.open();
                        break;
                        case RANGE_INPUT_ID:
                            range = Float.parseFloat(event.getActionCommand());
                        break;
                        case BPERIODS_INPUT_ID:
                            String command = event.getActionCommand();
                            if(!command.matches("([0-9]+?,? ?)+")) return;
                            String[] parts = command.contains(",")?command.split(","):command.split(" ");
                            long[] periods = new long[parts.length];
                            for(int i=0;i<parts.length;i++) {periods[i]=Long.parseLong(parts[i].trim());}
                            ((FlareShape)LightBrush.getSelectedLight()).setPeriods(periods);
                        break;
                        case BLINKS_TICK_ID:
                            List<ComponentP> list = components.get(Section.LIGHTS);
                            InputField periodsField = ((InputField)list.get((list.size()-1)-BPERIODS_INPUT_ID));
                            if(event.getActionCommand().equals("Enabled")) {
                                periodsField.setEnabled(true);
                                ((FlareShape)LightBrush.getSelectedLight()).setBlinking(true);
                            } else {
                                periodsField.setEnabled(false);
                                ((FlareShape)LightBrush.getSelectedLight()).setBlinking(false);
                            }
                        break;
                        case CIRCUITRY_LIGHT_TICK_ID:
                            LightBrush.setCircuitEnabled(event.getActionCommand().equals("Enabled"));
                        break;
                        case VOLTAGE_INPUT_ID:
                            if(!event.getActionCommand().matches("[0-1]\\.?[0-9]+")) return;
                                if(LightBrush.getSelectedLight()!=null)
                                    LightBrush.getSelectedLight().updateVoltage(null, Float.valueOf(event.getActionCommand()));
                        break;
                        case AMBIENT_DARKNESS_INPUT_ID:
                            String com = event.getActionCommand().trim();
                            if(!com.matches("[0-9]+")) return;
                            int dark = Integer.parseInt(com);
                            System.err.println("Old: " + LightManager.getDarkness() + "\tNew: " + dark);
                            LightManager.setDarkness(dark<=255&&dark>=0?dark:LightManager.getDarkness());
                        break;
                        case ADVANCED_PROPERTIES_ID:
                            new JFrame("Advanced Lighting Properties") {
                                private static final String FLOATER_PATTERN = "[0-9]\\.?[0-9]+";
                                JButton minTrap, maxTrap, refWidthTrap, back;
                                JFrame self;
                                ActionListener frameListener = new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                        if(e.getSource() == minTrap) {
                                            String res = JOptionPane.showInputDialog(self, 
                                                    "Input the size of the Trapezoid Light Source's minimum base.\n"
                                                    + "That is, the base line where the origin point of the trapezium is located at.", 
                                                    "Trapezoid Minimum Base Width", JOptionPane.QUESTION_MESSAGE);
                                            if(res==null) return;
                                            if(!res.matches(FLOATER_PATTERN)) return;
                                            SideWindow.this.trapMinBase = Float.parseFloat(res);
                                        } else if(e.getSource() == maxTrap) {
                                            String res = JOptionPane.showInputDialog(self,
                                                    "Input the size of the Trapezoid Light Source's maximum base.\n"
                                                    + "That is, the base line where the origin point is added to the width of the\n"
                                                    + "trapezium according to the angle the shape is rotated.", 
                                                    "Trapezoid Maximum Base Width", JOptionPane.QUESTION_MESSAGE);
                                            if(res==null) return;
                                            if(!res.matches(FLOATER_PATTERN)) return;
                                            SideWindow.this.trapMaxBase = Float.parseFloat(res);
                                        } else if(e.getSource() == refWidthTrap) {
                                            String res = JOptionPane.showInputDialog(self,
                                                    "Input the width of the Trapezoid's reference point's distance.\n"
                                                    + "That is, the distance between the medium point of the maximum base\n"
                                                    + "and the point where the light starts fading out disregarding\n"
                                                    + "the trapezium's angle. It is also known as \"Flare Width\".",
                                                    "Trapezoid Reference Width", JOptionPane.QUESTION_MESSAGE);
                                            if(res==null) return;
                                            if(!res.matches(FLOATER_PATTERN)) return;
                                            SideWindow.this.trapFlareWidth = Float.parseFloat(res);
                                        }
                                    }
                                };
                                {
                                    this.setLocation(ZM.gameFrame.getLocation().x + ZM.gameFrame.getWidth()/2 - 100, 
                                            ZM.gameFrame.getLocation().y + ZM.gameFrame.getHeight()/2 - 150);
                                    this.setSize(200, 300);
                                    this.setEnabled(true);
                                    this.setVisible(true);
                                    this.setAlwaysOnTop(true);
                                    GameFrame.pauseInterference = true;
                                    JPanel panel = new JPanel();
                                    minTrap = new JButton("Trapezoid Min Base");
                                    minTrap.setSize(200, 75);
                                    maxTrap = new JButton("Trapezoid Max Base");
                                    maxTrap.setSize(200, 75);
                                    refWidthTrap = new JButton("Trapezoid Reference Width");
                                    refWidthTrap.setSize(200, 75);
                                    back = new JButton("Back to Level Editor");
                                    back.setSize(200, 75);
                                    minTrap.addActionListener(frameListener);
                                    maxTrap.addActionListener(frameListener);
                                    refWidthTrap.addActionListener(frameListener);
                                    back.addActionListener(frameListener);
                                    panel.add(minTrap);
                                    panel.add(maxTrap);
                                    panel.add(refWidthTrap);
                                    this.add(panel);
                                    self = this;
                                }
                            };
                        break;
                    }
                else if(LevelEditor.current == Section.CIRCUITRY)
                    switch(event.getID()) {
                        case TYPE_SELECTOR_ID:
                            CircuitryBrush.close();
                            switch(event.getActionCommand()) {
                                case "Lever":
                                    CircuitryBrush.setComponentType(new Lever(voltage,1,Vector2D.ZERO.copy()));
                                break;
                                case "Dimmer":
                                    CircuitryBrush.setComponentType(new Dimmer());
                                break;
                                case "Emitter":
                                    CircuitryBrush.setComponentType(new Emitter());
                                break;
                                case "Battery":
                                    CircuitryBrush.setComponentType(new Battery(voltage,Vector2D.ZERO.copy()));
                                    addToSection(Section.CIRCUITRY,batlev);
                                    SideWindow.this.add(batlev);
                                break;
                                case "NotGate":
                                    CircuitryBrush.setComponentType(new NotGate());
                                break;
                                default: CircuitryBrush.close(); return;
                            }
                            CircuitryBrush.open();
                        break;
                        case VOLTAGE_INPUT_ID:
                            if(!event.getActionCommand().matches("[0-1]\\.?[0-9]+")) return;
                            voltage = Float.valueOf(event.getActionCommand());
                        break;
                    }
            }
        };
        
        private LightSource applySettings(LightSource e) {
            e.getIntensity().setRange((int)range);
            return e;
        }
        
        private Body applySettings(Body e) {
            e.setMass(mass);
            return e;
        }
        
        private void switchSection(Section s) {
            for(ComponentP ib : currentList)
                this.remove(ib);
            
            this.currentList = components.get(s);

            for(ComponentP ib : currentList)
                this.add(ib);
        }
        
        {        
            LightManager.setDarkness(0);
            this.setSize(new Vector2D(350, Vector2D.BOTTOM_RIGHT.getY()-TOOLBAR.getHeight()-20));
            this.setDraggeable(false);
             
            for(Section s : Section.values())
                if(!s.equals(Section.BOUNDS))
                    components.put(s, new ArrayList<ComponentP>());
            currentList = components.get(Section.UNITS);
            
            {   //UNITS
                float y = 50;
                Font baseFont = new Font(Font.DIALOG, Font.PLAIN, 20);

                geometryzombiesmayhem.Selector typeSelector = new geometryzombiesmayhem.Selector("Unit Type:", new Vector2D(50, y+=50), TYPE_SELECTOR_ID, 
                        "<None>", "Zombie", "Player", "Health Pack", "Ammo Crate", "Crawler", "Flashlight");
                typeSelector.setColor(Color.GRAY);
                typeSelector.setTooltip(new Tooltip("Sets the Unit's type.", Color.GRAY, 200, 0));
                typeSelector.addActionListener(actionListener);

                WLabel massLabel = new WLabel(new Vector2D(50, y+=50), baseFont, "Unit's Mass:");
                InputField massField = new InputField(new Vector2D(160, y+5), new Vector2D(100, 30), String.valueOf(mass), MASS_INPUT_ID);
                massField.addActionListener(actionListener);

                WLabel speedLabel = new WLabel(new Vector2D(50, y+=50), baseFont, "");

                this.currentList.add(massLabel);
                this.currentList.add(massField);
                this.currentList.add(typeSelector);
            }
            
            {   //LIGHTS
                float y = 50;
                Font baseFont = new Font(Font.DIALOG, Font.PLAIN, 20);
                
                geometryzombiesmayhem.Selector typeSelector = new geometryzombiesmayhem.Selector("Light Type:", new Vector2D(50, y+=50), TYPE_SELECTOR_ID, 
                        "<None>", "Elliptic", "Conical", "Polygonal", "Trapezoid");
                typeSelector.setColor(Color.GRAY);
                typeSelector.addActionListener(actionListener);
                
                WLabel rangeLabel = new WLabel(new Vector2D(50, y+=50), baseFont, "Light's Range:");
                InputField rangeField = new InputField(new Vector2D(190, y+5), new Vector2D(100, 30), String.valueOf(range), RANGE_INPUT_ID);
                rangeField.addActionListener(actionListener);
                
                WLabel blinkEnabledLabel = new WLabel(new Vector2D(50, y+=50), baseFont, "Blink Status:");
                TickGroup blinkEnabledTick = new TickGroup.Single(this, BLINKS_TICK_ID);
                blinkEnabledTick.add(new Vector2D(190, y+15), "Enabled");
                blinkEnabledTick.add(new Vector2D(190, y+=40), "Disabled", true);
                blinkEnabledTick.addActionListener(actionListener);
                
                WLabel periodsLabel = new WLabel(new Vector2D(50, y+=50), baseFont, "Blink Periods:");
                InputField periodsField = new InputField(new Vector2D(190, y+5), new Vector2D(100, 30), String.valueOf(0), BPERIODS_INPUT_ID);
                periodsField.setEnabled(false);
                periodsField.addActionListener(actionListener);
                
                WLabel circuitryEnabledLabel = new WLabel(new Vector2D(50, y+=50), baseFont, "Circuitry Status:");
                TickGroup circuitryEnabledTick = new TickGroup.Single(this, CIRCUITRY_LIGHT_TICK_ID);
                circuitryEnabledTick.add(new Vector2D(190, y+15), "Enabled");
                circuitryEnabledTick.add(new Vector2D(190, y+=40), "Disabled", true);
                circuitryEnabledTick.addActionListener(actionListener);
                
                WLabel voltageLabel = new WLabel(new Vector2D(50, y+=20), baseFont, "Voltage:");
                InputField voltageField = new InputField(new Vector2D(150, y+5), new Vector2D(100, 30), String.valueOf(1.0f), VOLTAGE_INPUT_ID);
                voltageField.addActionListener(actionListener);
                
                WLabel ambientLabel = new WLabel(new Vector2D(50, y+=50), baseFont, "Ambient's Darkness:");
                InputField ambientField = new InputField(new Vector2D(240, y+5), new Vector2D(100, 30), String.valueOf(0), AMBIENT_DARKNESS_INPUT_ID);
                ambientField.addActionListener(actionListener);
                
                ImageButton properties = new ImageButton(AssetManager.loadImage("resources/interface/leveleditor/properties.png"), 
                        new Vector2D(50, y+=50), ADVANCED_PROPERTIES_ID);
                properties.addActionListener(actionListener);
                
                this.addToSection(Section.LIGHTS, properties, ambientLabel, ambientField,
                        voltageLabel, voltageField, circuitryEnabledLabel, circuitryEnabledTick,
                        blinkEnabledLabel, blinkEnabledTick, periodsLabel, periodsField, rangeLabel,
                        rangeField, typeSelector);
            }
            
            {   //CIRCUITRY
                float y=50;
                Font baseFont = new Font(Font.DIALOG, Font.PLAIN, 20);

                geometryzombiesmayhem.Selector typeSelector = new geometryzombiesmayhem.Selector("Circuitry Component:", new Vector2D(50, y+=50), TYPE_SELECTOR_ID, 
                        "<None>", "Dimmer", "Lever", "Emitter", "Battery", "NotGate");
                typeSelector.addActionListener(actionListener);
                                
                WLabel voltageLabel = new WLabel(new Vector2D(50, y+=50), baseFont, "Voltage:");
                InputField voltageField = new InputField(new Vector2D(190, y+=5), new Vector2D(100, 30), String.valueOf(1.0f), VOLTAGE_INPUT_ID);
                voltageField.addActionListener(actionListener);
                batlev.add(voltageLabel);
                batlev.add(voltageField);
                p = voltageLabel;
                
                this.addToSection(Section.CIRCUITRY, typeSelector);
            }
            
            for(ComponentP comp : this.currentList)
                this.add(comp);
        }
        private void addToSection(Section s, ComponentP... comps) {this.components.get(s).addAll(Arrays.asList(comps));}
        private void removeFromSection(Section s, ComponentP... comps) {this.components.get(s).removeAll(Arrays.asList(comps));}
    }
}
