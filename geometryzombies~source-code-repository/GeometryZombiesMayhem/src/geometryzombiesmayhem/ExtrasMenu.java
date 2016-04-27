package geometryzombiesmayhem;

import geometryzombiesmayhem.io.StateManager;
import geometryzombiesmayhem.io.StateManager.StateStatus;
import java.awt.AWTException;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.EnumMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 *
 * @author Renato Lui Geh
 */
public class ExtrasMenu extends Menu {
    private static ExtrasMenu extras = new ExtrasMenu();
    public static ExtrasMenu getMenu() {
        if(extras==null) extras = new ExtrasMenu();
        return extras;
    }
    
    private static enum Type {LEVEL_EDITOR, CREDITS, ABOUT};
    private Map<Type, PanelP> panels = new EnumMap<>(Type.class);
    private Type type = Type.LEVEL_EDITOR;
    
    public ExtrasMenu() {
        for(int i=0;i<Type.values().length;i++)
            panels.put(Type.values()[i], new PanelP());
        
        PaintableImage leftPane = new PaintableImage(AssetManager.loadImage("resources/interface/panels/sideOptionsPane.png"),
                "resources/interface/panels/sideOptionsPane.png", new Vector2D(22, 0));
        PaintableImage rightPane = new PaintableImage(AssetManager.loadImage("resources/interface/panels/optionsPane.png"),
                "resources/interface/panels/optionsPane.png", new Vector2D(leftPane.getWidth()+72, 0));
        
        float y=70;
        
        ImageButton leveleditor = new ImageButton(getButtonImage("leveleditor_hover.png"), getButtonImage("leveleditor.png"),
                new Vector2D(25, y), LEVEL_EDITOR_BUTTON_ID);
        ImageButton credits = new ImageButton(getButtonImage("credits_hover.png"), getButtonImage("credits.png"),
                new Vector2D(25, y+=leveleditor.getHeight()), CREDITS_BUTTON_ID);
        ImageButton about = new ImageButton(getButtonImage("about_hover.png"), getButtonImage("about.png"),
                new Vector2D(25, y+=credits.getHeight()), ABOUT_BUTTON_ID);
        ImageButton back = new ImageButton(AssetManager.loadImage("resources/interface/buttons/options/back_hover.png"), AssetManager.loadImage("resources/interface/buttons/options/back.png"),
                new Vector2D(25, y+=3*credits.getHeight()), BACK_BUTTON_ID);
    
        leveleditor.addActionListener(gA); credits.addActionListener(gA); about.addActionListener(gA); back.addActionListener(gA);
        this.add(leveleditor, credits, about, back);
        this.add(leftPane, rightPane);
        
        { //Level Editor:
            Sentence label = new Sentence(new Vector2D(80+leftPane.getWidth()+75, 80), "Level Editor", 10, 0, .4f, Color.RED);
            
            ImageButton start = new ImageButton(getButtonImage("start.png"),
                    new Vector2D(180+leftPane.getWidth(), 180), START_LEVEL_EDITOR_BUTTON_ID);
            ImageButton imp = new ImageButton(getButtonImage("import.png"), 
                    new Vector2D(180+leftPane.getWidth(), 180+start.getHeight()+10), IMPORT_LEVEL_EDITOR_BUTTON_ID);
            TextButton open = new TextButton(new Vector2D(140+leftPane.getWidth(), 200+2*(start.getHeight()+10)), 50,
                    "Open Level Directory", Color.DARK_GRAY, OPEN_LEVEL_EDITOR_BUTTON_ID);            
            
            start.addActionListener(gA); imp.addActionListener(gA); open.addActionListener(gA);
            
            this.panels.get(Type.LEVEL_EDITOR).add(label);
            this.panels.get(Type.LEVEL_EDITOR).add(start, imp, open);
        }
        
        { //Credits
            Sentence label = new Sentence(new Vector2D(125+leftPane.getWidth()+75, 80), "Credits", 10, 0, .4f, Color.RED);
        
            TextBox info = new TextBox(new Vector2D(leftPane.getWidth()+85, 150), 
                    "Co-Developer: Renato Lui Geh\n" +
                    "Co-Developer: Yan Soares Couto\n\n" +
                    "Credits to Oracle's Java. Developed with the Java SE JDK.\n" +
                    "Subversion and repository thanks to Java.net.\n\n" +
                    "Specific work (major contributions) credits:\n\nYan Soares Couto:\n" +
                    "-Physics Engine\n-Circuitry Simulation\n-User Interface Base Coding\n-Real-time Dialogs\n" +
                    "-Bullet Time and Time Distortion\n-Thread Management\n-Camera Movement Coding\n\n" +
                    "Renato Lui Geh:\n-Art Drawing and Animation\n-General Input/Output Operations\n-Audio and Image Creation and Manipulation Code \n"+
                    "-Lighting and Shadow System\n-Saving and Loading Operations\n-Tree Structured Dialogs\n" + 
                    "-Image Shaders and Filtering\n-Multiplayer Support\n-User Friendly Level Editor", 
                    Color.BLACK, new Font(Font.DIALOG, Font.PLAIN, 15), 99999);
            
            this.panels.get(Type.CREDITS).add(label, info);
        }
        
        { //About
            Sentence label = new Sentence(new Vector2D(150+leftPane.getWidth()+75, 80), "About", 10, 0, .4f, Color.RED);
            
            TextBox info = new TextBox(new Vector2D(leftPane.getWidth()+85, 150), 
                    "Zombies Mayhem! is all about sex, drugs and rock 'n roll!\n\n"
                    + "Seriously though: we seek to create a different kind of adventure game. "
                    + "By taking inspiration from games from our childhood "
                    + "(Metal Slug, Roller Coaster Tycoon, Lemmings, Barbie House Adventures), "
                    + "we try to create games that are unique, immersive, fun, and still have "
                    + "that old school look 'n feel that we remember with fondness from when we were kids.\n\n"
                    + "We are always ready and willing to answer any questions you might have "
                    + "regarding the development of the game! If you have any "
                    + "suggestions/criticism/spam/peripherical member enlargement advertisements "
                    + "be sure to let us know by sending us your feedback on the "
                    + "following e-mail addresses:\n\n\n"
                    + "Renato Lui Geh: renatogeh@gmail.com\n"
                    + "Yan Soares Couto: yan.couto@gmail.com\n\n\n"
                    + "Peace out, bro.\n\n"
                    + "              ~ Renato Lui Geh (Co-Developer)"
                    , Color.BLACK, new Font(Font.DIALOG, Font.PLAIN, 15), 56);
            
            this.panels.get(Type.ABOUT).add(label, info);
        }
    }
    
    private GeneralAdapter gA = new GeneralAdapter() {
        @Override
        public void actionPerformed(ActionEvent event) {
            super.actionPerformed(event);
            int id = event.getSource() instanceof ImageButton?((ImageButton)event.getSource()).getID():((TextButton)event.getSource()).getID();
            switch(id) {
                case BACK_BUTTON_ID:
                    ZM.main.changeMenu(MainMenu.getMenu());
                break;
                case LEVEL_EDITOR_BUTTON_ID:
                    switchPanel(Type.LEVEL_EDITOR);
                break;
                case CREDITS_BUTTON_ID:
                    switchPanel(Type.CREDITS);
                break;
                case ABOUT_BUTTON_ID:
                    switchPanel(Type.ABOUT);
                break;
                case START_LEVEL_EDITOR_BUTTON_ID:
                    ZM.main.changeMenu(null);
                    try {ZM.main.play(false, "localhost", 1234, "God", null, true);}catch(MalformedURLException|InterruptedException|AWTException exc) {};
                break;
                case IMPORT_LEVEL_EDITOR_BUTTON_ID:
                    StateStatus level = StateManager.preloadLevel();
                    if(level.getResult() == StateManager.APPROVE_RESULT) {
                        ZM.main.changeMenu(null);
                        try {ZM.main.play(false, "localhost", 1234, "God", level, true);}catch(MalformedURLException|InterruptedException|AWTException exc) {}
                    }
//                    JOptionPane.showConfirmDialog(null, "We are currently working on importable\nlevels directly from the Menu.\nExpect more awesomeness.", 
//                            "Nope. Not there yet.", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);
                break;
                case OPEN_LEVEL_EDITOR_BUTTON_ID:
                    if(Desktop.isDesktopSupported())
                        try {
//                            System.err.println(AssetManager.DEFAULT_DATA_PATH+"levels" + "\t" + new java.io.File(AssetManager.DEFAULT_DATA_PATH+"levels").exists() + "\t" + new java.io.File(AssetManager.DEFAULT_DATA_PATH+"levels").isDirectory());
//                            System.err.println(Desktop.getDesktop().isSupported(Desktop.Action.OPEN));
                            Desktop.getDesktop().open(new File(AssetManager.DEFAULT_DATA_PATH+"levels"));
                        } catch (IOException ex) {
//                            System.err.println("Exception");
                            Logger.getLogger(ExtrasMenu.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    else
                        JOptionPane.showConfirmDialog(null, "Operational System is not supported by Java.\nOh well... :(", 
                                "User is not amused", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, new ImageIcon(AssetManager.load("resources/interface/okayguy.jpg")));
                break;
            }
        }
    };
    
    private void switchPanel(Type t) {
        if(t.equals(this.type)) return;
        this.remove(panels.get(this.type));
        this.add(panels.get(t), true);
        this.type = t;
    }
    
    @Override
    public void close(WindowP window) {
        super.close(window);
        this.remove(panels.get(type));
        this.type = null;
    }
    
    @Override
    public void load(WindowP window) {
        if(type == null) type = Type.LEVEL_EDITOR;
        super.load(window);
        this.add(panels.get(type), true);
    }
    
    private static final int BACK_BUTTON_ID = 0;
    private static final int LEVEL_EDITOR_BUTTON_ID = 1;
    private static final int CREDITS_BUTTON_ID = 2;
    private static final int ABOUT_BUTTON_ID = 3;
    private static final int START_LEVEL_EDITOR_BUTTON_ID = 4;
    private static final int IMPORT_LEVEL_EDITOR_BUTTON_ID = 5;
    private static final int OPEN_LEVEL_EDITOR_BUTTON_ID = 6;
    
    private MutableImage getButtonImage(String name) {return AssetManager.loadImage("resources/interface/buttons/extras/"+name);}
}
