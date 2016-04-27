package geometryzombiesmayhem;

import java.awt.event.ActionListener;
import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Timer;

/**
 * Plays any supported songs contained within the <code>radio</code> directory.
 * 
 * <p>
 *  Radio, by default, contains a static instance of itself. 
 *  This, added the fact that the constructor is defined private by default,
 *  prevents unwanted effects while playing songs.
 * </p>
 * 
 * <p>
 *  Since <code>javax.swing.Timer</code> does not allow big periods of time,
 *  a new timer was needed. <code>ScheduledThreadPoolExecutor</code> fits the
 *  job perfectly.
 * </p>
 * 
 * <p>
 *  <b>Note:</b> <code>getRemainingTime</code> is formatted. Use 
 *  <code>getRemainingTimeMs</code> to get the actual microseconds.
 * </p>
 * 
 * @author Renato Lui Geh
 * @version 0.0.5
 * 
 * @see ScheduledThreadPoolExecutor
 * @see AudioManager
 * @see AudioLine
 * @see AudioFader
 * @see SoundChannel
 */
public class Radio {    
    private ArrayList<File> music = null;
    private ArrayList<Integer> list = new ArrayList<>();
    private File current,musicDir;
    private int songIndex = 0;
    private boolean isEnabled = false;
    private Random r = ZM.RANDOM;   
    private PlayStyle playstyle;
    private boolean isPaused = false;
    
    private Radio() {
        try {
            init();
        } catch (URISyntaxException ex) {
            Logger.getLogger(Radio.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void init() throws URISyntaxException {    
        musicDir = AssetManager.getExternalFile(DEFAULT_RADIO_PATH);
        
        if(!musicDir.exists())
            musicDir.mkdir();
        
        this.setPlayStyle(PlayStyle.NORMAL);
    }

    private long getMusicTime(String s) {return AudioManager.getSoundTime(AudioManager.findChannel("Radio").findLine(s));}
    private long getCurrentMusicTime(String s) {return AudioManager.getCurrentSoundTime(AudioManager.findChannel("Radio").findLine(s));}
    private long getCurrentMusicTime() {return AudioManager.getCurrentSoundTime(AudioManager.findChannel("Radio").findLine(getMusicName()));}
    private long getRemainingMusicTime() {return getMusicTime(getMusicName()) - getCurrentMusicTime();}
    private long getRemainingMusicTime(String s) {return getMusicTime(s) - AudioManager.getCurrentSoundTime(AudioManager.findChannel("Radio").findLine(s));}
    
    private String getMusicName() {
        if(current == null)
            return null;
        
        return current.getName().substring(0, 
               current.getName().length() - DEFAULT_SOUND_EXTENSION.length());
    }
    
    private AudioLine findMusic(String name) {
        return AudioManager.findChannel("Radio").findLine(name);
    }
    
    public PlayStyle getPlayStyle() {return playstyle;}
    
    public void setPlayStyle(PlayStyle style) {        
        playstyle = style;
        
        switch(style) {
            case SHUFFLE_ANY:
                music = null;
                if(list==null) list = new ArrayList<>();
                break;
            case SHUFFLE_NO_REPEAT:
                music = new ArrayList<> (Arrays.asList(AssetManager.getSpecifiedExternalFiles(DEFAULT_RADIO_PATH, DEFAULT_SOUND_EXTENSION)));
                Collections.shuffle(music);
                list = null;
                break;
            case NORMAL:
                music = new ArrayList<> (Arrays.asList(AssetManager.getSpecifiedExternalFiles(DEFAULT_RADIO_PATH, DEFAULT_SOUND_EXTENSION)));
                list = null;
                break;
        }
        if(music!=null) {
            for(File m : music){
                current = m;
                AudioManager.findChannel("Radio").add(
                    AudioManager.generateLine(getMusicName(),
                    DEFAULT_RADIO_PATH + m.getName(),
                    false, true, false, true));
            }
        }
    }
    
    private void add(String s) {music.add(AssetManager.getFile(s));}
    
    private void play() {
        if(isPaused) {
            this.findMusic(this.getMusicName()).play();
            this.isPaused = false;
            
            return;
        }
        
        if(music!=null) {
            current = music.get(songIndex);
           
            findMusic(getMusicName()).restart();
            AwesomeTimer.addAction(songAction, getMusicTime(getMusicName()), false, TimeUnit.MICROSECONDS);
            AwesomeTimer.addAction(faderAction, getMusicTime(getMusicName()) - 3 * 1000000, false, TimeUnit.MICROSECONDS);
  
            return;
        }
        File[] songs = AssetManager.getSpecifiedExternalFiles(DEFAULT_RADIO_PATH, DEFAULT_SOUND_EXTENSION);
        int n = r.nextInt(songs.length);
        list.add(n);
        current = songs[n];
        AudioManager.findChannel("Radio").add(
                    AudioManager.generateLine(getMusicName(),
                    DEFAULT_RADIO_PATH + current.getName(),
                    false, true));
        findMusic(getMusicName()).restart();
        AwesomeTimer.addAction(songAction, getMusicTime(getMusicName()), false, TimeUnit.MICROSECONDS);
        AwesomeTimer.addAction(faderAction, getMusicTime(getMusicName()) - 3 * 1000000, false, TimeUnit.MICROSECONDS);
    }
    
    
    private void stopSong() {
        if(music!=null) findMusic(getMusicName()).stop();
        else AudioManager.findChannel("Radio").remove(findMusic(getMusicName()));
        current = null;
    }
    
    private void pauseSong() {
        isPaused = true;
        this.findMusic(this.getMusicName()).pause();
    }
    
    private void resetTimer(Timer timer, ActionListener listener, boolean isFade) {
        timer.stop();
        
        int width = (int)getMusicTime(getMusicName());
        
        if(isFade)
            width -= 3*1000;
            
        timer = new Timer(width, listener);
        
        
        timer.setRepeats(false);
    }
    
    private void resetFaderExecutor() {
        AwesomeTimer.remove(faderAction);
    }
    
    private void resetSongExecutor() {
        AwesomeTimer.remove(songAction);
    }
    
    
    private String parseTime(long ms) {
        int seconds = (int) (ms / 1000000) % 60 ;
        int minutes = (int) ((ms / (1000000*60)) % 60);
        
        String s = new String();
        
        if(minutes < 10)
            s += "0" + minutes;
        else
            s += Integer.toString(minutes);
        
        s += ":";
        
        if(seconds < 10)
            s += "0" + seconds;
        else
            s += Integer.toString(seconds);
        
        return s;
    }
    
    private void nextSong() {
        resetFaderExecutor();
        AwesomeTimer.executeAndRemove(songAction);
    }
    
    private void previousSong() {
        resetFaderExecutor();
        resetSongExecutor();
        if(music!=null) {
            if(songIndex==0) songIndex = music.size() - 1;
            else songIndex--;
            play();
            return;
        }
        File[] songs = AssetManager.getSpecifiedExternalFiles(DEFAULT_RADIO_PATH, DEFAULT_SOUND_EXTENSION);
        AudioManager.findChannel("Radio").remove(findMusic(getMusicName()));
        if(list.size() > 1) list.remove(list.size()-1);
        current = songs[list.get(list.size()-1)];
        
        AudioManager.findChannel("Radio").add(
                    AudioManager.generateLine(getMusicName(),
                    DEFAULT_RADIO_PATH + current.getName(),
                    false, true));
        findMusic(getMusicName()).restart();
        AwesomeTimer.addAction(songAction, getMusicTime(getMusicName()), false, TimeUnit.MICROSECONDS);
        AwesomeTimer.addAction(faderAction, getMusicTime(getMusicName()) - 3 * 1000000, false, TimeUnit.MICROSECONDS);
    }
    
    private AwesomeAction songAction = new AwesomeAction() {
        @Override
        public void run() {
            if(music!=null) {
                if(songIndex < music.size()-1) 
                    songIndex++;
                else 
                    songIndex = 0;
                
                System.err.println(songIndex);
            }
            
            
            AudioManager.findChannel("Radio").remove(findMusic(getMusicName()));
            resetSongExecutor();
            play();
        }
    };
    
    private AwesomeAction faderAction = new AwesomeAction() {
        @Override
        public void run() {
            AudioManager.fadeOut(findMusic(getMusicName()), 5);
            resetFaderExecutor();
        }
    };
    
    public static enum PlayStyle {SHUFFLE_ANY,SHUFFLE_NO_REPEAT,NORMAL;};

    public static final String DEFAULT_RADIO_PATH = AssetManager.DEFAULT_DATA_PATH + "radio" + File.separator;//"resources/sounds/music/radio/";
    private static final String DEFAULT_SOUND_EXTENSION = ".wav";
    
    private static final Radio radio = new Radio();
    
    //Runtime executable methods down here.
    
    /**
     * Plays the Radio from the very first song listed in predefined default directory.
     */
    public static void playRadio() {
        radio.play();
    }
    
    /**
     * Adds the given music path to the list of songs.
     * 
     * <p>
     *  <b>Note:</b> This is not recommended! Radio automatically gets all music
     *  from the default directory.
     * </p>
     * @param s 
     * @deprecated
     */
    public static void addMusic(String s) {
        radio.add(s);
    }
    
    /**
     * Plays the next song in the list.
     */
    public static void next() {
        radio.nextSong();
    }
    
    /**
     * Stops the current song.
     */
    public static void stop() {
        radio.stopSong();
    }
    
    public static void pause() {
        radio.pauseSong();
    }
    
    /**
     * Plays the previous song in the list.
     */
    public static void previous() {
        radio.previousSong();
    }
    
    /**
     * Returns the current song file.
     * @return 
     */
    public static File getCurrent() {
        return radio.current;
    }
    
    /**
     * Sets the Play Style.
     * 
     * @see PlayStyle
     * @param style 
     */
    public static void setStyle(PlayStyle style) {
        radio.setPlayStyle(style);
    }
    
    public static Radio getDetails() {return radio;}
    
    public static String[] getSongsNames() {
        if(radio.music == null) {
            radio.music = new ArrayList<> (Arrays.asList(AssetManager.getSpecifiedExternalFiles(DEFAULT_RADIO_PATH, DEFAULT_SOUND_EXTENSION)));
            return Radio.getSongsNames();
        }
        
        if(radio.music.isEmpty())
            return null;
        
        String[] names = new String[radio.music.size()];
        
        for(int i=0;i<names.length;i++)
            names[i] = radio.music.get(i).getName().substring(0, 
               radio.music.get(i).getName().length() - DEFAULT_SOUND_EXTENSION.length());
        
        return names;
    }
    
    /**
     * Gets the current song name.
     * @return 
     */
    public static String getCurrentSongName() {return radio.getMusicName();}
    /**
     * Gets the remaining time till the next song.
     * @return 
     */
    public static String getRemainingTime() {return radio.parseTime(getRemainingTimeMs());}
    /**
     * Gets the absolute remaining time till the next song in microseconds.
     * @return 
     */
    public static long getRemainingTimeMs() {return radio.getRemainingMusicTime();}
    /**
     * Gets the current song's time position.
     * @return 
     */
    public static String getTimePosition() {return radio.parseTime(getTimePositionMs());}
    /**
     * Gets the absolute current song's time position in microseconds.
     * @return 
     */
    public static long getTimePositionMs() {return radio.getCurrentMusicTime();}
    
    public static String getTimePosition(String name) {return radio.parseTime(radio.getCurrentMusicTime(name));}
    public static String getRemainingTime(String name) {return radio.parseTime(radio.getRemainingMusicTime(name));}
    public static String getAbsoluteMusicTime(String name) {return radio.parseTime(radio.getMusicTime(name));}
    
    public static void keyTyped(java.awt.event.KeyEvent event) {
        if(event.getKeyChar() == 'm')
            AudioManager.setVolume(100);
        if(event.getKeyChar() == 'n')
            AudioManager.setVolume(0);
        if(event.getKeyChar() == ',')
            Radio.next();
        if(event.getKeyChar() == 'p')
            Radio.previous();
        if(event.getKeyChar() == 'o') 
            UserInterface.getRadioPlayer().shift(!UserInterface.getRadioPlayer().isPlaying());
    }
}
