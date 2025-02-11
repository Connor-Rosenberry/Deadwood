public class Set extends Room  {
    //  VARIABLES
    private String status;
    private Scene scene;
    private Role[] roles;

    // CONSTRUCTORS
    public Set(String name, Room[] adjacentRooms, Player[] playersHere, String status, Scene scene, Role[] roles) {
        super(name, adjacentRooms, playersHere);
        this.status = status;
        this.scene = scene;
        this.roles = roles;
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

    // METHODS
    // TODO
    public void clearScene() {

    }

    @Override
    public boolean isSet() {
        return true;
    }
}
