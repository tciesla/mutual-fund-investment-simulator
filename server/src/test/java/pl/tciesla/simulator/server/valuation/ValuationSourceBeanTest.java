package pl.tciesla.simulator.server.valuation;

import org.junit.Test;
import pl.tciesla.simulator.server.builder.MutualFundBuilder;
import pl.tciesla.simulator.server.constant.FundCategory;
import pl.tciesla.simulator.server.dao.MutualFundDao;
import pl.tciesla.simulator.server.domain.MutualFund;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class ValuationSourceBeanTest {

    private ValuationSourceBean valuationSourceBean = new ValuationSourceBean();

    @Test
    public void shouldPersistUpdatedValuation() throws Exception {
        // given
        MutualFund exampleMutualFund = MutualFundBuilder.aMutualFundBuilder()
                .withValuation(new BigDecimal("500.00"))
                .withCategory(FundCategory.STOCK)
                .build();
        List<MutualFund> exampleMutualFundList = Arrays.asList(
                exampleMutualFund);
        MutualFundDao mutualFundDao = mock(MutualFundDao.class);
        when(mutualFundDao.fetchAll()).thenReturn(exampleMutualFundList);
        valuationSourceBean.setMutualFundDao(mutualFundDao);
        // when
        valuationSourceBean.updateMutualFundsValuation();
        // then
        verify(mutualFundDao).persistAll(exampleMutualFundList);
    }

    @Test
    public void shouldChangeValuationOfExampleMutualFund() throws Exception {
        // given
        MutualFund exampleMutualFund = MutualFundBuilder.aMutualFundBuilder()
                .withValuation(new BigDecimal("500.00"))
                .withCategory(FundCategory.STOCK)
                .build();
        List<MutualFund> exampleMutualFundList = Arrays.asList(
                exampleMutualFund);
        MutualFundDao mutualFundDao = mock(MutualFundDao.class);
        when(mutualFundDao.fetchAll()).thenReturn(exampleMutualFundList);
        valuationSourceBean.setMutualFundDao(mutualFundDao);
        // when
        valuationSourceBean.updateMutualFundsValuation();
        // then
        assertThat(exampleMutualFund).isNotEqualTo(new BigDecimal("500.00"));
    }

}