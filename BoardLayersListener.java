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

   // JPanels
   // containers that hold groups of components
   JPanel boardPanel;
   JPanel consolePanel;
   JPanel buttonPanel;
  
   //JButtons
   JButton bAct;  // act
   JButton bRehearse;  // rehearse
   JButton bMove;  // move
   // upgrade
   // take role
   // end turn
   // end game

   // JLayered Pane
   JLayeredPane bPane;

   // JSplitPane
   JSplitPane splitPane;

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
      setSize(icon.getIconWidth() + 400, icon.getIconHeight());
      
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


      // create the console
      consolePanel = new JPanel();
      consolePanel.setLayout(new BorderLayout());
      consoleArea = new JTextArea();
      consoleArea.setEditable(false);  // user cannot edit console
      consolePanel.add(new JScrollPane(consoleArea), BorderLayout.CENTER);

      // create button panel
      buttonPanel = new JPanel();
      // adjust the grid layout based on num of buttons
      buttonPanel.setLayout(new GridLayout(2, 4));

      // create player buttons
      bAct = new JButton("ACT");
      bAct.setBackground(Color.white);
      bAct.setBounds(icon.getIconWidth() + 10, 30, 100, 20);
      bAct.addMouseListener(new boardMouseListener());
        
      bRehearse = new JButton("REHEARSE");
      bRehearse.setBackground(Color.white);
      bRehearse.setBounds(icon.getIconWidth() + 10, 60, 100, 20);
      bRehearse.addMouseListener(new boardMouseListener());
        
      bMove = new JButton("MOVE");
      bMove.setBackground(Color.white);
      bMove.setBounds(icon.getIconWidth() + 10, 90, 100, 20);
      bMove.addMouseListener(new boardMouseListener());
 
      // Place the action buttons in the top layer
      bPane.add(bAct, 2);
      bPane.add(bRehearse, 2);
      bPane.add(bMove, 2);

      // add buttonPanel to the bottom of consolePanel
      consolePanel.add(buttonPanel, BorderLayout.SOUTH);

      // create a split pane to hold the board + console panels
      splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, boardPanel, consolePanel);
      // set the split location between the board and console/buttons
      splitPane.setDividerLocation(icon.getIconWidth());



      add(splitPane);  // add the split pane to the frame
      setVisible(true);  // show the frame
   }
  

   // This class implements Mouse Events
   class boardMouseListener implements MouseListener{
  
      // Code for the different button clicks
      public void mouseClicked(MouseEvent e) {
         
         if (e.getSource()== bAct){
            playerLabel.setVisible(true);
            System.out.println("Acting is Selected\n");
         }
         else if (e.getSource()== bRehearse){
            System.out.println("Rehearse is Selected\n");
         }
         else if (e.getSource()== bMove){
            System.out.println("Move is Selected\n");
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