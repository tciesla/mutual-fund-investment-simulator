package pl.tciesla.simulator.client.commands;

import pl.tciesla.simulator.client.ClientSingleton;
import pl.tciesla.simulator.client.CommandStrategy;
import pl.tciesla.simulator.client.Defaults;
import pl.tciesla.simulator.commons.domain.Customer;
import pl.tciesla.simulator.commons.domain.MutualFund;
import pl.tciesla.simulator.commons.domain.MutualFunds;

import javax.ws.rs.core.MediaType;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Strategy enables to print information about customer wallet.
 */
public class WalletCommandStrategy implements CommandStrategy {

    @Override
    public void execute() {
        Customer customer = ClientSingleton.getClient()
                .resource("http://localhost:8080/simulator/customer/" + Defaults.getUsername())
                .accept(MediaType.APPLICATION_XML)
                .get(Customer.class);

        MutualFunds mutualFunds = ClientSingleton.getClient()
                .resource("http://localhost:8080/simulator/funds")
                .accept(MediaType.APPLICATION_XML)
                .get(MutualFunds.class);

        Map<Long, MutualFund> funds = mutualFunds.getMutualFunds().stream()
                .collect(Collectors.toMap(MutualFund::getId, Function.identity()));

        System.out.println("Customer: " + customer.getUsername() + ", Cash: " + customer.getCash() + " PLN");
        System.out.println("Customer mutual fund shares:");
        customer.getFundShares().entrySet().forEach(e -> {
            MutualFund fund = funds.get(e.getKey().getFundId());
            System.out.print("id: " + fund.getId() + ", name: " + fund.getName());
            System.out.print(", type: " + e.getKey().getType() + ", amount: " + e.getValue());
            System.out.print(", valuation: " + fund.getValuation().multiply(new BigDecimal(e.getValue())));
            System.out.println();
        });
    }

    @Override
    public String getDescription() {
        return "print information about customer wallet";
    }
}
