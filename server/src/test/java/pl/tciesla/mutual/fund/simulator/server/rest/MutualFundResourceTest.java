package pl.tciesla.mutual.fund.simulator.server.rest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import pl.tciesla.mutual.fund.simulator.server.model.MutualFund;
import pl.tciesla.mutual.fund.simulator.server.repository.MutualFundRepository;

import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MutualFundResourceTest {

    @Mock
    private MutualFundRepository mutualFundRepository;

    @InjectMocks
    private MutualFundResource mutualFundResource;

    @Test
    public void shouldGetMutualFundsResponseReturnOkStatus() throws Exception {
        // when
        Response response = mutualFundResource.getMutualFunds();
        // then
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldGetMutualFundsResponseContainsTwoMutualFunds() throws Exception {
        // given
        MutualFund mutualFund1 = MutualFund.builder().id(1L).build();
        MutualFund mutualFund2 = MutualFund.builder().id(2L).build();
        when(mutualFundRepository.findAll()).thenReturn(Arrays.asList(mutualFund1, mutualFund2));
        // when
        Response response = mutualFundResource.getMutualFunds();
        // then
        assertThat(response.getEntity()).isInstanceOf(List.class);
        assertThat((List) response.getEntity())
                .hasSize(2).contains(mutualFund1, mutualFund2);
    }

}