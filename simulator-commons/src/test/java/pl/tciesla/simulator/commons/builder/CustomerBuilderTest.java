package pl.tciesla.simulator.commons.builder;

import org.junit.Test;
import pl.tciesla.simulator.commons.domain.Customer;
import pl.tciesla.simulator.commons.domain.FundShares;

import java.math.BigDecimal;
import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

public class CustomerBuilderTest {

    @Test
    public void shouldInitializeCustomerWithDefaultCash() throws Exception {
        // when
        Customer customer = CustomerBuilder.aCustomerBuilder().build();
        // then
        assertThat(customer.getCash()).isEqualTo(new BigDecimal("1000.00"));
    }

    @Test
    public void shouldFundSharesBeNotNull() throws Exception {
        // when
        Customer customer = CustomerBuilder.aCustomerBuilder().build();
        // then
        assertThat(customer.getFundShares()).isNotNull().hasSize(0);
    }

    @Test
    public void shouldSetCorrectUsername() throws Exception {
        // given
        String exampleUsername = "example";
        // when
        Customer customer = CustomerBuilder.aCustomerBuilder().withName(exampleUsername).build();
        // then
        assertThat(customer.getUsername()).isEqualTo(exampleUsername);
    }

    @Test
    public void shouldSetCorrectCash() throws Exception {
        // given
        BigDecimal exampleCash = new BigDecimal("30.50");
        // when
        Customer customer = CustomerBuilder.aCustomerBuilder().withCash(exampleCash).build();
        // then
        assertThat(customer.getCash()).isEqualTo(exampleCash);
    }

    @Test
    public void shouldSetCorrectFundShares() throws Exception {
        // given
        HashMap<FundShares, Long> exampleFundShares = new HashMap<>();
        // when
        Customer customer = CustomerBuilder.aCustomerBuilder().withFundShares(exampleFundShares).build();
        // then
        assertThat(customer.getFundShares()).isSameAs(exampleFundShares);
    }

}