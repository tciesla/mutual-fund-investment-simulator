package pl.tciesla.simulator.client.commands;

import pl.tciesla.simulator.client.CommandStrategy;

import java.util.Scanner;

/**
 * Strategy enables to close simulator application.
 */
public class ExitCommandStrategy implements CommandStrategy {

    @Override
    public void execute() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Are you sure to close application? [Y/N]: ");
        String line = scanner.nextLine().trim().toLowerCase();
        if (line.equals("y")) { System.exit(0); }
    }

    @Override
    public String getDescription() {
        return "close investment simulator";
    }
}
