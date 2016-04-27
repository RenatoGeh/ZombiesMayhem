package geometryzombiesmayhem;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * <p>A particular collection of individual <code>AudioLine</code>s.</p>
 * 
 * @author Renato Lui Geh
 */
public class SoundChannel {
    private String name;
    private Set<AudioLine> lines;
    
    public SoundChannel(String name) {
        this.name = name;
        
        lines = new HashSet<>();
    }
    
    public AudioLine findLine(String name) {
        for(AudioLine al : lines) {
            if(name.equals(al.getName()))
                return al;
        }
        throw new IllegalArgumentException("Line " + name + " not found!");
    }
    
    public void setVolume(int vol) {
        for(AudioLine line : lines) 
            if(line.getVolume() != vol)
                line.setVolume(vol);
    }
    
    public double getVolume() {
        double[] av = new double[lines.size()];
        int i=0;
        for(Iterator<AudioLine> it = lines.iterator();it.hasNext();) {
            AudioLine line = it.next();
            av[i] = line.getVolume();
            i++;
        }
        return ImageManipulator.avArithmetic(av);
    }
    
    public void checkLines() {
        if(!lines.isEmpty()) {
            for(Iterator<AudioLine> it = lines.iterator();it.hasNext();) {
                AudioLine al = it.next();
                
                if(!al.isPermanent()) {
                    if(!al.isActive()) {
                        al.stop();
                        it.remove();
                    }
                }
            }
        }
    }
    
    public Set<AudioLine> getLines() {return lines;}
    public void add(AudioLine al) {lines.add(al);}
    public void remove(AudioLine al) {al.stop(); lines.remove(al);}
    public String getName() {return name;}
}