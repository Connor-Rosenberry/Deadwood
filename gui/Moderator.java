package gui;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.ArrayList;
import java.util.List;

public class Moderator implements GameActionListener {
    private Board board;
    private BoardLayersListener boardView;
    private boolean gameRunning;
    private Scene[] sceneList;
    private Player[] playerList;
    private int dayCount;

    private String currentCommand = null;

    public Moderator(BoardLayersListener boardView, Board board) {
        this.boardView = boardView;
        this.board = board;
        this.gameRunning = true;
    }

    @Override
    public void getInput(String action) {
        currentCommand = action;
    }

    public void setBoardView(BoardLayersListener boardView) {
        this.boardView = boardView;
    }

    // start the game, includes the loop until game over
    public void startGame() {
        // start the board
        boardView.makeGUI();

        // get the number of players and adds them to playerList
        // view.displayMessage("How many players will be playing today? [2-8]");
        int numPlayers = boardView.getNumPlayers();

        // not possible to be out of bounds

        // if(numPlayers < 2 || numPlayers > 8) {
        //     view.displayMessage("Must be between 2 and 8 players");
        //     return;
        // }
        playerList = new Player[numPlayers];

        // create "numPlayers" number of players
        for(int i = 0; i < numPlayers; i++) {
            // view.displayMessage("what is player " + (i + 1) + "'s name?");
            // String input = view.getUserInput();
            String input = "player " + (i + 1);

            Player player = new Player(input, i);
            playerList[i] = player;
            // ADDED -- attach the obseserver to use in the view's playerData panel
            playerList[i].attach(boardView);

            if(numPlayers == 5) {
                player.setCredits(2);
            }
            if(numPlayers == 6) {
                player.setCredits(4);
            }
            if(numPlayers >= 7) {
                player.setRank(2);
            }




            player.setDollars(100);
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
            // view.displayMessage("It's " + playerList[currentPlayer].getName() + "'s turn.");
            boardView.setActivePlayer(currentPlayer, playerList[currentPlayer].getRank() - 1);
            // System.out.println("active player is " + playerList[currentPlayer].getName());
            BoardLayersListener.displayMessage("It is player " + playerList[currentPlayer].getName() + "'s turn!");

            boolean turnActive = true;
            
            // ADDED -- display active players data for playerData panel in GUI
            if (playerList[currentPlayer].getRole() != null) {
                // player has a role to parse in
                boardView.displayPlayerData(currentPlayer + 1, playerList[currentPlayer].getLocation().getName(),
                    playerList[currentPlayer].getRank(), playerList[currentPlayer].getDollars(), playerList[currentPlayer].getCredits(),
                    playerList[currentPlayer].getRole().getName(), dayCount);
            } else {
                // there is no role to parse in
                boardView.displayPlayerData(currentPlayer + 1, playerList[currentPlayer].getLocation().getName(),
                    playerList[currentPlayer].getRank(), playerList[currentPlayer].getDollars(), playerList[currentPlayer].getCredits(),
                    "no active role", dayCount);
            }

            // Player's turn loop
            while (turnActive && gameRunning) {
                currentCommand = null; // Reset command before waiting for a new one

                // Wait until a command is received
                while (currentCommand == null) {
                    try {
                        Thread.sleep(100); // Polling every 100ms instead of blocking
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                BoardLayersListener.displayMessage("Enter a command");
                // String input = view.getUserInput();
                turnActive = handleInput(currentCommand, playerList, currentPlayer);
            }

            // Move to the next player after their turn ends
            currentPlayer = (currentPlayer + 1) % playerList.length;
            playerList[currentPlayer].setHasMoved(false);

            // clear current players data from GUI
            boardView.clearPlayerData();
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

        // set up the scenes on the gui
        Room[] allRooms = board.getRooms();
        Set[] sets = new Set[10];
        for (int i = 0; i < 10; i++) {
            sets[i] = (Set) allRooms[i];
        }

        BoardLayersListener.setSceneCards(selectedScenes, sets);

        // for the 10 rooms that need scenes assign a scene
        for(int i = 0; i < 10; i++) {
            Set set = sets[i];
            
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


        int x = board.getRooms()[11].getX();
        int y = board.getRooms()[11].getY();
        // set the players back to the trailers
        for(int i = 0; i < playerList.length; i++) {
            Player player = playerList[i];
            player.setLocation(board.getRooms()[11]);
            player.setRole(null);

            // NO CURRENT PLAYER -- do not need to notify observer

            // for formatting
            if(i < 4) {
                boardView.setPlayersVisible(i, player.getRank() - 1, x + (i * 40), y);
            } else {
                boardView.setPlayersVisible(i, player.getRank() - 1, x + ((i - 4) * 40), y + 40);
            }
            
        }
        
        dayCount++;
        return remainingScenes;
    }

    // handle user input
    private boolean handleInput(String input, Player[] playerList, int currentPlayer) {
        List<String> command = new ArrayList<>(Arrays.asList(input.split(" ")));
        int commandLength = command.size();
    
        if (input.equals("move")) {
            if(playerList[currentPlayer].getHasMoved() == true) {
                BoardLayersListener.displayMessage("Player has already moved, please pick a different command");
                return true;
            }
            if(playerList[currentPlayer].getRole() != null) {
                BoardLayersListener.displayMessage("Player has already moved, please pick a different command");
                return true;
            }
    
            // get the requested destination and attempt to move there
            String destination = BoardLayersListener.roomSelection(playerList[currentPlayer].getLocation().getNeighbors());
            move(playerList[currentPlayer], destination);

            return true;

        }
        else if (command.get(0).equals("work")) {
            if(playerList[currentPlayer].getRole() != null) {
                BoardLayersListener.displayMessage("You are already working, you can act or rehearse");
                return true;
            }
            
            // player must be at a location with roles
            if(playerList[currentPlayer].getLocation().getName().equals("office") || playerList[currentPlayer].getLocation().getName().equals("trailer")) {
                BoardLayersListener.displayMessage("Must be at a location with roles to work");
                return true;
            }

            // get the active players location
            Set location = (Set) playerList[currentPlayer].getLocation(); 
            Scene locationScene = location.getScene();

            // wanted to try something new "Stream" the arrays together
            String[] combinedRoles = Stream.concat(Arrays.stream(location.getRoleNames()), Arrays.stream(locationScene.getRoleNames())).toArray(String[]::new);

            // get the requested destination and attempt to let the player work there

            String destination = BoardLayersListener.roomSelection(combinedRoles);
            int commaIndex = destination.indexOf(",");
            if (commaIndex != -1) {
                destination = destination.substring(0, commaIndex).trim();
            }
            return work(playerList[currentPlayer], destination);

        }
        else if (command.get(0).equals("act")) {
            // attempt to let the player act
            return act(playerList[currentPlayer]);

        } 
        else if (command.get(0).equals("rehearse")) {
            // attempt to let the player rehearse
            BoardLayersListener.displayMessage("rehearse command selected.");
            if(playerList[currentPlayer].getRole() == null) {
                BoardLayersListener.displayMessage("Must \\\"work\\\" a role before rehearsing");
                return true;
            }
            return rehearse(playerList[currentPlayer]);
        }  
        else if (command.get(0).equals("upgrade")) {
            // attempt to let the player upgrade
            CastingOffice office = (CastingOffice) board.getRooms()[10];
            if(playerList[currentPlayer].getLocation() != office) {
                BoardLayersListener.displayMessage("cannot upgrade Unless at casting office");
                return true;
            }
            // get upgrade info from the player and attempt to upgrade rank
            BoardLayersListener.displayMessage("Please enter the rank you would like and what you would like to purchase it with");
            String[] upgrades = {"2 dollars", "3 dollars", "4 dollars", "5 dollars", "6 dollars", "2 credits", "3 credits", "4 credits", "5 credits", "6 credits"};
            String destination = BoardLayersListener.upgradeSelection(upgrades);
            return tryUpgrade(playerList[currentPlayer], office, destination);
        } 
        else if (String.join(" ", command).equals("end turn")) {
            // ends the current players turn
            BoardLayersListener.displayMessage(playerList[currentPlayer].getName() + "'s turn has ended.");
            return false;
        }
        else if (String.join(" ", command).equals("end day")) {
            // ends the current day and starts a new one
            BoardLayersListener.displayMessage("Day is over");
            sceneList = startDay(sceneList, playerList);
            return false;
        } 
        else if (String.join(" ", command).equals("end game")) {
            // ends the game and moves to scoring
            gameRunning = false;
            BoardLayersListener.displayMessage("Game Over");
            return false;
        } 
        else {
            return true;
        }
    }

    // check the active players input and validate a move to a new location
    private boolean move(Player currentPlayer, String destination) {
        
        // if it is, loop through the rooms and set players location to the new room
        for(int i = 0; i < board.getRooms().length; i++) {
            if(board.getRooms()[i].getName().equals(destination)) {

                // if the location has a scene card
                if(board.getRooms()[i].getName().equals("office") == false && board.getRooms()[i].getName().equals("trailer") == false) {
                    Set set = (Set) board.getRooms()[i];
                    if(set.getStatus().equals("not active")) {
                        BoardLayersListener.flipCard(i);
                        set.setStatus("active");
                    }
                }
                currentPlayer.setLocation(board.getRooms()[i]);
                currentPlayer.setHasMoved(true);
            }
        }
        BoardLayersListener.setPlayerLocation(currentPlayer.getLocation().getX(), currentPlayer.getLocation().getY());

        // ADDED -- notify GUI of update
        if (currentPlayer.getRole() != null) {
            // active role to display
            boardView.update(currentPlayer.getPlayerIndex() + 1, currentPlayer.getLocation().getName(), currentPlayer.getRank(),
                    currentPlayer.getDollars(), currentPlayer.getCredits(), currentPlayer.getRole().getName());
        } else {
            // no active role to display
            boardView.update(currentPlayer.getPlayerIndex() + 1, currentPlayer.getLocation().getName(), currentPlayer.getRank(),
                    currentPlayer.getDollars(), currentPlayer.getCredits(), "no active role");
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
        boolean onCard = false;
        Role role = null;

        // check each board role
        for(int i = 0; i < boardRoles.length; i++) {
            // if the destination is a valid role
            if(boardRoles[i].getName().equals(destination)) {
                role = boardRoles[i];
            }
        }
        
        // then check each card role
        for(int i = 0; i < sceneRoles.length; i++) {
            // if the destination is a valid roll
            if(sceneRoles[i].getName().equals(destination)) {
                role = sceneRoles[i];
                onCard = true;
            }
        }

        // check if the scene is wrapped, role is taken, or if player rank is too low
        if(role.getScene().getStatus().equals("wrapped")) {
            BoardLayersListener.displayMessage("the scene for this room has been wrapped");
            return true;
        }
        if(role.getActor() != null) {
            BoardLayersListener.displayMessage("This role is already taken");
            return true;
        }
        if(role.getRankToAct() > currentPlayer.getRank()) {
            BoardLayersListener.displayMessage("This role's rank is too high");
            return true;
        }

        // then if not, set the players role 
        currentPlayer.setRole(role);
        currentPlayer.getRole().setActor(currentPlayer);

        if(onCard) {
            // get correct cords
            int setX = location.getX();
            int setY = location.getY();
            int roleX = role.getX();
            int roleY = role.getY();
            int roleW = role.getW();
            int roleH = role.getH();
            BoardLayersListener.movePlayerOnCardRole(setX, setY, roleX, roleY, roleW, roleH);
        } else {  
            int x = role.getX();
            int y = role.getY();
            int w = role.getW();
            int h = role.getH();
            BoardLayersListener.movePlayerOffCardRole(x, y, w, h);
        }
        BoardLayersListener.displayMessage("Player is now working " + role.getName());

        // ADDED -- notify GUI of update
        if (currentPlayer.getRole() != null) {
            // active role to display
            boardView.update(currentPlayer.getPlayerIndex() + 1, currentPlayer.getLocation().getName(), currentPlayer.getRank(),
                    currentPlayer.getDollars(), currentPlayer.getCredits(), currentPlayer.getRole().getName());
        } else {
            // no active role to display
            boardView.update(currentPlayer.getPlayerIndex() + 1, currentPlayer.getLocation().getName(), currentPlayer.getRank(),
                    currentPlayer.getDollars(), currentPlayer.getCredits(), "no active role");
        }

        // if the player has already moved then their turn is over
        if(currentPlayer.getHasMoved() == true) {
            BoardLayersListener.displayMessage("Player has moved, so their turn is over");
            return false;
        }
        return true;
    }

    // validate that the player meets the requirements to upgrade and then increase rank
    private boolean tryUpgrade(Player player, CastingOffice office, String request) {
        String[] parts = request.split("\\s+"); // Split by spaces
        try {
            int rank = Integer.parseInt(parts[0]);
            String type = parts[1];    
            BoardLayersListener.displayMessage("Attempting to upgrade to rank " + rank + " from a current rank of " + player.getRank());

            // get the rank that the user requested
            Rank[] ranks = office.getRanks();
            Rank requestedRank = ranks[rank-2];

            if(requestedRank.getRankLevel() <= player.getRank()) {
                BoardLayersListener.displayMessage("you are already this rank or higher");
                return true;
            }
            
            // if they want to upgrade with dollars
            if(type.equals("dollars")) {
                if(player.getDollars() < requestedRank.getDollarCost()) {
                    BoardLayersListener.displayMessage("You do not have enough dollars to upgrade, you need " + requestedRank.getDollarCost() + " for rank " + requestedRank.getRankLevel());
                    return true;
                }
                BoardLayersListener.increasePlayersRank(player.getPlayerIndex(), requestedRank.getRankLevel() - 1);
                player.setRank(requestedRank.getRankLevel());
                int newDollarAmount = player.getDollars() - requestedRank.getDollarCost();
                player.setDollars(newDollarAmount);
            }
            
            // if they want to upgrade with credits
            if(type.equals("credits")) {
                if(player.getCredits() < requestedRank.getCreditCost()) {
                    BoardLayersListener.displayMessage("You do not have enough credits to upgrade, you need " + requestedRank.getCreditCost() + " for rank " + requestedRank.getRankLevel());
                    return true;
                }
                player.setRank(requestedRank.getRankLevel());
                int newCreditAmount = player.getCredits() - requestedRank.getCreditCost();
                BoardLayersListener.increasePlayersRank(player.getPlayerIndex(), requestedRank.getRankLevel() - 1);
                player.setCredits(newCreditAmount);
            }
            BoardLayersListener.displayMessage("Player is now rank " + player.getRank());

            // ADDED -- notify GUI of update
            if (player.getRole() != null) {
                // active role to display
                boardView.update(player.getPlayerIndex() + 1, player.getLocation().getName(), player.getRank(),
                        player.getDollars(), player.getCredits(), player.getRole().getName());
            } else {
                // no active role to display
                boardView.update(player.getPlayerIndex() + 1, player.getLocation().getName(), player.getRank(),
                        player.getDollars(), player.getCredits(), "no active role");
            }

            return true;
        } catch (NumberFormatException e) {
            return true;
        }
    }

    // validate that the player meets the requirements to act and then perform action
    private boolean act(Player player) {
        Dice dice = new Dice();
        int diceRoll = dice.roll();
        Role role = player.getRole();
        Scene scene = role.getScene();
        
        // role dice to act
        if((diceRoll + role.getPracticeChips()) < scene.getBudget()) {
            BoardLayersListener.displayMessage("you rolled a " + diceRoll + " and have " + role.getPracticeChips() + " practice chips, the budget was " + scene.getBudget() + " better luck next time");
            return false;
        }
        BoardLayersListener.displayMessage("Congratulations you rolled a " + diceRoll + " and have + " + role.getPracticeChips() + " practice chips, the budget was " + scene.getBudget());

        // if success then remove shot counter
        int shots = scene.getShotCounter();
        scene.setShotCounter(shots-1);

        // remove take in gui
        for(int i = 0; i < board.getRooms().length; i++) {
            if(board.getRooms()[i].getName().equals(player.getLocation().getName())) {
                BoardLayersListener.removeTake(i, scene.getShotCounter());
            }
        }
        BoardLayersListener.displayMessage("This scene has " + scene.getShotCounter() + " shots left");
        
        // if scene is over then wrap it
        if(shots-1 == 0) {
            sceneWrap(scene, player);
        }

        // ADDED -- notify GUI of update
        if (player.getRole() != null) {
            // active role to display
            boardView.update(player.getPlayerIndex() + 1, player.getLocation().getName(), player.getRank(),
                    player.getDollars(), player.getCredits(), player.getRole().getName());
        } else {
            // no active role to display
            boardView.update(player.getPlayerIndex() + 1, player.getLocation().getName(), player.getRank(),
                    player.getDollars(), player.getCredits(), "no active role");
        }

        
        // otherwise pay player based on on vs off card roles
        if(role.getOnCard() == true) {
            if(diceRoll < scene.getBudget()) {
                return false;
            }
            player.addCredits(2);
            BoardLayersListener.displayMessage("Player " + player.getName() + " received 2 credits! they now have " + player.getCredits() + " credits and " + player.getDollars() + " dollars!");
        } else {
            if(diceRoll < scene.getBudget()) {
                player.addDollars(1);
                return false;
            }
            player.addCredits(1);
            player.addDollars(1);
            BoardLayersListener.displayMessage("Player " + player.getName() + " received 1 credits and 1 dollar they now have " + player.getCredits() + " credits and " + player.getDollars() + " dollars!");
        }

        // ADDED -- notify GUI of update
        if (player.getRole() != null) {
            // active role to display
            boardView.update(player.getPlayerIndex() + 1, player.getLocation().getName(), player.getRank(),
                    player.getDollars(), player.getCredits(), player.getRole().getName());
        } else {
            // no active role to display
            boardView.update(player.getPlayerIndex() + 1, player.getLocation().getName(), player.getRank(),
                    player.getDollars(), player.getCredits(), "no active role");
        }

        return false;
    }

    // validate that the player meets the requirements to rehearse and then perform action
    private boolean rehearse(Player player) {
        Role role = player.getRole();
        int chips = role.getPracticeChips();
        if(chips < role.getScene().getBudget()) {
            role.setPracticeChips(chips + 1);
            BoardLayersListener.displayMessage("You rehearsed and now have " + role.getPracticeChips() + " practice chips!");
            return false;
        }
        BoardLayersListener.displayMessage("You already have the max amount of practice chips, please act");
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
            BoardLayersListener.displayMessage("there is no one on a on card role, therefore no bonus payment");
        }

        // on card bonuses
        if(playerOnCard) {
            int[] rolls = new int[scene.getBudget()];
            BoardLayersListener.displayMessage("active player gets to roll " + scene.getBudget() + " dice");
            Dice dice = new Dice();
            
            // get and sort the rolls depending on the budget
            for(int i = 0; i < scene.getBudget(); i++) {
                int value = dice.roll();
                BoardLayersListener.displayMessage("you rolled a " + value);
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

        // get the room so the gui can flip the scene card
        int room = 0;
        for(int i = 0; i < board.getRooms().length; i++) {
            if(activePlayer.getLocation().getName().equals(board.getRooms()[i].getName())) {
                room = i;
            }
        }

        // loop through the scene and remove players role
        for(int i = 0; i < scene.getRoles().length; i++) {
            if(scene.getRoles()[i].getActor() != null) {
                // move off of role in gui
                int index = scene.getRoles()[i].getActor().getPlayerIndex();
                int rank = scene.getRoles()[i].getActor().getRank();
                int x = activePlayer.getLocation().getX();
                int y = activePlayer.getLocation().getY();
                BoardLayersListener.removePlayerFromScene(index, rank - 1, x, y);
                scene.getRoles()[i].getActor().setRole(null);
            }
        }

        // loop through the board and remove players role
        Set offCard = (Set) activePlayer.getLocation();
        for(int i = 0; i < offCard.getRoles().length; i++) {
            if(offCard.getRoles()[i].getActor() != null) {
                
                // move off of role in gui
                int index = offCard.getRoles()[i].getActor().getPlayerIndex();
                int rank = offCard.getRoles()[i].getActor().getRank();
                int x = activePlayer.getLocation().getX();
                int y = activePlayer.getLocation().getY();
                BoardLayersListener.removePlayerFromScene(index, rank - 1, x, y);
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
        // remove the players from roles
        BoardLayersListener.removeSceneCard(room);

        // ADDED -- notify GUI of update
        if (activePlayer.getRole() != null) {
            // active role to display
            boardView.update(activePlayer.getPlayerIndex() + 1, activePlayer.getLocation().getName(), activePlayer.getRank(),
                    activePlayer.getDollars(), activePlayer.getCredits(), activePlayer.getRole().getName());
        } else {
            // no active role to display
            boardView.update(activePlayer.getPlayerIndex() + 1, activePlayer.getLocation().getName(), activePlayer.getRank(),
                    activePlayer.getDollars(), activePlayer.getCredits(), "no active role");
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
            BoardLayersListener.displayMessage("The winner is " + winners.get(0).getName() + " with a score of " + winningScore);
        } else {
            String winnerNames = winners.stream()
                                        .map(Player::getName)
                                        .collect(Collectors.joining(", "));
            BoardLayersListener.displayMessage("It's a tie! The winners are " + winnerNames + " with a score of " + winningScore);
        }
        BoardLayersListener.gameOver();
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
