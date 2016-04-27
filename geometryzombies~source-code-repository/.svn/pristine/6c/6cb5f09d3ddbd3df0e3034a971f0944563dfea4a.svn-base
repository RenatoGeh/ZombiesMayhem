package geometryzombiesmayhem;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.util.concurrent.TimeUnit;

/**
 * 
 * Represents the game's GUI - Graphical User Interface.
 * 
 * @author Renato Lui Geh
 */
public class UserInterface implements Paintable {
    public static final MutableImage DEFAULT_CURSOR = new MutableImage(ZM.menuCursor,"resources/interface/cursor.png").resize(.75f, .75f);

    private Rectangle2D.Float healthRect, baseHealthRect;
    private Rectangle2D.Float staminaRect, baseStaminaRect;
    private MutableImage aimImage;
    private MutableImage cursorImage = DEFAULT_CURSOR;
    private Vector2D mousePosition;
    private TimedTextBox debugBox;

    private RadioPlayer radioPlayer = new RadioPlayer(Vector2D.TOP_RIGHT.subtract(300, -50), 200);
//    private AimArc aimArc;
    
    private Font font = new Font(Font.DIALOG, Font.BOLD, 20);
    
    private boolean theaterMode = false;
    private boolean debugMode = false;
    protected boolean isActive = true;
    
    private boolean gunNote, healthNote = gunNote = false;
    
    public UserInterface(Vector2D mousePosition) {
        this.mousePosition = mousePosition;
        this.initElements();
    }

    private void initElements() {
        initBars();
        initAim();
        initScreen();
        initConsole();
        initDebugBox();
    }
    
    private void initBars() {
        healthRect = new Rectangle2D.Float(30, GameFrame.FRAME_HEIGHT-30, Player.getDefaultPlayer().getHealth() * 2, 20);
        staminaRect = new Rectangle2D.Float(GameFrame.FRAME_WIDTH - 225, GameFrame.FRAME_HEIGHT-30, Player.getDefaultPlayer().getHealth() * 2, 20);
        
        baseHealthRect = new Rectangle2D.Float(30, GameFrame.FRAME_HEIGHT-30, (Player.getDefaultPlayer().getStamina() * 2) + 2, 22);
        baseStaminaRect = new Rectangle2D.Float(GameFrame.FRAME_WIDTH - 225, GameFrame.FRAME_HEIGHT-30, (Player.getDefaultPlayer().getStamina() * 2) + 2, 22);
    }
    
    private void initScreen() {
//        ScreenImage.initialize(ScreenImage.STATE_BLOOD);
//        Paintables.register(radioPlayer);
    }
    
    private void initAim() {
        aimImage = AssetManager.loadImage("resources/interface/aim_red_small.png");
//        aimArc = new AimArc(Player.getDefaultPlayer().getPosition(), GameFrame.mousePosition);
    }
    private void initConsole() {Console.initialize("Zombies Mayhem! Command Prompt. Type \"help\" for commands.", GameFrame.FRAME_WIDTH + 17);}
    
    private void initDebugBox() {
        debugBox = new TimedTextBox(Vector2D.BOTTOM_LEFT.add(10, -150), "D3BUGG1N' L13K 4 B055!!11", Color.BLACK, 180, new Font(Font.DIALOG_INPUT, Font.PLAIN, 12), 40, 6, TimeUnit.SECONDS, false);
        debugBox.pagesDown(true);
        debugBox.setFixedSize(300, 125);
        debugBox.setFontColor(Color.WHITE);
        debugBox.setEraseDelay(2700,TimeUnit.MILLISECONDS);
    }
    
    public synchronized void notifyHealth() {
        if(this.healthNote) 
            if(this.healthNotifier.timeToSet < TimeUnit.SECONDS.toNanos(2))
                this.healthNotifier.timeToSet += TimeUnit.SECONDS.toNanos(4);

        this.healthNote = true;
        AwesomeTimer.addAction(healthNotifier, 4, false, TimeUnit.SECONDS);
    }
    
    public synchronized void notifyGun() {
        if(this.gunNote)
            if(this.gunNotifier.timeToSet < TimeUnit.SECONDS.toNanos(2))
                this.gunNotifier.timeToSet += TimeUnit.SECONDS.toNanos(4);
        
        this.gunNote = true;
        AwesomeTimer.addAction(gunNotifier, 4, false, TimeUnit.SECONDS);
    }
    
//    public synchronized void notifyStamina() {
//        if(this.staminaNote)
//            if(this.staminaNotifier.timeToSet < TimeUnit.SECONDS.toNanos(2))
//                this.staminaNotifier.timeToSet += TimeUnit.SECONDS.toNanos(4);
//        
//        this.staminaNote = true;
//        AwesomeTimer.addAction(staminaNotifier, 4, false, TimeUnit.SECONDS);
//    }
    
    @Override
    public void paint(Graphics2D g2, Component c) {
//        if(Player.getDefaultPlayer().isSprinting() || Player.getDefaultPlayer().Vy > 0)
//            this.notifyStamina();
        
        if(!theaterMode) {
            GameFrame.paintArea.undoCamera(g2);

            if(debugMode)
                this.drawInfo(g2, mousePosition);

            this.drawUI(g2);
            
            radioPlayer.paint(g2, c);
            this.drawConsole(g2, c);
            this.drawBars(g2);
            debugBox.paint(g2, c);

            Paintables.paintInterface(g2, c);

            this.drawClient(g2, c);

            GameFrame.paintArea.doCamera(g2);

            if(Player.getDefaultPlayer().isAiming())
                g2.drawImage(aimImage, (int)(mousePosition.getX() - aimImage.getWidth(null)/2), 
                        (int)(mousePosition.getY() - aimImage.getHeight(null)/2), c);
            else
                g2.drawImage(this.cursorImage, (int)mousePosition.getX(), (int)mousePosition.getY(), c);
        } else {
            if(PauseMenu.getInstance().isVisible() || GameOverMenu.getInstance().isVisible())
                GameFrame.paintArea.undoCamera(g2);
            
            Paintables.paintTheater(g2, c);
            
            if(PauseMenu.getInstance().isVisible() || GameOverMenu.getInstance().isVisible()) {
                GameFrame.paintArea.doCamera(g2);
                g2.drawImage(this.cursorImage, (int)mousePosition.getX(), (int)mousePosition.getY(), c);
            }
        }
    }
    
    private void drawUI(Graphics2D g2) {
        Color defColor = g2.getColor();
        Font defFont = g2.getFont();

        g2.setColor(Color.CYAN);
        g2.setFont(font);
        
        if(this.gunNote) {
            g2.drawImage(Player.getDefaultPlayer().getGun().getIcon(), null, 5, 5);
            g2.drawString(Player.getDefaultPlayer().getGun().getName(), 5, 125);
            g2.drawString("Ammo: " + Player.getDefaultPlayer().getGun().getAmmo(), 
                    5, g2.getFontMetrics().getHeight() + 125);
        
        g2.drawString("Level: " + Player.getDefaultPlayer().getLevel(), 
                Vector2D.TOP_RIGHT.getX() - (5 + 1.5f*g2.getFontMetrics().stringWidth("Level: " + Player.getDefaultPlayer().getLevel())), 
                g2.getFontMetrics().getHeight() + 10);
        }

        g2.setColor(defColor);
        g2.setFont(defFont);
    }
    
    private void drawInfo(Graphics2D g2, Vector2D mousePosition) {
        g2.setColor(Color.WHITE);
                
        g2.drawString("Zombies Mayhem " + ZM.version, 10, 20);
        g2.drawString("Wave " + GameFrame.waveNumber, 10, GameFrame.FRAME_HEIGHT - 50);
        g2.drawString("Player Position " + Player.getDefaultPlayer().getPosition().toString(), 10, GameFrame.FRAME_HEIGHT - (50 + g2.getFont().getSize2D()));
        g2.drawString("Mouse Position: " + mousePosition, 10, GameFrame.FRAME_HEIGHT - (50 + 2* g2.getFont().getSize2D()));
        g2.drawString("Player Velocity: " + "[" + Player.getDefaultPlayer().Vx + " ," + Player.getDefaultPlayer().Vy + "]", 10, 3*GameFrame.FRAME_HEIGHT/4);
        g2.drawString("Player's Stamina: " + Player.getDefaultPlayer().getStamina(), GameFrame.FRAME_WIDTH - 150, GameFrame.FRAME_HEIGHT - 50);
        g2.drawString("Camera Zoom: " + GameFrame.paintArea.z, 10, GameFrame.FRAME_HEIGHT - (50 + 3*g2.getFont().getSize2D()));
        
        if(Client.isActive()) {
            g2.drawString("Player 2 Position: " + Client.getClient().pp2.getPosition().toString()
                    , 10, GameFrame.FRAME_HEIGHT - (50 + 4 * g2.getFont().getSize2D()));
            g2.drawString("Chat Mode: " + ChatFrame.isVisible(), Vector2D.CENTER.getX(), Vector2D.CENTER.getY());
        }

        g2.setColor(Color.BLACK);
    }
    
    private void drawConsole(Graphics2D g2, Component c) {
        if(Console.isVisible())
            Console.getConsole().paint(g2, c);
    }
    
    private void drawBars(Graphics2D g2) {
        g2.setColor(Color.BLUE);
        g2.setStroke(new BasicStroke(4));
                                
        if(this.healthNote)
            this.drawHealth(g2);
        if(Player.getDefaultPlayer().getStamina() < 100)
            this.drawStamina(g2);
    }
    
    private void drawStamina(Graphics2D g2) {
        g2.setColor(Color.BLACK);
            g2.setStroke(new BasicStroke(4));
                g2.draw(baseStaminaRect);
                    g2.setColor(Color.LIGHT_GRAY);
                    g2.fill(baseStaminaRect);
                        g2.setColor(Color.YELLOW);
                        g2.setStroke(new BasicStroke(1));
                            staminaRect.setRect(staminaRect.getX(), staminaRect.getY(), Player.getDefaultPlayer().getStamina() * 2, staminaRect.getHeight());
                            g2.draw(staminaRect);
                            g2.fill(staminaRect);
                                g2.setColor(Color.BLACK);
                                g2.drawString(Integer.toString((int)Player.getDefaultPlayer().getStamina()), 
                                        (int)((baseStaminaRect.getX() + baseStaminaRect.getWidth()/2) - g2.getFont().getSize2D()),
                                        (int)((baseStaminaRect.getY() + baseStaminaRect.getHeight()/2)));
    }
    
    private void drawHealth(Graphics2D g2) {
        g2.setColor(Color.BLACK);
            g2.setStroke(new BasicStroke(4));
                g2.draw(baseHealthRect);
                    g2.setColor(Color.LIGHT_GRAY);
                    g2.fill(baseHealthRect);
                        g2.setColor(Color.RED);
                        g2.setStroke(new BasicStroke(1));
                            healthRect.setRect(healthRect.getX(), healthRect.getY(), Player.getDefaultPlayer().getHealth() * 2, healthRect.getHeight());
                            g2.draw(healthRect);
                            g2.fill(healthRect);
                                g2.setColor(Color.BLACK);
                                g2.drawString(Integer.toString((int)Player.getDefaultPlayer().getHealth()), 
                                        (int)((baseHealthRect.getX() + baseHealthRect.getWidth()/2) - g2.getFont().getSize2D()),
                                        (int)((baseHealthRect.getY() + baseHealthRect.getHeight()/2)));
    }
    
    private void drawClient(Graphics2D g2, Component c) {
        if(Client.isActive())
            ChatFrame.getChat().paint(g2, c);
    }
    
    public void setCursor(MutableImage cursor) {this.cursorImage = cursor;}
    public void resetCursor() {this.cursorImage = UserInterface.DEFAULT_CURSOR;}
    public MutableImage getCursor() {return this.cursorImage;}
    
    private AwesomeAction healthNotifier = new AwesomeAction() {
        @Override
        public void run() {
            UserInterface.this.healthNote = false;
        }
    };
    
//    private AwesomeAction staminaNotifier = new AwesomeAction() {
//        @Override
//        public void run() {
//            UserInterface.this.staminaNote = false;
//        }
//    };
    
    private AwesomeAction gunNotifier = new AwesomeAction() {
        @Override
        public void run() {
            UserInterface.this.gunNote = false;
        }
    };

    @Override
    public boolean showOnlyOnEditor() {
        return false;
    }
    
    public class RadioPlayer implements Paintable {
        private boolean isActive = true;
        private BufferedImage label;
        private Graphics2D graph;
        private String[] names;
        private RoundRectangle2D frame;
        private String current;
        
        private TextBox listBox;
        
        private Font font = new Font(Font.DIALOG, Font.BOLD, 20);
        private Color fontColor = Color.GREEN;
        
        private Vector2D labelPosition;
        private Vector2D marqueePosition;
        private final Vector2D fontSize;
        private float marqueeSpeed = .5f;
        
        private ImageButton[] buttons = new ImageButton[7];
        
        private boolean isInteractive = false;
        private boolean isPlaying = false;
        private boolean isListing = false;
        
        public RadioPlayer(Vector2D pos, int width) {
            label = new BufferedImage(width + 35, ZM.GRAPHICS.getFontMetrics(font).getHeight(), BufferedImage.TYPE_INT_ARGB_PRE);
            
            graph = label.createGraphics();
            names = Radio.getSongsNames();
            
            if(names == null || names.length == 0)
                names = new String[] {"<Empty>"};
            
            labelPosition = pos;
            
            String name = Radio.getCurrentSongName() == null? "<Empty>" : Radio.getCurrentSongName();
            
            fontSize = new Vector2D(graph.getFontMetrics(font).stringWidth(name), 
                    graph.getFontMetrics(font).getHeight() - graph.getFontMetrics(font).getDescent());
            
            marqueePosition = new Vector2D(0, fontSize.getY());
            
            current = Radio.getCurrentSongName();
            String foo = current == null? "<Empty>" : current;
            
            graph.drawString(foo, (int)labelPosition.getX(), (int)labelPosition.getY());
            graph.dispose();
            
            ImageButton playButton, pauseButton, stopButton, showButton;
            
            buttons[0] = playButton = new ImageButton(AssetManager.loadImage("resources/interface/radio_player/play_button_hover.png"),
                    AssetManager.loadImage("resources/interface/radio_player/play_button.png"), labelPosition.add(0, label.getHeight() + 25), 0);
            buttons[1] = pauseButton = new ImageButton(AssetManager.loadImage("resources/interface/radio_player/pause_button_hover.png"),
                    AssetManager.loadImage("resources/interface/radio_player/pause_button.png"), labelPosition.add(0, label.getHeight() + 25), 1);
            buttons[2] = stopButton = new ImageButton(AssetManager.loadImage("resources/interface/radio_player/stop_button_hover.png"),
                    AssetManager.loadImage("resources/interface/radio_player/stop_button.png"), labelPosition.add(50, label.getHeight() + 25), 2);
            buttons[3] = showButton = new ImageButton(AssetManager.loadImage("resources/interface/radio_player/show_button_hover.png"),
                    AssetManager.loadImage("resources/interface/radio_player/show_button.png"), labelPosition.add(label.getWidth() - playButton.currentImage.getWidth()/2, label.getHeight() + 25), 3);
            buttons[4] = new ImageButton(AssetManager.loadImage("resources/interface/radio_player/previous_button_hover.png"), 
                    AssetManager.loadImage("resources/interface/radio_player/previous_button.png"), labelPosition.add(50 + buttons[2].getWidth() + 5, label.getHeight() + 25), 4);
            buttons[5] = new ImageButton(AssetManager.loadImage("resources/interface/radio_player/forward_button_hover.png"), 
                    AssetManager.loadImage("resources/interface/radio_player/forward_button.png"), buttons[4].getPosition().add(buttons[4].getWidth() + 5, 0), 5);
            buttons[6] = new ImageButton(AssetManager.loadImage("resources/interface/radio_player/hide_button_hover.png"), 
                    AssetManager.loadImage("resources/interface/radio_player/hide_button.png"), buttons[3].getPosition(), 6);
            
            buttons[1].setEnabled(false);
            buttons[6].setEnabled(false);
            
            for(int i=0;i<buttons.length;i++)
                buttons[i].addActionListener(actionListener);
            
            frame = new RoundRectangle2D.Float(pos.getX() - 10, pos.getY() - 20, width + 80, 125, 10, 10);
        
            String list = ""; for(String s : names) list += s + "\n";
            
            listBox = new TextBox(new Vector2D((float)frame.getX(), (float)(frame.getY() + frame.getHeight())),
                    list, fontColor, font, 30);
            listBox.setColor(ColorFactory.getColor(Color.CYAN.darker(), 200));
            listBox.setRoundedEdges(true, 5, 5);
        }

        public void setMarqueeSpeed(float speed) {this.marqueeSpeed = speed;}
        
        protected void shift(boolean toPlay) {
            if(current == null)
                return;
            
            buttons[RADIO_PLAYER_PAUSE].setEnabled(toPlay);
            
            if(toPlay)
                Radio.playRadio();
            else
                Radio.pause();
            
            buttons[RADIO_PLAYER_PLAY].setEnabled(!toPlay);
            
            GameFrame.paintArea.add(toPlay? buttons[RADIO_PLAYER_PAUSE] : buttons[RADIO_PLAYER_PLAY]);
            Paintables.registerInterface(toPlay? buttons[RADIO_PLAYER_PAUSE] : buttons[RADIO_PLAYER_PLAY]);
            
            GameFrame.paintArea.remove(!toPlay? buttons[RADIO_PLAYER_PAUSE] : buttons[RADIO_PLAYER_PLAY]);
            Paintables.removeInterface(!toPlay? buttons[RADIO_PLAYER_PAUSE] : buttons[RADIO_PLAYER_PLAY]);

            isPlaying = toPlay;
        }

        @Override
        public boolean isActive() {return isActive;}
        @Override
        public void setActive(boolean active) {this.isActive = active;}
        
        public boolean isPlaying() {return isPlaying;}

        private synchronized void update() {
            graph = label.createGraphics();
            graph.setColor(fontColor);
            graph.setFont(font);
            
            marqueePosition.subtractLocal(marqueeSpeed, 0);
            
            for(int i=0;i<label.getWidth();i++)
                for(int j=0;j<label.getHeight();j++)
                    label.setRGB(i, j, 0);
            
            String name = Radio.getCurrentSongName();
            if(name != null)
                current = name;

            String foo = "Name: " + current == null? "<Empty>" : current;
            
            if(current != null)
                foo += " / Track time: " + Radio.getTimePosition(current) + " - " + Radio.getAbsoluteMusicTime(current);
            
            fontSize.set(graph.getFontMetrics(font).stringWidth(foo == null? "<Empty>" : foo), fontSize.getY());
            
            if(marqueePosition.getX() < -fontSize.getX())
                marqueePosition.set(label.getWidth(), marqueePosition.getY());
            
            graph.drawString(foo == null? "<Empty>" : foo, (int)marqueePosition.getX(), (int)marqueePosition.getY());
            graph.dispose();
        }
        
        @Override
        public void paint(Graphics2D g2, Component c) {
            if(isInteractive) {
                Color defColor = g2.getColor();
                Stroke s = g2.getStroke();
                
                g2.setColor(ColorFactory.getColor(Color.CYAN.darker(), 200));
                g2.fill(frame);
                g2.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.setColor(ColorFactory.getColor(Color.BLUE, 128));
                g2.draw(frame);

                update();

                g2.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2.drawRect((int)labelPosition.getX()-1, (int)labelPosition.getY(), label.getWidth()+2, label.getHeight());
                g2.drawImage(label, null, (int)labelPosition.getX(), (int)labelPosition.getY());
                
                g2.setColor(defColor);
                g2.setStroke(s);
                
                if(isListing)
                    listBox.paint(g2, c);
            }
        }
        
        protected void toggleInteractible() {
            this.isInteractive = !this.isInteractive;
        
            if(isInteractive) {
                for(ImageButton ib : buttons) {
                    if(!ib.isEnabled())
                        continue;
                    GameFrame.paintArea.add(ib);
                    Paintables.registerInterface(ib);
                }
            }
            if(!isInteractive) {
                GameFrame.paintArea.remove(buttons);
                Paintables.removeInterface(buttons);
            }
        }
        
        private ActionListener actionListener = new ActionListener() {
            
            @Override
            public void actionPerformed(ActionEvent event) {
                if(isInteractive) {
                    if(!((ImageButton)event.getSource()).isEnabled())
                        return;
                        
                    switch(((ImageButton)event.getSource()).getID()) {
                        case RADIO_PLAYER_PLAY:
                            shift(true);
                        break;
                        case RADIO_PLAYER_PAUSE:
                            shift(false);
                        break;
                        case RADIO_PLAYER_STOP:
                            if(current == null) return;
                            Radio.stop();
                        break;
                        case RADIO_PLAYER_SHOW: case RADIO_PLAYER_HIDE:
                            isListing = !isListing;
                            
                            buttons[RADIO_PLAYER_HIDE].setEnabled(isListing);
                            buttons[RADIO_PLAYER_SHOW].setEnabled(!isListing);
            
                            GameFrame.paintArea.add(isListing? buttons[RADIO_PLAYER_HIDE] : buttons[RADIO_PLAYER_SHOW]);
                            Paintables.registerInterface(isListing? buttons[RADIO_PLAYER_HIDE] : buttons[RADIO_PLAYER_SHOW]);

                            GameFrame.paintArea.remove(!isListing? buttons[RADIO_PLAYER_HIDE] : buttons[RADIO_PLAYER_SHOW]);
                            Paintables.removeInterface(!isListing? buttons[RADIO_PLAYER_HIDE] : buttons[RADIO_PLAYER_SHOW]);
                        break;
                        case RADIO_PLAYER_PREVIOUS:
                            Radio.previous();
                        break;
                        case RADIO_PLAYER_NEXT:
                            Radio.next();
                        break;
                    }
                }
            }
        };

        @Override
        public boolean showOnlyOnEditor() {
            return false;
        }
    }
    
//    public class HealthBar implements Paintable {
//        private Rectangle2D healthRect, baseHealthRect;
//        
//        public HealthBar() {
//            this.healthRect = new Rectangle2D.Float(30, GameFrame.FRAME_HEIGHT-60, Player.getDefaultPlayer().getHealth() * 2, 20);
//            this.baseHealthRect = new Rectangle2D.Float(30, GameFrame.FRAME_HEIGHT-60, (Player.getDefaultPlayer().getStamina() * 2) + 2, 22);
//        }
//        
//        @Override
//        public boolean isActive() {return true;}
//        @Override
//        public void setActive(boolean active) {}
//
//        @Override
//        public void paint(Graphics2D g2, Component c) {
//            g2.setColor(Color.BLACK);
//            g2.setStroke(new BasicStroke(4));
//                g2.draw(baseHealthRect);
//                    g2.setColor(Color.LIGHT_GRAY);
//                    g2.fill(baseHealthRect);
//                        g2.setColor(Color.RED);
//                        g2.setStroke(new BasicStroke(1));
//                            healthRect.setRect(healthRect.getX(), healthRect.getY(), Player.getDefaultPlayer().getHealth() * 2, healthRect.getHeight());
//                            g2.draw(healthRect);
//                            g2.fill(healthRect);
//                                g2.setColor(Color.BLACK);
//                                g2.drawString(Integer.toString((int)Player.getDefaultPlayer().getHealth()), 
//                                        (int)((baseHealthRect.getX() + baseHealthRect.getWidth()/2) - g2.getFont().getSize2D()),
//                                        (int)((baseHealthRect.getY() + baseHealthRect.getHeight()/2)));
//        }
//        
//    }
    
    /*
     * Ahoy thar! Statics below this plank.
     */
    
    private static final int RADIO_PLAYER_PLAY = 0;
    private static final int RADIO_PLAYER_PAUSE = 1;
    private static final int RADIO_PLAYER_STOP = 2;
    private static final int RADIO_PLAYER_SHOW = 3;
    private static final int RADIO_PLAYER_PREVIOUS = 4;
    private static final int RADIO_PLAYER_NEXT = 5;
    private static final int RADIO_PLAYER_HIDE = 6;
    
    private static UserInterface ui;
    
    public static void initialize(Vector2D mousePosition) {
        ui = new UserInterface(mousePosition);
        Paintables.register(ui);
    }
    
    public static void print(String s) {ui.debugBox.print(s);}
    public static void println(String s) {ui.debugBox.println(s);}
    public static String read() {return ui.debugBox.read();}
    
    public static UserInterface getUI() {return ui;}
    public static RadioPlayer getRadioPlayer() {return ui.radioPlayer;}
    
    public static boolean onTheater() {return ui.theaterMode;}
    public static void toggleTheater() {ui.theaterMode = !ui.theaterMode; Zombie.setBars(!ui.theaterMode);}
    
    public static boolean isDebugging() {return ui.debugMode;}
    public static void toggleDebug() {ui.debugMode = !ui.debugMode;}
    
    public static void setTheater(boolean theaterMode) {ui.theaterMode = theaterMode; Zombie.setBars(!ui.theaterMode);}
    public static void setDebug(boolean debugMode) {ui.debugMode = debugMode;}
    
    
    private static float rx = 1,ry = 1;
    public static void fixResolution(int w, int h, Vector2D paintSize) {      
        rx = (w/paintSize.getX());
        ry = (h/paintSize.getY());
    }
    
    public static void manageResolution(Graphics2D g2) {
        g2.scale(rx, ry);
    }

    @Override
    public boolean isActive() {
        return isActive;
    }

    @Override
    public void setActive(boolean active) {
        isActive = active;
    }
}
