package geometryzombiesmayhem;

import geometryzombiesmayhem.GameSettings.Graphics.Filter;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>Manages all classes that should be mutated in real time.<p>
 * 
 * @author Renato Lui Geh
 */
public class MutationManager {
    private static final Set<Filterable> filts = new HashSet<>();
    
    public static synchronized void register(Filterable f) {filts.add(f);}
    public static synchronized void remove(Filterable f) {filts.remove(f);}
    public static synchronized boolean contains(Filterable f) {return filts.contains(f);}
    
    public static synchronized void removeAll() {filts.clear();}
    
    public static void filter(Filter f) {
        synchronized(filts) {
            for(Filterable filt : filts)
                filt.filter(f);
        }
    }
}