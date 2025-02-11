public class Deadwood {

    public static void main(String[] args) {
        // initialize the components of the game
        Board board = new Board();
        View view = new View();
        Moderator moderator = new Moderator(board, view);

        // start the game
        view.startGame();
        moderator.startGame();
    }
}