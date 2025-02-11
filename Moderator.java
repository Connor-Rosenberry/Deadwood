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
        // get the number of players
        view.displayMessage("How many players will be playing today? [2-8]");
        int numPlayers = view.getUserInt();


        Player[] playerList = new Player[numPlayers];
        
        // create "numPlayers" number of players
        for(int i = 0; i < numPlayers; i++) {
            view.displayMessage("what is player " + (i + 1) + "'s name?");
            String input = view.getUserInput();
            Player player = new Player(input);
            playerList[i] = player;
        }


        int playersTurn = 0;

        // loop for while the game is running
        // Also note that a "player's" number is one number higher than their index in playerList
        // ex. player 1 = playerList[0];
        while(gameRunning) {

            // get the player and tell them its their turn
            view.displayMessage("its player " + (playersTurn + 1) + "'s turn");

            String input = view.getUserInput();

            handleInput(input);
            
            playersTurn++;
            gameRunning = false;
        }

        view.displayMessage("Game Over");
    }

    private void handleInput(String input) {
        switch (input.toLowerCase()) {
            case "End Game":
                gameRunning = false;
                break;
            case "help":
                view.displayMessage("Avaliable commands: End Game, help");
                break;
            default:
                view.displayMessage("Unknown command. Type 'help' for options.");
        }
    }
}
