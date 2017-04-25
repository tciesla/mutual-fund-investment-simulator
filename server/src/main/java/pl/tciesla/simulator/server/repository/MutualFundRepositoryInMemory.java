package pl.tciesla.simulator.server.repository;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import pl.tciesla.simulator.server.domain.MutualFund;

import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Singleton
@Startup
@SuppressWarnings("unused")
public class MutualFundRepositoryInMemory implements MutualFundRepository {

    private static final Map<Long, MutualFund> mutualFunds = new HashMap<>();

    static {
        mutualFunds.put(1L, createMutualFund(1L, "Easy Money Mutual Fund", MutualFund.Category.MONEY_MARKET));
        mutualFunds.put(2L, createMutualFund(2L, "Gov Bond Mutual Fund", MutualFund.Category.BOND));
        mutualFunds.put(3L, createMutualFund(3L, "XYZ Stable Mutual Fund", MutualFund.Category.STABLE_GROWTH));
        mutualFunds.put(4L, createMutualFund(4L, "ABC Balanced Mutual Fund", MutualFund.Category.BALANCED));
        mutualFunds.put(5L, createMutualFund(5L, "Super Stock Mutual Fund", MutualFund.Category.STOCK));
        mutualFunds.put(6L, createMutualFund(6L, "Extra Stock Mutual Fund", MutualFund.Category.STOCK));
        mutualFunds.put(7L, createMutualFund(7L, "Perfect Stock Mutual Fund", MutualFund.Category.STOCK));
    }

    private static MutualFund createMutualFund(long id, String name, MutualFund.Category category) {
        return MutualFund.builder()
                .id(id)
                .name(name)
                .category(category)
                .build();
    }

    @Override
    public Optional<MutualFund> find(long mutualFundId) {
        return mutualFunds.containsKey(mutualFundId) ?
                Optional.of(mutualFunds.get(mutualFundId)) : Optional.empty();
    }

    @Override
    public List<MutualFund> findAll() {
        return Lists.newArrayList(mutualFunds.values());
    }

    @Override
    public void save(MutualFund mutualFund) {
        Preconditions.checkNotNull(mutualFund, "mutualFund == null");
        mutualFunds.put(mutualFund.getId(), mutualFund);
    }

}
