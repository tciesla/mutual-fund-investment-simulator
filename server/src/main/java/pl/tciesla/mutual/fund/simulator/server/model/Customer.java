package pl.tciesla.mutual.fund.simulator.server.model;

import com.google.common.collect.Maps;

import javax.xml.bind.annotation.*;
import java.math.BigDecimal;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

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
            checkNotNull(username, "username == null");
            checkArgument(!username.isEmpty(), "username is empty");

            customer.username = username;
            return this;
        }

        public Builder cash(BigDecimal cash) {
            checkNotNull(cash, "cash == null");
            checkArgument(cash.compareTo(BigDecimal.ZERO) >= 0, "cash is negative");

            customer.cash = cash;
            return this;
        }

        public Builder fundShares(Map<Long, Long> fundShares) {
            customer.fundShares = checkNotNull(fundShares, "fundShares == null");
            return this;
        }

        public Customer build() {
            if (customer.username == null) {
                throw new NullPointerException("username == null");
            }
            return customer;
        }
    }

    public boolean hasEnoughCash(BigDecimal requiredCash) {
        checkNotNull(requiredCash, "requiredCash == null");
        return cash.compareTo(requiredCash) >= 0;
    }

    public boolean hasEnoughShares(long fundId, long amount) {
        return fundShares.getOrDefault(fundId, 0L) >= amount;
    }

    public void buy(long fundId, long amount, BigDecimal cost) {
        checkArgument(cash.compareTo(cost) >= 0, "cost too high");

        cash = cash.subtract(cost);
        Long oldAmount = fundShares.getOrDefault(fundId, 0L);
        fundShares.put(fundId, oldAmount + amount);
    }

    public void sell(long fundId, long amount, BigDecimal transactionProfit) {
        checkArgument(fundShares.containsKey(fundId), "customer does not have any fund shares");
        checkArgument(fundShares.get(fundId).compareTo(amount) >= 0, "sharesAmount too high");

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
