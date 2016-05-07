package pl.tciesla.simulator.server.valuation;

import org.junit.Test;
import pl.tciesla.simulator.commons.constant.FundCategory;
import pl.tciesla.simulator.server.valuation.strategies.*;

import static org.assertj.core.api.Assertions.assertThat;

public class FundValuationStrategiesTest {

    @Test
    public void shouldContainMoneyMarketValuationStrategy() throws Exception {
        // when
        FundValuationStrategy strategy = FundValuationStrategies.getStrategy(FundCategory.MONEY_MARKET);
        // then
        assertThat(strategy).isInstanceOf(MoneyMarketFundValuationStrategy.class);
    }

    @Test
    public void shouldContainBondValuationStrategy() throws Exception {
        // when
        FundValuationStrategy strategy = FundValuationStrategies.getStrategy(FundCategory.BOND);
        // then
        assertThat(strategy).isInstanceOf(BondFundValuationStrategy.class);
    }

    @Test
    public void shouldContainStableGrowthValuationStrategy() throws Exception {
        // when
        FundValuationStrategy strategy = FundValuationStrategies.getStrategy(FundCategory.STABLE_GROWTH);
        // then
        assertThat(strategy).isInstanceOf(StableGrowthFundValuationStrategy.class);
    }

    @Test
    public void shouldContainBalancedValuationStrategy() throws Exception {
        // when
        FundValuationStrategy strategy = FundValuationStrategies.getStrategy(FundCategory.BALANCED);
        // then
        assertThat(strategy).isInstanceOf(BalancedFundValuationStrategy.class);
    }

    @Test
    public void shouldContainStockValuationStrategy() throws Exception {
        // when
        FundValuationStrategy strategy = FundValuationStrategies.getStrategy(FundCategory.STOCK);
        // then
        assertThat(strategy).isInstanceOf(StockFundValuationStrategy.class);
    }

    @Test
    public void shouldReturnDummyStrategyWhenCategoryIsNull() throws Exception {
        // when
        FundValuationStrategy strategy = FundValuationStrategies.getStrategy(null);
        // then
        assertThat(strategy).isInstanceOf(DummyValuationStrategy.class);
    }

}