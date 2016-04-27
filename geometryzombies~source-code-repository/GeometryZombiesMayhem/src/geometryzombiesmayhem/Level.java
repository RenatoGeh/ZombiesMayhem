package geometryzombiesmayhem;

/**
 *
 * @author Yan
 */
public abstract class Level implements java.io.Serializable {
    private Scene currentScene;
    private String name = "Level_00";
    
    public abstract void load();
    public abstract void update();
    public abstract void close();
    
    public void setName(String name) {this.name = name;}
    public String getName() {return this.name;}
    
    public void setScene(Scene s) {this.currentScene = s;}
    public Scene getScene() {return this.currentScene;}
    
    private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
        this.name = (String)in.readObject();
        this.currentScene = (Scene)in.readObject();
    }
    
    private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
        out.writeObject(this.name);
        out.writeObject(this.currentScene);
    }
    
    private static final long serialVersionUID = -64654456985168L;
}
