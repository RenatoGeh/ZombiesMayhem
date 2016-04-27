package geometryzombiesmayhem;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.net.MalformedURLException;
import java.util.ArrayList;

/**
 * <p>Main Menu</p>
 * 
 * <p>
 *  This class is supposed to handle all the functionality
 *  of the Main Menu.
 * </p>
 * 
 * <p>
 *  The Main Menu refers to the first menu the user encounters.
 * </p>
 * 
 * <p>
 *  The main menu is connected to the following menus and frames:
 * </p>
 * 
 * <ul>
 *  <li> Single-player menu (SingleMenu.java) </li>
 *  <li> Options menu (OptionsMenu.java) </li>
 *  <li> Multi-player menu (MultiMenu.java) </li>
 *  <li> Extras menu (ExtrasMenu.java) </li>
 * </ul>
 * 
 * @see OptionsMenu
 * @see ExtrasMenu
 * @see GameFrame
 * @see MultiMenu
 * @see SingleMenu
 * 
 * @author Renato Lui Geh
 * @author Yan
 */
public class MainMenu extends Menu {    
    private int logoPanelHeight,infoPanelHeight;
    
    private Vector2D buttonPanelPosition, logoPanelPosition;
    
    private TextBox infoText;
    
    public MainMenu() {
        initPanels();
        initButtons();
        initInfo();
    }
    
    private void initButtons() {    
        ArrayList<MutableImage> buttonImages = new ArrayList<>();
        
        buttonImages.add(getButtonImage("singleplayer.png"));
        buttonImages.add(getButtonImage("multiplayer.png"));
        buttonImages.add(getButtonImage("options.png"));
        buttonImages.add(getButtonImage("extras.png"));
    
        buttonImages.add(getButtonImage("singleplayer_not.png"));
        buttonImages.add(getButtonImage("multiplayer_not.png"));
        buttonImages.add(getButtonImage("options_not.png"));
        buttonImages.add(getButtonImage("extras_not.png"));
        
        ImageButton ib; 
        int size = buttonImages.get(0).getHeight(null);
        for(int i=0;i<buttonImages.size()/2;i++) {
            add(ib = new ImageButton(buttonImages.get(i), buttonImages.get(i+buttonImages.size()/2),
                    new Vector2D(75, 65 + i*size), i), true);
            ib.addMouseListener(gA);
            ib.addActionListener(gA);
        }
        
        add(ib = new ImageButton(getButtonImage("exit.png"), 
                new Vector2D(Vector2D.BOTTOM_RIGHT.getX() - 100, 
                Vector2D.BOTTOM_RIGHT.getY() - 85), BUTTON_EXIT), true);
            ib.addMouseListener(gA);
            ib.addActionListener(gA);
    }

    @Override
    public void load(WindowP window) {
        super.load(window);
        
        infoText.setText(INFO_NONE);
    }
    
    private void initPanels() {
        buttonPanelPosition = new Vector2D(50, 0);
        logoPanelPosition = buttonPanelPosition.add(370, 0);
        
        PaintableImage buttonPanel = new PaintableImage(this.getPanelImage("buttonpanel_1.png"),panelPath("buttonpanel_1.png"), buttonPanelPosition);
        PaintableImage logoPanel = new PaintableImage(this.getPanelImage("buttonpanel_2.png"), panelPath("buttonpanel_2.png"), logoPanelPosition);
        PaintableImage infoPanel = new PaintableImage(this.getPanelImage("buttonpanel_3.png"), panelPath("buttonpanel_3.png"), logoPanelPosition.add(0, logoPanelHeight = logoPanel.getHeight()));
        infoPanelHeight = infoPanel.getHeight();
        
        add(buttonPanel);
        add(logoPanel);
        add(infoPanel);
    }
    
    private void initInfo() {
        infoText = new TextBox(logoPanelPosition.add(20, logoPanelHeight), 
                "", Color.LIGHT_GRAY, 0, new Font(Font.DIALOG, Font.PLAIN, 15), 40);
        infoText.refreshPosition(logoPanelPosition.add(20, logoPanelHeight + infoPanelHeight/3.0f));
        add(infoText);
    }
    
    private GeneralAdapter gA = new GeneralAdapter() {

        @Override
        public void mouseEntered(MouseEvent event) {
            super.mouseEntered(event);
            ImageButton ib = (ImageButton) event.getSource();
            infoText.setText(getInfo(ib.getID()));
        }

        @Override
        public void mouseExited(MouseEvent event) {
            super.mouseExited(event);
            infoText.setText(INFO_NONE);
        }

        @Override
        public void actionPerformed(ActionEvent event) {
            super.actionPerformed(event);
            ImageButton ib = (ImageButton) event.getSource();
//            AudioManager.doInterfaceClick(true);

            switch(ib.getID()) {
                case MainMenu.BUTTON_EXIT:
                    System.exit(0);
                break;
                case MainMenu.BUTTON_SINGLE:
                    ZM.main.changeMenu(SingleMenu.getMenu());
                break;
                case MainMenu.BUTTON_MULTI:
                    ZM.main.changeMenu(MultiMenu.getMenu());
                break;
                case MainMenu.BUTTON_EXTRAS:
                    ZM.main.changeMenu(ExtrasMenu.getMenu());
                break;
                case MainMenu.BUTTON_OPTIONS:
                    ZM.main.changeMenu(OptionsMenu.getMenu());
                break;    
            }
        }
        
    };
    
    private String getInfo(int i) {
        switch(i) {
            case BUTTON_NONE:
                return INFO_NONE;
            case BUTTON_SINGLE:
                return INFO_SINGLE;
            case BUTTON_MULTI:
                return INFO_MULTI;
            case BUTTON_OPTIONS:
                return INFO_OPTIONS;
            case BUTTON_EXTRAS:
                return INFO_EXTRAS;
            case BUTTON_EXIT:
                return INFO_EXIT;
        }
        
        return "null";
    }
    
    private MutableImage getButtonImage(String filename) {
        return AssetManager.loadImage("resources/interface/buttons/main/" + filename);
    }
    
    private String buttonPath(String filename) {
        return "resources/interface/buttons/main/" + filename;
    }
    
    private Image getPanelImage(String filename) {
        return AssetManager.loadRawImage("resources/interface/panels/" + filename);
    }
    
    private String panelPath(String filename) {
        return "resources/interface/panels/" + filename;
    }

    public static final int BUTTON_NONE = ImageButton.BUTTON_NONE;
    
    public static final int BUTTON_SINGLE = 0;
    public static final int BUTTON_MULTI = 1;
    public static final int BUTTON_OPTIONS = 2;
    public static final int BUTTON_EXTRAS = 3;
    public static final int BUTTON_EXIT = 4;
    
    private final String INFO_NONE = "Welcome to Zombies Mayhem! " + "Version: " + ZM.version + ". " + ZM.versioning;
    private final String INFO_SINGLE = "Play the Demo or a user created "
            + "level! As of this version, no Campaign has been created yet. "
            + "We ask you to try our InDev Early Pre-Alpha Demo of our "
            + "current work on this game!";
    private final String INFO_MULTI = "Fight alongside (or against) your friends (or foes) "
            + "on Co-Op, Versus or Shared Campaign! As of this version, Multiplayer "
            + "is still in a primitive and unfinished version.\nPlay at your own risk!";
    private final String INFO_OPTIONS = "Adjust audio preferences, controls and video "
            + "options for optimal ownage!\nNote: some options may require restart.";
    private final String INFO_EXTRAS = "Create your own level with our Level Editor! "
            + "Especially built for the community (as well as for us, developers), "
            + "this tool provides everything you need to create your own\nawesome levels!";
    private final String INFO_EXIT = "sudo apt-get a-life. Get your ass out there nurd.";
    
    private static MainMenu main = null;

    public static MainMenu getMenu() {
        if(main == null) main = new MainMenu();
        return main;
    }
}
