package pl.tciesla.mutual.fund.simulator.server.service;

import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import pl.tciesla.mutual.fund.simulator.server.model.MutualFund;
import pl.tciesla.mutual.fund.simulator.server.repository.MutualFundRepository;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class MutualFundsValuationCronTest {

    @Mock
    private MutualFundRepository mutualFundRepository;

    @InjectMocks
    private MutualFundsValuationCron mutualFundsValuationCron;

    private MutualFund mutualFund1;
    private MutualFund mutualFund2;

    @Before
    public void setUp() throws Exception {
        mutualFund1 = createMutualFund(1L, "Best of US Stocks", BigDecimal.valueOf(100.11));
        mutualFund2 = createMutualFund(2L, "Best of UK Stocks", BigDecimal.valueOf(200.22));
        List<MutualFund> mutualFunds = Lists.newArrayList(mutualFund1, mutualFund2);
        doReturn(mutualFunds).when(mutualFundRepository).findAll();
    }

    @Test
    public void shouldUpdateMutualFundValuations() throws Exception {
        // given
        BigDecimal mutualFund1OldValuation = mutualFund1.getValuation();
        BigDecimal mutualFund2OldValuation = mutualFund2.getValuation();
        // when
        mutualFundsValuationCron.updateMutualFundsValuation();
        // then
        assertThat(mutualFund1.getValuation()).isNotEqualTo(mutualFund1OldValuation);
        assertThat(mutualFund2.getValuation()).isNotEqualTo(mutualFund2OldValuation);
    }

    @Test
    public void shouldPersistMutualFundsWithUpdatedValuation() throws Exception {
        // when
        mutualFundsValuationCron.updateMutualFundsValuation();
        // then
        verify(mutualFundRepository).save(mutualFund1);
        verify(mutualFundRepository).save(mutualFund2);
    }

    private MutualFund createMutualFund(long id, String name, BigDecimal valuation) {
        return MutualFund.builder()
                .id(id)
                .name(name)
                .valuation(valuation)
                .category(MutualFund.Category.STOCK)
                .build();
    }

}