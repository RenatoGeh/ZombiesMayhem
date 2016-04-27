package geometryzombiesmayhem;

import geometryzombiesmayhem.io.State;
import geometryzombiesmayhem.io.StateManager;
import geometryzombiesmayhem.io.StateManager.StateStatus;
import geometryzombiesmayhem.leveleditor.LevelEditor;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.net.MalformedURLException;
import java.util.concurrent.TimeUnit;
import javax.swing.JFrame;

/**
 * Frame where game runs.
 *
 * @author Yan Soares Couto
 * @author Renato Lui Geh
 *
 * @version 0.0.2
 */
public class GameFrame extends JFrame {

    private static final Toolkit toolkit = ZM.TOOLKIT;
    public final static float LAND_DEVIATION = 1f;
    public final static int FRAME_WIDTH = 817;
    public final static int FRAME_HEIGHT = 623;
    protected static int waveNumber = 0;
    public static int LEVEL_MAX_HEIGHT = -1;
    public static int translateX = 0;
    public static int translateY = 0;
    public static int maxTX = Integer.MAX_VALUE;
    public static int maxTY = Integer.MAX_VALUE;
    public static boolean cameraFollowsPlayer = true;
    public static Vector2D mousePosition = new Vector2D(Vector2D.CENTER.copy());
    public static GameFrame.PaintArea paintArea;
    public Vector2D DEFAULT_PAINT_SIZE;
    public static boolean isPaused = false;
    protected static boolean isInitialized = false;
    private Cursor gameCursor;
    private String username;
    private boolean firstMouse = true;
    private Robot robot;
    public static float velocity = 1f;
    public static boolean pauseInterference = false;
    public static boolean godMode = false;
    public static boolean onEditor = false;
//    FlareShape.Conical cone;// = new FlareShape.Conical(mousePosition, Player.getDefaultPlayer().getPosition(), 150, (float)Math.PI/6, Color.YELLOW);
//    MutableImage pi;
//    FlareShape.Polygonal poly;
//    FlareShape.Trapezoid trap;
    
    private final AwesomeAction paintAction = new AwesomeAction() {
        @Override
        public void run() {
            if (isInitialized) {
                paintArea.repaint();
            }
        }
    };
    private final UpdateAction bodyAction = new UpdateAction() {
        @Override
        public void update(long dt) {
            if (!isPaused) {
                if (currentLevel != null) {
                    currentLevel.update();
                }
                Body.updateBodies(((long) (dt * velocity)));
                AudioManager.checkChannels();
            }
        }
    };
//////////////////////////  test
    public static Level currentLevel;
//////////////////////////    

    public GameFrame() throws MalformedURLException, InterruptedException, AWTException {
        super("Zombies Mayhem!");
//////////////        test


//        inventory.add(new TextBox(new Vector2D(0, 0), "See ya later, SHITLORDS!", ColorFactory.getColor(Color.YELLOW, 255), FontFactory.ZOMBIE_FONT.deriveFont(30f), 50));

//////////////////        
        robot = new Robot();
//        this.setUndecorated(!GameSettings.Graphics.hasFrame());


    }

    public void startStuff(String username, boolean online, String host, int port, StateStatus state, boolean leveleditor) {

//        LightManager.setDarkness(250);
        paintArea = new PaintArea(host, port);
        if(leveleditor) {
            LevelEditor.start();
            if(state!=null)
                StateManager.rawLoading(state.getType(), state.getPath());
        } else if(state != null) {
            StateManager.rawLoading(state.getType(), state.getPath());
            GameOverMenu.setAutoState(state);
        } else {
            StateManager.loadLevel("demo");
            GameOverMenu.setAutoState(new StateStatus(StateStatus.DEFAULT_LEVEL_PATH+"demo.level"));
        }
        
        initControls();
        initInterface();
        UserInterface.initialize(mousePosition);
        if (online) {
            Client.initialize(Player.getDefaultPlayer(), host, port);
            initOtherPlayers();
        }
        initActions();

        this.setIconImage(ZM.icon);
        this.add(paintArea);
        this.setSize(GameSettings.Graphics.getDefaultResolution());
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(GameSettings.Graphics.isResizable());
//        paintArea.setSize(FRAME_WIDTH,FRAME_HEIGHT);

        //Only for Windows!     
        //System.setProperty("sun.java2d.translaccel", "true");

        Dialog.load(gAdapter);

        DEFAULT_PAINT_SIZE = new Vector2D(paintArea.getWidth(), paintArea.getHeight());

        this.setSize(GameSettings.Graphics.getResolution());
        GameSettings.Graphics.centerWindow(this);

        isInitialized = true;

        System.err.println("Game Initializing with: " + this.toString());

        LightManager.setColor(State.getDarknessColor());
//        LightManager.setDarkness(150);
//        cone = new FlareShape.Conical(mousePosition, Player.getDefaultPlayer().getPosition(), 150, (float)Math.PI/6, LightManager.getColor());
//        poly = new FlareShape.Polygonal(new Vector2D[] {new Vector2D(100, 300), new Vector2D(100, 500), new Vector2D(500, 500), new Vector2D(500, 300)}, 50, 50, 255, LightManager.getColor());
//        trap = new FlareShape.Trapezoid(mousePosition, Vector2D.CENTER.copy(), 100, 300, 200, 50, LightManager.getColor());
    
//        AudioManager.generateLine("Background_Music", "resources/sounds/music/ambient/music.wav", true, true, true, false);
//        AudioManager.setVolume(100);
    }

    private void setSize(Vector2D size) {
        this.setSize((int) size.getX(), (int) size.getY());
    }

    private void initControls() {
        this.addKeyListener(gAdapter);
    }

    private void initOtherPlayers() {
        Player p2 = new Player("Player 2", Vector2D.BOTTOM_LEFT.copy(), 100f);

        Player.register(p2);
    }

    private void initInterface() {
        gameCursor = toolkit.createCustomCursor(toolkit.createImage(new byte[0]), new java.awt.Point(0, 0), "Invisible");
    }

    private static void debug(String s) {
        System.err.println(s);
    }

    private void initActions() {
        AwesomeTimer.addAction(paintAction, ZM.milliToTick, true, TimeUnit.MILLISECONDS);
        AwesomeTimer.addUpdateAction(bodyAction, 5);
    }

    public class PaintArea extends WindowP {

        public Storage.Inventory e;

        public PaintArea(String host, int port) {
            super(GameFrame.this);
            this.setCursor(gameCursor);
            initMenus(host, port);
            this.removeMouseListeners();
            this.addMouseListener(gAdapter);
            this.addMouseMotionListener(gAdapter);
            this.addMouseWheelListener(gAdapter);
            this.addComponentListener(gAdapter);

            e = new Storage.Inventory(new Vector2D(100, 100));
            this.setDefShape();
//            e.setVisible(true);
//            this.add(e);
        }

        private void initMenus(String host, int port) {
            if (Client.isActive()) {
                ChatFrame.initialize(username, host, port);
            }
        }

        @Override
        public void paint(Graphics g) {
            //Order: SceneLayer -> Bodies -> Interface

            Graphics2D g2 = checkEnvironment(g);

            if (isInitialized) {
                UserInterface.manageResolution(g2);

                camera(g2);
                checkScene(g2);
                performAI(g2);
                checkBodies(g2);
                checkForegroundScene(g2);
                checkPaintables(g2);
                checkOtherPlayers(g2);
                g2.dispose();
            }
        }

        public void camera(Graphics2D g2) {
            if(focusBody==null) focusBody = Player.getDefaultPlayer();
            int ntranslateX = translateX;
            int ntranslateY = translateY;
            if (cameraFollowsPlayer) {
                int xtranslateX = 0, ytranslateY = 0;
                if (focusBody.getPosition().getX() > FRAME_WIDTH / (2)) {
                    xtranslateX = (int) (focusBody.getPosition().getX() - FRAME_WIDTH / (2));
                }

                xtranslateX = Math.min(maxTX, xtranslateX);
                if (Math.abs(xtranslateX - translateX) < 10) {
                    if(translateX>xtranslateX) translateX--;
                    else if(translateX<xtranslateX) translateX++;
                } else {
                    translateX += (xtranslateX - translateX) / 10;
                }
                if (focusBody.getPosition().getY() < FRAME_HEIGHT / (2)) {
                    ytranslateY = (int) (-focusBody.getPosition().getY() + FRAME_HEIGHT / (2));
                }
                ytranslateY = Math.min(maxTY, ytranslateY);
                if (Math.abs(ytranslateY - translateY) < 10) {
                    if(translateY>ytranslateY) translateY--;
                    else if(translateY<ytranslateY) translateY++;
                } else {
                    translateY += (ytranslateY - translateY) / 10;
                }
            }            
            ntranslateX = translateX - ntranslateX;
            ntranslateY = translateY - ntranslateY;
            if (ntranslateX != 0 || ntranslateY != 0) {
                mousePosition.addLocal(ntranslateX, -ntranslateY);
            }
            tx = translateX; ty = translateY;
            zoom(g2,z);
            doCamera(g2);
        }
        int tx,ty;
        float sx=1,sy=1;
        float z = 1f;
        public Body focusBody = null;
        private void zoom(Graphics2D g2,float z) {
            float side = (FRAME_HEIGHT/z);
            int aux = (int) ((FRAME_HEIGHT - side)/2);
            float side2 = (FRAME_WIDTH/z);
            int aux2 = (int) ((FRAME_WIDTH - side2)/2);
            tx+=aux2; ty-=aux;
            sx = sy = z;
        }
        
        public void doCamera(Graphics2D g2) { g2.scale(sx, sy);
            g2.translate(-tx, ty);
             
        }
        public void undoCamera(Graphics2D g2) {g2.translate(tx,-ty);
            g2.scale(1/sx, 1/sy);
            
        }

        public Graphics2D checkEnvironment(Graphics g) {
            super.paint(g);

            if (this.getCursor().equals(Cursor.getDefaultCursor())) {
                this.setCursor(gameCursor);
            }

            Graphics2D g2 = (Graphics2D) g;

            GameSettings.Graphics.apply(g2);

            g2.setColor(Color.BLACK);

            return g2;
        }

        public void checkPaintables(Graphics2D g2) {
            Paintables.paint(g2, this);
        }

        public void checkScene(Graphics2D g2) {
            if (currentLevel != null)
                if(currentLevel.getScene() != null)
                    currentLevel.getScene().paint(g2, this);
        }
        
        public void checkForegroundScene(Graphics2D g2) {
            if(currentLevel != null)
                if(currentLevel.getScene() != null)
                    currentLevel.getScene().paintForeground(g2, this);
        }

        public void checkBodies(Graphics2D g2) {
            Body.paintBodies(g2, this);
        }

        public void performAI(Graphics2D g2) {
            AIManager.update();
        }

        public void checkOtherPlayers(Graphics2D g2) {
            Player.managePlayers(Client.isActive(), g2, this, Client.getClient());
        }

        public void help(MouseEvent e) {
            this.processMouseEvent(e);
        }
    }

    public MouseEvent getFixedEvent(MouseEvent e) {
        return new MouseEvent(GameFrame.this, e.getID(), e.getWhen(), e.getModifiers(), (int) mousePosition.getX() - translateX, (int) mousePosition.getY() + translateY,
                e.getClickCount(), e.isPopupTrigger(), e.getButton());
    }
    public GeneralAdapter gAdapter = new GeneralAdapter() {
        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            super.mouseWheelMoved(e);
            Player.getDefaultPlayer().mouseWheelMoved(e);
        }

        @Override
        public void keyPressed(KeyEvent event) {
            super.keyPressed(event);
            if (!isPaused && !ChatFrame.isVisible() && !Console.isVisible()) {
                Player.getDefaultPlayer().keyPressed(event);
            }
            if (event.getKeyCode() == KeyEvent.VK_F1) {
                UserInterface.toggleDebug();
            }
            if (event.getKeyCode() == KeyEvent.VK_F2) {
                UserInterface.toggleTheater();
            }
            if (event.getKeyCode() == KeyEvent.VK_F3) {
                paintArea.z+=.1f;
//                onEditor = !onEditor;
            }
            if (event.getKeyCode() == KeyEvent.VK_F12) {
                ScreenCapture.printScreen(paintArea, GameSettings.Graphics.getHints(), true);
            }
//            if (event.getKeyCode() == KeyEvent.VK_F) {
//                cone.toggleEnabled();
//                cone.setPeriods(125, 250, 500, 750, 1000);
//                LightBulb bulb = new LightBulb.Conical(1, true, cone);
//                Dimmer d = new Dimmer(0, .01f, new Vector2D(200, 500));
//                d.add(bulb);
//                Body.register(d);
//                Body.register(bulb);
//            }
//            if (event.getKeyCode() == KeyEvent.VK_B) {
//                cone.setBlinking(!cone.isBlinking());
//            }
//            if(event.getKeyCode() == KeyEvent.VK_P) {
//               if(LightManager.contains(trap))
//                   LightManager.remove(trap);
//               else
//                   LightManager.register(trap);
//            }
            if (event.getKeyCode() == KeyEvent.VK_I) {
                if (!paintArea.e.isVisible()) {
                    paintArea.add(paintArea.e);
                    paintArea.e.setVisible(true);
                } else {
                    paintArea.remove(paintArea.e);
                    paintArea.e.setVisible(false);
                }
            }

            if (event.isAltDown() && event.getKeyCode() == KeyEvent.VK_F4) {
                System.exit(0);
            }
            
//            if(event.getKeyCode() == KeyEvent.VK_PERIOD)
//                GameFrame.paintArea.z += .01f;
//            else if(event.getKeyCode() == KeyEvent.VK_COMMA)
//                GameFrame.paintArea.z -= .01f;
        }

        @Override
        public void keyReleased(KeyEvent event) {
            super.keyReleased(event);
            if (!isPaused && !ChatFrame.isVisible() && !Console.isVisible()) {
                Player.getDefaultPlayer().keyReleased(event);
            }

            if (event.getKeyCode() == KeyEvent.VK_ESCAPE) {
                if (pauseInterference) {
                    return;
                }

                isPaused = !isPaused;
                if (isPaused) {
                    PauseMenu.getInstance().load(GameFrame.paintArea);
                } else {
                    PauseMenu.getInstance().close(GameFrame.paintArea);
                }
            }

//            if (event.getKeyCode() == KeyEvent.VK_ESCAPE) {
//                isPaused = !isPaused;
//                if (isPaused) {
//                    robot.mouseMove((int) (paintArea.getLocationOnScreen().x + mousePosition.getX() - translateX),
//                            (int) (paintArea.getLocationOnScreen().y + mousePosition.getY()));
//                }
//            }
        }

        @Override
        public void mouseMoved(MouseEvent event) {
            super.mouseMoved(event);
            {
                paintArea.gA.mouseMoved(getFixedEvent(event));
            }
            mouseMoving(event);
        }

        public void mouseMoving(MouseEvent event) {
            if (!isPaused) {
                Player.getDefaultPlayer().mouseMoved(event, mousePosition);
                Vector2D diff = (new Vector2D(event.getX(), event.getY())).subtractLocal(Vector2D.CENTER);
                mousePosition.addLocal(diff);

                if (mousePosition.getX() < translateX) {
                    mousePosition.setX(translateX);
                } else if (mousePosition.getX() > translateX + FRAME_WIDTH) {
                    mousePosition.setX(translateX + FRAME_WIDTH);
                }
                if (mousePosition.getY() < -translateY) {
                    mousePosition.setY(-translateY);
                } else if (mousePosition.getY() > FRAME_HEIGHT - translateY) {
                    mousePosition.setY(FRAME_HEIGHT - translateY);
                }

                robot.mouseMove((int) (event.getXOnScreen() - diff.getX()), (int) (event.getYOnScreen() - diff.getY()));

            } else {
                mousePosition.set(event.getX(), event.getY()).addLocal(translateX, -translateY);
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            super.mouseDragged(e);
            {
                paintArea.gA.mouseDragged(getFixedEvent(e));
            }
            mouseMoving(e);
        }

        @Override
        public void mouseClicked(MouseEvent event) {
            super.mouseClicked(event);
//            if(event.getButton() == MouseEvent.BUTTON2)
//                poly.addPoint(mousePosition);
//            if (!isPaused) {
////                if (event.getButton() == MouseEvent.BUTTON2 && !isPaused)
////                    Paintables.register(new ImageFader().new Text(mousePosition.copy(), "{}{}{}{}{}{}{}", FontFactory.ZOMBIE_FONT, ColorFactory.getRandomColor(ColorFactory.Type.Integer, false), .01f));
//                
            paintArea.gA.mouseClicked(getFixedEvent(event));
//            } else if (pauseMenu.checkRectangles(mousePosition, event)) {
//                isPaused = !isPaused;
//            }
        }

        @Override
        public void mousePressed(MouseEvent event) {
            super.mousePressed(event);
            Player.getDefaultPlayer().mousePressed(event, mousePosition);
            {
                paintArea.gA.mousePressed(getFixedEvent(event));
            }
        }

        @Override
        public void mouseReleased(MouseEvent event) {
            super.mouseReleased(event);
            Player.getDefaultPlayer().mouseReleased(event);
            {
                paintArea.gA.mouseReleased(getFixedEvent(event));
            }
        }

        @Override
        public void keyTyped(KeyEvent event) {
            super.keyTyped(event);
            if (Client.isActive()) {
                if (event.getKeyChar() == 't') {
                    ChatFrame.setVisible(true);
                }
                if (ChatFrame.isVisible()) {
                    ChatFrame.setVisible(ChatFrame.getChat().keyTyped(event));
                }
            }
            if (event.getKeyChar() == '`' || event.getKeyChar() == '\'') {
                Console.setVisible(!Console.isVisible());
                return;
            }
            if (Console.isVisible()) {
                Console.getConsole().handleText(event);
                return;
            }
//            if (event.getKeyChar() == 'o') {
//                Paintables.register(new ImageFader().new Text(mousePosition.copy(), "Le Awesome Projectile!", new Font(Font.DIALOG, Font.BOLD, 30), Color.GREEN, .05f));
//                ProjectileType.ARROW.shoot(Player.getDefaultPlayer().getPosition(), mousePosition);
//            }
//            if (event.getKeyChar() == 'y') {
//                ProjectileType.LASER.shoot(Player.getDefaultPlayer().getPosition(), mousePosition);
//            }
//            if (event.getKeyChar() == 'u') {
//                velocity += .1f;
//            }
//            if (event.getKeyChar() == 'i') {
//                velocity -= .1f;
//            }
//            if (event.getKeyChar() == 'x') {
//                UserInterface.println("NANANANA!");
//            }
            if (event.getKeyChar() == 'b') {
                currentLevel.close();
                currentLevel = null;
            }
            if (event.isAltDown() && event.getKeyChar() == 'r') {
                UserInterface.getRadioPlayer().toggleInteractible();
            }
//            Radio.keyTyped(event);
        }

        @Override
        public void componentResized(ComponentEvent e) {
            if (isInitialized) {
                UserInterface.fixResolution(paintArea.getWidth(), paintArea.getHeight(), DEFAULT_PAINT_SIZE);
            }
        }
    };
}
