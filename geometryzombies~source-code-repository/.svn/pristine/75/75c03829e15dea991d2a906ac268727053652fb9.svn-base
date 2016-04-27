package geometryzombiesmayhem;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.JComponent;

/**
 * 
 * Captures Screen Prints
 * 
 * @author Renato Lui Geh
 */
public final class ScreenCapture {
    public static final String DEFAULT_SCREENSHOT_PATH = AssetManager.DEFAULT_DATA_PATH;
    
    public static void printScreen(String ref, JComponent c, RenderingHints render, boolean machinima) {
        ImageManipulator.save(ScreenCapture.getScreen(c, render, machinima), ref);
    }
    
    public static void printScreen(JComponent c, RenderingHints render, boolean machinima) {
        String folderPath = DEFAULT_SCREENSHOT_PATH + "screenshots" + File.separator;
        String defaultPath = DEFAULT_SCREENSHOT_PATH + "screenshots" + File.separator + "screen";
        
        if(!AssetManager.existsCannonical(folderPath))
            new File(folderPath).mkdirs();
        
        boolean foo = true;
        int n = 0;
        String path = defaultPath;
        while(foo) {
            if(AssetManager.existsCannonical(path + ".png")){
                path = defaultPath + Integer.toString(n);
            } else {
                foo = false;
            }
            n++;
        }
        ScreenCapture.printScreen(path + ".png", c, render, machinima);
    }
    
    public static BufferedImage getScreen(JComponent c, RenderingHints render, boolean machinima) {
        BufferedImage buffer = new BufferedImage(c.getWidth(), c.getHeight(), BufferedImage.TYPE_INT_ARGB_PRE);
        Graphics2D graph = buffer.createGraphics();
        graph.addRenderingHints(render);
        
        if(c instanceof GameFrame.PaintArea) {
            boolean theaterMode = UserInterface.onTheater(), debugMode = UserInterface.isDebugging();

            if(machinima) {
                UserInterface.setDebug(false);
                UserInterface.setTheater(true);
            }

            c.paint(graph);
            graph.dispose();

            if(machinima) {
                UserInterface.setDebug(debugMode);
                UserInterface.setTheater(theaterMode);
            }
        } else if(c instanceof ZM.MenuArea) {
            c.paint(graph);
            graph.dispose();
        }
        
        return buffer;
    }
}