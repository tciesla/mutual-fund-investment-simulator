package pl.tciesla.simulator.client.commands;


import pl.tciesla.simulator.client.ClientSingleton;
import pl.tciesla.simulator.client.CommandStrategy;
import pl.tciesla.simulator.client.Defaults;

import javax.ws.rs.core.MediaType;

/**
 * Strategy enables to print information about actual wallet valuation.
 */
public class ValuationCommandStrategy implements CommandStrategy {

    @Override
    public void execute() {
        String valuation = ClientSingleton.getClient()
                .resource("http://localhost:8080/simulator/customer/" + Defaults.getUsername() + "/valuation")
                .accept(MediaType.TEXT_PLAIN)
                .get(String.class);
        System.out.println("Current  wallet valuation: " + valuation + " PLN");
    }

    @Override
    public String getDescription() {
        return "print customer wallet valuation";
    }
}
