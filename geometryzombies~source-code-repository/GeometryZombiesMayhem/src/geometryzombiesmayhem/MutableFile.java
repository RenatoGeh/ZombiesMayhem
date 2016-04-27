package geometryzombiesmayhem;

import java.io.File;
import java.util.jar.JarEntry;

/**
 * <p>A class that can either represent a <code>File</code> or a <code>JarEntry</code>.</p>
 * 
 * <p><code>MutableFile</code> behaves just like a <code>File</code> or <code>JarEntry</code>
 * according to the current state of the project. If the use of <code>jar</code>s is present,
 * <code>MutableFile</code> "mutates" into a <code>JarEntry</code>, whilst when the project is
 * not compressed, it turns into a <code>File</code>.</p>
 * 
 * <p>This is necessary since, when compressed, <code>File</code>s are not fully supported 
 * -- since they are not actually files, but entries in an archive.</p>
 * 
 * @author Renato Lui Geh
 */
public class MutableFile {
    private File file;
    private JarEntry entry;
    
    public MutableFile(String path) {
        file = new File(AssetManager.CLASSPATH + path);
        entry = new JarEntry(AssetManager.PACKAGE_NAME + path);
    }
    
    public File getFile() {return file;}
    public JarEntry getEntry() {return entry;}
    
    public String getName() {
        if(!AssetManager.isCompressed())
            return file.getName();
        else {
            String[] s = entry.getName().split(File.pathSeparator);
            return s[s.length-1];
        }
    }
    
    public String getPath() {
        if(!AssetManager.isCompressed()) 
            return file.getPath();
        else 
            return AssetManager.JAR_PATH + entry.getName();
    }
    
    public String getNoun() {return this.getName().split(".")[0];}
}