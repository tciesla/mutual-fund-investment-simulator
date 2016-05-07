package pl.tciesla.simulator.commons.builder;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import pl.tciesla.simulator.commons.constant.ShareType;
import pl.tciesla.simulator.commons.domain.FundShares;

import static org.assertj.core.api.Assertions.assertThat;

public class FundSharesBuilderTest {

    @Test
    public void shouldSetCorrectFundId() throws Exception {
        // given
        long exampleFundId = 5L;
        // when
        FundShares fundShares = FundSharesBuilder.aFundSharesBuilder().withFundId(exampleFundId).build();
        // then
        assertThat(fundShares.getFundId()).isEqualTo(exampleFundId);
    }

    @Test
    public void shouldSetCorrectType() throws Exception {
        // when
        FundShares fundShares = FundSharesBuilder.aFundSharesBuilder()
                .withFundId(1L).withType(ShareType.A).build();
        // then
        assertThat(fundShares.getType()).isEqualTo(ShareType.A);
    }

}