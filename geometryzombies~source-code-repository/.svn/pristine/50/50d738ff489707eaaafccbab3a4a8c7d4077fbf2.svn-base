package geometryzombiesmayhem;

import java.awt.AlphaComposite;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

/**
 * 
 * Image representing the current Instance of the Screen.
 * 
 * @author Renato Lui Geh
 */
public final class ScreenImage implements Paintable{
    private Graphics2D graph;
    private BufferedImage buffer;
    private Image[] stances = new Image[1];
    private Image redScreen;
    private Vector2D size = new Vector2D(GameFrame.FRAME_WIDTH + 17, GameFrame.FRAME_HEIGHT + 70);
    private int state;
    private float alpha = 0;
    protected boolean isActive = true;
    private boolean needsRefresh = false;
    
    public ScreenImage(int initialState) {
        this.state = initialState;
        
        initScreen();
        setState(initialState);
    }
    
    private void initScreen() {
        buffer = new BufferedImage((int)size.getX(), (int)size.getY(),BufferedImage.TRANSLUCENT);
        graph = buffer.createGraphics();
        
        redScreen = AssetManager.loadImage(DEFAULT_PATH + COLLECTION_PATHS[1]);
        redScreen = redScreen.getScaledInstance((int)size.getX(), (int)size.getY(), Image.SCALE_SMOOTH);
        
        for(int i=0;i<stances.length;i++) {
            stances[i] = AssetManager.loadImage(DEFAULT_PATH + COLLECTION_PATHS[i]);
            stances[i] = stances[i].getScaledInstance((int)size.getX(), (int)size.getY(), Image.SCALE_SMOOTH);
        }
    }
    
    public void refresh() {
        float health = Player.getDefaultPlayer().getHealth();
        
        if(health > 0 && health <= TRIGGER_EVENT_HEALTH) {
            alpha = health/TRIGGER_EVENT_HEALTH;
            alpha = Math.abs(alpha - 1);
        } else if(health > TRIGGER_EVENT_HEALTH) {
            alpha = 0;
            needsRefresh = true;
        }
        
        if(state == STATE_BLOOD)
            update();
    }
    
    @Override
    public void paint(Graphics2D g2, Component c) {
        refresh();
        if(state != STATE_CLEAR) 
            g2.drawImage(buffer, null, 0, 0);
    }
    
    public void setState(int state) {
        this.state = state;
        
        if(state == STATE_BLOOD) {
            this.state = STATE_BLOOD;
            
            graph.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            graph.drawImage(stances[STATE_BLOOD], 0, 0, null);
            graph.drawImage(redScreen, 0, 0, null);
            graph.dispose();

            this.isActive = true;
        } else if(state != STATE_CLEAR) {
            throw new IllegalArgumentException("No state found!");
        }
    }
    
    private void update() {
        if(alpha > 0 || needsRefresh) {
            buffer = new BufferedImage((int)size.getX(), (int)size.getY(),BufferedImage.TRANSLUCENT);
            graph = buffer.createGraphics();

            graph.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            graph.drawImage(stances[state], 0, 0, null);
            graph.drawImage(redScreen, 0, 0, null);
            graph.dispose();

            if(needsRefresh)
                needsRefresh = false;
        }
    }
    
    public void reset() {
        this.isActive = false;
        this.state = STATE_CLEAR;
        initScreen();
    }
    
    private static final String DEFAULT_PATH = "resources/interface/screen/";
    
    public static final int STATE_CLEAR = -1;
    public static final int STATE_BLOOD = 0;
    
    private final String[] COLLECTION_PATHS = {"blood_screen.png", "red_screen.png"};
    private final int TRIGGER_EVENT_HEALTH = 75;
    
    private static ScreenImage screen;
    
    public static void initialize(int state) {
        screen = new ScreenImage(state);
        Paintables.registerInterface(screen);
    }
    
    public static ScreenImage getScreen() {return screen;}

    @Override
    public boolean isActive() {
        return isActive;
    }

    @Override
    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public boolean showOnlyOnEditor() {
        return false;
    }
}