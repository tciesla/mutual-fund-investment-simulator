package pl.tciesla.simulator.client.commands;

import pl.tciesla.simulator.client.ClientSingleton;
import pl.tciesla.simulator.client.CommandStrategy;
import pl.tciesla.simulator.client.Defaults;

/**
 * Strategy enables to remove customer from simulator.
 */
public class DeleteCommandStrategy implements CommandStrategy {

    @Override
    public void execute() {

        String response = ClientSingleton.getClient()
                .resource("http://localhost:8080/simulator/customer/" + Defaults.getUsername())
                .delete(String.class);
        System.out.println(response);
    }

    @Override
    public String getDescription() {
        return "delete customer from simulator";
    }
}
