package gui;

public class Deadwood {

    public static void main(String[] args) {
        // initialize the components of the game

        Board boardStats = new Board();
        BoardCreator fill = new BoardCreator();
        boardStats = fill.parseBoard();

        View view = new View();
        Moderator moderator = new Moderator(null, boardStats, view);

        BoardLayersListener boardView = new BoardLayersListener(moderator);
        moderator.setBoardView(boardView);

        // start the game
        view.startGame();
        moderator.startGame();
    }
}