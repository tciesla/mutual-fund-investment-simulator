package pl.tciesla.mutual.fund.simulator.server.service;

import org.junit.Test;

public class FundValuationStrategiesTest {

    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionWhenMutualFundCategoryIsNull() throws Exception {
        // when
        FundValuationStrategies.getStrategy(null);
    }

}