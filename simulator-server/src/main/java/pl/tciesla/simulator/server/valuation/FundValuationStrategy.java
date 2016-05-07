package pl.tciesla.simulator.server.valuation;

import pl.tciesla.simulator.commons.domain.MutualFund;

/**
 * Interface for update valuation strategies.
 */
public interface FundValuationStrategy {
    void updateValuation(MutualFund mutualFund);
}
