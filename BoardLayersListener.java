/*

   Deadwood GUI helper file
   Author: Moushumi Sharmin
   This file shows how to create a simple GUI using Java Swing and Awt Library
   Classes Used: JFrame, JLabel, JButton, JLayeredPane

*/

import java.awt.*;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.event.*;

// View: handles UI
// JFrame: main window
public class BoardLayersListener extends JFrame {
   // JLabels
   JLabel boardLabel;  // the gameboard
   JLabel cardLabel;
   JLabel playerLabel;
   JLabel mLabel;
   JLabel playerDataLabel;

   // JPanels
   // containers that hold groups of components
   JPanel boardPanel;
   JPanel consolePanel;
   JPanel buttonPanel;
   JPanel playerDataPanel;
  
   //JButtons
   JButton bAct;  // act
   JButton bRehearse;  // rehearse
   JButton bMove;  // move
   JButton bUpgrade;  // upgrade
   JButton bTakeRole;  // take role
   JButton bEndTurn;  // end turn
   JButton bEndGame;  // end game

   // JLayered Pane
   JLayeredPane bPane;

   // JSplitPane
   JSplitPane splitPane;
   JSplitPane consoleSplitPane;  // console + button split
   JSplitPane dataSplitPane;  // all data to the right of board

   // JTextArea
   JTextArea consoleArea;  // the console
  

   // Constructor
   public BoardLayersListener() {
      // Set the title of the JFrame
      super("Deadwood");
      // Set the exit option for the JFrame
      setDefaultCloseOperation(EXIT_ON_CLOSE);
      
      // Create the JLayeredPane to hold the display, cards, dice and buttons
      bPane = getLayeredPane();
    
      // Create the deadwood board
      boardPanel = new JPanel();
      boardPanel.setLayout(new BorderLayout());

      boardLabel = new JLabel();
      ImageIcon icon =  new ImageIcon("img/board.jpg");
      boardLabel.setIcon(icon); 
      boardLabel.setBounds(0, 0, icon.getIconWidth(), icon.getIconHeight());

      boardPanel.add(boardLabel, BorderLayout.CENTER);
      
      // Add the board to the lowest layer
      bPane.add(boardLabel, 0);
      
      // Set the size of the GUI
      setSize(icon.getIconWidth() + 400, icon.getIconHeight() + 50);
      
      // Add a scene card to this room
      cardLabel = new JLabel();
      ImageIcon cIcon =  new ImageIcon("img/card/01.png");
      cardLabel.setIcon(cIcon); 
      cardLabel.setBounds(20,65,cIcon.getIconWidth()+2,cIcon.getIconHeight());
      cardLabel.setOpaque(true);
      
      // Add the card to the lower layer
      bPane.add(cardLabel, 1);
       
      

    
      // Add a dice to represent a player. 
      // Role for Crusty the prospector. The x and y co-ordiantes are taken from Board.xml file
      playerLabel = new JLabel();
      ImageIcon pIcon = new ImageIcon("img/dice/r2.png");
      playerLabel.setIcon(pIcon);
      //playerlabel.setBounds(114,227,pIcon.getIconWidth(),pIcon.getIconHeight());  
      playerLabel.setBounds(114,227,46,46);
      playerLabel.setVisible(false);
      bPane.add(playerLabel, 3);


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
      buttonPanel.setLayout(new GridLayout(2, 7));

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
      splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, boardPanel, dataSplitPane);
      // set the split location between the board and console/buttons
      splitPane.setDividerLocation(icon.getIconWidth());

      add(splitPane);  // add the split pane to the frame
      setVisible(true);  // show the frame
   }
  

   // This class implements Mouse Events
   class boardMouseListener implements MouseListener{
  
      // Code for the different button clicks
      public void mouseClicked(MouseEvent e) {
         
         if (e.getSource() == bAct) {
            playerLabel.setVisible(true);
            System.out.println("Act selected\n");
         }
         else if (e.getSource() == bRehearse) {
            System.out.println("Rehearse selected\n");
         }
         else if (e.getSource() == bMove) {
            System.out.println("Move selected\n");
         }
         else if (e.getSource() == bUpgrade) {
            System.out.println("Upgrade selected\n");
         }
         else if (e.getSource() == bTakeRole) {
            System.out.println("Take Role selected\n");
         }
         else if (e.getSource() == bEndTurn) {
            System.out.println("End turn selected\n");
         }
         else if (e.getSource() == bEndGame) {
            System.out.println("End game selected\n");
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


   public static void main(String[] args) {
  
      BoardLayersListener board = new BoardLayersListener();
      board.setVisible(true);
    
      // Take input from the user about number of players
      JOptionPane.showInputDialog(board, "How many players?"); 
   }
}