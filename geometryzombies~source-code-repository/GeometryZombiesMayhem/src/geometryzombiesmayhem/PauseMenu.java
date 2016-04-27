package geometryzombiesmayhem;

import geometryzombiesmayhem.io.StateManager;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 * A singleton base pause menu.
 * 
 * @author Renato Lui Geh
 */
public final class PauseMenu extends Menu {
    private boolean isVisible = false;
    private Rectangle2D filter = new Rectangle2D.Float();
    private Color filterColor = ColorFactory.getColor(Color.BLACK, 200);
    
    private Font labelFont = FontFactory.ZOMBIE_FONT.deriveFont(150f);
    
    private PauseMenu() {
        float y = 185;
        
        NoisedTextButton resume = new NoisedTextButton(new Vector2D(100, y), 30, "RESUME", 0, Color.RED, 2, 3f);
        NoisedTextButton options = new NoisedTextButton(new Vector2D(100, y+=(resume.getHeight() + 50)), 30, "OPTIONS", 1, Color.RED, 2, 3f);
        NoisedTextButton save = new NoisedTextButton(new Vector2D(100, (y+=options.getHeight() + 50)), 30, "SAVE", 2, Color.RED, 2, 3f);
        NoisedTextButton load = new NoisedTextButton(new Vector2D(100, (y+=save.getHeight() + 50)), 30, "LOAD", 3, Color.RED, 2, 3f);
        NoisedTextButton exit = new NoisedTextButton(new Vector2D(100, (y+=options.getHeight() + 50)), 30, "EXIT", 4, Color.RED, 2, 3f);
        
        final NoisedTextButton exitFromGame = new NoisedTextButton(new Vector2D(250, load.getY()), 30, "TO DESKTOP", 5, Color.RED, 2, 3f);
        final NoisedTextButton exitToMenu = new NoisedTextButton(new Vector2D(250, y), 30, "TO MAIN MENU", 6, Color.RED, 2, 3f);
        
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!((NoisedTextButton)e.getSource()).isEnabled())
                    return;
                
                switch(((NoisedTextButton)e.getSource()).getID()) {
                    case 0:
                        GameFrame.isPaused = false;
                        PauseMenu.this.close(GameFrame.paintArea);
                    break;
                    case 1:
                        System.err.println("TODO: In-game Options Menu.");
                        JOptionPane.showConfirmDialog(ZM.gameFrame, "We haven't implemented an in-game option menu yet..."
                                + "\nSo have a Spock instead!", "Logically saddening...", JOptionPane.DEFAULT_OPTION, 
                                JOptionPane.WARNING_MESSAGE, new javax.swing.ImageIcon(AssetManager.loadImage("resources/spock.jpg")));
                    break;
                    case 2:
                        StateManager.save();
                    break;
                    case 3:
                        StateManager.load();
                    break;
                    case 5:
                    case 6:
                        if(JOptionPane.showConfirmDialog(ZM.gameFrame, "Are you sure you want to exit \"Zombies Mayhem!\"? "
                                + "Any unsaved data will be lost!", "SRSLY?", 
                                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.NO_OPTION)
                                break;
                        
                    if(((NoisedTextButton)e.getSource()).getID() == 5)
                        System.exit(0);
                    else
                        try {
                            PauseMenu.this.close(PauseMenu.this.window);
                            Body.removeAll();
                            LightManager.removeAll();
                            MutationManager.removeAll();
                            Paintables.clearAll();
                            LightManager.setDarkness(0);
                            
                            ZM.gameFrame.dispose();
                            ZM.main = new ZM();
                        } catch (MalformedURLException | InterruptedException ex) {
                            Logger.getLogger(PauseMenu.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        
                    break;
                }
                
                if(((NoisedTextButton)e.getSource()).getID() == 4) {
                    if(!GameFrame.paintArea.components.contains(exitFromGame)) {
                        PauseMenu.this.add(exitToMenu);
                        PauseMenu.this.add(exitFromGame);

                        Paintables.registerTheater((Paintable)exitFromGame);
                        Paintables.registerTheater((Paintable)exitToMenu);
                    }
                } else if(GameFrame.paintArea.components.contains(exitFromGame)) {
                    PauseMenu.this.remove(exitToMenu);
                    PauseMenu.this.remove(exitFromGame);

                    Paintables.removeTheater((Paintable)exitFromGame);
                    Paintables.removeTheater((Paintable)exitToMenu);
                }
            }
        };
        
        this.add(resume, options, save, load, exit);
        
        for(ComponentP comp : components)
            ((NoisedTextButton)comp).addActionListener(actionListener);
        
        exitToMenu.addActionListener(actionListener);
        exitFromGame.addActionListener(actionListener);
    }
    
    @Override
    public void load(WindowP window) {
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
    public boolean isVisible() {return this.isVisible;}
    @Override
    public void setVisible(boolean visible) {
        if(visible == isVisible) return;
        
        if(visible)
            Paintables.registerTheater(this);
        else
            Paintables.removeTheater(this);

        UserInterface.toggleTheater();
        this.isVisible = visible;
    }
    public void toggleVisibility() {this.setVisible(!this.isVisible);}
    
    @Override
    public boolean isActive() {return this.isVisible();}
    @Override
    public void setActive(boolean active) {this.setVisible(active);}

    @Override
    public void paint(Graphics2D g2, Component c) {
        Font defFont = g2.getFont();
        Color defColor = g2.getColor();
        
        g2.setColor(this.filterColor);
        g2.draw(filter);
        g2.fill(filter);
        g2.setColor(Color.RED);
        g2.setFont(this.labelFont);
        g2.drawString("PAUSED", 70, 125);
        
        g2.setFont(defFont);
        g2.setColor(defColor);
    }     
    
    private static final PauseMenu MENU = new PauseMenu();
    public static PauseMenu getInstance() {return PauseMenu.MENU;} 
}