package pl.tciesla.mutual.fund.simulator.server.model;

import com.google.common.collect.Maps;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class CustomerTest {

    @Test
    public void shouldCustomerHasDefaultConstructorForJsonLibrary() throws Exception {
        // when
        new Customer();
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionWhenCustomerDoesNotHaveUsername() throws Exception {
        // when
        Customer.builder().build();
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionWhenUsernameIsNull() throws Exception {
        // when
        Customer.builder().username(null).build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenUsernameIsEmpty() throws Exception {
        // given
        String emptyUsername = "";
        // when
        Customer.builder().username(emptyUsername).build();
    }

    @Test
    public void shouldSetUsername() throws Exception {
        // given
        String username = "username";
        // when
        Customer customer = Customer.builder().username(username).build();
        // then
        assertThat(customer.getUsername()).isSameAs(username);
    }

    @Test
    public void shouldCreateCustomerWithInitialCashEquals1000() throws Exception {
        // when
        Customer customer = Customer.builder().username("username").build();
        // then
        assertThat(customer.getCash()).isEqualTo(BigDecimal.valueOf(1000.00));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenCashIsNegative() throws Exception {
        // when
        Customer.builder().username("username").cash(BigDecimal.valueOf(-1)).build();
    }

    @Test
    public void shouldSetCash() throws Exception {
        // given
        BigDecimal cash = BigDecimal.valueOf(10000.00);
        // when
        Customer customer = Customer.builder().username("username").cash(cash).build();
        // then
        assertThat(customer.getCash()).isSameAs(cash);
    }

    @Test
    public void shouldCreateCustomerWithEmptyFundSharesMap() throws Exception {
        // when
        Customer customer = Customer.builder().username("username").build();
        // then
        assertThat(customer.getFundShares()).isEmpty();
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionWhenFundSharesMapIsNull() throws Exception {
        // when
        Customer.builder().username("username").fundShares(null).build();
    }

    @Test
    public void shouldSetFundSharesMap() throws Exception {
        // given
        Map<Long, Long> fundShares = Maps.newHashMap();
        // when
        Customer customer = Customer.builder().username("username").fundShares(fundShares).build();
        // then
        assertThat(customer.getFundShares()).isSameAs(fundShares);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionWhenRequiredCashIsNull() throws Exception {
        // given
        Customer customer = Customer.builder().username("username").cash(BigDecimal.TEN).build();
        // when
        customer.hasEnoughCash(null);
    }

    @Test
    public void shouldCustomerHasRequiredCash() throws Exception {
        // given
        Customer customer = Customer.builder().username("username").cash(BigDecimal.TEN).build();
        // when
        boolean hasEnoughCash = customer.hasEnoughCash(BigDecimal.TEN);
        // then
        assertThat(hasEnoughCash).isTrue();
    }

    @Test
    public void shouldNotCustomerHasRequiredCash() throws Exception {
        // given
        Customer customer = Customer.builder().username("username").cash(BigDecimal.TEN).build();
        // when
        boolean hasEnoughCash = customer.hasEnoughCash(BigDecimal.valueOf(11.00));
        // then
        assertThat(hasEnoughCash).isFalse();
    }

    @Test
    public void shouldCustomerHasEnoughShares() throws Exception {
        // given
        long fundId = 5L;
        long sharesAmount = 10L;
        BigDecimal transactionCost = BigDecimal.TEN;

        Customer customer = Customer.builder().username("username").cash(transactionCost).build();
        customer.buy(fundId, sharesAmount, transactionCost);
        // when
        boolean hasEnoughShares = customer.hasEnoughShares(fundId, sharesAmount);
        // then
        assertThat(hasEnoughShares).isTrue();
    }

    @Test
    public void shouldNotCustomerHasEnoughSharesWhenNeverBoughtGivenFund() throws Exception {
        // given
        long fundId = 5L;
        long sharesAmount = 10L;
        Customer customer = Customer.builder().username("username").build();
        // when
        boolean hasEnoughShares = customer.hasEnoughShares(fundId, sharesAmount);
        // then
        assertThat(hasEnoughShares).isFalse();
    }

    @Test
    public void shouldNotCustomerHasEnoughShares() throws Exception {
        // given
        long fundId = 5L;
        long sharesAmount = 10L;
        BigDecimal transactionCost = BigDecimal.TEN;

        Customer customer = Customer.builder().username("username").cash(transactionCost).build();
        customer.buy(fundId, sharesAmount, transactionCost);
        // when
        boolean hasEnoughShares = customer.hasEnoughShares(fundId, sharesAmount + 1);
        // then
        assertThat(hasEnoughShares).isFalse();
    }
}