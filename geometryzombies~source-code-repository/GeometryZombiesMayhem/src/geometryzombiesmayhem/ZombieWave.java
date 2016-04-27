package geometryzombiesmayhem;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

/**
 * Zombie Wave.
 * 
 * @see Zombie
 * 
 * @author Renato Lui Geh
 * @version 0.0.2
 */
public class ZombieWave {
    
    private java.util.ArrayList<Zombie> zombies;
    private java.util.Random random = ZM.RANDOM;
    private Timer countdown;
    private boolean isIncremental = false;
    private int increment = 0;
    private int time;
    
    /**
     * Default constructor. 
     * Keep in mind that nZombies (first parameter) is actually the
     * initial wave's incoming zombies.
     * 
     * @param nZombies
     * @param isIncremental
     * @param bodies
     * @param ai 
     */
    public ZombieWave(int nZombies, boolean isIncremental, int time) {
        this.isIncremental = isIncremental;
        this.time = time;
        
        this.generateZombies(nZombies);
        
        if(isIncremental)
            initTimer();
    }
    
    private void initTimer() {
        countdown = new Timer(5000, countdownListener);
        countdown.setRepeats(true);
        countdown.start();
    }
    
    private void generateZombies(int nZombies) {
        Vector2D position;
        
        for(int i=0;i<nZombies;i++) {
            if(random.nextBoolean())
                position = new Vector2D(GameFrame.FRAME_WIDTH + 200, Vector2D.CENTER.copy().getY());
            else
                position = new Vector2D(-200, Vector2D.CENTER.copy().getY());
            
            Zombie z = new Zombie("Zombie_" + Zombie.getList().size(), position, Zombie.getDefaultSprite(), 100f);
            Body.register(z);
        }
    }
    
    private ActionListener countdownListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent event) {
            if((increment-1) * 1000 >= time) {
                //System.err.println("Stopping!");
                if(countdown.isRunning())
                    countdown.stop();
            }
            else if(increment <= 5){
                increment++;
                //System.err.println(increment);
                generateZombies(increment);
            }
        }
    };
}