package gui;

public class Deadwood {

    public static void main(String[] args) {
        // initialize the components of the game

        // set up the board
        BoardLayersListener boardView = new BoardLayersListener();
        boardView.makeGUI();
        Board boardStats = new Board();
        BoardCreator fill = new BoardCreator();
        boardStats = fill.parseBoard();
        
        View view = new View();
        Moderator moderator = new Moderator(boardView, boardStats, view);

        // start the game
        view.startGame();
        moderator.startGame();
    }
}