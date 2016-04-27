package geometryzombiesmayhem;

import geometryzombiesmayhem.io.StateManager;
import geometryzombiesmayhem.io.StateManager.StateStatus;
import java.awt.AWTException;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.util.EnumMap;
import java.util.Map;

/**
 *
 * @author Renato Lui Geh
 */
public class SingleMenu extends Menu {
    private static SingleMenu MENU = new SingleMenu();
    public static SingleMenu getMenu() {
        if(MENU==null)return MENU = new SingleMenu();
        return MENU;
    }
    
    private static enum Type {CAMPAIGN, LOAD, SURVIVAL, CUSTOM};
    
    private Map<Type, PanelP> panels = new EnumMap<>(Type.class);
    private Type type = Type.CAMPAIGN;
    
    public SingleMenu() {
        for(int i=0;i<Type.values().length;i++)
            panels.put(Type.values()[i], new PanelP());
        
        PaintableImage leftPane = new PaintableImage(AssetManager.loadImage("resources/interface/panels/sideOptionsPane.png"),
                "resources/interface/panels/sideOptionsPane.png", new Vector2D(22, 0));
        PaintableImage rightPane = new PaintableImage(AssetManager.loadImage("resources/interface/panels/optionsPane.png"),
                "resources/interface/panels/optionsPane.png", new Vector2D(leftPane.getWidth()+72, 0));
        
        float y=70;
        
        ImageButton campaign = new ImageButton(getButtonImage("campaign_hover.png"), getButtonImage("campaign.png"),
                new Vector2D(25, y), CAMPAIGN_ID);
        ImageButton loadSave = new ImageButton(getButtonImage("load_save_hover.png"), getButtonImage("load_save.png"),
                new Vector2D(25, y+=campaign.getHeight()), LOAD_ID);
        ImageButton survival = new ImageButton(getButtonImage("survival_hover.png"), getButtonImage("survival.png"),
                new Vector2D(25, y+=campaign.getHeight()), SURVIVAL_ID);
        ImageButton custom = new ImageButton(getButtonImage("custom_hover.png"), getButtonImage("custom.png"),
                new Vector2D(25, y+=survival.getHeight()), CUSTOM_ID);
        ImageButton back = new ImageButton(AssetManager.loadImage("resources/interface/buttons/options/back_hover.png"), 
                AssetManager.loadImage("resources/interface/buttons/options/back.png"),
                new Vector2D(25, y+=2*custom.getHeight()), BACK_ID);
        
        campaign.addActionListener(listener); survival.addActionListener(listener); custom.addActionListener(listener); back.addActionListener(listener);
        loadSave.addActionListener(listener);
        
        this.add(campaign, loadSave, survival, custom, back);
        this.add(leftPane, rightPane);
        
        { //Campaign
            Sentence label = new Sentence(new Vector2D(125+leftPane.getWidth()+65, 80), "Campaign", 10, 0, .4f, Color.RED);
        
            TextBox info = new TextBox(new Vector2D(leftPane.getWidth()+85, 150),
                    "As of this current version of Zombies Mayhem!, there is no\n"
                    + "Campaign per se yet. We are actively working on the game\n"
                    + "itself however, and will provide a thrilling, adventurous\n"
                    + "and immersive campaign as soon as possible.\n\n"
                    + "In the meantime, why don't you take a peek at our first\n"
                    + "InDev Early-Pre-Alpha Demo? We encourage all and every\n"
                    + "feedback comment, be them good or bad, about the current\n"
                    + "stage of the game.\n\n"
                    + "P.S.: Tired of our demo? Why don't you try out our own\n"
                    + "Level Editor under Extras? The demo itself was made with\n"
                    + "this useful tool we created for the game community!",
                    Color.BLACK, new Font(Font.DIALOG, Font.PLAIN, 15), 200);
            
            TextButton play = new TextButton(new Vector2D(190+leftPane.getWidth(), 400),
                    50, "Play Demo!", Color.DARK_GRAY, PLAY_DEMO_ID);
            play.addActionListener(listener);
            
            this.panels.get(Type.CAMPAIGN).add(label, info, play);
        }
        
        { //Survival
            Sentence label = new Sentence(new Vector2D(125+leftPane.getWidth()+65, 80), "Survival", 10, 0, .4f, Color.RED);
        
            TextBox info = new TextBox(new Vector2D(leftPane.getWidth()+85, 150),
                    "Vanilla Zombies Mayhem! does not come with any levels or\nsaves. "
                    + "We rely both on our community to build up levels as\nwell as our "
                    + "own creativity.\n\nThat said, although there is no Survival mode in "
                    + "particular,\nour Level Editor provides all the tools to create\nsuch "
                    + "a play style.\n\nTo find out more about our Level Editor,\nstart creating "
                    + "your own Level by going\nto the Extras Menu (found on our Main menu) "
                    + "and\nselecting \"Create\" under the Level Editor option.\n\n"
                    + "We are looking to separate the most common play\nstyle modes "
                    + "into menu items, in order to ease user access.\nExpect a list containing "
                    + "downloadable levels\ntagged as \"Survival\" in this page.",
                    Color.BLACK, new Font(Font.DIALOG, Font.PLAIN, 15), 200);
            
            this.panels.get(Type.SURVIVAL).add(label, info);
        }
        
        { //Custom
            Sentence label = new Sentence(new Vector2D(125+leftPane.getWidth()+75, 80), "Custom", 10, 0, .4f, Color.RED);
        
            TextBox info = new TextBox(new Vector2D(leftPane.getWidth()+85, 150),
                    "Load a custom game directly from your computer or\n"
                    + "(in future versions) search for a level you might like\n"
                    + "and download it directly from Zombies Mayhem!.",
                    Color.BLACK, new Font(Font.DIALOG, Font.PLAIN, 15), 200);
            
            TextButton load = new TextButton(new Vector2D(120+leftPane.getWidth(), 400),
                    50, "Load a Custom Level!", Color.DARK_GRAY, LOAD_CUSTOM_ID);
            load.addActionListener(listener);
            
            this.panels.get(Type.CUSTOM).add(label, info, load);
        }
        
        { //Load Save
            Sentence label = new Sentence(new Vector2D(105+leftPane.getWidth()+75, 80), "Load Game", 10, 0, .4f, Color.RED);
        
            TextBox info = new TextBox(new Vector2D(leftPane.getWidth()+85, 150),
                    "Load a save state from a previous game.",
                    Color.BLACK, new Font(Font.DIALOG, Font.PLAIN, 15), 200);
            
            TextButton load = new TextButton(new Vector2D(120+leftPane.getWidth(), 400),
                    50, "Load your Save State!", Color.DARK_GRAY, LOAD_SAVE_ID);
            load.addActionListener(listener);
            
            this.panels.get(Type.LOAD).add(label, info, load);
        }
    }
    
    private ActionListener listener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent event) {
            switch((event.getSource() instanceof ImageButton?((ImageButton)event.getSource()).getID():((TextButton)event.getSource()).getID())) {
                case BACK_ID:
                    ZM.main.changeMenu(MainMenu.getMenu());
                break;
                case CAMPAIGN_ID:
                    SingleMenu.this.switchPanel(Type.CAMPAIGN);
                break;
                case LOAD_ID:
                    SingleMenu.this.switchPanel(Type.LOAD);
                break;
                case SURVIVAL_ID:
                    SingleMenu.this.switchPanel(Type.SURVIVAL);
                break;
                case CUSTOM_ID:
                    SingleMenu.this.switchPanel(Type.CUSTOM);
                break;
                case PLAY_DEMO_ID:
                    ZM.main.changeMenu(null);
                    try {ZM.main.play(false, "localhost", 1234, "Player_1", null, false);} catch(MalformedURLException | InterruptedException | AWTException exc) {};
                break;
                case LOAD_CUSTOM_ID:
                    StateStatus level = StateManager.preloadLevel();
                    if(level.getResult() == StateManager.APPROVE_RESULT) {
                        ZM.main.changeMenu(null);
                        try {ZM.main.play(false, "localhost", 1234, "Player_1", level, false);}catch(MalformedURLException|InterruptedException|AWTException exc) {};
                    }
                break;
                case LOAD_SAVE_ID:
                    StateStatus save = StateManager.preload();
                    if(save.getResult() == StateManager.APPROVE_RESULT) {
                        ZM.main.changeMenu(null);
                        try {ZM.main.play(false, "localhost", 1234, "Player_1", save, false);}catch(MalformedURLException|InterruptedException|AWTException exc) {};
                    }
                break;
            }
        }
    };
    
    private void switchPanel(Type e) {
        if(e.equals(this.type)) return;
        this.remove(panels.get(this.type));
        this.add(panels.get(e), true);
        this.type = e;
    }
    
    @Override
    public void close(WindowP window) {
        super.close(window);
        this.remove(panels.get(type));
        this.type = null;
    }
    @Override
    public void load(WindowP window) {
        if(type==null)type = Type.CAMPAIGN;
        super.load(window);
        this.add(panels.get(type), true);
    }
    
    private static final int BACK_ID = -1;    
    private static final int CAMPAIGN_ID = 0;
    private static final int LOAD_ID = 1;
    private static final int SURVIVAL_ID = 2;
    private static final int CUSTOM_ID = 3;
    
    private static final int PLAY_DEMO_ID = 4;
    private static final int LOAD_CUSTOM_ID = 5;
    private static final int LOAD_SAVE_ID = 6;
    
    private static MutableImage getButtonImage(String name) {return AssetManager.loadImage("resources/interface/buttons/single/"+name);}
}
