package gui;

public class Player {
    private String name;
    private Room location;
    private int rank;
    private int dollars;
    private int credits;
    private Role role;
    private boolean hasMoved;

    public Player(String name) {
        this.name = name;
        this.rank = 1;
        this.dollars = 0;
        this.credits = 0;
        this.hasMoved = false;
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

    public boolean getHasMoved() {
        return this.hasMoved;
    }

    public void setHasMoved(boolean set) {
        this.hasMoved = set;
    }

    // 

    // validate that the player meets the requirements to act and then perform action false = end turn, true = scene wrap
    public Boolean act(View view) {
        Dice dice = new Dice();
        int diceRoll = dice.roll();
        Role role = getRole();
        Scene scene = role.getScene();
        
        // role dice to act
        if((diceRoll + role.getPracticeChips()) < scene.getBudget()) {
            view.displayMessage("you rolled a " + diceRoll + " and have " + role.getPracticeChips() + " practice chips, the budget was " + scene.getBudget() + " better luck next time");
            if(role.getOnCard() == false) {
                addDollars(1);
            }
            return false;
        }

        view.displayMessage("Congratulations you rolled a " + diceRoll + " and have + " + role.getPracticeChips() + " practice chips, the budget was " + scene.getBudget());

        // if success then remove shot counter
        int shots = scene.getShotCounter();
        scene.setShotCounter(shots-1);
        view.displayMessage("This scene has " + scene.getShotCounter() + " shots left");
        
        // pay the player
        if(role.getOnCard() == true) {
            addCredits(2);
        } else {
            addCredits(1);
            addDollars(1);
        }

        // if scene is over then wrap it
        if(shots-1 == 0) {
            return true;
        }

        return false;
    }
}