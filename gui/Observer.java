package gui;

// specifically for player
public interface Observer {
    public void update(int playerNum, String location, int rank, int dollars, int credits, String role);
}
