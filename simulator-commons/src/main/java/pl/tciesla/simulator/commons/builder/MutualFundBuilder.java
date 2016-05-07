package pl.tciesla.simulator.commons.builder;

import pl.tciesla.simulator.commons.constant.FundCategory;
import pl.tciesla.simulator.commons.domain.MutualFund;

import java.math.BigDecimal;

public class MutualFundBuilder {

    private static final String INITIAL_FUND_VALUATION = "100.00";

    private Long id;
    private String name;
    private FundCategory category;
    private BigDecimal valuation = new BigDecimal(INITIAL_FUND_VALUATION);

    public static MutualFundBuilder aMutualFundBuilder() {
        return new MutualFundBuilder();
    }

    public MutualFundBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public MutualFundBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public MutualFundBuilder withCategory(FundCategory category) {
        this.category = category;
        return this;
    }

    public MutualFundBuilder withValuation(BigDecimal valuation) {
        this.valuation = valuation;
        return this;
    }

    public MutualFund build() {
        MutualFund mutualFund = new MutualFund();
        mutualFund.setId(id);
        mutualFund.setName(name);
        mutualFund.setCategory(category);
        mutualFund.setValuation(valuation);
        return mutualFund;
    }
}
