public class Moderator {
    private Board board;
    private View view;
    private boolean gameRunning;

    public Moderator(Board board, View view) {
        this.board = board;
        this.view = view;
        this.gameRunning = true;
    }

    public void startGame() {
        view.displayMessage("How many players will be playing today?");

        while(gameRunning) {

            // get the player and tell them its their turn
            view.displayMessage("its player" + "'s turn");

            String input = view.getUserInput();

            handleInput(input);
        }

        view.displayMessage("Game Over");
    }

    private void handleInput(String input) {
        
    }
}
