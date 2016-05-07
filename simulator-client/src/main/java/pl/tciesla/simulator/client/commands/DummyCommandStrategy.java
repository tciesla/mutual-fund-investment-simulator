package pl.tciesla.simulator.client.commands;

import pl.tciesla.simulator.client.CommandStrategy;

/**
 * Strategy enables to print message for incorrect commands.
 */
public class DummyCommandStrategy implements CommandStrategy {

    @Override
    public void execute() {
        System.out.println("Incorrect command, please try \"help\" command.");
    }

    @Override
    public String getDescription() {
        return "print incorrect command message";
    }
}
