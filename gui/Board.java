package gui;

public class Board {
    private Room[] rooms;
    private static Board instance;

    public static Board getInstance() {
        if (instance == null) {
            instance = new Board();
        }
        return instance;
    }

    // GETS/SETS
    public Room[] getRooms() {
        return this.rooms;
    }

    public void setRooms(Room[] rooms) {
        this.rooms = rooms;
    }
}
