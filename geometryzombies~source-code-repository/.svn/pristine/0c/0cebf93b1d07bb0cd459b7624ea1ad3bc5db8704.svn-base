package geometryzombiesmayhem.leveleditor;

import geometryzombiesmayhem.AssetManager;
import geometryzombiesmayhem.ColorFactory;
import geometryzombiesmayhem.Dialog;
import geometryzombiesmayhem.ZM;
import geometryzombiesmayhem.circuitry.DialogAdapter;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author Renato Lui Geh
 */
public class DialogCreator extends JFrame {
    public static boolean inUse = false;
    private CreatorArea panel;
    
    private DialogCreator() {
        super("Dialog Creator");
        this.setLocationRelativeTo(ZM.gameFrame);
        this.setSize(600, 500);
        this.setEnabled(true);
        this.setVisible(true);
        this.setAlwaysOnTop(true);
        
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(screen.width/2-this.getWidth()/2, screen.height/2-this.getHeight()/2);
        
        panel = new CreatorArea();
        this.add(panel);
        
        DialogCreator.inUse = true;
    }
    
    private class CreatorArea extends JPanel {
        private Dialog.Predefined predef = null;
        private JComboBox predefined;
        private JCheckBox attention;
        private JTextField path;
        private JButton browse;
        private JTextArea description;
        private JButton create, cancel;
        private Box vertBox;
        
        private Color color = ColorFactory.getColor(Color.BLACK, 80);
        private Font font = new Font(Font.DIALOG_INPUT, Font.PLAIN, 15);
        private DialogAdapter.Type type = DialogAdapter.Type.ONCE;
        
        public CreatorArea() {      
            vertBox = Box.createVerticalBox();
            
            { //Predefined Combo Box
                JLabel predefinedLabel = new JLabel("Predefined Dialog:");
                predefined = new JComboBox();
                predefined.addItem("Tarma");
                predefined.addItem("Luigi Jesus");
                predefined.addItem("Custom");
                predefined.setSelectedItem("Custom");
                predefined.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent event) {
                        Object item = predefined.getSelectedItem();
                        if(item.equals("Tarma"))
                            predef = Dialog.Predefined.TARMA;
                        else if(item.equals("Luigi Jesus"))
                            predef = Dialog.Predefined.JESUS;
                        else if(item.equals("Custom"))
                            predef = null;
                        
                        boolean custom = item.equals("Custom");
                        path.setEditable(custom);
                        browse.setEnabled(custom);
                    }
                });
                
                attention = new JCheckBox("Requires Attention:", false);
                
                Box box = Box.createHorizontalBox();
                this.add(box, 
                        Box.createHorizontalStrut(20), predefinedLabel,
                        Box.createHorizontalStrut(10), predefined,
                        Box.createHorizontalStrut(50), attention);
                
//                this.add(new JLabel("Advanced Dialog Creator"));
                this.add(vertBox, box, Box.createVerticalStrut(20));
            }
            
            { //Dialog Image
                JLabel pathLabel = new JLabel("Avatar's Image Path:");
                path = new JTextField(25);
                browse = new JButton("Browse");
                browse.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent event) {
                        try {
                            JFileChooser open = new JFileChooser();
                            open.setDialogTitle("Choose an image file:");
                            open.setDialogType(JFileChooser.OPEN_DIALOG);
                            open.setFileFilter(new FileFilter() {
                                @Override
                                public boolean accept(File f) {
                                    for(String s : suffix) if(f.getName().matches(s)) return true;
                                    return f.isDirectory();
                                }
                                @Override
                                public String getDescription() {
                                    return "Java supported image files (*.jpg, *.png, *.gif, *.bmp, *.wbmp)";
                                }
                                String[] suffix = new String[]{".+\\.jpg", ".+\\.png", ".+\\.gif", 
                                    ".+\\.bmp", ".+\\.wbmp"};
                            });
                            open.setFileSelectionMode(JFileChooser.FILES_ONLY);
                            open.setMultiSelectionEnabled(false);
                            if(open.showOpenDialog(DialogCreator.this)==JFileChooser.APPROVE_OPTION)
                                path.setText(open.getSelectedFile().getCanonicalPath());
                        } catch (IOException ex) {
                            Logger.getLogger(DialogCreator.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });
                
                Box box = Box.createHorizontalBox();
                this.add(box,
                        Box.createHorizontalStrut(20), pathLabel,
                        Box.createHorizontalStrut(10), path,
                        Box.createHorizontalStrut(10), browse);
                
                this.add(vertBox, box, Box.createVerticalStrut(20));
            }
            
            { //Dialog Text
                description = new JTextArea("Welcome to the Dialog Creator!\n"
                        + "This is the body of the Dialog's text. Be creative!", 20, 50);
                description.setLineWrap(true);
                description.setWrapStyleWord(true);
                JScrollPane scroll = new JScrollPane(description, 
                        JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                
                this.add(vertBox, scroll, Box.createVerticalStrut(20));
            }
            
            { //Create or Cancel
                create = new JButton("Create");
                cancel = new JButton("Cancel");
                
                ActionListener buttonListener = new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent event) {
                        if(event.getSource().equals(create)) {
                            Dialog diag = predef!=null?
                                    Dialog.getDialog(predef, CreatorArea.this.description.getText(), attention.isSelected()):
                                    new Dialog(AssetManager.loadImage(CreatorArea.this.path.getText()), 
                                    CreatorArea.this.description.getText(), color, color.getAlpha(), font, attention.isSelected());
                            DialogBrush.getBrush().submit(diag, type);
                            DialogCreator.inUse = false;
                            DialogCreator.this.dispose();
                        } else if(event.getSource().equals(cancel))
                            if(JOptionPane.showConfirmDialog(DialogCreator.this, 
                                    "Are you sure you want to discard the current Dialog?", "u sur, bro?", 
                                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE)==JOptionPane.YES_OPTION) {
                                DialogCreator.this.dispose();
                                DialogCreator.inUse = false;
                            }
                    }
                };
                
                create.addActionListener(buttonListener);
                cancel.addActionListener(buttonListener);
                
                Box box = Box.createHorizontalBox();
                this.add(box,
                        Box.createHorizontalStrut(20), cancel,
                        Box.createHorizontalStrut(100), create);
                
                this.add(vertBox, box, Box.createVerticalStrut(20));
            }
            
            this.add(vertBox);
        }
        
        private void add(Box b, Component... c) {for(Component comp : c) b.add(comp);}
    } 
    
    public static void activate() {DialogCreator e = new DialogCreator();}
}
