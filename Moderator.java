import java.util.Arrays;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;

public class Moderator {
    private Board board;
    private View view;
    private boolean gameRunning;

    public Moderator(Board board, View view) {
        this.board = board;
        this.view = view;
        this.gameRunning = true;
    }

    // start the game, includes the loop until game over
    public void startGame() {
        // create scene cards
        Scene[] sceneList;
        SceneCreator createCard = new SceneCreator();
        sceneList = createCard.parseSceneCards();

        // set up the rooms with scenes
        sceneList = startDay(sceneList);

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

        int currentPlayer = 0;

        // loop for while the game is running
        // Also note that a "player's" number is one number higher than their index in playerList
        // ex. player 1 = playerList[0];
        while (gameRunning) {
            view.displayMessage("It's " + playerList[currentPlayer].getName() + "'s turn.");
            boolean turnActive = true;

            // Player's turn loop
            while (turnActive && gameRunning) {
                view.displayMessage("\nEnter a command (type 'help' for options):");
                String input = view.getUserInput();
                turnActive = handleInput(input, playerList, currentPlayer);
            }

            // Move to the next player after their turn ends
            currentPlayer = (currentPlayer + 1) % playerList.length;
            playerList[currentPlayer].setHasMoved(false);

            // if(dayOver) {
                // start new day
            // }
        }
    }

    // start a new day, clear the board and set new scenes
    public Scene[] startDay(Scene[] sceneList) {
        // clear board, and assign 10 new rooms
        Random rand = new Random();
        List<Scene> sceneArrayList = new ArrayList<>(List.of(sceneList)); // Convert array to List

        Scene[] selectedScenes = new Scene[10]; // Array for selected scenes

        for (int i = 0; i < 10; i++) {
            int randomIndex = rand.nextInt(sceneArrayList.size()); // Pick a random index
            selectedScenes[i] = sceneArrayList.remove(randomIndex); // Remove and store the scene
        }

        // Convert the remaining list back to an array
        Scene[] remainingScenes = sceneArrayList.toArray(new Scene[0]);

        // for the 10 rooms that need scenes assign a scene
        for(int i = 0; i < 10; i++) {
            ((Set) board.getRooms()[i]).setScene(selectedScenes[i]);
        }

        return remainingScenes;
    }

    // handle user input
    private boolean handleInput(String input, Player[] playerList, int currentPlayer) {
        List<String> command = new ArrayList<>(Arrays.asList(input.split(" "))); // Split input into words
        int commandLength = command.size();
    
        if (command.get(0).equals("move")) {
            if(playerList[currentPlayer].getHasMoved() == true) {
                view.displayMessage("Player has already moved, please pick a different command");
                return true;
            }
            if (commandLength == 1) {
                view.displayMessage("Move where? Please specify a destination.");
                return true;
            }
    
            String destination = String.join(" ", command.subList(1, commandLength));
            // view.displayMessage("Attempting to move to: " + destination);
    
            // Implement movement logic here (validate if destination exists and is adjacent)
            move(playerList[currentPlayer], destination);

            return true;

        } 
        else if (String.join(" ", command).equals("all players locations")) {
            for (Player player : playerList) {
                view.displayMessage(player.getName() + " is in " + 
                    (player.getLocation() != null ? player.getLocation().getName() : "an unknown location"));
            }
            return true;

        } 
        else if (String.join(" ", command).equals("active player")) {
            view.displayMessage(playerList[currentPlayer].getName() + " is in " + 
                (playerList[currentPlayer].getLocation().getName()) + "\nThey have " + playerList[currentPlayer].getDollars() + " dollars and " + playerList[currentPlayer].getCredits() + " credits and they are rank " + playerList[currentPlayer].getRank());
                if(playerList[currentPlayer].getRole() != null) {
                    view.displayMessage("\n they are also working the " + playerList[currentPlayer].getRole().getName() + " role");
                }
            return true;

        } 
        else if (String.join(" ", command).equals("active player")) {
            view.displayMessage("Active player: " + playerList[currentPlayer].getName());
            return true;

        } 
        else if (String.join(" ", command).equals("adjacent rooms")) {
            view.displayMessage(Arrays.toString(playerList[currentPlayer].getLocation().getNeighbors()));
            return true;

        } 
        else if (command.get(0).equals("work")) {
            view.displayMessage("Work command selected.");
            if (commandLength == 1) {
                view.displayMessage("Please specify a role to work");
                return true;
            }
            // player must be at a location with roles
            if(playerList[currentPlayer].getLocation().getName().equals("office") || playerList[currentPlayer].getLocation().getName().equals("trailer")) {
                view.displayMessage("Must be at a location with roles to work");
                return true;
            }

            String destination = String.join(" ", command.subList(1, commandLength));
            
            boolean check = work(playerList[currentPlayer], destination);
            
            // if the player was sucessfully able to move, their turn is over
            if(check == true) {
                view.displayMessage("player is now working at ");
                return false;
            }
            return true;

        }
        else if (command.get(0).equals("act")) {
            view.displayMessage("act command selected.");
            if(playerList[currentPlayer].getRole() == null) {
                view.displayMessage("Must \"work\" a role before acting");
                return true;
            }
            // work();
            
            return false;

        } 
        else if (command.get(0).equals("rehearse")) {
            view.displayMessage("rehearse command selected.");
            if(playerList[currentPlayer].getRole() == null) {
                view.displayMessage("Must \"work\" a role before rehearsing");
                return true;
            }
            
            // work();
            
            return false;

        }  
        else if (command.get(0).equals("upgrade")) {
            view.displayMessage("Upgrade command selected.");
            CastingOffice office = (CastingOffice) board.getRooms()[10];
            if(playerList[currentPlayer].getLocation() != office) {
                view.displayMessage("Cannot upgrade unless at casting office");
            }
            view.displayMessage("Enter the rank you would like and if you want to pay will dollars or credits: Ex. \"3 dollar\"");
            String request = view.getUserInput();
            return tryUpgrade(playerList[currentPlayer], office, request);

        } 
        else if (commandLength == 1 && command.get(0).equals("end")) {
            view.displayMessage(playerList[currentPlayer].getName() + "'s turn has ended.");
            return false;
        } 
        else if (String.join(" ", command).equals("end game")) {
            gameRunning = false;
            view.displayMessage("Game Over");
            return false;
        } 
        else if (command.get(0).equals("help")) {
            view.displayMessage("Available commands: all players locations, active player location, active player, move <destination>, work, upgrade, end, end game");
            return true;
        } 
        else {
            view.displayMessage("Unknown command. Type 'help' for options.");
            return true;
        }
    }

    private boolean move(Player currentPlayer, String destination) {

        // move this to player class, in moderator loop through and find the room based on the name, if cant find then room doesnt exist. if can find then send that room in the destination then in player we can see if it is an adjacent room 


        boolean legalMove = false;
        for(int i = 0; i < currentPlayer.getLocation().getNeighbors().length; i ++) {
            if(currentPlayer.getLocation().getNeighbors()[i].equals(destination)) {
                legalMove = true;
            }
        }
        if(!legalMove) {
            view.displayMessage("The destination is not an adjacent move");
            return false;
        }
        for(int i = 0; i < board.getRooms().length; i++) {
            if(board.getRooms()[i].getName().equals(destination)) {
                view.displayMessage("Player has moved from: " + currentPlayer.getLocation().getName() + " to " + board.getRooms()[i].getName());
                currentPlayer.setLocation(board.getRooms()[i]);
                currentPlayer.setHasMoved(true);
            }
        }
        return true;
    }

    private boolean work(Player currentPlayer, String destination) {
        // for the players location check each board role and see if one matches the destination
        Set location = (Set) currentPlayer.getLocation(); 
        Role[] boardRoles = location.getRoles();
        Scene locationScene = location.getScene();
        Role[] sceneRoles = locationScene.getRoles();

        Role role = null;
        // combine these
        for(int i = 0; i < boardRoles.length; i++) {
            // if the destination is a valid roll
            if(boardRoles[i].getName().equals(destination)) {
                role = boardRoles[i];
            }
        }
        // then check each card role
        for(int i = 0; i < sceneRoles.length; i++) {
            // if the destination is a valid roll
            if(sceneRoles[i].getName().equals(destination)) {
                role = sceneRoles[i];
            }
        }

        if(role == null) {
            view.displayMessage("The role you entered does not exist at this location \n roles at this location are " + Arrays.toString(location.getRoleNames()) + Arrays.toString(locationScene.getRoleNames()));
            return false;
        }

        // check if role is taken or if player rank is too low
        if(role.getActor() != null) {
            view.displayMessage("This role is already taken");
            return false;
        }
        if(role.getRankToAct() > currentPlayer.getRank()) {
            view.displayMessage("This role's rank is too high");
            return false;
        }

        // then if so, set the players role 
        currentPlayer.setRole(role);
        currentPlayer.getRole().setActor(currentPlayer);

        return true;
    }

    private boolean tryUpgrade(Player player, CastingOffice office, String request) {
        String[] parts = request.split("\\s+"); // Split by spaces
        if (parts.length < 2) {
            view.displayMessage("Invalid input format");
            return true;
        }
        try {
            int rank = Integer.parseInt(parts[0]);
            String type = parts[1];    

            if(type.equals("dollar") == false && type.equals("credit") == false) {
                view.displayMessage("Invalid Type: \"dollar\" or \"credit\"");
                return true;
            }

            view.displayMessage("Attempting to upgrade to rank " + rank + " from a current rank of " + player.getRank());
            
            // get the rank that the user requested
            Rank[] ranks = office.getRanks();
            Rank requestedRank = ranks[rank-2];

            if(requestedRank.getRankLevel() <= player.getRank()) {
                view.displayMessage("you are already this rank or higher");
                return true;
            }
            if(type.equals("dollar")) {
                if(player.getDollars() < requestedRank.getDollarCost()) {
                    view.displayMessage("You do not have enough dollars to upgrade, you need " + requestedRank.getDollarCost() + " for rank " + requestedRank.getRankLevel());
                    return true;
                }
                player.setRank(requestedRank.getRankLevel());
                int newDollarAmount = player.getDollars() - requestedRank.getDollarCost();
                player.setDollars(newDollarAmount);
            }
            if(type.equals("credit")) {
                if(player.getCredits() < requestedRank.getCreditCost()) {
                    view.displayMessage("You do not have enough credits to upgrade, you need " + requestedRank.getCreditCost() + " for rank " + requestedRank.getRankLevel());
                    return true;
                }
                player.setRank(requestedRank.getRankLevel());
                int newCreditAmount = player.getCredits() - requestedRank.getCreditCost();
                player.setDollars(newCreditAmount);
            }
            view.displayMessage("Player is now rank " + player.getRank());
            return true;
        } catch (NumberFormatException e) {
            view.displayMessage("Invalid number format: " + parts[0]);
            return true;
        }
    }
}
