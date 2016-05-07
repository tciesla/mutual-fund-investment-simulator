package pl.tciesla.simulator.server.valuation.strategies;

import pl.tciesla.simulator.commons.domain.MutualFund;
import pl.tciesla.simulator.server.valuation.FundValuationStrategy;

import java.util.logging.Logger;

/**
 * Print warning log if mutual fund category not exists.
 */
public class DummyValuationStrategy implements FundValuationStrategy {

    private static final Logger log = Logger.getLogger(DummyValuationStrategy.class.getName());

    @Override
    public void updateValuation(MutualFund mutualFund) {
        log.warning("Invoked valuation process for incorrect fund category.");
    }
}
