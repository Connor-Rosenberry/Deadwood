package gui;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.List;

public class Moderator {
    private Board board;
    private BoardLayersListener boardView;
    private View view;
    private boolean gameRunning;
    private Scene[] sceneList;
    private Player[] playerList;
    private int dayCount;

    public Moderator(BoardLayersListener boardView, Board board, View view) {
        this.boardView = boardView;
        this.board = board;
        this.view = view;
        this.gameRunning = true;
    }

    // start the game, includes the loop until game over
    public void startGame() {

        // get the number of players and adds them to playerList
        view.displayMessage("How many players will be playing today? [2-8]");
        int numPlayers = view.getUserInt();

        if(numPlayers < 2 || numPlayers > 8) {
            view.displayMessage("Must be between 2 and 8 players");
            return;
        }
        playerList = new Player[numPlayers];

        // create "numPlayers" number of players
        for(int i = 0; i < numPlayers; i++) {
            view.displayMessage("what is player " + (i + 1) + "'s name?");
            String input = view.getUserInput();
            Player player = new Player(input);
            playerList[i] = player;
            if(numPlayers == 5) {
                player.setCredits(2);
            }
            if(numPlayers == 6) {
                player.setCredits(4);
            }
            if(numPlayers >= 7) {
                player.setRank(2);
            }
        }

        // create scene cards
        SceneCreator createCard = new SceneCreator();
        sceneList = createCard.parseSceneCards();

        // set up the rooms with scenes
        sceneList = startDay(sceneList, playerList);
        dayCount = 1;
        
                
        // choose a random player to go first
        Random random = new Random();
        int randomIndex = random.nextInt(playerList.length);
        int currentPlayer = randomIndex;

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
        }
        endGame();
    }

    // start a new day, reset the board and set new scenes
    public Scene[] startDay(Scene[] sceneList, Player[] playerList) {
        if(dayCount == 4) {
            gameRunning = false;
            return null;
        }
        if(playerList.length <= 3 && dayCount == 3) {
            gameRunning = false;
            return null;
        }
        // clear board, and assign 10 new rooms
        Random rand = new Random();
        List<Scene> sceneArrayList = new ArrayList<>(List.of(sceneList)); // Convert array to List
        Scene[] selectedScenes = new Scene[10]; // Array for selected scenes

        // for each room with scenes pick a random new scene from the remaining
        for (int i = 0; i < 10; i++) {
            int randomIndex = rand.nextInt(sceneArrayList.size());
            selectedScenes[i] = sceneArrayList.remove(randomIndex);
        }

        // Convert the remaining list back to an array
        Scene[] remainingScenes = sceneArrayList.toArray(new Scene[0]);

        // for the 10 rooms that need scenes assign a scene
        for(int i = 0; i < 10; i++) {
            Set set = (Set) board.getRooms()[i];
            
            // set up each room back to default
            for(int j = 0; j < set.getRoles().length; j++) {
                set.getRoles()[j].setActor(null);
                set.getRoles()[j].setPracticeChips(0);
            }
            set.setScene(selectedScenes[i]);
            set.getScene().setShotCounter(set.getTakes().length);
            set.setStatus("not active");

            // set the scene for each board role
            for(int j = 0; j < set.getRoles().length; j++) {
                set.getRoles()[j].setScene(selectedScenes[i]);
            }

        }

        // set the players back to the trailers
        for(int i = 0; i < playerList.length; i++) {
            Player player = playerList[i];
            player.setLocation(board.getRooms()[11]);
            player.setRole(null);
        }
        
        dayCount++;
        return remainingScenes;
    }

    // handle user input
    private boolean handleInput(String input, Player[] playerList, int currentPlayer) {
        List<String> command = new ArrayList<>(Arrays.asList(input.split(" ")));
        int commandLength = command.size();
    
        if (command.get(0).equals("move")) {
            if(playerList[currentPlayer].getHasMoved() == true) {
                view.displayMessage("Player has already moved, please pick a different command");
                return true;
            }
            if(playerList[currentPlayer].getRole() != null) {
                view.displayMessage("cannot move while working");
                return true;
            }
            if (commandLength == 1) {
                view.displayMessage("Move where? Please specify a destination.");
                return true;
            }
    
            // get the requested destination and attempt to move there
            String destination = String.join(" ", command.subList(1, commandLength));
            move(playerList[currentPlayer], destination);

            return true;

        } 
        else if (String.join(" ", command).equals("all players locations")) {
            // display the location of all players
            for (Player player : playerList) {
                view.displayMessage(player.getName() + " is in " + 
                    (player.getLocation() != null ? player.getLocation().getName() : "an unknown location"));
            }
            return true;

        } 
        else if (String.join(" ", command).equals("active player")) {
            // display information about the active player
            view.displayMessage(playerList[currentPlayer].getName() + " is in " + 
                (playerList[currentPlayer].getLocation().getName()) + "\nThey have " + playerList[currentPlayer].getDollars() + " dollars and " + playerList[currentPlayer].getCredits() + " credits and they are rank " + playerList[currentPlayer].getRank());
                if(playerList[currentPlayer].getRole() != null) {
                    view.displayMessage("\n they are also working the " + playerList[currentPlayer].getRole().getName() + " role");
                }
            return true;

        } 
        else if (String.join(" ", command).equals("adjacent rooms")) {
            // display the adjacent rooms from the current players location
            view.displayMessage(Arrays.toString(playerList[currentPlayer].getLocation().getNeighbors()));
            return true;
        } 
        else if (command.get(0).equals("work")) {
            view.displayMessage("Work command selected.");
            if(playerList[currentPlayer].getRole() != null) {
                view.displayMessage("You are already working, you can act or rehearse");
                return true;
            }
            
            // player must be at a location with roles
            if(playerList[currentPlayer].getLocation().getName().equals("office") || playerList[currentPlayer].getLocation().getName().equals("trailer")) {
                view.displayMessage("Must be at a location with roles to work");
                return true;
            }

            // get the active players location
            Set location = (Set) playerList[currentPlayer].getLocation(); 
            Scene locationScene = location.getScene();
            
            // display the available roles
            if (commandLength == 1) {
                view.displayMessage("Please specify a role to work, the options are \n" + Arrays.toString(location.getRoleNames()) + Arrays.toString(locationScene.getRoleNames()));
                return true;
            }

            // get the requested destination and attempt to let the player work there
            String destination = String.join(" ", command.subList(1, commandLength));
            return work(playerList[currentPlayer], destination);

        }
        else if (command.get(0).equals("act")) {
            // attempt to let the player act
            view.displayMessage("act command selected.");
            if(playerList[currentPlayer].getRole() == null) {
                view.displayMessage("Must \"work\" a role before acting");
                return true;
            }
            boolean response = playerList[currentPlayer].act(view);
            if(response == true) {
                sceneWrap(playerList[currentPlayer].getRole().getScene(), playerList[currentPlayer]);
            }

            return false;

        } 
        else if (command.get(0).equals("rehearse")) {
            // attempt to let the player rehearse
            view.displayMessage("rehearse command selected.");
            if(playerList[currentPlayer].getRole() == null) {
                view.displayMessage("Must \"work\" a role before rehearsing");
                return true;
            }
            
            return rehearse(playerList[currentPlayer]);

        }  
        else if (command.get(0).equals("upgrade")) {
            // attempt to let the player upgrade
            view.displayMessage("Upgrade command selected.");
            CastingOffice office = (CastingOffice) board.getRooms()[10];
            if(playerList[currentPlayer].getLocation() != office) {
                view.displayMessage("Cannot upgrade unless at casting office");
                return true;
            }
            
            // get upgrade info from the player and attempt to upgrade rank
            view.displayMessage("Enter the rank you would like and if you want to pay will dollars or credits: Ex. \"3 dollar\"");
            String request = view.getUserInput();
            return tryUpgrade(playerList[currentPlayer], office, request);

        } 
        else if (String.join(" ", command).equals("end turn")) {
            // ends the current players turn
            view.displayMessage(playerList[currentPlayer].getName() + "'s turn has ended.");
            return false;
        }
        else if (String.join(" ", command).equals("end day")) {
            // ends the current day and starts a new one
            view.displayMessage("Day is over");
            sceneList = startDay(sceneList, playerList);
            return false;
        } 
        else if (String.join(" ", command).equals("end game")) {
            // ends the game and moves to scoring
            gameRunning = false;
            view.displayMessage("Game Over");
            return false;
        } 
        else if (command.get(0).equals("help")) {
            // provides a list of commands
            view.displayMessage("Available commands: all players locations, active player, adjacent rooms, move <destination>, work, act, rehearse, upgrade, end turn, end day, end game");
            return true;
        } 
        else {
            view.displayMessage("Unknown command. Type 'help' for options.");
            return true;
        }
    }

    // check the active players input and validate a move to a new location
    private boolean move(Player currentPlayer, String destination) {
        
        // check if the requested move is legal
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
        
        // if it is, loop through the rooms and set players location to the new room
        for(int i = 0; i < board.getRooms().length; i++) {
            if(board.getRooms()[i].getName().equals(destination)) {
                view.displayMessage("Player has moved from: " + currentPlayer.getLocation().getName() + " to " + board.getRooms()[i].getName());

                // if the location has a scene card
                if(board.getRooms()[i].getName().equals("office") == false && board.getRooms()[i].getName().equals("trailer") == false) {
                    Set set = (Set) board.getRooms()[i];
                    if(set.getStatus().equals("not active")) {
                        view.displayMessage("Scene card flipped");
                        set.setStatus("active");
                    }
                }

                currentPlayer.setLocation(board.getRooms()[i]);
                currentPlayer.setHasMoved(true);

            }
        }
        return true;
    }

    // validates that the player meets the requirements to work and assigns them a role
    private boolean work(Player currentPlayer, String destination) {
        
        // for the players location check each board role and see if one matches the destination
        Set location = (Set) currentPlayer.getLocation(); 
        Role[] boardRoles = location.getRoles();
        Scene locationScene = location.getScene();
        Role[] sceneRoles = locationScene.getRoles();

        Role role = null;

        // check each board role
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

        // if not a valid role
        if(role == null) {
            view.displayMessage("The role you entered does not exist at this location \n roles at this location are " + Arrays.toString(location.getRoleNames()) + Arrays.toString(locationScene.getRoleNames()));
            return true;
        }

        // check if the scene is wrapped, role is taken, or if player rank is too low
        if(role.getScene().getStatus().equals("wrapped")) {
            view.displayMessage("the scene for this room has been wrapped");
            return true;
        }
        if(role.getActor() != null) {
            view.displayMessage("This role is already taken");
            return true;
        }
        if(role.getRankToAct() > currentPlayer.getRank()) {
            view.displayMessage("This role's rank is too high");
            return true;
        }

        // then if not, set the players role 
        currentPlayer.setRole(role);
        currentPlayer.getRole().setActor(currentPlayer);

        view.displayMessage("Player is now working " + role.getName());

        // if the player has already moved then their turn is over
        if(currentPlayer.getHasMoved() == true) {
            view.displayMessage("Because you have already moved your turn is over");
            return false;
        }
        return true;
    }

    // validate that the player meets the requirements to upgrade and then increase rank
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
            
            // if they want to upgrade with dollars
            if(type.equals("dollar")) {
                if(player.getDollars() < requestedRank.getDollarCost()) {
                    view.displayMessage("You do not have enough dollars to upgrade, you need " + requestedRank.getDollarCost() + " for rank " + requestedRank.getRankLevel());
                    return true;
                }
                player.setRank(requestedRank.getRankLevel());
                int newDollarAmount = player.getDollars() - requestedRank.getDollarCost();
                player.setDollars(newDollarAmount);
            }
            
            // if they want to upgrade with credits
            if(type.equals("credit")) {
                if(player.getCredits() < requestedRank.getCreditCost()) {
                    view.displayMessage("You do not have enough credits to upgrade, you need " + requestedRank.getCreditCost() + " for rank " + requestedRank.getRankLevel());
                    return true;
                }
                player.setRank(requestedRank.getRankLevel());
                int newCreditAmount = player.getCredits() - requestedRank.getCreditCost();
                player.setCredits(newCreditAmount);
            }
            view.displayMessage("Player is now rank " + player.getRank());
            return true;
        } catch (NumberFormatException e) {
            view.displayMessage("Invalid number format: " + parts[0]);
            return true;
        }
    }

    // validate that the player meets the requirements to rehearse and then perform action
    private boolean rehearse(Player player) {
        Role role = player.getRole();
        int chips = role.getPracticeChips();
        if(chips < role.getScene().getBudget()) {
            role.setPracticeChips(chips + 1);
            return false;
        }
        view.displayMessage("You already have the max amount of practice chips, please act");
        return true;
    }

    // wrap a scene and pay bonus if player on card
    private void sceneWrap(Scene scene, Player activePlayer) {
        scene.setStatus("wrapped");
        boolean playerOnCard = false;
        ArrayList<Player> onCardPlayers = new ArrayList<>();
        
        // check to see if a player is on the card
        for(int i = 0; i < scene.getRoles().length; i++) {
            if(scene.getRoles()[i].getActor() != null) {
                playerOnCard = true;
                onCardPlayers.add(scene.getRoles()[i].getActor());
            }
        }

        if(!playerOnCard) {
            view.displayMessage("there is no one on a on card role, therefore no bonus payment");
        }

        // on card bonuses
        if(playerOnCard) {
            int[] rolls = new int[scene.getBudget()];
            view.displayMessage("active player gets to roll " + scene.getBudget() + " dice");

            Dice dice = new Dice();
            
            // get and sort the rolls depending on the budget
            for(int i = 0; i < scene.getBudget(); i++) {
                int value = dice.roll();
                view.displayMessage("you rolled a " + value);
                rolls[i] = value;
            }
            Arrays.sort(rolls); 
            reverseArray(rolls);
            
            // calculate the payout for each role
            int[] payout = new int[scene.getRoles().length];
            for(int i = 0; i < scene.getBudget(); i++) {
                payout[i % payout.length] += rolls[i];
            }

            // pay the players
            for(int i = 0; i < payout.length; i++) {
                if(scene.getRoles()[i].getActor() != null) {
                    scene.getRoles()[i].getActor().addDollars(payout[(payout.length - i - 1)]);
                }
            }
        }

        // off card bonuses
        if(playerOnCard) {
            Set offCard = (Set) activePlayer.getLocation();
            for(int i = 0; i < offCard.getRoles().length; i++) {
                if(offCard.getRoles()[i].getActor() != null) {
                    offCard.getRoles()[i].getActor().addDollars(offCard.getRoles()[i].getRankToAct());
                }
            }
        }

        // loop through the scene and remove players role
        for(int i = 0; i < scene.getRoles().length; i++) {
            if(scene.getRoles()[i].getActor() != null) {
                scene.getRoles()[i].getActor().setRole(null);
            }
        }

        // loop through the board and remove players role
        Set offCard = (Set) activePlayer.getLocation();
        for(int i = 0; i < offCard.getRoles().length; i++) {
            if(offCard.getRoles()[i].getActor() != null) {
                offCard.getRoles()[i].getActor().setRole(null);
                offCard.getRoles()[i].setActor(null);
            }
        }

        // loop through all of the rooms and see if their scenes are wrapped
        int counter = 0;
        for(int i = 0; i < 10; i++) {
            Set set = (Set) board.getRooms()[i];
            Scene allScenes = set.getScene();
            // if a scene is not wrapped add to counter
            if(allScenes.getStatus().equals("wrapped") != true) {
                counter++;
            }
        }
        if(counter == 1) {
            // if all scenes wrapped besides one, then start a new day
            sceneList = startDay(sceneList, playerList);
        }
    }

    // Method to reverse an array in-place
    private static void reverseArray(int[] arr) {
        int left = 0, right = arr.length - 1;
        while (left < right) {
            // Swap left and right elements
            int temp = arr[left];
            arr[left] = arr[right];
            arr[right] = temp;
            left++;
            right--;
        }
    }

    private void endGame() {
    List<Player> winners = new ArrayList<>();
    int winningScore = 0;

    // Determine the highest score
    for (Player player : playerList) {
        int dollars = player.getDollars();
        int credits = player.getCredits();
        int rank = player.getRank();
        int score = dollars + credits + (rank * 5);

        if (score > winningScore) {
            winningScore = score;
            winners.clear(); // Clear previous winners
            winners.add(player);
        } else if (score == winningScore) {
            winners.add(player); // Add to winners list in case of a tie
        }
    }

    // Display the winner(s)
    if (winners.size() == 1) {
        view.displayMessage("The winner is " + winners.get(0).getName() + " with a score of " + winningScore);
    } else {
        String winnerNames = winners.stream()
                                    .map(Player::getName)
                                    .collect(Collectors.joining(", "));
        view.displayMessage("It's a tie! The winners are " + winnerNames + " with a score of " + winningScore);
    }
    }

    // GETS/SETS
    public Scene[] getSceneList() {
        return this.sceneList;
    }

    public void setSceneList(Scene[] sceneList) {
        this.sceneList = sceneList;
    }

    public Board getBoard() {
        return this.board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

}
