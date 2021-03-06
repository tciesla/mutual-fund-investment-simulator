package pl.tciesla.mutual.fund.simulator.server.service;

import pl.tciesla.mutual.fund.simulator.server.model.MutualFund;

@FunctionalInterface
public interface FundValuationStrategy {

    void updateValuation(MutualFund mutualFund);
}
