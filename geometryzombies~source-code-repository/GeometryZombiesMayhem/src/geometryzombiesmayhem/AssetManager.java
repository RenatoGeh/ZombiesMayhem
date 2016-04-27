package geometryzombiesmayhem;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.swing.ImageIcon;

/**
 * An asset manager. It loads specific files to replace the use of manual construction of objects.
 * Files that the AssetManager can load have "stat" extensions. 
 * They are contained inside a .player, .zomb, .map, .gun or .prj extensions.
 * 
 * <b>NOTE: </b> .stats are contained inside the main compressed files (.player, .zomb, etc...).
 *
 * @see InfoFile
 * @author Renato Lui Geh
 * @version 0.0.5
 */
public class AssetManager {
    protected static final String PACKAGE_NAME = "geometryzombiesmayhem/";
    
    protected static final String CLASSPATH = AssetManager.class.getResource("").getPath();
    protected static final Path SAVE_PATH;
    protected static final Toolkit TOOLKIT = Toolkit.getDefaultToolkit();
    protected static final ClassLoader CLASS_LOADER = Thread.currentThread().getContextClassLoader();
    protected static final String JAR_PATH = CLASSPATH.replace(PACKAGE_NAME, "");

    private static JarFile DEFAULT_JAR = null;
    
    public static enum OperationalSystem {Windows, Linux, Macintosh, Other};
    
    static {
        if(System.getenv("AppData")!=null)
            SAVE_PATH = Paths.get(System.getenv("AppData"), "ZombiesMayhem");
        else
            SAVE_PATH = Paths.get(System.getProperty("user.home"), "ZombiesMayhem");
    }
    
    public static boolean existsCannonical(String s) {return new File(s).exists();}
    
    /**
     * <p><b>Attention:</b> This method should not be called! Use the public final
     * variable <code>AssetManager.DEFAULT_SYSTEM_PATH</code> instead!
     * 
     * @return 
     */
    protected static String getSystemPath() {
        OperationalSystem os = AssetManager.getOS();
        
        switch(os) {
            case Windows:
                return System.getenv("APPDATA");
            case Macintosh:
                return System.getProperty("user.home") + "/Library/Application Support";
            case Linux:
                String env = System.getenv("XDG_CONFIG_HOME");
                
                if(env == null)
                    return System.getProperty("user.home");
                
                return env;
            case Other:
                return System.getProperty("user.dir");
        }
        
        throw new IllegalArgumentException("OS name could not be found!");
    }
    
    /**
     * <p><b>Attention:</b> This method should not be called! Use the public final
     * variable <code>AssetManager.DEFAULT_DATA_PATH</code> instead!
     * 
     * @return 
     */
    protected static String getDefaultDataPath() {
        String out = AssetManager.DEFAULT_SYSTEM_PATH + File.separator + "ZombiesMayhem" + File.separator;
        File dir = new File(out);
        dir.mkdirs();
        return out;
    } 
    
    public static OperationalSystem getOS() {
        String os = System.getProperty("os.name").toUpperCase();
        
        if(os.contains("WIN"))
            return OperationalSystem.Windows;
        if(os.contains("NUX")) //Not sure whether this works with Ubuntu
            return OperationalSystem.Linux;
        if(os.contains("MAC"))
            return OperationalSystem.Macintosh;

        return OperationalSystem.Other;
    }
    
    public static File getExternalFile(String s) {return new File(s);}
    public static File[] getSpecifiedExternalFiles(String s, String suffix) {
        File[] files = AssetManager.getExternalFile(s).listFiles();
        File[] result;
        int n=0;
        
        for(int i=0;i<files.length;i++)
            if(files[i].getName().endsWith(suffix))
                n++;
        
        result = new File[n];
        
        n = 0;
        for(int i=0;i<files.length;i++)
            if(files[i].getName().endsWith(suffix))
                result[n++] = files[i];
        
        return result;
    }
    
    public static File getFile(String s) {return new File(load(s).getPath());}
    public static File[] getFiles(String s) {return new File(load(s).getPath()).listFiles();}
    public static File[] getSpecifiedFiles(String s, String suffix) {
        if(!AssetManager.isCompressed()) {
            File[] files = getFiles(s);
            File[] sounds;
            int n = 0;

            for(int i=0;i<files.length;i++) 
                if(files[i].getName().endsWith(suffix))
                    n++;

            sounds = new File[n];
            n = 0;

            for(int i=0;i<files.length;i++) {
                if(files[i].getName().endsWith(suffix)) {
                    sounds[n] = files[i];
                    n++;
                }
            }

            return sounds;
        } else {
            ArrayList<JarEntry> entries = AssetManager.getSpecifiedEntries(s, suffix);
            File[] files = new File[entries.size()];
            
            for(int i=0;i<entries.size();i++)
                files[i] = AssetManager.convertEntry(entries.get(i));
            
            return files;
        }
    }
    
    public static ArrayList<JarEntry> getEntries(String s) {
        if(!AssetManager.isCompressed())
            throw new IllegalArgumentException("The application is not jar compressed!");
        else {
            JarFile jar = AssetManager.getDefaultJar();
            Enumeration<JarEntry> entries = jar.entries();
            
            ArrayList<JarEntry> result = new ArrayList<>();
            
            while(entries.hasMoreElements()) {
                JarEntry e = entries.nextElement();
                String name = e.getName();
                if(name.contains(s) && !e.isDirectory())
                    result.add(e);
            }
            
            return result;
        }
    }
    
    public static ArrayList<JarEntry> getSpecifiedEntries(String s, String suffix) {
        ArrayList<JarEntry> entries = AssetManager.getEntries(s);
        ArrayList<JarEntry> result = new ArrayList<>();
        
        for(JarEntry e : entries)
            if(e.getName().endsWith(suffix))
                result.add(e);
        
        return result;
    }
    
    public static File convertEntry(JarEntry e) {
        return new File(AssetManager.JAR_PATH + e.getName());
    }
    
    public static URL load(String resource) {
        return AssetManager.class.getResource(resource);
    }
    
    public static URL loadExternal(String resource) {
        URL f = null;
        try {
            f = new File(resource).toURI().toURL();
        } catch (MalformedURLException ex) {
            Logger.getLogger(AssetManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            return f;
        }
    }
    
    public static String loadPath(String resource) {
            return load(resource).getPath();
    }
    
    public static JarFile getDefaultJar() {
        try {
            if(DEFAULT_JAR == null)
                DEFAULT_JAR = new JarFile(AssetManager.CLASSPATH.replace("!/" + AssetManager.PACKAGE_NAME, "").replace("file:/", ""));
            return DEFAULT_JAR;
        } catch (IOException ex) {
            Logger.getLogger(AssetManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        throw new IllegalArgumentException("Jar not found.");
    }
    
    public static boolean isCompressed() {return AssetManager.class.getResource("AssetManager.class").toString().startsWith("jar:");}
    
    public static boolean createFile(String name) {
        try {
            return new File(name).createNewFile();
        } catch (IOException ex) {
            Logger.getLogger(AssetManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        throw new IllegalArgumentException("IOException caught! Could not create file " + name);
    }
    
    public static MutableImage createImage(String path) {
        InputStream in = CLASS_LOADER.getResourceAsStream(PACKAGE_NAME + path);//ZM.class.getResourceAsStream(path);
        byte[] buffer = null;
        
        try {
            buffer = new byte[in.available()];
            in.read(buffer);
            in.close();
        } catch (IOException ex) {
            Logger.getLogger(ZM.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        System.err.println(buffer.length);
        
        ImageIcon icon = new javax.swing.ImageIcon(buffer);
        icon = new javax.swing.ImageIcon(icon.getImage());
        
        MutableImage im = new MutableImage(icon.getImage(),path);
        im.filter(GameSettings.Graphics.getFilter());
        return im;
    }
    
    public static MutableImage loadIndependentImage(String path) {
        return new MutableImage(new javax.swing.ImageIcon(TOOLKIT.getImage(AssetManager.class.getResource(path))).getImage(),path, false);
    }
    
    public static MutableImage loadImage(String path) {
        MutableImage mi = new MutableImage(new javax.swing.ImageIcon(TOOLKIT.getImage(AssetManager.class.getResource(path))).getImage(),path);
        mi.filter(GameSettings.Graphics.getFilter());
        return mi;
    }
    public static MutableImage loadExternalImage(String path) {
        MutableImage mi = new MutableImage(new javax.swing.ImageIcon(TOOLKIT.getImage(path)).getImage(), "");
        mi.filter(GameSettings.Graphics.getFilter());
        return mi;
    }
    
    protected static Image filterImage(Image im, GameSettings.Graphics.Filter f) {
        switch(f) {
            case Default:
                return im;
            case Blur:
                return ImageManipulator.blur(ImageManipulator.convert(im));
            case Brighten:
                return ImageManipulator.brighten(ImageManipulator.convert(im), 10);
            case Darken:
                return ImageManipulator.darken(ImageManipulator.convert(im), 10);
            case Negative:
                return ImageManipulator.negative(ImageManipulator.convert(im), 0);
            case Gray:
                return ImageManipulator.gray(ImageManipulator.convert(im), ImageManipulator.GRAYSCALE_AVERAGE.ARITHMETIC);
        }
        
        throw new IllegalArgumentException("Unexpected exception caught: Filter not supported.");
    }
    
    protected static Image filterImage(Image im) {
        GameSettings.Graphics.Filter f = GameSettings.Graphics.getFilter();
        return filterImage(im, f);
    }
    
    public static Image loadRawImage(String path) {
        if(!exists(path)) 
            return new javax.swing.ImageIcon().getImage();
        else
            return new javax.swing.ImageIcon(Toolkit.getDefaultToolkit().getImage(load(path))).getImage();
    }
    
    public static boolean exists(String path) {
        if(!AssetManager.isCompressed()) {
            String aux = getClassPath() + path;        
            if(new File(aux).exists())
                return true;
            else {
                System.err.println("File " + path + " does not exist!");
                return false;
            }
        } else {
            JarFile jar = AssetManager.getDefaultJar();
            Enumeration<JarEntry> entries = jar.entries();
            
            while(entries.hasMoreElements()) {
                JarEntry e = entries.nextElement();
                if(e.getName().contains(path))
                    return true;
            }
            
            return false;
        }
    }
    
    public static String getClassPath() {
        return CLASSPATH;
    }
    
    public static InfoFile loadStats(String path) throws IOException {
        InfoFile stats = new InfoFile();
        ZipFile zip = new ZipFile(DEFAULT_SYSTEM_PATH + path);
        if(!checkExtension(zip)) {
            System.err.println("File is not a .stats extension!");
            return null;
        }
        
        Enumeration entries = zip.entries();
        BufferedReader zipInput = new BufferedReader(new InputStreamReader(System.in));
                
        while(entries.hasMoreElements()) {
            ZipEntry elem = (ZipEntry) entries.nextElement();
            if(elem.getName().endsWith(".stats")) {
                BufferedReader in = new BufferedReader(new InputStreamReader(zip.getInputStream(elem)));
                String line;
                System.err.println("Acessing " + elem.getName() + "...\n");
                while((line = in.readLine()) != null) {
                    if(line.startsWith("#"))
                        continue;
                    if(line.startsWith("name"))
                        stats.setName(getName(line));
                    if(line.startsWith("position")) 
                        stats.setPosition(getPosition(line));
                    if(line.startsWith("health"))
                        stats.setHealth(getHealth(line));
                    if(line.startsWith("control"))
                        stats.setControllability(getControl(line));
                    if(line.startsWith("mass"))
                        stats.setMass(getMass(line));
                    if(line.startsWith("image"))
                        stats.setImage(getImage(line));
                }
            }
        }
        return stats;
    }
    

    public static Image getImage(String line) {
        return loadImage(line).getImage();
    }
    
    public static float getMass(String line) {
        String out = line.replaceAll(" ", "");
        out = out.substring(5);
        System.err.println("Loading Mass " + out + "...");
        return Float.parseFloat(out);
    }
    
    public static boolean getControl(String line) {
        String out = line.replaceAll(" ", "");
        out = out.substring(8);
        System.err.println("Loading Control " + out + "...");
        return Boolean.parseBoolean(out);
    }
    
    public static float getHealth(String line) {
        String out = line.replaceAll(" ", "");
        out = out.substring(7);
        System.err.println("Loading Health " + out + "...");
        return Float.parseFloat(out);
    }
    
    public static Vector2D getPosition(String line) {
        String out = line.replaceAll(" ", "");
        out = out.substring(9);
        String pos[] = out.split(",");
        Vector2D output = new Vector2D(Float.parseFloat(pos[0]), Float.parseFloat(pos[1]));
        System.err.println("Loading Position " + output.toString() + "...");
        return output;
    }
    
    public static String getName(String line) {
        String out = line.replaceAll(" ", "");
        out = out.substring(5);
        System.err.println("Loading Name " + out + "...");
        return out;
    }
    
    public static boolean checkExtension(ZipFile zip) {
        for(int i=0;i<NUMBER_OF_EXTENSIONS;i++) {
            if(zip.getName().endsWith(EXTENSIONS_ARRAY[i]))
                return true;
        }
        return false;
    }
    
    public static void debugZipFile(ZipFile zf) {
        try {
            Enumeration entries = zf.entries();

            BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
            while (entries.hasMoreElements()) {
                ZipEntry ze = (ZipEntry) entries.nextElement();
                System.out.println("Read " + ze.getName() + "?");
                String inputLine = input.readLine();
                if (inputLine.equalsIgnoreCase("yes")) {
                    long size = ze.getSize();
                    if (size > 0) {
                        System.out.println("Length is " + size);
                        BufferedReader br = new BufferedReader(new InputStreamReader(zf.getInputStream(ze)));
                        String line;
                        while ((line = br.readLine()) != null) {
                            System.out.println(line);
                        }
                        br.close();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static final String DEFAULT_SYSTEM_PATH = getSystemPath();
    public static final String DEFAULT_DATA_PATH = getDefaultDataPath();
    
    public static final String PLAYER_EXTENSION = ".player";
    public static final String ZOMBIE_EXTENSION = ".zomb";
    public static final String GUN_EXTENSION = ".gun";
    public static final String PROJECTILE_EXTENSION = ".prj";
    public static final String MAP_EXTENSION = ".map";
    
    public static final String[] EXTENSIONS_ARRAY = {PLAYER_EXTENSION, ZOMBIE_EXTENSION, GUN_EXTENSION, PROJECTILE_EXTENSION, MAP_EXTENSION};
    
    public static int NUMBER_OF_EXTENSIONS = 5;
}