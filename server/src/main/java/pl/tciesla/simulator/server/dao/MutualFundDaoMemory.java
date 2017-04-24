package pl.tciesla.simulator.server.dao;

import pl.tciesla.simulator.server.builder.MutualFundBuilder;
import pl.tciesla.simulator.server.domain.MutualFund;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Simple in-memory implementation of Mutual Funds Data Access Object
 */
@Singleton
@Startup
public class MutualFundDaoMemory implements MutualFundDao {

    private Map<Long, MutualFund> mutualFunds = new HashMap<>();

    @PostConstruct
    public void initialize() {
        initializeFund(1L, "Fund 1", MutualFund.Category.MONEY_MARKET);
        initializeFund(2L, "Fund 2", MutualFund.Category.BOND);
        initializeFund(3L, "Fund 3", MutualFund.Category.STABLE_GROWTH);
        initializeFund(4L, "Fund 4", MutualFund.Category.BALANCED);
        initializeFund(5L, "Fund 5", MutualFund.Category.STOCK);
    }

    private void initializeFund(long id, String name, MutualFund.Category category) {
        MutualFund fund = MutualFundBuilder.aMutualFundBuilder()
                .withId(id)
                .withName(name)
                .withCategory(category)
                .build();
        mutualFunds.put(fund.getId(), fund);
    }

    @Override
    public MutualFund fetch(long id) {
        return mutualFunds.get(id);
    }

    @Override
    public List<MutualFund> fetchAll() {
        return mutualFunds.values().stream().collect(Collectors.toList());
    }

    @Override
    public void persist(MutualFund mutualFund) {
        mutualFunds.put(mutualFund.getId(), mutualFund);
    }

    @Override
    public void persistAll(List<MutualFund> mutualFunds) {
        mutualFunds.forEach(e -> persist(e));
    }
}
