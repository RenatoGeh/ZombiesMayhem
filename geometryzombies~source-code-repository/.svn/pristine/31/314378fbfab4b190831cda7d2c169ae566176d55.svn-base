package geometryzombiesmayhem;

import java.io.IOException;
import java.net.Socket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import javax.swing.JOptionPane;
/**
 * A thread-able Client.
 *
 * <br/><br/>
 * 
 * <b>Important: The default port is 1234.</b>
 * 
 * <br/>
 * 
 * <p>
 *  To make a player <i>connectable</i>, simply
 *  create an instance of this class and run it as a normal thread.
 * </p>
 * 
 * <p>
 *  This class handles all incoming and outcoming,
 *  server/client-socket related data.
 * </p>
 * 
 * <p>
 *  As the usual way of connecting a server to a client in Java, 
 *  this class uses TCP/IP to connect to an IP-static server.
 * </p>
 * 
 * <br/>
 * <br/>
 * 
 *      <u>Notes and observations of the author: </u>
 * <i>
 *  <p>
 *      Note that this class uses the ObjectOutput/InputStream.
 *  </p>
 *  <p>
 *      This is required since the data used through the sockets
 *      are primarily class related stuff.
 *  </p>
 *  <p>
 *      Also notice that I did not use the boolean methods from that
 *      input and output stream. It seems like these methods are 
 *      quite buggy, interrupting the connectivity for an unknown reason.
 *  </p>
 *  <p>
 *      As a final observation, when initializing the ObjectI/OStreams, 
 *      certain caution related to their order is necessary. If you 
 *      initialize them in the same order in the server side and client side,
 *      they appear to conflict themselves and stop connectivity.
 *  </p>
 * </i>
 * 
 * <br/>
 * <br/>
 * 
 * <p>
 *  Post Scriptum: This class may be modified as you like. 
 *  I took a long time to learn and figure out all the tips and
 *  tricks of the socket and object I/O stream classes. As a side-note,
 *  I recommend taking a look at multi-threading, the java.io package and
 *  especially the rather confusing (and buggy) ObjectInputStream and
 *  ObjectOutputStream classes.
 * </p>
 * 
 * @author Renato Lui Geh
 * @version 0.0.5
 * 
 * @see PlayerPackage
 */
public class Client extends Thread {
    private ObjectInputStream in = null;
    private ObjectOutputStream out = null;
    private Socket s;
    
    public PlayerPackage pp1;
    public PlayerPackage pp2;
    
    public Player player;
    
    public String host = "localhost";
    public int port = 1234;
    
    private boolean hasNoticed = false;
    
    public Client(Player player, String host, int port) {      
        this.host = host;
        this.port = port;
        
        initClient();
        initPackage(player);
    }
    
    private void initClient() {
        try {
            s = getSocket(port);
            
            in = new ObjectInputStream(s.getInputStream());
            out = new ObjectOutputStream(s.getOutputStream());
            
        } catch(IOException | NullPointerException exc) {
            JOptionPane.showMessageDialog(null, "Zombies Mayhem! encountered a fatal error. "
                    + "Please send the following message to our \nhighly trained monkey army of doom: \n"
                    + "\n"
                    + "Cause: " + exc.getCause() + "\n"
                    + "Message: " + exc.getMessage() + "\n"
                    + "Stack Trace: " + exc.getStackTrace() + "\n"
                    + "Exception type: " + exc.toString() + "\n", 
                    "I/O Exception! Oh noes!", JOptionPane.ERROR_MESSAGE);
            
            System.exit(0);
        }
    }
    
    private void initPackage(Player player) {
        this.player = player;
        
        pp1 = player.generatePackage();
        pp2 = new PlayerPackage();
    }
    
    private void handleConnection() {        
        try {   
            while(true) {
                if(in.readObject().equals("true")) {
                    out.writeObject(pp1);
                    
                    pp2 = (PlayerPackage)in.readObject();
                    
                    refreshPackage();
                    
                    out.reset();
                } else {
                    if(!hasNoticed) {
                        System.err.println("Waiting for another player...");
                        hasNoticed = true;
                    }
                }
            }
        } catch (IOException | ClassNotFoundException exc) {
            System.err.println(exc.getMessage());
        }
    }
    
    public Socket getSocket(int port) {
        try {
            return new Socket(InetAddress.getByName(host), port);
        } catch(UnknownHostException exc) {     
            JOptionPane.showMessageDialog(null, "Host " + host + " at port " + port + " was not found."
                    + " Yarr vessel may not boarrrd tha ship ahoy! Nay!!"
                    + "\n"
                    + "Cause: " + exc.getCause() + "\n"
                    + "Message: " + exc.getMessage() + "\n"
                    + "Stack Trace: " + exc.getStackTrace() + "\n"
                    + "Exception type: " + exc.toString() + "\n", 
                    "Unknown Host Exception! Ooops!", JOptionPane.ERROR_MESSAGE);
            return null;
        } catch(IOException exc) {
            System.err.println("I/O Error: " + exc.getMessage());
            return null;
        }
        
    }
    
    public void closeConnection() {
        try {
            s.close();
            in.close();
            out.close();
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }
    
    public void refreshPackage() {
        pp1 = player.generatePackage();
    }
    
    @Override
    public void run() {
        handleConnection();
    }
    
    private static Client client;
    private static boolean isActive = false;
    
    public static void initialize(Player player, String host, int port) {
        client = new Client(player, host, port);
        client.start();
        isActive = true;
    }
    
    public static Client getClient() {return client;}
    public static boolean isActive() {return isActive;}
}