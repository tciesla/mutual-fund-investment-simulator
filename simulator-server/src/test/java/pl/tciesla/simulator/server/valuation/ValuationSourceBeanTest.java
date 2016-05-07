package pl.tciesla.simulator.server.valuation;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import pl.tciesla.simulator.commons.builder.MutualFundBuilder;
import pl.tciesla.simulator.commons.constant.FundCategory;
import pl.tciesla.simulator.commons.domain.MutualFund;
import pl.tciesla.simulator.server.dao.MutualFundDao;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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