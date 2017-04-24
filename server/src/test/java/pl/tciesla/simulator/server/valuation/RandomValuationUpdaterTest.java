package pl.tciesla.simulator.server.valuation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import pl.tciesla.simulator.server.domain.MutualFund;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class RandomValuationUpdaterTest {

    @Mock
    private MutualFund mutualFund;

    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionWhenMutualFundIsNull() throws Exception {
        // when
        RandomValuationUpdater.updateFromRange(null, -10, 10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenCentsFromAreAboveZero() throws Exception {
        // when
        RandomValuationUpdater.updateFromRange(mutualFund, 1, 10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenCentsToBelowZero() throws Exception {
        // when
        RandomValuationUpdater.updateFromRange(mutualFund, -10, -1);
    }

    @Test
    public void shouldUpdateMutualFundValuationInGivenRange() throws Exception {
        // given
        MutualFund mutualFund = MutualFund.builder()
                .valuation(BigDecimal.valueOf(10.00))
                .build();
        // when
        RandomValuationUpdater.updateFromRange(mutualFund, -10, 10);
        // then
        assertThat(mutualFund.getValuation())
                .isLessThanOrEqualTo(BigDecimal.valueOf(10.10))
                .isGreaterThanOrEqualTo(BigDecimal.valueOf(9.90));
    }

}