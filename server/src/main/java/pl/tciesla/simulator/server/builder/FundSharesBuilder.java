package pl.tciesla.simulator.server.builder;

import pl.tciesla.simulator.server.constant.ShareType;
import pl.tciesla.simulator.server.domain.FundShares;

public class FundSharesBuilder {

    private Long fundId;
    private ShareType type;

    public static FundSharesBuilder aFundSharesBuilder() {
        return new FundSharesBuilder();
    }

    public FundSharesBuilder withFundId(Long fundId) {
        this.fundId = fundId;
        return this;
    }

    public FundSharesBuilder withType(ShareType type) {
        this.type = type;
        return this;
    }

    public FundShares build() {
        FundShares fundShares = new FundShares();
        fundShares.setFundId(fundId);
        fundShares.setType(type);
        return fundShares;
    }
}
