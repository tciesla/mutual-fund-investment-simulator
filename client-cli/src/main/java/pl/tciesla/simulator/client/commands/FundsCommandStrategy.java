package pl.tciesla.simulator.client.commands;

import pl.tciesla.simulator.client.ClientSingleton;
import pl.tciesla.simulator.client.CommandStrategy;
import pl.tciesla.simulator.client.domain.MutualFunds;

import javax.ws.rs.core.MediaType;

/**
 * Strategy enables to print available mutual funds in simulator.
 */
public class FundsCommandStrategy implements CommandStrategy {

    @Override
    public void execute() {
        MutualFunds mutualFunds = ClientSingleton.getClient()
                .resource("http://localhost:8080/simulator/funds")
                .accept(MediaType.APPLICATION_XML)
                .get(MutualFunds.class);
        System.out.println("Available mutual funds:");
        mutualFunds.getMutualFunds().stream().forEach(fund -> {
            System.out.print("id: " + fund.getId() + ", ");
            System.out.print("name: " + fund.getName() + ", ");
            System.out.print("valuation: " + fund.getValuation() + ", ");
            System.out.print("category: " + fund.getCategory());
            System.out.println();
        });
    }

    @Override
    public String getDescription() {
        return "prints information about available mutual funds";
    }
}
