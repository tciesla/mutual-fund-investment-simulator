package pl.tciesla.simulator.commons.builder;

import org.junit.Test;
import pl.tciesla.simulator.commons.constant.FundCategory;
import pl.tciesla.simulator.commons.domain.MutualFund;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class MutualFundBuilderTest {

    @Test
    public void shouldSetDefaultValuation() throws Exception {
        // when
        MutualFund mutualFund = MutualFundBuilder.aMutualFundBuilder().build();
        // then
        assertThat(mutualFund.getValuation()).isEqualTo(new BigDecimal("100.00"));
    }

    @Test
    public void shouldSetCorrectId() throws Exception {
        // given
        long exampleId = 7L;
        // when
        MutualFund mutualFund = MutualFundBuilder.aMutualFundBuilder().withId(exampleId).build();
        // then
        assertThat(mutualFund.getId()).isEqualTo(exampleId);
    }

    @Test
    public void shouldSetCorrectName() throws Exception {
        // given
        String exampleName = "example";
        // when
        MutualFund mutualFund = MutualFundBuilder.aMutualFundBuilder().withName(exampleName).build();
        // then
        assertThat(mutualFund.getName()).isEqualTo(exampleName);
    }

    @Test
    public void shouldSetCorrectFundCategory() throws Exception {
        // when
        MutualFund mutualFund = MutualFundBuilder.aMutualFundBuilder().withCategory(FundCategory.STOCK).build();
        // then
        assertThat(mutualFund.getCategory()).isEqualTo(FundCategory.STOCK);
    }

    @Test
    public void shouldSetCorrectValuation() throws Exception {
        // given
        BigDecimal exampleValuation = new BigDecimal("1200.00");
        // when
        MutualFund mutualFund = MutualFundBuilder.aMutualFundBuilder().withValuation(exampleValuation).build();
        // then
        assertThat(mutualFund.getValuation()).isEqualTo(exampleValuation);
    }

}