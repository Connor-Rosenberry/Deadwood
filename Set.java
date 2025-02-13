public class Set extends Room  {
    //  VARIABLES
    private String status;
    private Scene scene;
    private Role[] roles;
    private Take[] takes;

    // CONSTRUCTORS
    public Set() {
        super();
        this.status = null;
        this.scene = null;
        this.roles = null;
        this.takes = null;
    }

    public Set(String name, String[] adjacentRooms, Player[] playersHere, int x, int y, int w, int h, String status, Scene scene, Role[] roles, Take[] takes) {
        super(name, adjacentRooms, playersHere, x, y, w, h);
        this.status = status;
        this.scene = scene;
        this.roles = roles;
        this.takes = takes;
    }

    // GETS/SETS
    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Scene getScene() {
        return this.scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public Role[] getRoles() {
        return this.roles;
    }

    public void setRoles(Role[] roles) {
        this.roles = roles;
    }

    public Take[] getTakes() {
        return this.takes;
    }

    public void setTakes(Take[] takes) {
        this.takes = takes;
    }

    // METHODS
    // TODO
    public void clearScene() {

    }

    @Override
    public boolean isSet() {
        return true;
    }
}
