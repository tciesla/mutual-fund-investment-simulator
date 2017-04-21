package pl.tciesla.simulator.server.valuation;

import pl.tciesla.simulator.commons.constant.FundCategory;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class FundValuationStrategies {

    private static final Logger log = Logger.getLogger(FundValuationStrategies.class.getName());

    private static final Map<FundCategory, FundValuationStrategy> strategies = new HashMap<>();

    static {
        strategies.put(FundCategory.MONEY_MARKET, fund -> RandomValuationUpdater.updateFromRange(fund, -5, 40));
        strategies.put(FundCategory.BOND, fund -> RandomValuationUpdater.updateFromRange(fund, -15, 60));
        strategies.put(FundCategory.STABLE_GROWTH, fund -> RandomValuationUpdater.updateFromRange(fund, -65, 85));
        strategies.put(FundCategory.BALANCED, fund -> RandomValuationUpdater.updateFromRange(fund, -80, 96));
        strategies.put(FundCategory.STOCK, fund -> RandomValuationUpdater.updateFromRange(fund, -100, 110));
        strategies.put(FundCategory.DUMMY, fund -> log.warning("Invoked valuation process for incorrect fund category."));
    }

    public static FundValuationStrategy getStrategy(FundCategory category) {
        return category == null || !strategies.containsKey(category) ?
            strategies.get(FundCategory.DUMMY) : strategies.get(category);
    }

}
