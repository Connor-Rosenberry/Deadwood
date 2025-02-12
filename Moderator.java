import java.util.Arrays;

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
            player.setLocation(board.getRooms()[11]);
            playerList[i] = player;
        }

        System.out.println(Arrays.toString(board.getRooms()));

        int currentPlayer = 0;

        // loop for while the game is running
        // Also note that a "player's" number is one number higher than their index in playerList
        // ex. player 1 = playerList[0];
        while (gameRunning) {
            view.displayMessage("It's " + playerList[currentPlayer].getName() + "'s turn.");

            boolean turnActive = true;

            // Player's turn loop
            while (turnActive && gameRunning) {
                view.displayMessage("Enter a command (type 'help' for options):");
                String input = view.getUserInput();
                turnActive = handleInput(input, playerList, currentPlayer);
            }

            // Move to the next player after their turn ends
            currentPlayer = (currentPlayer + 1) % playerList.length;
        }

        view.displayMessage("Game Over");
    }

    private boolean handleInput(String input, Player[] playerList, int currentPlayer) {
        switch (input.toLowerCase()) {
            case "all players locations":
                for (Player player : playerList) {
                    view.displayMessage(player.getName() + " is in " + (player.getLocation() != null ? player.getLocation().getName() : "an unknown location"));
                }
                return true; // Continue turn

            case "active player location":
                Player activePlayer = playerList[currentPlayer];
                view.displayMessage(activePlayer.getName() + " is in " + (activePlayer.getLocation() != null ? activePlayer.getLocation().getName() : "an unknown location"));
                return true; // Continue turn

            case "active player":
                view.displayMessage("Active player: " + playerList[currentPlayer].getName());
                return true; // Continue turn

            case "move":
                // Implement movement logic here
                view.displayMessage("Move command selected.");
                return true; // Continue turn

            case "work":
                // Implement work logic here
                view.displayMessage("Work command selected.");
                return true; // Continue turn

            case "upgrade":
                // Implement upgrade logic here
                view.displayMessage("Upgrade command selected.");
                return true; // Continue turn

            case "end":
                view.displayMessage(playerList[currentPlayer].getName() + "'s turn has ended.");
                return false; // End turn

            case "end game":
                gameRunning = false;
                view.displayMessage("Game Over");
                return false; // End game immediately

            case "help":
                view.displayMessage("Available commands: all players locations, active player location, active player, move, work, upgrade, end, end game");
                return true; // Continue turn

            default:
                view.displayMessage("Unknown command. Type 'help' for options.");
                return true; // Continue turn
        }
    }
}
