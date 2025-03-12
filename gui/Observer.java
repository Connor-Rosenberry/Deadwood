package gui;

// specifically for player
public interface Observer {
    public void update(String name, Room location, int rank, int dollars, int credits, Role role);
}
