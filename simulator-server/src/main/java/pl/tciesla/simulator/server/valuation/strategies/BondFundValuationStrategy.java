package pl.tciesla.simulator.server.valuation.strategies;

import pl.tciesla.simulator.commons.domain.MutualFund;
import pl.tciesla.simulator.server.valuation.util.RandomValuationUpdater;
import pl.tciesla.simulator.server.valuation.FundValuationStrategy;

/**
 * Updates valuation for bond mutual funds.
 */
public class BondFundValuationStrategy implements FundValuationStrategy {

    private static final int CHANGE_FROM = -15;
    private static final int CHANGE_TO = 60;

    @Override
    public void updateValuation(MutualFund mutualFund) {
        RandomValuationUpdater.updateValuation(mutualFund, CHANGE_FROM, CHANGE_TO);
    }
}
