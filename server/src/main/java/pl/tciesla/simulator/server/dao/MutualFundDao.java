package pl.tciesla.simulator.server.dao;

import pl.tciesla.simulator.server.domain.MutualFund;

import java.util.List;

/**
 * Data Access Object for Mutual Funds
 */
public interface MutualFundDao {
    MutualFund fetch(long id);
    List<MutualFund> fetchAll();
    void persist(MutualFund mutualFund);
    void persistAll(List<MutualFund> mutualFunds);
}
