package geometryzombiesmayhem;

import geometryzombiesmayhem.io.StateManager;
import geometryzombiesmayhem.io.StateManager.StateStatus;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.net.MalformedURLException;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Renato Lui Geh 
 */
public class GameOverMenu extends Menu {
    private boolean isVisible = false;
    private Rectangle2D filter = new Rectangle2D.Float();
    private Color filterColor = ColorFactory.getColor(Color.BLACK, 200);
    private Font labelFont = FontFactory.ZOMBIE_FONT.deriveFont(150f);
    
    private GameOverMenu() {
        float y=425, x=100;
        
        NoisedTextButton retry = new NoisedTextButton(new Vector2D(x, y), 40, "retry", 0, Color.RED, 2, 3f);
        NoisedTextButton load = new NoisedTextButton(new Vector2D(x+=retry.getWidth()+100, y), 40, "load", 1, Color.RED, 2, 3f);
        NoisedTextButton exit = new NoisedTextButton(new Vector2D(x+=retry.getWidth()+100, y), 40, "exit", 2, Color.RED, 2, 3f);
        
        final NoisedTextButton toDesktop = new NoisedTextButton(new Vector2D(exit.getX(), y+=exit.getHeight()+50), 40, "desktop", 3, Color.RED, 2, 3f);
        final NoisedTextButton toMenu = new NoisedTextButton(new Vector2D(exit.getX()+toDesktop.getWidth()+100, y), 40, "menu", 4, Color.RED, 2, 3f);
    
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                if(!((NoisedTextButton)event.getSource()).isEnabled()) return;
                
                switch(((NoisedTextButton)event.getSource()).getID()) {
                    case 0:
                        if(RETRY_STATE!=null)
                            StateManager.rawLoading(RETRY_STATE.getType(), RETRY_STATE.getPath());
                        else
                            JOptionPane.showMessageDialog(ZM.gameFrame, "Error 404: Save not found.", "Nope. No fucks found.", JOptionPane.WARNING_MESSAGE);
                    break;
                    case 1:
                        StateManager.load();
                    break;
                    case 3:
                    case 4:
                        if(JOptionPane.showConfirmDialog(ZM.gameFrame, "Are you sure you want to exit \"Zombies Mayhem!\"? "
                                + "Any unsaved data will be lost!", "SRSLY?", 
                                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.NO_OPTION)
                                break;
                        
                        if(((NoisedTextButton)event.getSource()).getID()==3)
                            System.exit(0);
                        else
                            try {
                            GameOverMenu.this.close(GameOverMenu.this.window);
                            Body.removeAll();
                            LightManager.removeAll();
                            MutationManager.removeAll();
                            Paintables.clearAll();
                            LightManager.setDarkness(0);
                            
                            ZM.gameFrame.dispose();
                            ZM.main = new ZM();
                        } catch (MalformedURLException | InterruptedException ex) {
                            Logger.getLogger(GameOverMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                        }
                    break;
                }
                
                if(((NoisedTextButton)event.getSource()).getID()==2) {
                    if(!GameFrame.paintArea.components.contains(toDesktop)) {
                        GameOverMenu.this.add(toDesktop);
                        GameOverMenu.this.add(toMenu);
                        
                        Paintables.registerTheater((Paintable)toDesktop);
                        Paintables.registerTheater((Paintable)toMenu);
                    }
                } else if(GameFrame.paintArea.components.contains(toDesktop)) {
                    GameOverMenu.this.remove(toDesktop);
                    GameOverMenu.this.remove(toMenu);
                    
                    Paintables.removeTheater((Paintable)toDesktop);
                    Paintables.removeTheater((Paintable)toMenu);
                }
            }
        };
        
        retry.setEnabled(!GameFrame.onEditor);

        
        
        this.add(retry, load, exit);
        
        for(ComponentP comp : components)
            ((NoisedTextButton)comp).addActionListener(actionListener);
        
        toDesktop.addActionListener(actionListener);
        toMenu.addActionListener(actionListener);
    }
    
    @Override
    public void paint(Graphics2D g2, Component c) {
        Font defFont = g2.getFont();
        Color defColor = g2.getColor();
        
        g2.setColor(this.filterColor);
        g2.draw(filter);
        g2.fill(filter);
        g2.setColor(Color.RED);
        g2.setFont(this.labelFont);
        g2.drawString("Game Over", 70, 110);
        
        g2.setFont(defFont);
        g2.setColor(defColor);
    }
    
    @Override
    public void load(WindowP window) {
        GameFrame.isPaused = true;
        
        this.loaded = true;
        this.addTo(window, false);
        
        this.filter.setFrame(0, 0, window.getWidth(), window.getHeight());
        
        this.setVisible(true);
        
        for(ComponentP comp : components) {
            window.add(comp);
            Paintables.registerTheater((Paintable)comp);
        }
        
        for(Paintable pain : paintables)
            Paintables.registerTheater(pain);
    }
    @Override
    public void close(WindowP window) {
        super.close(window);
        
        this.setVisible(false);
        
        for(ComponentP comp : components) {
            window.remove(comp);
            Paintables.removeTheater((Paintable)comp);
        }
        
        for(Paintable pain : paintables)
            Paintables.removeTheater(pain);
    }
    
    @Override
    public boolean isActive() {return this.isVisible;}
    @Override
    public void setActive(boolean active) {this.setVisible(active);}
    
    @Override
    public void setVisible(boolean visible) {
        if(visible == this.isVisible) return;
        
        if(visible)
            Paintables.registerTheater(this);
        else
            Paintables.removeTheater(this);
        
        UserInterface.toggleTheater();
        this.isVisible = visible;
    }
    public void toggleVisibility() {this.setVisible(!this.isVisible);}
    
    private static GameOverMenu MENU = new GameOverMenu();
    public static GameOverMenu getInstance() {return MENU==null?MENU=new GameOverMenu():MENU;}

    public static final String[] messages = new String[] {
        "VIRUS TRANSFER COMPLETE",
        "Hard Drive wiped out! Thank you for using HackMe.",
        "Player, I am dissapoint.",
        "Did you know?\nSyrup makes pancakes and waffles taste better.",
        "Did you know?\nYour computer is turned on.",
        "Did you know?\nA triangle has three sides.",
        "Did you know?\nThe chance of everything being a computer simulation is 99.99%.",
        "Fact: You are thinking of a pink elephant.",
        "Fact: Physics are cool!",
        "Fact: Mathematicians are crazy.",
        "Fact: Logicians are crazier than mathematicians.",
        "I feel you bro."
    };
    
    private static StateStatus RETRY_STATE = null;
    public static void setAutoState(StateStatus s) {RETRY_STATE = s;}
}
