import java.util.Scanner;

public class View {

    public void startGame() {
        System.out.println("Welcome to Deadwood!");
    }

    // display a message to the user
    public void displayMessage(String message) {
        System.out.println(message);
    }

    // get input from the user
    public String getUserInput() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    public int getUserInt() {
        System.out.print("Enter a number: ");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextInt();
    }

}
