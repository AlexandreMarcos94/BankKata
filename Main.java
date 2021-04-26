import bank.Bank;

import java.util.Scanner;

public class Main extends Application {

    private static Scanner s = new Scanner(System.in);

    // Nettoie l'écran des prints précédents
    private static void flushScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void main(String[] args) {

        // Init
        Bank b = new Bank();

        /// Declaration before loop
        boolean endOfSession = false;
        String userInput;
        String name;
        int balance;
        int threshold;


        // Loop
        while (!endOfSession) {

            // Menu display
            System.out.println("\n\nWhat operation do you want to do ?");
            System.out.println("0. See all accounts");
            System.out.println("1. Create a new account");
            System.out.println("2. Change balance on a given account");
            System.out.println("3. Block an account");
            System.out.println("q. Quit\n");

            // Getting primary input
            userInput = s.nextLine();

            // Processing user input
            switch (userInput) {
                case "q" -> {
                    endOfSession = true;
                    b.closeDb();
                }
                case "0" -> System.out.println(b.printAllAccounts());
                case "1" -> {
                    System.out.println("Please enter the name for the account");
                    userInput = s.nextLine();
                    name = userInput;
                    System.out.println("Please enter the balance for the account");
                    userInput = s.nextLine();
                    balance = Integer.parseInt(userInput);
                    System.out.println("Please enter the threshold for the account");
                    userInput = s.nextLine();
                    threshold = Integer.parseInt(userInput);
                    b.createNewAccount(name, balance, threshold);
                }
                case "2" -> {
                    System.out.println("Please enter the name of the account");
                    userInput = s.nextLine();
                    name = userInput;
                    System.out.println("Enter the amount you want to add or withdraw of the account");
                    userInput = s.nextLine();
                    balance = Integer.parseInt(userInput);
                    b.changeBalanceByName(name, balance);
                }
                case "3" -> {
                    System.out.println("Please Enter the name of the account you want to block");
                    userInput = s.nextLine();
                    name = userInput;
                    b.blockAccount(name);
                }
            }
        }

    }
}

