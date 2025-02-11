public class CastingOffice extends Room {
    //  VARIABLES
    private Rank[] ranks;

    // CONSTRUCTORS
    public CastingOffice(String name, Room[] adjacentRooms, Player[] playersHere, Rank[] ranks) {
        super(name, adjacentRooms, playersHere);
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
