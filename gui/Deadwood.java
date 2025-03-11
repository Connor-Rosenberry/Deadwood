package gui;

public class Deadwood {

    public static void main(String[] args) {
        // initialize the components of the game

        Board boardStats = new Board();
        BoardCreator fill = new BoardCreator();
        boardStats = fill.parseBoard();

        Moderator moderator = new Moderator(null, boardStats);

        BoardLayersListener boardView = new BoardLayersListener(moderator);
        moderator.setBoardView(boardView);

        moderator.startGame();
    }
}