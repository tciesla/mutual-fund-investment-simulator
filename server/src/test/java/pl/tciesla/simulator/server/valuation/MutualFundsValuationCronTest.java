package pl.tciesla.simulator.server.valuation;

import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import pl.tciesla.simulator.server.builder.MutualFundBuilder;
import pl.tciesla.simulator.server.constant.FundCategory;
import pl.tciesla.simulator.server.dao.MutualFundDao;
import pl.tciesla.simulator.server.domain.MutualFund;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class MutualFundsValuationCronTest {

    @Mock
    private MutualFundDao mutualFundDao;

    @InjectMocks
    private MutualFundsValuationCron mutualFundsValuationCron;

    private MutualFund mutualFund1;
    private MutualFund mutualFund2;

    @Before
    public void setUp() throws Exception {
        mutualFund1 = createMutualFund(1L, "Best of US Stocks", BigDecimal.valueOf(100.11));
        mutualFund2 = createMutualFund(2L, "Best of UK Stocks", BigDecimal.valueOf(200.22));
        List<MutualFund> mutualFunds = Lists.newArrayList(mutualFund1, mutualFund2);
        doReturn(mutualFunds).when(mutualFundDao).fetchAll();
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
        verify(mutualFundDao).persist(mutualFund1);
        verify(mutualFundDao).persist(mutualFund2);
    }

    private MutualFund createMutualFund(long id, String name, BigDecimal valuation) {
        return MutualFundBuilder.aMutualFundBuilder()
                .withId(id)
                .withName(name)
                .withValuation(valuation)
                .withCategory(FundCategory.STOCK)
                .build();
    }

}