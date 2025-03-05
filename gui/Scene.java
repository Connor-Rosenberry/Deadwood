package gui;

public class Scene {
    // VARIABLES
    private int id;
    private String name;
    private String img;
    private int budget;
    private int number;
    private String status;  // "not active", "active", or "wrapped"
    private int shotCounter;  // depends on location
    private Set location;
    private String description;
    private Role[] roles;

    // CONSTRUCTORS
    public Scene(){
        this.id = 0;
        this.name = null;
        this.budget = 0;
        this.status = null;
        this.shotCounter = 0;
        this.location = null;
        this.description = null;
        this.roles = null;
    }

    public Scene(int id, String name, int budget, String status, int shotCounter, Set location, String description, Role[] roles){
        this.id = id;
        this.name = name;
        this.budget = budget;
        this.status = status;
        this.shotCounter = shotCounter;
        this.location = location;
        this.description = description;
        this.roles = roles;
    }

    // GETS/SETS
    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return this.img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getNumber() {
        return this.number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getBudget() {
        return this.budget;
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getShotCounter() {
        return this.shotCounter;
    }

    public void setShotCounter(int shotCounter) {
        this.shotCounter = shotCounter;
    }

    public Set getLocation() {
        return this.location;
    }

    public void setLocation(Set location) {
        this.location = location;
    }

    public String getDescription(){
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Role[] getRoles() {
        return this.roles;
    }

    public void setRoles(Role[] roles) {
        this.roles = roles;
    }

    // METHODS
    // return total number of roles
    public int totalRoles() {
        return roles.length;
    }

    // return the names of all roles in this scene 
    public String[] getRoleNames() {
        String[] names = new String[roles.length];
        for(int i = 0; i < roles.length; i++) {
            names[i] = roles[i].getName() + ", rank required " + roles[i].getRankToAct();;
        }
        return names;
    }
}
