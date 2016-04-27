package server;

import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * <b>Server</b>
 * 
 * <p> 
 *  Main server class. 
 *  Handles the management of server threads and packages.
 * </p>
 * 
 * @author Renato Lui Geh
 */
public class Server extends JFrame {
    
    private ClientHandler ch;
    private ChatHandler chat;
    
    private JPanel panel = new JPanel();
    private JButton runButton, stopButton;
    private JTextArea debugArea, positionArea, packagesArea, chatArea;
    private JScrollPane scrollDebugArea, scrollPositionArea, scrollPackagesArea, scrollChatArea;
    private JRadioButton trueDebugMode, falseDebugMode;
    
    private Rectangle defaultScrollRect;
    
    private boolean isRunning = false;
    private boolean debugMode = true;
    public int port;
    
    public static void main(String[] args) { 
        Server server = new Server();
    }
    
    public Server() {
        String s = JOptionPane.showInputDialog(null, "Please provide the port in which you wish"
                + "\n the client to connect to:", "Port Input", JOptionPane.INFORMATION_MESSAGE);
        port = Integer.parseInt(s);
        
        this.setSize(990, 750);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Zombies Mayhem! - Dedicated Server");
        this.setEnabled(true);
        this.setVisible(true);
        this.setResizable(false);
        
        initLayout();
        initComponents();  
    }
    
    public void writeLine(String line) {
        if(debugMode)
            debugArea.setText(debugArea.getText() + "\n" + line);
    }
    
    public void writePosition(String line) {
        if(debugMode)
            positionArea.setText(positionArea.getText() + "\n" + line);
    }
    
    public void writePackage(String line) {
        if(debugMode)
            packagesArea.setText(packagesArea.getText() + "\n" + line);
    }
    
    public void writeString(String s) {
        if(debugMode)
            debugArea.setText(debugArea.getText() + s);
    }
    
    public void writeOnChat(String s) {
        if(debugMode)
            chatArea.setText(chatArea.getText() + "\n" + s);
    }
    
    private void initComponents() {        
        panel.setVisible(true);
        
        runButton = new JButton("Start");
        stopButton = new JButton("Stop");
        
        debugArea = new JTextArea(30, 40);
        debugArea.setLineWrap(true);
        
        positionArea = new JTextArea(30, 17);
        positionArea.setLineWrap(true);
        
        packagesArea = new JTextArea(30, 17);
        packagesArea.setLineWrap(true);
        
        chatArea = new JTextArea(5, 80);
        chatArea.setLineWrap(true);
        
        scrollPositionArea = new JScrollPane(positionArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPositionArea.setBorder(BorderFactory.createTitledBorder("Position Panel"));
        
        scrollPackagesArea = new JScrollPane(packagesArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPackagesArea.setBorder(BorderFactory.createTitledBorder("Packages Panel"));
        
        scrollDebugArea = new JScrollPane(debugArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollDebugArea.setBorder(BorderFactory.createTitledBorder("Debug Panel"));
        
        scrollChatArea = new JScrollPane(chatArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollChatArea.setBorder(BorderFactory.createTitledBorder("Chat Panel"));
        
        
        scrollDebugArea.setWheelScrollingEnabled(true);
        scrollPackagesArea.setWheelScrollingEnabled(true);
        scrollPositionArea.setWheelScrollingEnabled(true);
        scrollChatArea.setWheelScrollingEnabled(true);
        
        defaultScrollRect = new Rectangle(0,debugArea.getHeight()-2,1,1);
        
        JPanel debugModePanel = new JPanel();
        
        ButtonGroup debugModeGroup = new ButtonGroup();
        trueDebugMode = new JRadioButton("On");
        trueDebugMode.setSelected(true);
        falseDebugMode = new JRadioButton("Off");
        
        debugModeGroup.add(trueDebugMode);
        debugModeGroup.add(falseDebugMode);
        
        debugModePanel.add(trueDebugMode);
        debugModePanel.add(falseDebugMode);
        
        debugModePanel.setBorder(BorderFactory.createTitledBorder("Debug Mode"));
        
        stopButton.addActionListener(buttonListener);
        runButton.addActionListener(buttonListener);
        
        this.panel.add(scrollDebugArea);
        this.panel.add(scrollPositionArea);
        this.panel.add(scrollPackagesArea);
        this.panel.add(scrollChatArea);
        this.panel.add(runButton);
        this.panel.add(debugModePanel);
        this.panel.add(stopButton);
        
        this.add(panel);
        
        panel.updateUI();
    }
    
    private void initLayout() {
        this.panel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
    }
    
    private void runServer() {        
        ch = new ClientHandler(this, port);
        ch.start();
        
        chat = new ChatHandler(this, port);
        chat.start();
        
        InterfaceThread it = new InterfaceThread();
        it.start();
    }
    
    private ActionListener buttonListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent event) {
            if(event.getSource().equals(runButton)) {
                if(isRunning)
                    JOptionPane.showMessageDialog(null, 
                            "Server is already running. You cannot have two servers"
                            + " on a same static IP. Please stop the server before"
                            + " creating another one.", 
                            "Error: Server already started!",
                            JOptionPane.ERROR_MESSAGE);
                else {
                    runServer();
                    isRunning = true;
                }
            } else if(event.getSource().equals(stopButton)) {
                if(isRunning) {
                    ch.disconnectAll();
                    ch.interrupt();
                } else {
                    JOptionPane.showMessageDialog(null, 
                            "Server is already not active."
                            + " This means either you have already stopped"
                            + " the server or you have not even started it yet.", 
                            "Error: Server is already inactive!",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    };
    
    public class InterfaceThread extends Thread {       
        @Override
        public void run() {
            while(true) {
                if(debugArea.getLineCount() >= 300)
                    debugArea.setText("");
                if(positionArea.getLineCount() >= 300)
                    positionArea.setText("");
                if(packagesArea.getLineCount() >= 300)
                    packagesArea.setText("");
                if(chatArea.getLineCount() >= 300)
                    chatArea.setText("");

                if(trueDebugMode.isSelected())
                    debugMode = true;
                if(falseDebugMode.isSelected())
                    debugMode = false;
            }
        }
    }
}

