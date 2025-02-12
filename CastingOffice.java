public class CastingOffice extends Room {
    //  VARIABLES
    private Rank[] ranks;

    // CONSTRUCTORS
    public CastingOffice() {
        super();
        this.ranks = null;
    }

    public CastingOffice(String name, String[] adjacentRooms, Player[] playersHere, int x, int y, int w, int h, Rank[] ranks) {
        super(name, adjacentRooms, playersHere, x, y, w, h);
        this.ranks = ranks;
    }

    // GETS/SETS
    public Rank[] getRanks() {
        return this.ranks;
    }

    public void setRanks(Rank[] ranks) {
        this.ranks = ranks;
    }

    // METHODS
    @Override
    public boolean isCastingOffice() {
        return true;
    }
}
