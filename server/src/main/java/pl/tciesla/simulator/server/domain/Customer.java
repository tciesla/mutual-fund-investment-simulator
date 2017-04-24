package pl.tciesla.simulator.server.domain;

import com.google.common.collect.Maps;

import javax.xml.bind.annotation.*;
import java.math.BigDecimal;
import java.util.Map;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"username", "cash", "fundShares"})
public class Customer {

    private static final BigDecimal INITIAL_CASH = BigDecimal.valueOf(1000.00);

    @XmlElement(required = true)
    private String username;
    @XmlElement(required = true)
    private BigDecimal cash;
    @XmlElement(required = true)
    private Map<Long, Long> fundShares;

    public static Builder builder() {
        Builder builder = new Builder();
        builder.cash(INITIAL_CASH);
        builder.fundShares(Maps.newHashMap());
        return builder;
    }

    public static class Builder {
        private Customer customer = new Customer();

        public Builder username(String username) {
            customer.username = username;
            return this;
        }

        public Builder cash(BigDecimal cash) {
            customer.cash = cash;
            return this;
        }

        public Builder fundShares(Map<Long, Long> fundShares) {
            customer.fundShares = fundShares;
            return this;
        }

        public Customer build() {
            return customer;
        }
    }

    public boolean hasEnoughCash(BigDecimal totalCost) {
        return cash.compareTo(totalCost) >= 0;
    }

    public boolean hasEnoughShares(Long fundId, Long amount) {
        return fundShares.getOrDefault(fundId, 0L) >= amount;
    }

    public void buy(Long fundId, Long amount, BigDecimal transactionCost) {
        cash = cash.subtract(transactionCost);
        Long oldAmount = fundShares.getOrDefault(fundId, 0L);
        fundShares.put(fundId, oldAmount + amount);
    }

    public void sell(Long fundId, Long amount, BigDecimal transactionProfit) {
        cash = cash.add(transactionProfit);
        long oldSharesAmount = fundShares.get(fundId);
        fundShares.put(fundId, oldSharesAmount - amount);
        if (fundShares.get(fundId) == 0) {
            fundShares.remove(fundId);
        }
    }

    public String getUsername() {
        return username;
    }

    public BigDecimal getCash() {
        return cash;
    }

    public Map<Long, Long> getFundShares() {
        return fundShares;
    }

    @Override
    public String toString() {
        return "Customer{" + username + ", " + cash + ", " + fundShares + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        Customer otherCustomer = (Customer) o;
        return username.equals(otherCustomer.username);
    }

    @Override
    public int hashCode() {
        return username.hashCode();
    }
}
