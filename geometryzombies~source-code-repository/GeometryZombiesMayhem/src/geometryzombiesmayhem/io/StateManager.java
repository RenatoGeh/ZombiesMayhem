package geometryzombiesmayhem.io;

import geometryzombiesmayhem.AssetManager;
import geometryzombiesmayhem.ZM;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

/**
 * <p>A class for serialization management, including basic I/O handling for state saving.</p>
 * 
 * @author Renato Lui Geh
 */
public final class StateManager {
    public static final int ERROR_RESULT = JFileChooser.ERROR_OPTION;
    public static final int CANCEL_RESULT = JFileChooser.CANCEL_OPTION;
    public static final int APPROVE_RESULT = JFileChooser.APPROVE_OPTION;
    
    protected static final String DEFAULT_SAVE_PATH = AssetManager.DEFAULT_DATA_PATH + "saves" + File.separator;
    protected static final String DEFAULT_LEVEL_PATH = AssetManager.DEFAULT_DATA_PATH + "levels" + File.separator;
    
    static {
        new File(DEFAULT_SAVE_PATH).mkdirs();
        new File(DEFAULT_LEVEL_PATH).mkdirs();
    }
    
    public static void autoSave() {StateManager.baseSaving(State.Type.SAVE, "autosave");}
    
    private static void baseSaving(State.Type type, String name) {
        try {
            State s = new State();
            try(ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(s.getPath() + name + StateManager.getAppendix(type))))) {
                out.writeObject(s);
            }
        } catch(java.io.IOException exc) {
            System.err.println(exc.getMessage());
        }
    }
    
    private static void rawSaving(State.Type type, String name) {
        try {
            State s = new State(name);
            try(ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(s.getPath() + StateManager.getAppendix(type))))) {
                out.writeObject(s);
            }
        } catch(java.io.IOException exc) {
            System.err.println(exc.getMessage());
        }
    }
    
    public static int save() {
        int res = JFileChooser.ERROR_OPTION;
        try {
            JFileChooser savePane = new JFileChooser(DEFAULT_SAVE_PATH);
            savePane.setDialogTitle("Save ZM's state (*.save).");
            savePane.setDialogType(JFileChooser.SAVE_DIALOG);
            savePane.setFileFilter(new FileFilter() {
                @Override
                public boolean accept(File f) {return f.getName().matches(".*\\.save") || f.isDirectory();}
                @Override
                public String getDescription() {return "ZombiesMayhem! save files (*.save)";}
            });
            savePane.setFileSelectionMode(JFileChooser.FILES_ONLY);
            savePane.setMultiSelectionEnabled(false);
            if((res=savePane.showSaveDialog(ZM.gameFrame))==JFileChooser.APPROVE_OPTION)
                StateManager.rawSaving(State.Type.SAVE, savePane.getSelectedFile().getCanonicalPath());
        } catch (IOException ex) {
            Logger.getLogger(StateManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            return res;
        }
    }
    public static int saveLevel() {
        int res = JFileChooser.ERROR_OPTION;
        try {
            JFileChooser savePane = new JFileChooser(DEFAULT_LEVEL_PATH);
            savePane.setDialogTitle("Save a ZM level (*.level).");
            savePane.setDialogType(JFileChooser.SAVE_DIALOG);
            savePane.setFileFilter(new FileFilter() {
                @Override
                public boolean accept(File f) {return f.getName().matches(".*\\.level") || f.isDirectory();}
                @Override
                public String getDescription() {return "ZombiesMayhem! level files (*.level)";}
            });
            savePane.setFileSelectionMode(JFileChooser.FILES_ONLY);
            savePane.setMultiSelectionEnabled(false);
            if((res=savePane.showSaveDialog(ZM.gameFrame))==JFileChooser.APPROVE_OPTION)
                StateManager.rawSaving(State.Type.LEVEL, savePane.getSelectedFile().getCanonicalPath());
        } catch (IOException ex) {
            Logger.getLogger(StateManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            return res;
        }
    }
    
    public static void save(String name) {StateManager.baseSaving(State.Type.SAVE, name);}
    public static void saveLevel(String name) {StateManager.baseSaving(State.Type.LEVEL, name);}
    
    private static void baseLoading(State.Type type, String name) {
        try {
            String path = type.equals(State.Type.LEVEL)?DEFAULT_LEVEL_PATH:DEFAULT_SAVE_PATH + name + StateManager.getAppendix(type);
            try(ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(path)))) {
                in.readObject();
            }
        } catch(java.io.IOException | ClassNotFoundException exc) {           
            System.err.println(exc.getMessage());
        }
    }

    public static int load() {
        int res = JFileChooser.ERROR_OPTION;
        try {
            JFileChooser loadPane = new JFileChooser(DEFAULT_SAVE_PATH);
            loadPane.setDialogTitle("Load ZM's state (*.save).");
            loadPane.setDialogType(JFileChooser.OPEN_DIALOG);
            loadPane.setFileFilter(new FileFilter() {
                @Override
                public boolean accept(File f) {return f.getName().matches(".*\\.save") || f.isDirectory();}
                @Override
                public String getDescription() {return "ZombiesMayhem! save files (*.save)";}
            });
            loadPane.setFileSelectionMode(JFileChooser.FILES_ONLY);
            loadPane.setMultiSelectionEnabled(false);
            if((res=loadPane.showOpenDialog(ZM.gameFrame))==JFileChooser.APPROVE_OPTION)
                StateManager.rawLoading(State.Type.SAVE, loadPane.getSelectedFile().getCanonicalPath());
        } catch (IOException ex) {
            Logger.getLogger(StateManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            return res;
        }
    }
    
    public static int loadLevel() {
        int res = JFileChooser.ERROR_OPTION;
        try {
            JFileChooser loadPane = new JFileChooser(DEFAULT_LEVEL_PATH);
            loadPane.setDialogTitle("Save a ZM level (*.level).");
            loadPane.setDialogType(JFileChooser.OPEN_DIALOG);
            loadPane.setFileFilter(new FileFilter() {
                @Override
                public boolean accept(File f) {return f.getName().matches(".*\\.level") || f.isDirectory();}
                @Override
                public String getDescription() {return "ZombiesMayhem! level files (*.level)";}
            });
            loadPane.setFileSelectionMode(JFileChooser.FILES_ONLY);
            loadPane.setMultiSelectionEnabled(false);
            if((res=loadPane.showOpenDialog(ZM.gameFrame))==JFileChooser.APPROVE_OPTION)
                StateManager.rawLoading(State.Type.LEVEL, loadPane.getSelectedFile().getCanonicalPath());
        } catch (IOException ex) {
            Logger.getLogger(StateManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            return res;
        }
    }
    
    public static void rawLoading(State.Type type, String name) {
        try {
            State s = new State(name);
            try(ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(s.getPath())))) {
                in.readObject();
            }
        } catch(java.io.IOException | ClassNotFoundException exc) {           
            System.err.println(exc.getMessage());
        }
    }
    
    public static void load(String name) {StateManager.baseLoading(State.Type.SAVE, name);}
    public static void loadLevel(String name) {StateManager.baseLoading(State.Type.LEVEL, name);}
    
    private static String getAppendix(State.Type type) {return type.equals(State.Type.LEVEL)?".level":".save";}

    public static StateStatus preload() {
        StateStatus status = null;
        try {
            int res;
            JFileChooser loadPane = new JFileChooser(DEFAULT_SAVE_PATH);
            loadPane.setDialogTitle("Load ZM's state (*.save).");
            loadPane.setDialogType(JFileChooser.OPEN_DIALOG);
            loadPane.setFileFilter(new FileFilter() {
                @Override
                public boolean accept(File f) {return f.getName().matches(".*\\.save") || f.isDirectory();}
                @Override
                public String getDescription() {return "ZombiesMayhem! save files (*.save)";}
            });
            loadPane.setFileSelectionMode(JFileChooser.FILES_ONLY);
            loadPane.setMultiSelectionEnabled(false);
            res = loadPane.showOpenDialog(ZM.gameFrame);
            status = new StateStatus(State.Type.SAVE, res==JFileChooser.CANCEL_OPTION?"NULL":loadPane.getSelectedFile().getCanonicalPath(), res);
        } catch (IOException ex) {
            Logger.getLogger(StateManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            return status;
        }
    }
    
    public static StateStatus preloadLevel() {
        StateStatus status = null;
        try {
            int res;
            JFileChooser loadPane = new JFileChooser(DEFAULT_LEVEL_PATH);
            loadPane.setDialogTitle("Save a ZM level (*.level).");
            loadPane.setDialogType(JFileChooser.OPEN_DIALOG);
            loadPane.setFileFilter(new FileFilter() {
                @Override
                public boolean accept(File f) {return f.getName().matches(".*\\.level") || f.isDirectory();}
                @Override
                public String getDescription() {return "ZombiesMayhem! level files (*.level)";}
            });
            loadPane.setFileSelectionMode(JFileChooser.FILES_ONLY);
            loadPane.setMultiSelectionEnabled(false);
            res = loadPane.showOpenDialog(ZM.gameFrame);
            status = new StateStatus(State.Type.LEVEL, res==JFileChooser.CANCEL_OPTION?"NULL":loadPane.getSelectedFile().getCanonicalPath(), res);
        } catch (IOException ex) {
            Logger.getLogger(StateManager.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            return status;
        }
    }
    
    public static class StateStatus {
        private State.Type type;
        private String path;
        private int result;
        
        private StateStatus(State.Type type, String path, int result) {
            this.type = type;
            this.path = path;
            this.result = result;
        }
        public StateStatus(String path) {
            this(path.endsWith(".level")?State.Type.LEVEL:State.Type.SAVE, path, StateManager.APPROVE_RESULT);
        }
        
        public State.Type getType() {return this.type;}
        public String getPath() {return this.path;}
        public int getResult() {return result;}
        
        public static final String DEFAULT_LEVEL_PATH = StateManager.DEFAULT_LEVEL_PATH;
        public static final String DEFAULT_SAVE_PATH = StateManager.DEFAULT_SAVE_PATH;
    }
}
