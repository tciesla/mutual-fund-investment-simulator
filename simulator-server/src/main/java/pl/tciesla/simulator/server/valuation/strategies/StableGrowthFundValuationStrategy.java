package pl.tciesla.simulator.server.valuation.strategies;

import pl.tciesla.simulator.commons.domain.MutualFund;
import pl.tciesla.simulator.server.valuation.util.RandomValuationUpdater;
import pl.tciesla.simulator.server.valuation.FundValuationStrategy;

/**
 * Updates valuation for stable growth mutual funds.
 */
public class StableGrowthFundValuationStrategy implements FundValuationStrategy {

    private static final int CHANGE_FROM = -65;
    private static final int CHANGE_TO = 85;

    @Override
    public void updateValuation(MutualFund mutualFund) {
        RandomValuationUpdater.updateValuation(mutualFund, CHANGE_FROM, CHANGE_TO);
    }
}
