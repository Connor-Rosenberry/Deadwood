public class Dice {

    public int role() {
        return (int) (Math.random() * (7 - 1) + 1);
    }
    
    // public static void main(String args[]) {
    //     Dice dice = new Dice();
    //     int test;
    //     test = dice.role();
    //     System.out.println(test);
    // }
}