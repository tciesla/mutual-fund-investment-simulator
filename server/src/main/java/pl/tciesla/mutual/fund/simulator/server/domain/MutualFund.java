package pl.tciesla.mutual.fund.simulator.server.domain;

import javax.xml.bind.annotation.*;
import java.math.BigDecimal;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = {"id", "name", "category", "valuation"})
public class MutualFund {

    private static final BigDecimal INITIAL_VALUATION = BigDecimal.valueOf(100.00);

    @XmlElement(required = true)
    private Long id;
    @XmlElement(required = true)
    private String name;
    @XmlElement(required = true)
    private Category category;
    @XmlElement(required = true)
    private BigDecimal valuation;

    public enum Category {
        MONEY_MARKET, BOND, STABLE_GROWTH, BALANCED, STOCK
    }

    public static Builder builder() {
        Builder builder = new Builder();
        builder.valuation(INITIAL_VALUATION);
        return builder;
    }

    public static class Builder {
        private MutualFund mutualFund = new MutualFund();

        public Builder id(Long id) {
            mutualFund.id = id;
            return this;
        }

        public Builder name(String name) {
            mutualFund.name = name;
            return this;
        }

        public Builder category(Category category) {
            mutualFund.category = category;
            return this;
        }

        public Builder valuation(BigDecimal valuation) {
            mutualFund.valuation = valuation;
            return this;
        }

        public MutualFund build() {
            return mutualFund;
        }
    }

    public void updateValuation(BigDecimal valuation) {
        this.valuation = valuation;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Category getCategory() {
        return category;
    }

    public BigDecimal getValuation() {
        return valuation;
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
