package geometryzombiesmayhem.leveleditor;

import geometryzombiesmayhem.AwesomeAction;
import geometryzombiesmayhem.AwesomeTimer;
import geometryzombiesmayhem.Dialog;
import geometryzombiesmayhem.DialogTree;
import geometryzombiesmayhem.ZM;
import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 *
 * @author Renato Lui Geh
 */
public class DialogFrame extends JFrame {
    private DialogPanel panel;
    private static ArrayList<Dialog> independents = new ArrayList<>();
    private static ArrayList<DialogTree> trees = new ArrayList<>();
    
    public static void register(Dialog d) {independents.add(d);}
    public static void register(DialogTree tree) {trees.add(tree);}
    public static void remove(Dialog d) {independents.remove(d);}
    public static void remove(DialogTree tree) {trees.remove(tree);}
    
    public DialogFrame() {
        super("Advanced Dialog Creator");
        this.setLocationRelativeTo(ZM.gameFrame);
        this.setSize(500, 400);
        java.awt.Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((screen.width+this.getWidth())/2, (screen.height+this.getHeight())/2);
        this.setVisible(true);
        this.setAlwaysOnTop(true);
        
        panel = new DialogPanel();
        this.getContentPane().add(new JScrollPane(panel), BorderLayout.CENTER);
        this.add(panel);
        
        AwesomeTimer.addAction(paintAction, 200, true, TimeUnit.MILLISECONDS);
    }
    
    private AwesomeAction paintAction = new AwesomeAction() {
        @Override
        public void run() {
            panel.repaint();
        }
    };
    
    private class DialogPanel extends JPanel {
        private DialogPanel() {
            
        }
    }
    
    private class DialogLine {
        private ArrayList<DialogIcon> line = new ArrayList<>();
        
        public DialogLine() {
            
        }
    
        private class DialogIcon extends JButton {
            
        }
    }
}
