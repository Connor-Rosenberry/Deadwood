package gui;

public class CastingOffice extends Room {
    //  VARIABLES
    private Rank[] ranks;

    // CONSTRUCTORS
    public CastingOffice() {
        super();
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
