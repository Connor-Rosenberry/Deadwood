public class Room {
    //  VARIABLES
    protected String name;
    protected Room[] adjacentRooms;
    protected Player[] playersHere;

    // CONSTRUCTORS
    public Room(String name, Room[] adjacentRooms, Player[] playersHere) {
        this.name = name;
        this.adjacentRooms = adjacentRooms;
        this.playersHere = playersHere;
    }

    // GETS/SETS
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Room[] getRooms() {
        return this.adjacentRooms;
    }

    public void setRooms(Room[] adjacentRooms) {
        this.adjacentRooms = adjacentRooms;
    }

    public Player[] getPlayersHere() {
        return this.playersHere;
    }

    public void setPlayersHere(Player[] playersHere) {
        this.playersHere = playersHere;
    }

    // METHODS
    // TODO
    public void movePlayerHere(Player player) {

    }

    // TODO
    public void movePlayerOut(Player player) {

    }

    public boolean isSet() {
        return false;
    }

    public boolean isCastingOffice() {
        return false;
    }
}
