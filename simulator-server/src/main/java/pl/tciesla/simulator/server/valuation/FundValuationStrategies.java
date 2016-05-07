package pl.tciesla.simulator.server.valuation;

import pl.tciesla.simulator.commons.constant.FundCategory;
import pl.tciesla.simulator.server.valuation.strategies.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Stores all possible fund category to strategies mappings.
 */
public class FundValuationStrategies {

    private static final Map<FundCategory, FundValuationStrategy> strategies = new HashMap<>();

    static {
        strategies.put(FundCategory.MONEY_MARKET, new MoneyMarketFundValuationStrategy());
        strategies.put(FundCategory.BOND, new BondFundValuationStrategy());
        strategies.put(FundCategory.STABLE_GROWTH, new StableGrowthFundValuationStrategy());
        strategies.put(FundCategory.BALANCED, new BalancedFundValuationStrategy());
        strategies.put(FundCategory.STOCK, new StockFundValuationStrategy());
    }

    /**
     * Return update valuation strategy for fund category.
     */
    public static FundValuationStrategy getStrategy(FundCategory category) {
        if (category == null || !strategies.containsKey(category)) {
            return new DummyValuationStrategy();
        }
        return strategies.get(category);
    }
}
