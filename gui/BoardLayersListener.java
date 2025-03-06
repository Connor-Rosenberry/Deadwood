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
   public static int numPlayers;
   
   // JLabels
   private static JLabel boardLabel;  // the gameboard
   private static JLabel playerLabel;
   private static JLabel playerDataLabel;  // player data box
   private static JLabel[] activeCardLabels;
   private static JLabel[][] activeTakeLabels = new JLabel[10][];  // an array per set

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
   private static JLayeredPane bPane;
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
      // Create the JLayeredPane to hold the display, cards, dice and buttons
      bPane = getLayeredPane();
    
      // Create the deadwood board
      setUpBoard();
      
      // Set the size of the GUI
      setSize(board.getIconWidth() + 400, board.getIconHeight() + 50);

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

      // Add a dice to represent a player. 
      // Role for Crusty the prospector. The x and y co-ordiantes are taken from Board.xml file
      playerLabel = new JLabel();
      ImageIcon pIcon = new ImageIcon("gui/img/dice/r2.png");
      playerLabel.setIcon(pIcon);
      playerLabel.setBounds(114,227,pIcon.getIconWidth(),pIcon.getIconHeight());  
      //playerLabel.setBounds(114,227,46,46);
      playerLabel.setVisible(true);

      boardPane.add(playerLabel, Integer.valueOf(3));

      // revalidate and repaint to make sure components are displayed
      boardPane.revalidate();
      boardPane.repaint();
   }

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

   // removes scene cards AND takes, ideally always call this one
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

   // remove just one scene card AND its takes, from the given JLabel
   public static void removeSceneCards(JLabel cardLabel) {
      boardPane.remove(cardLabel);

      boardPane.revalidate();
      boardPane.repaint();
      return;
   }

   // remove just one scene card AND its takes, from the given JLabel
   public static void removeSceneCards(JLabel cardLabel, JLabel[]takeLabels) {
      boardPane.remove(cardLabel);
      clearTakes(takeLabels);

      boardPane.revalidate();
      boardPane.repaint();
      return;
   }

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
   public static void clearTakes(JLabel[] takeLabels) {
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