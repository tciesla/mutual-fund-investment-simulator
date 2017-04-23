package pl.tciesla.simulator.server.valuation;

import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.tciesla.simulator.server.constant.FundCategory;

import java.util.HashMap;
import java.util.Map;

public class FundValuationStrategies {

    private static final Logger logger = LoggerFactory.getLogger(FundValuationStrategies.class);

    private static final Map<FundCategory, FundValuationStrategy> strategies = new HashMap<>();

    static {
        strategies.put(FundCategory.MONEY_MARKET, fund -> RandomValuationUpdater.updateFromRange(fund, -5, 40));
        strategies.put(FundCategory.BOND, fund -> RandomValuationUpdater.updateFromRange(fund, -15, 60));
        strategies.put(FundCategory.STABLE_GROWTH, fund -> RandomValuationUpdater.updateFromRange(fund, -65, 85));
        strategies.put(FundCategory.BALANCED, fund -> RandomValuationUpdater.updateFromRange(fund, -80, 96));
        strategies.put(FundCategory.STOCK, fund -> RandomValuationUpdater.updateFromRange(fund, -100, 110));
    }

    public static FundValuationStrategy getStrategy(FundCategory fundCategory) {
        Preconditions.checkNotNull(fundCategory, "fundCategory == null");
        return strategies.containsKey(fundCategory) ? strategies.get(fundCategory) :
                fund -> logger.error("valuation strategy for fundCategory[{}] do not exists", fund.getCategory());
    }

}
