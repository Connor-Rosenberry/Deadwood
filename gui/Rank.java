package gui;

public class Rank {
    //  VARIABLES
    private int rankLevel;
    private int dollarCost;
    private int creditCost;

    // CONSTRUCTORS
    public Rank() {
        this.rankLevel = 0;
        this.dollarCost = 0;
        this.creditCost = 0;
    }

    public Rank(int rankLevel, int dollarCost, int creditCost) {
        this.rankLevel = rankLevel;
        this.dollarCost = dollarCost;
        this.creditCost = creditCost;
    } 

    // GETS/SETS
    public int getRankLevel() {
        return this.rankLevel;
    }

    public void setRankLevel(int rankLevel) {
        this.rankLevel = rankLevel;
    }

    public int getDollarCost() {
        return this.dollarCost;
    }

    public void setDollarCost(int dollarCost) {
        this.dollarCost = dollarCost;
    }

    public int getCreditCost() {
        return this.creditCost;
    }

    public void setCreditCost(int creditCost) {
        this.creditCost = creditCost;
    }

    // METHODS
}
