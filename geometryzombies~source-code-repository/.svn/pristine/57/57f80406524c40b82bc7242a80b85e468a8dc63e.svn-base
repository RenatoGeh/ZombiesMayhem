package geometryzombiesmayhem.leveleditor;

import geometryzombiesmayhem.AssetManager;
import geometryzombiesmayhem.GameFrame;
import geometryzombiesmayhem.Scene;
import geometryzombiesmayhem.SceneLayer;
import geometryzombiesmayhem.Vector2D;
import geometryzombiesmayhem.ZM;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author Renato Lui Geh
 */
public class SceneFrame extends JFrame {
    private ScenePanel panel;
    
    public SceneFrame() {
        super("Scene Level Properties");
        
        this.setSize(800, 600);
        this.setLocationRelativeTo(ZM.gameFrame);
        this.setVisible(true);
        this.setEnabled(true);
        this.setAlwaysOnTop(true);
        
        java.awt.Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((screen.width-this.getWidth())/2, (screen.height-this.getHeight())/2);
        
        panel = new ScenePanel();
        this.add(new javax.swing.JScrollPane(panel), BorderLayout.CENTER);
        this.add(panel);
    }
    
    private class ScenePanel extends JPanel {
        private Scene scene = new Scene("Untitled");
        private ArrayList<JComboField> fields = new ArrayList<>();        
        private Box fieldBox = Box.createVerticalBox();
        private JComboField sceneName;
        private JButton apply, cancel;
        
        private ScenePanel() {
            SceneFrame.inUse = true;
            
            sceneName = new JComboField("Scene Name:", 25, "Add Layer", false);
            sceneName.getField().setText(scene.getName());
            sceneName.getButton().addActionListener(listener);
            Box box = Box.createHorizontalBox();
            this.addToBox(box, 
                    sceneName.getLabel(), Box.createHorizontalStrut(20),
                    sceneName.getField(), Box.createHorizontalStrut(20), 
                    sceneName.getButton());
            
            cancel = new JButton("Cancel");
            apply = new JButton("Apply");
            
            ActionListener applyListener = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent event) {
                    if(event.getSource().equals(cancel)) {
                        if(JOptionPane.showConfirmDialog(SceneFrame.this, 
                                    "Are you sure you want to discard the current Scene?", "u sur, bro?", 
                                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE)==JOptionPane.YES_OPTION) {
                                SceneFrame.this.dispose();
                                SceneFrame.inUse = false;
                        }
                    } else {
                        scene = new Scene(sceneName.getField().getName());
                        
                        for(int i=0;i<fields.size();i++) {
                            if(fields.get(i).getField().getText().trim().isEmpty()) return;
                            
                            SceneLayer layer = new SceneLayer("Layer " + i,
                                    AssetManager.loadExternalImage(fields.get(i).getField().getText()),
                                    false, false, Vector2D.TOP_LEFT.copy(), ((float)i)/fields.size());
                            
                            if(fields.get(i).getCheckBox().isSelected())
                                scene.addForegroundLayer(layer);
                            else
                                scene.addLayer(layer);
                        }
                        
                        GameFrame.currentLevel.setScene(scene);
                    }
                }
            };
            
            cancel.addActionListener(applyListener);
            apply.addActionListener(applyListener);
            
            Box upper = Box.createVerticalBox();
            Box lastBox = Box.createHorizontalBox();
            this.addToBox(lastBox, cancel, Box.createHorizontalStrut(150), apply);
            this.addToBox(upper, Box.createVerticalStrut(20), lastBox);
            
            this.add();
            
            this.add(box);
            this.add(fieldBox);
            this.add(upper);
        }
        
        private void removeLayer(int index) {
            System.err.println("Removing... " + index);
            JComboField field = fields.get(index);
            this.removeFromBox(fieldBox, field.getLabel(), field.getField(), 
                    field.getButton(), field.getCheckBox(), field.getRemoveButton());
            fieldBox.validate();
        }

        private void removeFromBox(Box b, Component... cs) {for(Component c:cs)b.remove(c);}
        
        private void add(boolean hasButton) {
            JComboField field = !hasButton?
                    new JComboField("Layer " + (fields.size()+1), 25):
                    new JComboField("Layer " + (fields.size()+1), 25, "Browse");
            if(hasButton) {
                field.getButton().addActionListener(listener);
                field.getRemoveButton().addActionListener(listener);
            }
            fields.add(field);
            Box horizontal = Box.createHorizontalBox();
            this.addToBox(horizontal, field.getLabel(), Box.createHorizontalStrut(20),
                    field.getField(), Box.createHorizontalStrut(20));
            if(hasButton)
                this.addToBox(horizontal, field.getButton(), Box.createHorizontalStrut(20),
                        field.getCheckBox(), Box.createHorizontalStrut(20), 
                        field.getRemoveButton());
            this.addToBox(fieldBox, Box.createVerticalStrut(20), horizontal);
            this.validate();
        }
        
        public final void add() {this.add(true);}
        
        private void addToBox(Box b, Component... c) {for(Component cs:c)b.add(cs);}
        
        private class JComboField {
            private JLabel label;
            private JTextField field;
            private JButton button = null, remove = null;
            private JCheckBox fore = null;
            
            public JComboField(String label, int columns) {this(label, columns, null, false);}
            public JComboField(String label, int columns, String button) {this(label, columns, button, true);}
            public JComboField(String label, int columns, String button, boolean check) {
                this.label = new JLabel(label);
                this.field = new JTextField(columns);
                this.button = new JButton(button);
                this.remove = new JButton("Remove");
                if(check) this.fore = new JCheckBox("Is Foreground?", false);
            }
            
            public JLabel getLabel() {return label;}
            public JTextField getField() {return field;}
            public JButton getButton() {return button;}
            public JCheckBox getCheckBox() {return fore;}
            public JButton getRemoveButton() {return remove;}
            
            public boolean matches(Object e) {return e.equals(button)||e.equals(remove);}
        }
        
        private int search(Object e) {
            for(int i=0;i<fields.size();i++)
                if(fields.get(i).matches(e))
                    return i;
            return -1;
        }
        
        private ActionListener listener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                if(event.getSource().equals(sceneName.getButton())) {
                    ScenePanel.this.add();
                    return;
                }
                
                int index = ScenePanel.this.search(event.getSource());
                if(fields.get(index).getButton().equals(event.getSource()))
                    try {
                        JFileChooser open = new JFileChooser();
                        open.setDialogTitle("Choose an image file:");
                        open.setDialogType(JFileChooser.OPEN_DIALOG);
                        open.setFileFilter(new FileFilter() {
                            @Override
                            public boolean accept(File f) {
                                for(String s : suffix) if(f.getName().matches(s) || f.isDirectory()) return true;
                                return false;
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
                        if(open.showOpenDialog(SceneFrame.this)==JFileChooser.APPROVE_OPTION)
                            fields.get(index).getField().setText(open.getSelectedFile().getCanonicalPath());
                    } catch (IOException ex) {
                        Logger.getLogger(SceneFrame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                else
                    ScenePanel.this.removeLayer(index);
            }
        };
    }
    
    public static boolean inUse = false;
    public static void activate() {SceneFrame e = new SceneFrame();}
}
