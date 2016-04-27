package geometryzombiesmayhem;

public abstract class WComponent extends ComponentP implements Paintable {
    private Vector2D position = new Vector2D();
    private boolean isActive = true;
    private boolean isVisible = false;
    
    @Override
    public void updateWindowDefaultPosition() {
        this.position.addLocal(window.x, window.y);
    }
    
    @Override
    public boolean isVisible() {return isVisible;}    
    @Override
    public void setVisible(boolean visible) {this.isVisible = visible;}

    @Override
    public boolean isActive() {return isActive;}
    @Override
    public void setActive(boolean active) {this.isActive = active;}
}