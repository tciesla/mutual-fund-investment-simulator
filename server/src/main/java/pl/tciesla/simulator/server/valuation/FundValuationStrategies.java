package pl.tciesla.simulator.server.valuation;

import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.tciesla.simulator.server.domain.MutualFund;

import java.util.HashMap;
import java.util.Map;

public class FundValuationStrategies {

    private static final Logger logger = LoggerFactory.getLogger(FundValuationStrategies.class);

    private static final Map<MutualFund.Category, FundValuationStrategy> strategies = new HashMap<>();

    static {
        strategies.put(MutualFund.Category.MONEY_MARKET, fund -> RandomValuationUpdater.updateFromRange(fund, -5, 40));
        strategies.put(MutualFund.Category.BOND, fund -> RandomValuationUpdater.updateFromRange(fund, -15, 60));
        strategies.put(MutualFund.Category.STABLE_GROWTH, fund -> RandomValuationUpdater.updateFromRange(fund, -65, 85));
        strategies.put(MutualFund.Category.BALANCED, fund -> RandomValuationUpdater.updateFromRange(fund, -80, 96));
        strategies.put(MutualFund.Category.STOCK, fund -> RandomValuationUpdater.updateFromRange(fund, -100, 110));
    }

    public static FundValuationStrategy getStrategy(MutualFund.Category category) {
        Preconditions.checkNotNull(category, "category == null");
        return strategies.containsKey(category) ? strategies.get(category) :
                fund -> logger.error("valuation strategy for fund category[{}] do not exists", fund.getCategory());
    }

}
