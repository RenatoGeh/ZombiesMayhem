package geometryzombiesmayhem;

import geometryzombiesmayhem.Menu;
import geometryzombiesmayhem.io.StateManager.StateStatus;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.net.MalformedURLException;
import java.util.Random;
import javax.swing.JFrame;

/**
 * A Game about zombies and shit.
 *
 * @author Yan Soares Couto
 * @author Renato Geh
 * @version 0.1
 */
public class ZM extends JFrame {

    //TODOS
    //MEDIUM/MAJOR CHANGES
    //TODO: Make diagonal moveable bounds better
    //TODO: Level One
    //TODO: Create our own art
    //TODO: Working Demo
    //TODO: Sounds
    //TODO: MOAR images and sprites
    //TODO: Scripting (aka cutscenes)
    //TODO: Options and Extras Menu
    //TODO: Fix AI/Zombies
    //MINIMAL CHANGES
    //TODO: Change all big big ifs to switches
    //\TODOS
    //DONE
    //\DONE
    //All static game-use methods are placed here!
    public static float convertNewtons(float f) {
        return ((f * pixelsPerMeter) / (ticksPerSecond * ticksPerSecond));
    }

    public static float convertVelocityMetersPerSecond(float f) {
        return (f * pixelsPerMeter) / ticksPerSecond;
    }

    public static float convertVelocityKilometerPerHour(float f) {
        return convertVelocityMetersPerSecond(f / 3.6F);
    }
    //All static and final game-use fields are placed here!
    public final static Toolkit TOOLKIT = Toolkit.getDefaultToolkit();
    public final static Random RANDOM = new Random();
    public final static Random SEEDED_RANDOM = new Random(10);
    public final static Graphics2D GRAPHICS = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB_PRE).createGraphics();
    public final static Image icon = AssetManager.loadRawImage("resources/interface/icon.png");
    public final static int milliToTick = 20;
    public final static float ticksPerSecond = 1000 / milliToTick;
    public final static float realPixelsPerMeter = 3779.527559055F;
    public final static float pixelsPerMeter = 400F;
    public final static float earthGravity = 9.80065F;
    public final static float gravity = earthGravity;
    public final static String version = "0.0.0.7 - Early-Pre-Alpha";
    public final static String versioning = "";
    public final ImageObserver io = new ImageObserver() {

        @Override
        public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
            return true;
        }
    };

    public Vector2D mousePosition = Vector2D.CENTER.copy();
    
    public static GameFrame gameFrame;
    public static ZM main;
    protected final MenuArea menuArea = new MenuArea(this);
    protected Menu menu = null;
    private final int NUMBER_ZOMBIES = 10;
    private Scene backScene;
    protected static Image menuCursor;
    private Sentence versionLabel = new Sentence(new Vector2D(5, 5), "alpha", 0, 0, .25f);
    private Vector2D offsetPosition = Vector2D.ZERO.copy();
    
    public Vector2D DEFAULT_PAINT_SIZE;
    private boolean isInitialized = false;
    public boolean isFunctional = true;
    public ConfigLoader loader;
    
    /**
     * The beginning of all
     *
     * @param args will not be used
     */
    public static void main(String[] args) throws MalformedURLException, InterruptedException {   
        main = new ZM();
    }

    public ZM() throws MalformedURLException, InterruptedException {
        super("Zombies Mayhem!");

        if (ConfigLoader.isFirstRun()) {
            loader = new ConfigLoader(new ConfigurationFile("resources/config.ini", true));
            loader.export(ConfigLoader.DEFAULT_CONFIG_PATH);
        } else {
            loader = new ConfigLoader(new ConfigurationFile(ConfigLoader.DEFAULT_CONFIG_PATH, false));
        }

        this.add(menuArea);
        this.setSize((int) GameSettings.Graphics.getResolution().getX(), (int) GameSettings.Graphics.getResolution().getY());
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(GameSettings.Graphics.isResizable());
        this.setIconImage(icon);

        GameSettings.Graphics.handleScreenMode(this);

        this.setVisible(true);

        initBodies();
        initScene();
        initTimers();
        initControls();

        Radio.setStyle(Radio.PlayStyle.SHUFFLE_ANY);

        AudioManager.setVolume(0);

        changeMenu(MainMenu.getMenu());

        DEFAULT_PAINT_SIZE = new Vector2D(menuArea.getWidth(), menuArea.getHeight());
        isInitialized = true;
    }

    private void initControls() {
        this.setCursor(TOOLKIT.createCustomCursor(TOOLKIT.createImage(new byte[0]), new java.awt.Point(0, 0), "Invisible"));
        menuCursor = AssetManager.loadImage("resources/interface/cursor.png");

        menuArea.gA.add((MouseAdapter) gAdapter); //for some reason it "overrides" the frame mouse adapter, so I add it as a son to the main area adapter
        this.addKeyListener(gAdapter);
        this.addComponentListener(gAdapter);
    }

    private void initTimers() {
        AwesomeTimer.addAction(mainAction, ZM.milliToTick * 1000000, true);
        AwesomeTimer.addUpdateAction(updater, 5);
    }

    public final void changeMenu(Menu newMenu) {
        if (menu != null) {
            menu.close(menuArea);
        }
        menu = newMenu;
        if (menu != null) {
            menu.load(menuArea);
        }
    }
    private AwesomeAction mainAction = new AwesomeAction() {

        @Override
        public void run() {
            if (isInitialized && isFunctional) {
                Body.updateBodies(ZM.milliToTick);
                menuArea.repaint();
                offsetPosition = Vector2D.convertFrom(main.getLocation());
            }
        }
    };
    private UpdateAction updater = new UpdateAction() {

        @Override
        public void update(long delta) {
            Body.updateBodies(delta);
        }
    };

    private void initBodies() {
        for (int i = 0; i < NUMBER_ZOMBIES; i++) {
            Vector2D position;
            int multiplier;

            if (RANDOM.nextBoolean()) {
                multiplier = 1;
            } else {
                multiplier = -1;
            }

            if (RANDOM.nextBoolean()) {
                position = Vector2D.BOTTOM_LEFT.subtract(50, 100);
            } else {
                position = Vector2D.BOTTOM_RIGHT.add(50, 100);
            }

            Zombie z = new Zombie("Zombie_" + i, position, Zombie.getDefaultSprite(), 100f, true);
            z.Vx = (multiplier * (RANDOM.nextInt(1) + RANDOM.nextFloat() + RANDOM.nextFloat() / 10 + RANDOM.nextFloat() / 10)) - (multiplier * .4f);
            z.showBars = false;
//            z.hasFriction = false;
            z.lookAt(new Vector2D(Math.signum(z.Vx) * 5000, z.getPosition().getY()));

            Body.register(z);
        }
    }

    private void initScene() {
        backScene = new Scene("Background Scene");

        backScene.addLayer(new SceneLayer("Sky", AssetManager.loadImage("resources/backgrounds/sky_city_burn_big.png"), .05f, true, Vector2D.TOP_LEFT.copy(), false));
        backScene.addLayer(new SceneLayer("Land", AssetManager.loadImage("resources/backgrounds/land_rail_burn.png"), .1f, true, Vector2D.BOTTOM_LEFT.copy().subtract(0, 290), false));
    
        Body.register(new Bound.Simple(new Line2D.Double(-100, 600 , 900, 600), null, true));
    }

    protected void play(boolean online, String host, int port, String username, StateStatus state, boolean leveleditor) throws MalformedURLException, InterruptedException, AWTException {
        isFunctional = false;
        AwesomeTimer.remove(mainAction);
        AwesomeTimer.remove(updater);

        Body.removeAll();
        Zombie.removeAll();
        Paintables.clearAll();

        gameFrame = new GameFrame();
        gameFrame.startStuff(username, online, host, port, state, leveleditor);

        this.dispose();
    }
    
    public GeneralAdapter gAdapter = new GeneralAdapter() {

        @Override
        public void keyPressed(KeyEvent event) {
            super.keyPressed(event);
            if (event.getKeyCode() == KeyEvent.VK_F12) {
                ScreenCapture.printScreen(menuArea, GameSettings.Graphics.getHints(), false);
            }
        }

        @Override
        public void keyTyped(KeyEvent event) {
            super.keyTyped(event);
        }

        @Override
        public void mouseMoved(MouseEvent event) {
            super.mouseMoved(event);
            mousePosition.set(Vector2D.convertFrom(event.getLocationOnScreen()).subtract(offsetPosition).subtract(GameSettings.Graphics.getInsets()));
        }

        @Override
        public void mouseDragged(MouseEvent event) {
            super.mouseDragged(event);
            mouseMoved(event);
        }

        @Override
        public void mousePressed(MouseEvent event) {
            super.mousePressed(event);
        }

        @Override
        public void componentResized(ComponentEvent e) {
            super.componentResized(e);
            if (isInitialized) {
                UserInterface.fixResolution(menuArea.getWidth(), menuArea.getHeight(), DEFAULT_PAINT_SIZE);
            }
        }
    };

    protected class MenuArea extends WindowP {        
        
        public MenuArea(JFrame f) {
            super(f);
        }
        @Override
        public void paint(Graphics g) {
            Graphics2D g2 = checkEnvironment(g);

            if (isFunctional && isInitialized) {
                UserInterface.manageResolution(g2);
                this.manageBack(g2);

                Paintables.paint(g2, this);

                g2.drawImage(menuCursor, (int) mousePosition.getX(), (int) mousePosition.getY(), this);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font(Font.DIALOG, Font.BOLD, 30));
                //if(Radio.getCurrent() != null) g2.drawString(Radio.getRemainingTime(), Vector2D.CENTER.getX(), Vector2D.CENTER.getY());
                versionLabel.paint(g2, this);
                
                g2.dispose();
            } else if (isInitialized) {
                g2.dispose();
            }
        }

        private Graphics2D checkEnvironment(Graphics g) {
            super.paint(g);

            Graphics2D g2 = (Graphics2D) g;
            GameSettings.Graphics.apply(g2);
            g2.setColor(Color.BLACK);

            return g2;
        }

        private void checkAI() {
            for (Zombie z : Zombie.getList()) {
                if (z.isOutOfBounds()) {
                    if (z.getPosition().getX() <= (GameFrame.FRAME_WIDTH - 100)) {
                        if (z.Vx < 0) {
                            z.Vx = -z.Vx;
                            z.lookAt(new Vector2D(Math.signum(z.Vx) * 5000, z.getPosition().getY()));
                        }
                    } else if (z.getPosition().getX() >= (GameFrame.FRAME_WIDTH + 100)) {
                        if (z.Vx > 0) {
                            z.Vx = -z.Vx;
                            z.lookAt(new Vector2D(Math.signum(z.Vx) * 5000, z.getPosition().getY()));
                        }
                    }
                }
            }
        }

        private void checkScene(Graphics2D g2) {
            backScene.paint(g2, this);
        }

        private void checkBodies(Graphics2D g2) {
            Body.paintBodies(g2, this);
        }

        private void manageBack(Graphics2D g2) {
            this.checkScene(g2);
            this.checkBodies(g2);
            this.checkAI();
        }
    }
}
