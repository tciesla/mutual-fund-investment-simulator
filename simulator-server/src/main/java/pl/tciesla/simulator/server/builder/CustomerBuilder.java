package pl.tciesla.simulator.server.builder;

import pl.tciesla.simulator.server.domain.Customer;
import pl.tciesla.simulator.server.domain.FundShares;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class CustomerBuilder {

    private static final String INITIAL_CUSTOMER_CASH_AMOUNT = "1000.00";

    private String username;
    private BigDecimal cash = new BigDecimal(INITIAL_CUSTOMER_CASH_AMOUNT);
    private Map<FundShares, Long> fundShares = new HashMap<>();

    public static CustomerBuilder aCustomerBuilder() {
        return new CustomerBuilder();
    }

    public CustomerBuilder withName(String username) {
        this.username = username;
        return this;
    }

    public CustomerBuilder withCash(BigDecimal cash) {
        this.cash = cash;
        return this;
    }

    public CustomerBuilder withFundShares(Map<FundShares, Long> fundShares) {
        this.fundShares = fundShares;
        return this;
    }

    public Customer build() {
        Customer customer = new Customer();
        customer.setUsername(username);
        customer.setCash(cash);
        customer.setFundShares(fundShares);
        return customer;
    }
}
