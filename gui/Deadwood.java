package gui;

public class Deadwood {

    public static void main(String[] args) {
        // initialize the components of the game

        // set up the board
        Board board = new Board();
        BoardCreator fill = new BoardCreator();
        board = fill.parseBoard();
        
        View view = new View();
        Moderator moderator = new Moderator(board, view);

        // start the game
        view.startGame();
        moderator.startGame();
    }
}