package pl.tciesla.simulator.client;

import java.util.Scanner;

/**
 * Client application main class.
 */
public class ClientApplication {

    /**
     * Client application entry method.
     */
    public static void main(String[] args) {
        printApplicationAboutInformation();
        getUsernameFromUserAndSetAsDefault();
        printApplicationCommands();
        processInfinityCommandLoop();
    }

    /**
     * Prints information about application's name and author.
     */
    private static void printApplicationAboutInformation() {
        System.out.println("***** Investing Simulator by Tomasz Ciesla *****");
    }

    /**
     * Ask customer to insert username and set username as default.
     */
    private static void getUsernameFromUserAndSetAsDefault() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter username [default: " + Defaults.getUsername() + "]: ");
        String username = scanner.nextLine().trim().toLowerCase();
        Defaults.setUsername(username.isEmpty() ? Defaults.getUsername() : username);
    }

    /**
     * Prints available application's commands.
     */
    private static void printApplicationCommands() {
        System.out.println("Available commands:");
        CommandStrategies.getStrategy("help").execute();
    }

    /**
     * Process user commands in infinity interaction loop.
     */
    private static void processInfinityCommandLoop() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print(">>> ");
            String inputLine = scanner.nextLine().trim().toLowerCase();
            CommandStrategies.getStrategy(inputLine).execute();
        }
    }
}
