package tui;

public class Room {
    //  VARIABLES
    protected String name;
    protected String[] neighbors;  // array of adjacent rooms names
    protected Player[] playersHere;
    protected int x;
    protected int y;
    protected int w;
    protected int h;

    // CONSTRUCTORS
    public Room() {
        this.name = null;
        this.neighbors = null;
        this.playersHere = null;
        this.x = 0;
        this.y = 0;
        this.w = 0;
        this.h = 0;
    }

    public Room(String name, String[] neighbors, Player[] playersHere, int x, int y, int w, int h) {
        this.name = name;
        this.neighbors = neighbors;
        this.playersHere = playersHere;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    // GETS/SETS
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getNeighbors() {
        return this.neighbors;
    }

    public void setNeighbors(String[] neighbors) {
        this.neighbors = neighbors;
    }

    public Player[] getPlayersHere() {
        return this.playersHere;
    }

    public void setPlayersHere(Player[] playersHere) {
        this.playersHere = playersHere;
    }

    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getW() {
        return this.w;
    }

    public void setW(int w) {
        this.w = w;
    }

    public int getH() {
        return this.h;
    }

    public void setH(int h) {
        this.h = h;
    }

    // METHODS
    public boolean isSet() {
        return false;
    }

    public boolean isCastingOffice() {
        return false;
    }
}
