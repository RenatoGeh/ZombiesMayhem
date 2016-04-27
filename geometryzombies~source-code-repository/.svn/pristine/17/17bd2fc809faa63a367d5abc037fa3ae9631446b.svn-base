package geometryzombiesmayhem;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * <p>Manages all the Audio.</p>
 * 
 * @author Renato Lui Geh
 */
public final class AudioManager {
    private static Set<SoundChannel> channels;

    static {
        initChannels();
    }
    
    private static void initChannels() {
        channels = new HashSet<>();
        
        channels.add(new SoundChannel("Music"));
        channels.add(new SoundChannel("Player"));
        channels.add(new SoundChannel("Interface"));
        channels.add(new SoundChannel("Zombies"));
        channels.add(new SoundChannel("Environment"));
        channels.add(new SoundChannel("Radio"));
        
        initInterfaceChannel();
    }
    
    private static void initInterfaceChannel() {
        SoundChannel inter = AudioManager.findChannel("Interface");
        
//        inter.add(generateLine(INTERFACE_CLICK_OFF, "resources/sounds/fx/menu/select/2.wav", false, true));
//        inter.add(generateLine(INTERFACE_CLICK_ON, "resources/sounds/fx/menu/select/1.wav", false, true));
    }
    
    public static SoundChannel findChannel(String name) {
        for(SoundChannel sc : channels) {
            if(name.equals(sc.getName()))
                return sc;
        }
        throw new IllegalArgumentException("Channel " + name + " not found!");
    }
    
    private static float convert(float max, float min, float vol) {
        if(vol > 100)
            vol = 100;
        else if(vol < 0)
            vol = 0;
        
        float total = Math.abs(max) + Math.abs(min);
        float n = (total*vol)/100;
        
        if(n>min-max)
            n = -(n-max);
        
        return n;
    }
    
    public static void setVolume(int vol) {
        for(SoundChannel channel : channels)
            channel.setVolume(vol);
    }
    
    public static void setVolume(SoundChannel sc, int vol) {sc.setVolume(vol);}
    
    public static void checkChannels() {
        for(SoundChannel sc : channels)
            sc.checkLines();
    }
    
    public static void fadeOut(AudioLine line, int speed) {
        new AudioFader(line, speed, 0);
    }
    
    public static void fadeIn(AudioLine line, int speed) {
        new AudioFader(line, speed, 1);
    }
    
    public static double getVolume() {
        double[] av = new double[channels.size()];
        int i=0;
        for(Iterator<SoundChannel> it = channels.iterator();it.hasNext();) {
            SoundChannel c = it.next();
            av[i] = c.getVolume();
            i++;
        }
        return ImageManipulator.avArithmetic(av);
    }
    
    public static void doInterfaceClick(boolean type) {
        if(type)
            generateLine(INTERFACE_CLICK_ON, "resources/sounds/fx/menu/select/1.wav", false);
        else
            generateLine(INTERFACE_CLICK_ON, "resources/sounds/fx/menu/select/1.wav", false);
    }
    
    public static AudioLine findLine(String name) {        
        for (Iterator<SoundChannel> it = channels.iterator();it.hasNext();) {            
            SoundChannel sc = it.next();
            try {
                return sc.findLine(name);
            } catch(IllegalArgumentException exc) {}
        }
        throw new IllegalArgumentException("No such line!");
    }
    
    public static AudioLine findLine(String channel, String line) {
        return findChannel(channel).findLine(line);
    }
    
    public static AudioLine generateLine(String name, String path, boolean loop) {
        return new AudioLine(name, path, loop);
    }
    
    public static AudioLine generateLine(String name, String path, boolean loop, boolean permanent) {
        return new AudioLine(name, path, loop, permanent);
    }
    
    public static AudioLine generateLine(String name, String path, boolean loop, boolean permanent, boolean startsOnCreation, boolean isExternal) {
        return new AudioLine(name, path, loop, permanent, startsOnCreation, isExternal);
    }
    
    public static long getSoundTime(AudioLine line) {return line.getClip().getMicrosecondLength();} 
    public static long getCurrentSoundTime(AudioLine line) {return line.getClip().getMicrosecondPosition();}
    
    public static final String INTERFACE_CLICK_ON = "INTERFACE_CLICK_ON";
    public static final String INTERFACE_CLICK_OFF = "INTERFACE_CLICK_OFF";
}