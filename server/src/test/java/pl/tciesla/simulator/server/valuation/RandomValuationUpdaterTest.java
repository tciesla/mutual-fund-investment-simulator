package pl.tciesla.simulator.server.valuation;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.test.annotation.Repeat;
import pl.tciesla.simulator.server.builder.MutualFundBuilder;
import pl.tciesla.simulator.server.constant.FundCategory;
import pl.tciesla.simulator.server.domain.MutualFund;

import java.math.BigDecimal;

public class RandomValuationUpdaterTest {

    @Test @Repeat(10)
    public void shouldUpdateExampleMutualFundValuation() throws Exception {
        // given
        MutualFund mutualFund = MutualFundBuilder.aMutualFundBuilder()
                .withCategory(FundCategory.STOCK)
                .withValuation(new BigDecimal("5.00"))
                .build();
        // when
        RandomValuationUpdater.updateFromRange(mutualFund, -10, 10);
        // then
        BigDecimal resultValuation = mutualFund.getValuation();
        Assertions.assertThat(resultValuation)
                .isLessThanOrEqualTo(new BigDecimal("5.10"))
                .isGreaterThanOrEqualTo(new BigDecimal("4.90"));
    }

}