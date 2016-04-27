package geometryzombiesmayhem;

import geometryzombiesmayhem.GameSettings.Graphics.Filter;
import geometryzombiesmayhem.Radio.PlayStyle;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.util.*;
import javax.swing.event.ChangeEvent;

/**
 * <p>Options menu.</p>
 * 
 * @author Renato Lui Geh
 */
public class OptionsMenu extends Menu{
    private Type type = null; 
    
    private Font defFont = new Font(Font.DIALOG, Font.PLAIN, 20);
    private Map<Type, PanelP> panels = new EnumMap<>(Type.class);

    {
        for(Type t : Type.values()){
            panels.put(t, new PanelP());
        }
    }
    
    public OptionsMenu() {
        init();
    }
    
    private void init() {
        Vector2D sidePanePosition = new Vector2D(22, 0);
        
        PaintableImage sidePane = new PaintableImage(
                AssetManager.loadImage("resources/interface/panels/sideOptionsPane.png"),
                "resources/interface/panels/sideOptionsPane.png",
                sidePanePosition);
        
        Vector2D mainPanePosition = sidePanePosition.add(sidePane.getWidth() + 50, 0);
        
        PaintableImage mainPane = new PaintableImage(
                AssetManager.loadImage("resources/interface/panels/optionsPane.png"),
                "resources/interface/panels/optionsPane.png",
                mainPanePosition);
        
        float height = AssetManager.loadImage("resources/interface/buttons/options/game.png").getHeight();
        Vector2D buttonPosition = sidePanePosition.subtract(-2.5f, 20);
        
        ImageButton game = new ImageButton(AssetManager.loadImage(DEFAULT_BUTTONS_PATH + "game_hover.png"),
                AssetManager.loadImage(DEFAULT_BUTTONS_PATH + "game.png"), buttonPosition.add(0, 10 + height), 0);
        ImageButton controls = new ImageButton(AssetManager.loadImage(DEFAULT_BUTTONS_PATH + "controls_hover.png"),
                AssetManager.loadImage(DEFAULT_BUTTONS_PATH + "controls.png"), buttonPosition.add(0, 10 + 2 * height), 1);
        ImageButton graphics = new ImageButton(AssetManager.loadImage(DEFAULT_BUTTONS_PATH + "graphics_hover.png"),
                AssetManager.loadImage(DEFAULT_BUTTONS_PATH + "graphics.png"), buttonPosition.add(0, 10 + 3 * height), 2);
        ImageButton audio = new ImageButton(AssetManager.loadImage(DEFAULT_BUTTONS_PATH + "audio_hover.png"),
                AssetManager.loadImage(DEFAULT_BUTTONS_PATH + "audio.png"), buttonPosition.add(0, 10 + 4 * height), 3);
        ImageButton defSet = new ImageButton(AssetManager.loadImage(DEFAULT_BUTTONS_PATH + "default_hover.png"),
                AssetManager.loadImage(DEFAULT_BUTTONS_PATH + "default.png"), buttonPosition.add(0, 10 + 5 * height), 4);
        ImageButton back = new ImageButton(AssetManager.loadImage(DEFAULT_BUTTONS_PATH + "back_hover.png"),
                AssetManager.loadImage(DEFAULT_BUTTONS_PATH + "back.png"), buttonPosition.add(0, 10 + 6 * height), 5);
        
        game.addActionListener(gA);controls.addActionListener(gA);graphics.addActionListener(gA);
        audio.addActionListener(gA);defSet.addActionListener(gA);back.addActionListener(gA);
        
        this.addComponent(game,controls,graphics,audio,defSet,back);
        this.addGroupComponent(sidePane,mainPane);
        initGame(mainPanePosition); initControls(mainPanePosition); initGraphics(mainPanePosition); initAudio(mainPanePosition);
    }
    
    private void initGame(Vector2D mainPos) {        
        Sentence label = new Sentence(mainPos.add(25, 75), "Game Settings", 10, 0, .5f, Color.RED);
        
        this.addGroupComponent(Type.Game, label);
    }
    
    private void initControls(Vector2D mainPos) {
        Sentence label = new Sentence(mainPos.add(10, 75), "Controls Settings", 10, 0, .4f, Color.RED);
        
        this.addGroupComponent(Type.Controls, label);
    }
    
    private void initGraphics(Vector2D mainPos) {
        Sentence label = new Sentence(mainPos.add(22, 75), "Graphic Settings", 10, 0, .4f, Color.RED);
        
        Selector resolution = new Selector("Screen Resolution: ", defFont, Color.DARK_GRAY, mainPos.add(25, 150),
                new Tooltip("Sets the game's resolution based on pixels. This scales the window to "
                + "fit the designated dimension. It is recommended that it stays at Default resolution. "
                + "Other resolutions may cause distortions. Changes will take effect immediately after applying.", 
                Color.LIGHT_GRAY, 180, 500), SELECTOR_RESOLUTION,
                "Default", "800x600", "1024x720", "1024x768", "1248x720", "1248x768", "1600x900", "1900x1200");
        
        Selector screenMode = new Selector("Screen Mode: ", defFont, Color.DARK_GRAY, mainPos.add(25, 200),
                new Tooltip("Sets the screen mode as Window, Borderless or Fullscreen. Window represents a "
                + "regular frame with title bar and icon. Borderless is a Window without title bar and icon. "
                + "Fullscreen makes the entire screen the game frame, but without title bar and icon. Note that switching "
                + "the screen mode to Window or Borderless may have an impact on performance, however tabbing in and "
                + "out from one window/frame to another is much faster. Also note that fullscreen may not work on "
                + "multi-screen computers or systems that do not have fullscreen support. Enabling fullscreen mode "
                + "disables the Screen Resolution option.                               ", 
                Color.LIGHT_GRAY, 180, 500), SELECTOR_SCREENMODE,
                "Window", "Borderless", "Fullscreen");
        
        Selector filter = new Selector("Display Filter: ", defFont, Color.DARK_GRAY, mainPos.add(25, 250), 
                new Tooltip("Sets the game filter. This option distorts image rendering to cause various effect, "
                + "such as Negative, Grayscale or Blurring effects. These options may impact on performance. "
                + "Changes will take effect when the actual game is started.", Color.LIGHT_GRAY, 180, 500), SELECTOR_FILTER,
                "Default", "Blur", "Negative", "Black & White");
        
        resolution.addChangeListener(gA);screenMode.addChangeListener(gA);filter.addChangeListener(gA);
        
        this.addComponent(Type.Graphics, filter, screenMode, resolution);
        this.addGroupComponent(Type.Graphics, label);
    }

    private void initAudio(Vector2D mainPos) {
        Sentence label = new Sentence(mainPos.add(20, 75), "Audio Settings", 10, 0, .47f, Color.RED);
        
        Slider masterVol = new Slider(mainPos.add(25, 125), "Master Volume: ", defFont, Color.DARK_GRAY, Color.BLACK, 
                Slider.Format.Precise, 0, 100, 50, new Vector2D(150, 10),
                new Tooltip("Sets the absolute volume of the game. All channels will be affected by this change. "
                + "Changes will take effect immediately after applying.", 
                Color.LIGHT_GRAY, 180, 500),SLIDER_MASTERVOL);
        Slider musicVol = new Slider(mainPos.add(25, 185), "Music Volume: ", defFont, Color.DARK_GRAY, Color.BLACK,
                Slider.Format.Precise, 0, 100, 50, new Vector2D(150, 10),
                new Tooltip("Sets the music volume. Only the Music channel will be affected by this change. "
                + "Changes will take effect immediately after applying.", 
                Color.LIGHT_GRAY, 180, 500),SLIDER_MUSICVOL);
        Slider fxVol = new Slider(mainPos.add(25, 245), "SFX Volume: ", defFont, Color.DARK_GRAY, Color.BLACK, 
                Slider.Format.Precise, 0, 100, 50, new Vector2D(150, 10),
                new Tooltip("Sets the Special Effects volume. Player, Interface, Zombies and Environment"
                + " channels will be affected by this change. "
                + "Changes will take effect immediately after applying.", 
                Color.LIGHT_GRAY, 180, 500),SLIDER_FXVOL);
        Slider radioVol = new Slider(mainPos.add(25, 305), "Radio Volume: ", defFont, Color.DARK_GRAY, Color.BLACK,
                Slider.Format.Precise, 0, 100, 50, new Vector2D(150, 10),
                new Tooltip("Sets the radio volume. Only the Radio channel will be affected by this change. "
                + "Changes will take effect immediately after applying.", 
                Color.LIGHT_GRAY, 180, 500),SLIDER_RADIOVOL);
        
        Selector radioFader = new Selector("Radio Fading: ", defFont, Color.DARK_GRAY, mainPos.add(25, 370),
                new Tooltip("Sets the fading effects of Radio. Fading causes the moments "
                + "in between the songs to behave in different forms. Fading means the after or before "
                + "moments of a song will gradually have their volumes increased/decreased to cause an "
                + "audibly pleasent transition between songs.", 
                Color.LIGHT_GRAY, 180, 500), SELECTOR_RADIOFADER,
                "Only Fade Out", "Fade Out and In", "Only Fade In", "No Fading");
        Selector radioPlaystyle = new Selector("Radio Playstyle: ", defFont, Color.DARK_GRAY, mainPos.add(25, 420), 
                new Tooltip("Sets the Radio's playstyle. Playstyle governs how the repeating and shuffling "
                + "of songs behave.", 
                Color.LIGHT_GRAY, 180, 500), SELECTOR_RADIOPLAYSTYLE,
                "Normal", "Shuffle Any", "Shuffle No Repeat");
        
        masterVol.addChangeListener(gA);musicVol.addChangeListener(gA);fxVol.addChangeListener(gA);radioVol.addChangeListener(gA);
        radioFader.addChangeListener(gA);radioPlaystyle.addChangeListener(gA);

        this.addComponent(Type.Audio, radioVol, fxVol, musicVol, masterVol, radioPlaystyle, radioFader);
        this.addGroupComponent(Type.Audio, label);
    }
    
    private GeneralAdapter gA = new GeneralAdapter() {

        @Override
        public void actionPerformed(ActionEvent event) {
            super.actionPerformed(event);
            ImageButton ib = (ImageButton) event.getSource();
            switch(ib.getID()) {
                case BUTTON_GAME:
                    setType(OptionsMenu.Type.Game);
                    //Game options
                break;
                case BUTTON_CONTROLS:
                    setType(OptionsMenu.Type.Controls);
                    //Controls options
                break;
                case BUTTON_GRAPHICS:
                    setType(OptionsMenu.Type.Graphics);
                    //Graphics options
                break;
                case BUTTON_AUDIO:
                    setType(OptionsMenu.Type.Audio);
                    //Audio options
                break;
                case BUTTON_DEFAULT:
                    OptionsMenu.resetSettings();
                break;
                case BUTTON_BACK:
                    ZM.main.changeMenu(MainMenu.getMenu());
                break;
            }
        }
        
        @Override
        public void stateChanged(ChangeEvent event) {
            super.stateChanged(event);
            int id = ((IDable) event.getSource()).getID();
            switch(id) {
                case SELECTOR_RESOLUTION:
                    Selector resolution = (Selector)event.getSource();
                    String resOption = resolution.getSelected();
                    Vector2D resDim;

                    if(resOption.equals("Default"))
                        resDim = GameSettings.Graphics.getDefaultResolution();
                    else 
                        resDim = parseResolution(resOption);

                    if(resDim.equals(GameSettings.Graphics.getResolution())) break;
                    
                    GameSettings.Graphics.setResolution(resDim);
                    break;
                case SELECTOR_SCREENMODE:
                    Selector screenMode = (Selector)event.getSource();
                    String screenOption = screenMode.getSelected();
                    ((Selector) panels.get(Type.Graphics).components.get(0)).setActive(!("Fullscreen".equals(screenOption)));                    
                    GameSettings.Graphics.ScreenMode sm = GameSettings.Graphics.ScreenMode.valueOf(screenOption);
                    
                    if(sm.equals(GameSettings.Graphics.getScreenMode())) break;
                    
                    GameSettings.Graphics.setScreenMode(sm);
                    GameSettings.Graphics.handleScreenMode(ZM.main);
                    break;
                case SELECTOR_FILTER:
                    Selector filter = (Selector)event.getSource();
                    String filtOption = filter.getSelected();
                    Filter f = (filtOption.equals("Black & White")? Filter.Gray : Filter.valueOf(filtOption));
                    
                    if(f.equals(GameSettings.Graphics.getFilter())) break;
                    
                    GameSettings.Graphics.setFilter(f);
                    break;
                case SLIDER_MASTERVOL:
                    Slider master = (Slider)event.getSource();
                    int masterVol = (int)master.getValue();

                    GameSettings.Sounds.setVolume(masterVol);
                    break;
                case SLIDER_MUSICVOL:
                    Slider music = (Slider)event.getSource();
                    int musicVol = (int)music.getValue();

                    GameSettings.Sounds.setVolume(AudioManager.findChannel("Music"), musicVol);
                    break;
                case SLIDER_FXVOL:
                    Slider fx = (Slider)event.getSource();
                    int fxVol = (int)fx.getValue();

                    GameSettings.Sounds.setVolume(AudioManager.findChannel("Player"), fxVol);
                    GameSettings.Sounds.setVolume(AudioManager.findChannel("Interface"), fxVol);
                    GameSettings.Sounds.setVolume(AudioManager.findChannel("Zombies"), fxVol);
                    GameSettings.Sounds.setVolume(AudioManager.findChannel("Environment"), fxVol);
                    break;
                case SLIDER_RADIOVOL:
                    Slider radio = (Slider)event.getSource();
                    int radioVol = (int)radio.getValue();

                    GameSettings.Sounds.setVolume(AudioManager.findChannel("Radio"), radioVol);
                    break;
                case SELECTOR_RADIOFADER:
                    Selector radioFader = (Selector)event.getSource();
                    String fader = radioFader.getSelected();
                    //WIP
                    break;
                case SELECTOR_RADIOPLAYSTYLE:
                    Selector radioPlaystyle = (Selector)event.getSource();
                    String playstyle = radioPlaystyle.getSelected();
                    PlayStyle ps;

                    switch(playstyle) {
                        case "Normal":
                            ps = PlayStyle.NORMAL;
                        break;
                        case "Shuffle Any":
                            ps = PlayStyle.SHUFFLE_ANY;
                        break;
                        case "Shuffle No Repeat":
                            ps = PlayStyle.SHUFFLE_NO_REPEAT;
                        break;
                        default:
                            throw new IllegalArgumentException("Unexpected Exception Caught: No such PlayStyle.");
                    }

                    Radio.setStyle(ps);
                    break;
            }
            ZM.main.loader.export(ConfigLoader.DEFAULT_CONFIG_PATH, GameSettings.getFields());
        }
    };
    

    @Override
    public void load(WindowP window) {
        super.load(window);
        loadSection(window,Type.Game);
    }
    
    private void loadSection(WindowP window,Type t) {
        add(panels.get(t), true);
        type = t;
    }

    @Override
    public void close(WindowP window) {
        super.close(window);
        closeSection(window,type);
        type = null;
    }
    
    private void closeSection(WindowP window, Type t) {
        remove(panels.get(t));
    }
    
    private void changeSectionTo(Type t) {
        closeSection(window,type);
        loadSection(window,t);
    } 
    
    private Vector2D parseResolution(String resOption) {
        String[] dims = resOption.split("x");
        return new Vector2D(Float.parseFloat(dims[0]), Float.parseFloat(dims[1]));
    }
    
    public void reset() {
        for(Type t:Type.values())
            for(ComponentP c : panels.get(t).getComponentPs()) {
                c.reset();
                this.gA.stateChanged(new ChangeEvent(c));
            }
    }
    
    @Deprecated
    public void apply() {
        //Resolution:
    }
    
    private void addGroupComponent(Type t, Paintable... p) {panels.get(t).add(p);}
    private void addGroupComponent(Paintable... p) {add(p);}
    private void addComponent(Type t, ComponentP... p) {panels.get(t).add(p);}
    private void addComponent(ComponentP... p) {add(p);}
    
    private static final String DEFAULT_BUTTONS_PATH = "resources/interface/buttons/options/";
    
    //General
    public static final byte BUTTON_NONE = ImageButton.BUTTON_NONE;
    public static final byte BUTTON_GAME = 0;
    public static final byte BUTTON_CONTROLS = 1;
    public static final byte BUTTON_GRAPHICS = 2;
    public static final byte BUTTON_AUDIO = 3;
    public static final byte BUTTON_DEFAULT = 4;
    public static final byte BUTTON_BACK = 5;
    //Graphics
    public static final byte SELECTOR_RESOLUTION = 6;
    public static final byte SELECTOR_SCREENMODE = 7;
    public static final byte SELECTOR_FILTER = 8;
    //Audio
    public static final byte SLIDER_MASTERVOL = 9;
    public static final byte SLIDER_MUSICVOL = 10;
    public static final byte SLIDER_FXVOL = 11;
    public static final byte SLIDER_RADIOVOL = 12;
    public static final byte SELECTOR_RADIOFADER = 13;
    public static final byte SELECTOR_RADIOPLAYSTYLE = 14;
    
    public static enum Type {Game, Controls, Graphics, Audio};
    
    private static OptionsMenu options = null;
    
    public static OptionsMenu getMenu() {
        if(options==null) options = new OptionsMenu();
        return options;
    }
    
    @Deprecated
    public static void applySettings() {
        options.apply();
    }
    
    public static void resetSettings() {
        options.reset();
    }
    
    public static Type getType() {return options.type;}
    public static void setType(Type type) {options.changeSectionTo(type); }
}
