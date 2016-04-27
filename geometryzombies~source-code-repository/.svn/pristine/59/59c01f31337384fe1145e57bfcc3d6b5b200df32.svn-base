package geometryzombiesmayhem;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * 
 * <p>Manages AI implementations.</p>
 * 
 * @author Renato Lui Geh
 */
public final class AIManager {
    private static boolean isEnabled = true;
    private final static Set<ArtificialIntelligence> ai = new HashSet<>();
    
    public synchronized static void remove(ArtificialIntelligence entity) {ai.remove(entity);}
    public synchronized static void register(ArtificialIntelligence entity) {ai.add(entity);}
    
    public synchronized static void setEnabled(boolean isEnabled) {AIManager.isEnabled = isEnabled;}
    public synchronized static boolean isEnabled() {return AIManager.isEnabled;}
    
    public static void update() {
        synchronized(ai) {
            if(Player.getDefaultPlayer().isDead() || !isEnabled) return;
            for(Iterator<ArtificialIntelligence> it = ai.iterator();it.hasNext();) {
                ArtificialIntelligence entity = it.next();
                
                entity.handleBefore();
                
                if(entity.checkMovementsRange()) {
                    if(entity.checkWeaponsRange())
                        entity.handleAttack();
                    else
                        entity.handleMovement();
                } else {
                    entity.handleIdling();
                }
            }
        }
    }
}