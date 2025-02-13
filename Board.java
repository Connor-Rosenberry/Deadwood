public class Board {
    //  VARIABLES
    private Room[] rooms;

    // CONSTRUCTORS
    public Board() {}

    public Board(Room[] rooms) {
        this.rooms = rooms;
    }

    // GETS/SETS
    public Room[] getRooms() {
        return this.rooms;
    }

    public void setRooms(Room[] rooms) {
        this.rooms = rooms;
    }
    
    public void clearBoard(){}
}
