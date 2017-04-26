package pl.tciesla.mutual.fund.simulator.server.repository;

import pl.tciesla.mutual.fund.simulator.server.domain.MutualFund;

import java.util.List;
import java.util.Optional;

public interface MutualFundRepository {
    Optional<MutualFund> find(long id);
    List<MutualFund> findAll();
    void save(MutualFund mutualFund);
}
