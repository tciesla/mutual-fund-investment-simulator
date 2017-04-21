package pl.tciesla.simulator.server.valuation;

import pl.tciesla.simulator.commons.domain.MutualFund;

@FunctionalInterface
public interface FundValuationStrategy {

    void updateValuation(MutualFund mutualFund);
}
