package statscompressor;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class StatsCompressor extends JFrame {
    
    private JPanel mainPanel;
    
    private JButton browseImageButton, createButton, advancedButton, pathButton;
    private JTextField nameField, xField, yField, damageField, healthField, massField, imageField, filenameField;
    private JTextArea statsPreviewArea, advancedStatsArea;
    private JPanel imagePreviewPanel;
    private JComboBox extensionBox;
    private Box controlBox, deadBox;
    private JRadioButton controlTrueRadio, controlFalseRadio, deadTrueRadio, deadFalseRadio;
    private ButtonGroup controlGroup, deadGroup;
    
    public static void main(String[] args) { 
        new StatsCompressor();
    }
    public StatsCompressor() {
        this.setSize(800, 600);
        this.setFocusable(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setVisible(true);
        
        this.setTitle("Zombies Mayhem Stats Compressor");
        
        mainPanel = new JPanel();
        mainPanel.setVisible(true);
        mainPanel.setLayout(new GridBagLayout());
        
        initComponents();
        initLayout();
        
        this.add(mainPanel);
    }
    
    public void initComponents() {
        filenameField = new JTextField(100);
        nameField = new JTextField(100);
        imageField = new JTextField(100);
        xField = new JTextField(40);
        yField = new JTextField(40);
        damageField = new JTextField(50);
        healthField = new JTextField(50);
        massField = new JTextField(20);
        
        String[] extensionArray = {"player", "zomb", "prj", "gun", "map"};
        extensionBox = new JComboBox(extensionArray);
        
        controlTrueRadio = new JRadioButton("True");
        controlFalseRadio = new JRadioButton("False");
        
        controlBox = Box.createVerticalBox();
        controlBox.setBorder(BorderFactory.createTitledBorder(null, "Control", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.LEFT));
        controlGroup = new ButtonGroup();
        controlGroup.add(controlTrueRadio);
        controlGroup.add(controlFalseRadio);
        
        deadTrueRadio = new JRadioButton("True");
        deadFalseRadio = new JRadioButton("False");
        
        deadBox = Box.createVerticalBox();
        deadBox.setBorder(BorderFactory.createTitledBorder(null, "isDead", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.LEFT));
        deadGroup = new ButtonGroup();
        deadGroup.add(deadTrueRadio);
        deadGroup.add(deadFalseRadio);
        
        statsPreviewArea = new JTextArea((int)(this.getWidth()/5*2 - 10), (int)(this.getHeight()/8*5 - 10));
        statsPreviewArea.setName("Stats Preview");
        
        imagePreviewPanel = new JPanel();
        imagePreviewPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
        imagePreviewPanel.setSize((int)(this.getWidth()/5*2 - 10), (int)(this.getHeight()/8*5 - 10));
        imagePreviewPanel.setVisible(true);
        imagePreviewPanel.setName("Image Preview Panel");
    }
    
    public void initLayout() {
        addItem(new JLabel("Filename:"), 0, 0, 1, 1, GridBagConstraints.EAST);
        addItem(filenameField, 0, 1, 1, 1, GridBagConstraints.WEST);
        addItem(new JLabel("."), 0, 2, 1, 1, GridBagConstraints.WEST);
        addItem(extensionBox, 0, 2, 1, 1, GridBagConstraints.WEST);
        addItem(new JLabel("Name:"), 2, 0, 1, 1, GridBagConstraints.EAST);
        addItem(nameField, 2, 1, 1, 1, GridBagConstraints.WEST);
        addItem(new JLabel("Image:"), 3, 0, 1, 1, GridBagConstraints.EAST);
        addItem(imageField, 3, 1, 1, 1, GridBagConstraints.WEST);
        addItem(new JLabel("X-Axis:"), 4, 0, 1, 1, GridBagConstraints.EAST);
        addItem(xField, 4, 1, 1, 1, GridBagConstraints.WEST);
        addItem(new JLabel("Y-Axis:"), 4, 2, 1, 1, GridBagConstraints.EAST);
        addItem(yField, 4, 3, 1, 1, GridBagConstraints.WEST);
        addItem(new JLabel("Damage:"), 5, 0, 1, 1, GridBagConstraints.EAST);
        addItem(damageField, 5, 1, 1, 1, GridBagConstraints.WEST);
        addItem(new JLabel("Health:"), 5, 2, 1, 1, GridBagConstraints.EAST);
        addItem(healthField, 5, 3, 1, 1, GridBagConstraints.WEST);
        addItem(new JLabel("Mass:"), 6, 0, 1, 1, GridBagConstraints.EAST);
        addItem(massField, 6, 1, 1, 1, GridBagConstraints.WEST);
        addItem(controlBox, 7, 0, 2, 2, GridBagConstraints.CENTER);
        addItem(deadBox, 7, 2, 2, 2, GridBagConstraints.CENTER);
        addItem(new JLabel(".stats Preview:"), 0, 4, 2, 1, GridBagConstraints.CENTER);
        addItem(statsPreviewArea, 0, 4, 2, 5, GridBagConstraints.CENTER);
        addItem(new JLabel("Image Preview:"), 4, 4, 2, 1, GridBagConstraints.CENTER);
        addItem(imagePreviewPanel, 4, 4, 2, 5, GridBagConstraints.CENTER);
    }
    
    public void addItem(JComponent component, int y, int x, int width, int height, int align) {
        GridBagConstraints constraint = new GridBagConstraints();
        
        constraint.gridy = y;
        constraint.gridx = x;
        constraint.gridwidth = width;
        constraint.gridheight = height;
        constraint.anchor = align;
        constraint.weightx = 100.0;
        constraint.weighty = 100.0;
        constraint.insets = new Insets(10, 10, 10, 10);
        constraint.fill = GridBagConstraints.HORIZONTAL;
        
        if(component.getName() != null)
            constraint.fill = GridBagConstraints.BOTH;
            
        
        mainPanel.add(component, constraint);
    }
}