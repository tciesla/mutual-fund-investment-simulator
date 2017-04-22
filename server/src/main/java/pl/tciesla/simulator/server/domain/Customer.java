package pl.tciesla.simulator.server.domain;

import javax.xml.bind.annotation.*;
import java.math.BigDecimal;
import java.util.Map;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"username", "cash", "fundShares"})
public class Customer {

    @XmlElement(required = true)
    private String username;
    @XmlElement(required = true)
    private BigDecimal cash;
    @XmlElement(required = true)
    private Map<Long, Long> fundShares;

    public Customer() {}

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

    public void setUsername(String username) {
        this.username = username;
    }

    public BigDecimal getCash() {
        return cash;
    }

    public void setCash(BigDecimal cash) {
        this.cash = cash;
    }

    public Map<Long, Long> getFundShares() {
        return fundShares;
    }

    public void setFundShares(Map<Long, Long> fundShares) {
        this.fundShares = fundShares;
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
