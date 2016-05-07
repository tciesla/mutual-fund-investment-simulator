package pl.tciesla.simulator.commons.domain;

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
    private Map<FundShares, Long> fundShares;

    public Customer() {}

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

    public Map<FundShares, Long> getFundShares() {
        return fundShares;
    }

    public void setFundShares(Map<FundShares, Long> fundShares) {
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
