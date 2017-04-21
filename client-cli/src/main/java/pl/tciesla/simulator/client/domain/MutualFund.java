package pl.tciesla.simulator.client.domain;

import pl.tciesla.simulator.client.constant.FundCategory;

import javax.xml.bind.annotation.*;
import java.math.BigDecimal;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"id", "name", "category", "valuation"})
public class MutualFund {

    @XmlElement(required = true)
    private Long id;
    @XmlElement(required = true)
    private String name;
    @XmlElement(required = true)
    private FundCategory category;
    @XmlElement(required = true)
    private BigDecimal valuation;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FundCategory getCategory() {
        return category;
    }

    public void setCategory(FundCategory category) {
        this.category = category;
    }

    public BigDecimal getValuation() {
        return valuation;
    }

    public void setValuation(BigDecimal valuation) {
        this.valuation = valuation;
    }

    @Override
    public String toString() {
        return "MutualFund{" + id + ", " + name + ", " + category + ", " + valuation + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        MutualFund otherMutualFund = (MutualFund) o;
        return id.equals(otherMutualFund.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
