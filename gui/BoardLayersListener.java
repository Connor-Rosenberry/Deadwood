package gui;

import java.awt.*;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.List;

// View: handles UI
// JFrame: main window
public class BoardLayersListener extends JFrame {
   // default data types
   private static int numPlayers;
   // helpers for player dice rank (0-5 representing rank 1-6)
   private static int totalPlayers;
   private static int playerDieLevel[] = new int[8];
   
   // JLabels
   private static JLabel boardLabel;  // the gameboard
   private static JLabel playerLabel;
   private static JLabel playerDataLabel;  // player data box
   private static JLabel[] activeCardLabels;
   private static JLabel[][] activeTakeLabels = new JLabel[10][];  // an array per set
   private static JLabel[][] playerLabels = new JLabel[8][6];  // array of player dice arrays

   // JPanels
   // containers that hold groups of components
   private static JPanel consolePanel;
   private static JPanel buttonPanel;
   private static JPanel playerDataPanel;
  
   //JButtons
   private static JButton bAct;  // act
   private static JButton bRehearse;  // rehearse
   private static JButton bMove;  // move
   private static JButton bUpgrade;  // upgrade
   private static JButton bTakeRole;  // take role
   private static JButton bEndTurn;  // end turn
   private static JButton bEndGame;  // end game

   // JLayered Pane
   private static JLayeredPane boardPane;

   // JSplitPane
   private static JSplitPane splitPane;
   private static JSplitPane consoleSplitPane;  // console + button split
   private static JSplitPane dataSplitPane;  // all data to the right of board

   // JTextArea
   private static JTextArea consoleArea;  // the console
  
   // Images
   private static ImageIcon board;  // board

   // Constructor
   public BoardLayersListener() {
      // Set the title of the JFrame
      super("Deadwood");
      // Set the exit option for the JFrame
      setDefaultCloseOperation(EXIT_ON_CLOSE);
    
      // Create the deadwood board
      setUpBoard();
      
      // Set the size of the GUI
      setSize(board.getIconWidth() + 400, board.getIconHeight() + 50);

      // Init all player dice (assume max players)
      initAllPlayers();

      // DATA TO THE RIGHT OF BOARD SETUP
      // create the console
      consolePanel = new JPanel();
      consolePanel.setLayout(new BorderLayout());
      consoleArea = new JTextArea();
      consoleArea.setEditable(false);  // user cannot edit console
      consolePanel.add(new JScrollPane(consoleArea), BorderLayout.CENTER);

      // create button panel
      buttonPanel = new JPanel();
      // adjust the grid layout based on num of buttons
      buttonPanel.setLayout(new GridLayout(4, 2));

      // create player buttons
      // act
      bAct = new JButton("Act");
      bAct.setBackground(Color.white);
      bAct.addMouseListener(new boardMouseListener());
        
      // rehearse
      bRehearse = new JButton("Rehearse");
      bRehearse.setBackground(Color.white);
      bRehearse.addMouseListener(new boardMouseListener());
        
      // move
      bMove = new JButton("Move");
      bMove.setBackground(Color.white);
      bMove.addMouseListener(new boardMouseListener());

      // upgrade
      bUpgrade = new JButton("Upgrade");
      bUpgrade.setBackground(Color.white);
      bUpgrade.addMouseListener(new boardMouseListener());

      // take role
      bTakeRole = new JButton("Take Role");
      bTakeRole.setBackground(Color.white);
      bTakeRole.addMouseListener(new boardMouseListener());

      // end turn
      bEndTurn = new JButton("End Turn");
      bEndTurn.setBackground(Color.white);
      bEndTurn.addMouseListener(new boardMouseListener());

      // end game
      bEndGame = new JButton("End Game");
      bEndGame.setBackground(Color.white);
      bEndGame.addMouseListener(new boardMouseListener());
 
      // Place the action buttons in the top layer
      buttonPanel.add(bAct);
      buttonPanel.add(bRehearse);
      buttonPanel.add(bMove);
      buttonPanel.add(bUpgrade);
      buttonPanel.add(bTakeRole);
      buttonPanel.add(bEndTurn);
      buttonPanel.add(bEndGame);

      // create the player data panel
      playerDataPanel = new JPanel();
      playerDataPanel.setLayout(new BorderLayout());
      playerDataLabel = new JLabel("Player Data");
      playerDataPanel.add(playerDataLabel, BorderLayout.CENTER);

      // create a vertical split pane for console + buttons
      consoleSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, consolePanel, buttonPanel);
      consoleSplitPane.setDividerLocation(400);
      consoleSplitPane.setResizeWeight(0.8);

      // create a vertical split pane for console/buttons + playerData
      dataSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, consoleSplitPane, playerDataPanel);
      dataSplitPane.setDividerLocation(600);

      // create a split pane to hold the board + console panels
      splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, boardPane, dataSplitPane);
      // set the split location between the board and console/buttons
      splitPane.setDividerLocation(board.getIconWidth());

      add(splitPane);  // add the split pane to the frame
      setVisible(true);  // show the frame
   }

   // TODO DELETE!! ONLY FOR TESTING
   private static Scene[] getSampleScenes() {
      SceneCreator createCard = new SceneCreator();
      Scene[] sceneList = createCard.parseSceneCards();

      // randomly grab 10 scenes
      Random rand = new Random();
      List<Scene> sceneArrayList = new ArrayList<>(List.of(sceneList)); // Convert array to List
      Scene[] selectedScenes = new Scene[10]; // Array for selected scenes

      // for each room with scenes pick a random new scene from the remaining
      for (int i = 0; i < 10; i++) {
          int randomIndex = rand.nextInt(sceneArrayList.size());
          selectedScenes[i] = sceneArrayList.remove(randomIndex);
      }

      return selectedScenes;
   }

   // TODO DELETE!! ONLY FOR TESTING
   private static Set[] getSampleSets() {
      // set 0: train station
      // set 1: secert hideout
      // set 2: church
      // set 3: hotel
      // set 4: main st
      // set 5: jail
      // set 6: general store
      // set 7: ranch
      // set 8: bank
      // set 9: saloon
      Set[] sets = new Set[10];

      Board board = new Board();
      BoardCreator fill = new BoardCreator();
      board = fill.parseBoard();

      for(int i = 0; i < 10; i++) {
         sets[i] = (Set) board.getRooms()[i];
      }

      return sets;
   }

   // constructor helper: setting up the board
   // GUI ONLY
   private static void setUpBoard() {
      // create the board
      boardLabel = new JLabel();
      board = new ImageIcon("gui/img/board.jpg");
      boardLabel.setIcon(board); 
      boardLabel.setBounds(0, 0, board.getIconWidth(), board.getIconHeight());

      boardPane = new JLayeredPane();
      // add board label(image) to the lowest layer
      boardPane.add(boardLabel, 0);

      // set 10 scene cards on the board
      activeCardLabels = setSceneCards(getSampleScenes(), getSampleSets());

      // revalidate and repaint to make sure components are displayed
      boardPane.revalidate();
      boardPane.repaint();
   }

   // SCENE CARDS

   // constructor helper: will randomly select 10 cards to set on the scene
   // scenes should ONLY contain the 10 scene cards that shall be place
   // sets should ONLY contain the 10 sets the scenes should be in
   // scenes and sets will be access with the same i
   // returns the JLabels for the set up cards
   // GUI ONLY
   public static JLabel[] setSceneCards(Scene[] scenes, Set[] sets) {
      JLabel[] cardLabels = new JLabel[10];
      // for the 10 rooms that need scenes assign a scene
      for(int i = 0; i < 10; i++) {
         // get img file from
         String filename = "gui/img/card/" + scenes[i].getImg();
         // get set card placement area
         int x = sets[i].getX();
         int y = sets[i].getY();
         
         // add a scene card to the room
         JLabel cardLabel = new JLabel();
         ImageIcon cardIcon = new ImageIcon(filename);
         cardLabel.setIcon(cardIcon);
         cardLabel.setBounds(x, y, cardIcon.getIconWidth(), cardIcon.getIconHeight());
         cardLabel.setOpaque(true);

         // Add the card to the lower layer
         boardPane.add(cardLabel, Integer.valueOf(1));

         // add to the array
         cardLabels[i] = cardLabel;
         
         // set up the takes on the board
         activeTakeLabels[i] = setTakes(sets[i].getTakes());
      }
      return cardLabels;
   }

   // clean scene cards from board (GUI ONLY)
   public static void removeSceneCards(JLabel[] cardLabels) {
      // for each set
      for (int i = 0; i < 10; i++) {
         boardPane.remove(cardLabels[i]);
      }
      boardPane.revalidate();
      boardPane.repaint();
      return;
   }

   // removes all scene cards AND takes, ideally always call this one
   public static void removeSceneCards(JLabel[] cardLabels, JLabel[][]takeLabels) {
      // for each set
      for (int i = 0; i < 10; i++) {
         boardPane.remove(cardLabels[i]);
         clearTakes(takeLabels[i]);
      }
      boardPane.revalidate();
      boardPane.repaint();
      return;
   }

   // remove just one scene card, from the given JLabel
   // NOTE: be careful to track what scenes have already been removed
   public static void removeSceneCards(JLabel cardLabel) {
      if (cardLabel == null) {
         // for array/loop handling, no card to remove
         return;
      }
      boardPane.remove(cardLabel);

      boardPane.revalidate();
      boardPane.repaint();
      return;
   }

   // remove just one scene card AND its takes, from the given JLabel
   // NOTE: be careful to track what scenes have already been removed
   public static void removeSceneCards(JLabel cardLabel, JLabel[]takeLabels) {
      if (cardLabel == null) {
         // for array/loop handling, no card to remove
         clearTakes(takeLabels);
         return;
      }
      
      boardPane.remove(cardLabel);
      clearTakes(takeLabels);

      boardPane.revalidate();
      boardPane.repaint();
      return;
   }

   // TAKES

   // set up the takes for the set associated w takes (shot counter)
   public static JLabel[] setTakes(Take[] takes) {
      JLabel[] takeLabels = new JLabel[takes.length];
      String filename = "gui/img/shot.png";
      // for every take in the array
      for (int i = 0; i <takes.length; i++) {
         int x = takes[i].getX();
         int y = takes[i].getY();

         JLabel takeLabel = new JLabel();
         ImageIcon takeIcon = new ImageIcon(filename);
         takeLabel.setIcon(takeIcon);
         takeLabel.setBounds(x, y, takeIcon.getIconWidth(), takeIcon.getIconHeight());
         takeLabel.setOpaque(true);

         // Add the take to the lower layer
         boardPane.add(takeLabel, Integer.valueOf(1));

         // add to the array
         takeLabels[i] = takeLabel;
      }
      return takeLabels;
   }

   // remove one of the takes for the set associated w takes (shot counter)
   // WARNING: does not error check for if there are no more shots to remove
   // returns updated JLabel (where shot is removed from)
   public static JLabel[] removeTake(JLabel[] takeLabels) {
      // if necessary, get to the take that needs to be deleted
      int i = takeLabels.length - 1;  // xml puts takes in backwards order
      while (i >= 0 && takeLabels[i] == null) {
         i--;
      }
      if (i >= 0) {
         // there is a take to remove
         boardPane.remove(takeLabels[i]);
         takeLabels[i] = null;  // set to null as it has been deleted
      }

      boardPane.revalidate();
      boardPane.repaint();
      return takeLabels;
   }

   // removes all takes from the given set
   // NOTE: be careful to track what scenes have already been removed
   public static void clearTakes(JLabel[] takeLabels) {
      if (takeLabels == null) {
         // no takes to clear
         return;
      }
      // for each set
      for (int i = 0; i < takeLabels.length; i++) {
         if (takeLabels[i] != null) {
            boardPane.remove(takeLabels[i]);
         }
      }
      boardPane.revalidate();
      boardPane.repaint();
      return;
   }

   // PLAYER DICE

   // initalize all player dice JLabels
   // not setting bounds or visible, as we do not want to display them on the board yet
   public static void initAllPlayers() {
      totalPlayers = 8; // assume max number of players

      // init all players to rank 1 (index 0)
      for (int i = 0; i < totalPlayers; i ++) {
         playerDieLevel[i] = 0;
      }

      // init player 1 (blue)
      for (int i = 0; i < 6; i++) {
         String filename = "gui/img/dice/b" + (i + 1) +".png";
         JLabel dieLabel = new JLabel();
         ImageIcon dieIcon = new ImageIcon(filename);
         dieLabel.setIcon(dieIcon);
         dieLabel.setOpaque(true);
         dieLabel.setVisible(false);

         boardPane.add(dieLabel, Integer.valueOf(3));

         // add to the player1 array
         playerLabels[0][i] = dieLabel;
      }

      // init player 2 (cyan)
      for (int i = 0; i < 6; i++) {
         String filename = "gui/img/dice/c" + (i + 1) +".png";
         JLabel dieLabel = new JLabel();
         ImageIcon dieIcon = new ImageIcon(filename);
         dieLabel.setIcon(dieIcon);
         dieLabel.setOpaque(true);
         dieLabel.setVisible(false);

         boardPane.add(dieLabel, Integer.valueOf(3));

         // add to the player1 array
         playerLabels[1][i] = dieLabel;
      }

      // init player 3 (green)
      for (int i = 0; i < 6; i++) {
         String filename = "gui/img/dice/g" + (i + 1) +".png";
         JLabel dieLabel = new JLabel();
         ImageIcon dieIcon = new ImageIcon(filename);
         dieLabel.setIcon(dieIcon);
         dieLabel.setOpaque(true);
         dieLabel.setVisible(false);

         boardPane.add(dieLabel, Integer.valueOf(3));

         // add to the player1 array
         playerLabels[2][i] = dieLabel;
      }

      // init player 4 (orange)
      for (int i = 0; i < 6; i++) {
         String filename = "gui/img/dice/o" + (i + 1) +".png";
         JLabel dieLabel = new JLabel();
         ImageIcon dieIcon = new ImageIcon(filename);
         dieLabel.setIcon(dieIcon);
         dieLabel.setOpaque(true);
         dieLabel.setVisible(false);

         boardPane.add(dieLabel, Integer.valueOf(3));

         // add to the player1 array
         playerLabels[3][i] = dieLabel;
      }

      // init player 5 (pink)
      for (int i = 0; i < 6; i++) {
         String filename = "gui/img/dice/p" + (i + 1) +".png";
         JLabel dieLabel = new JLabel();
         ImageIcon dieIcon = new ImageIcon(filename);
         dieLabel.setIcon(dieIcon);
         dieLabel.setOpaque(true);
         dieLabel.setVisible(false);

         boardPane.add(dieLabel, Integer.valueOf(3));

         // add to the player1 array
         playerLabels[4][i] = dieLabel;
      }

      // init player 6 (red)
      for (int i = 0; i < 6; i++) {
         String filename = "gui/img/dice/r" + (i + 1) +".png";
         JLabel dieLabel = new JLabel();
         ImageIcon dieIcon = new ImageIcon(filename);
         dieLabel.setIcon(dieIcon);
         dieLabel.setOpaque(true);
         dieLabel.setVisible(false);

         boardPane.add(dieLabel, Integer.valueOf(3));

         // add to the player1 array
         playerLabels[5][i] = dieLabel;
      }

      // init player 7 (violet)
      for (int i = 0; i < 6; i++) {
         String filename = "gui/img/dice/v" + (i + 1) +".png";
         JLabel dieLabel = new JLabel();
         ImageIcon dieIcon = new ImageIcon(filename);
         dieLabel.setIcon(dieIcon);
         dieLabel.setOpaque(true);
         dieLabel.setVisible(false);

         boardPane.add(dieLabel, Integer.valueOf(3));

         // add to the player1 array
         playerLabels[6][i] = dieLabel;
      }

      // init player 8 (yellow)
      for (int i = 0; i < 6; i++) {
         String filename = "gui/img/dice/y" + (i + 1) +".png";
         JLabel dieLabel = new JLabel();
         ImageIcon dieIcon = new ImageIcon(filename);
         dieLabel.setIcon(dieIcon);
         dieLabel.setOpaque(true);
         dieLabel.setVisible(false);

         boardPane.add(dieLabel, Integer.valueOf(3));

         // add to the player1 array
         playerLabels[7][i] = dieLabel;
      }
   }

   // update the total number of players playing (helper for specific move operations)
   // playerNum must be between 2-8
   // should only be called once
   public static void adjustPlayerCount(int playerNum) {
      // get rid of extra playerDice JFrames
      for (int i = playerNum; i < totalPlayers; i++) {
         // set not visible on board
         for (int j = 0; j < 6; j++) {
            playerLabels[i][j].setVisible(false);
         }

         // take out of array
         playerLabels[i] = null;
      }
      // update totalPlayers
      totalPlayers = playerNum;
   }

   // remove all players dice JLabels
   public static void clearAllPlayers() {
      for (int i = 0; i < totalPlayers; i++) {
         // set not visible on board
         for (int j = 0; j < 6; j++) {
            playerLabels[i][j].setVisible(false);
         }

         // take out of array
         playerLabels[i] = null;
      }
   }

   // increase a given players rank (and dice display)
   // player is 1-8 (do not consider 0 indexing for param)
   public static void increasePlayersRank(int playerNum) {
      playerDieLevel[playerNum - 1]++;
   }

   // move a player to the given room
   public static void movePlayerRoom() {

   }

   // move a player to a given on-card role
   public static void movePlayerOnCard() {

   }

   // move a player to a given off-card role (on the set)
   public static void movePlayerOffCard() {

   }

   // maybe make some wrappers that make it easier to move to specific rooms
   // + one to move all players to the trailers (keeping in mind not all 8 will always be playing)
  
   // END OF CONSTRUCTOR (and helpers)

   // This class implements Mouse Events
   class boardMouseListener implements MouseListener{
      // Code for the different button clicks
      public void mouseClicked(MouseEvent e) { 
         if (e.getSource() == bAct) {
            playerLabel.setVisible(true);
            displayMessage("Act selected");
         }
         else if (e.getSource() == bRehearse) {
            displayMessage("Rehearse selected");
         }
         else if (e.getSource() == bMove) {
            displayMessage("Move selected");
         }
         else if (e.getSource() == bUpgrade) {
            displayMessage("Upgrade selected");
         }
         else if (e.getSource() == bTakeRole) {
            displayMessage("Take Role selected");
         }
         else if (e.getSource() == bEndTurn) {
            displayMessage("End turn selected");
         }
         else if (e.getSource() == bEndGame) {
            displayMessage("End game selected");
         }
      }

      public void mousePressed(MouseEvent e) {
      }

      public void mouseReleased(MouseEvent e) {
      }

      public void mouseEntered(MouseEvent e) {
      }

      public void mouseExited(MouseEvent e) {
      }
   }

   // print messaged to the console  // VIEW
   public static void displayMessage(String message) {
      consoleArea.append(message + "\n");
   }

   // popup for inputing number players  // INPUT
   public static int playerSelection() {
      Integer[] options = {2, 3, 4, 5, 6, 7, 8};
      // prompt user
      Integer selection = (Integer) JOptionPane.showInputDialog(
         null,
         "How many players?",
         "Player selection",
         JOptionPane.QUESTION_MESSAGE,
         null,
         options,
         options[0]);
      if (selection == null) {
         // player didn't select
         return 2;  // default option
      }
      return selection;
   }

   // FOR TESTING, acts as this classes main method
   public static void makeGUI() {
      BoardLayersListener board = new BoardLayersListener();
      board.setVisible(true);
    
      // Take input from the user about number of players
      numPlayers = playerSelection();

      // example of how to remove a single take
      // removeTake(activeTakeLabels[0]);

      // example of how to remove takes and labels
      removeSceneCards(activeCardLabels, activeTakeLabels);
   }
}