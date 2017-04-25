package pl.tciesla.simulator.server.repository;

import pl.tciesla.simulator.server.domain.MutualFund;

import java.util.List;
import java.util.Optional;

public interface MutualFundRepository {
    Optional<MutualFund> find(long id);
    List<MutualFund> findAll();
    void save(MutualFund mutualFund);
}
