package pl.tciesla.simulator.client.commands;

import pl.tciesla.simulator.client.CommandStrategies;
import pl.tciesla.simulator.client.CommandStrategy;

/**
 * Strategy enables to print available commands with descriptions.
 */
public class HelpCommandStrategy implements CommandStrategy {

    @Override
    public void execute() {
        CommandStrategies.getStrategies().entrySet().stream().forEach(command ->
                System.out.println("\"" + command.getKey() + "\": " + command.getValue().getDescription())
        );
    }

    @Override
    public String getDescription() {
        return "shows available simulator commands";
    }
}
