public class Player {
    private String name;
    private Room location;
    private int rank;
    private int dollars;
    private int credits;
    private Role role;

    public Player(String name) {
        this.name = name;
        this.location = "trailerPark";
        this.rank = 1;
        this.dollars = 0;
        this.credits = 0;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Room getLocation() {
        return this.location;
    }

    public void setLocation(Room location) {
        this.location = location;
    }

    public int getRank() {
        return this.rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getDollars() {
        return this.dollars;
    }

    // maybe change to increase dollars or decrease dollars
    public void setDollars(int dollars) {
        this.dollars = dollars;
    }

    public int getCredits() {
        return this.credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public Role getRole() {
        return this.role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void addDollars(int dollars) {
        this.dollars += dollars;
    }

    public void addCredits(int credits) {
        this.credits += credits;
    }

    // might be in moderator class below
    public void move(Room location) {

    }
    public void upgrade(int rank) {

    }
    public void takeRole(Role role) {
    
    }
    public void workRole() {

    }
    private void act() {

    }
    private void rehearse() {
        
    }


}