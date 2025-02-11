import java.util.Scanner;

public class View {

    public void startGame() {
        System.out.println("Welcome to Deadwood!");
    }

    public void displayMessage(String message) {
        System.out.println(message);
    }

    public String getUserInput() {
        System.out.print("Enter your command: ");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    public int getUserInt() {
        System.out.print("Enter a number: ");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextInt();
    }

}
