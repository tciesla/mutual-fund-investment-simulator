package pl.tciesla.mutual.fund.simulator.server.rest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import pl.tciesla.mutual.fund.simulator.server.model.Customer;
import pl.tciesla.mutual.fund.simulator.server.repository.CustomerRepository;

import javax.ws.rs.core.Response;
import java.util.Optional;

import static javax.ws.rs.core.Response.Status.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CustomerResourceTest {

    private static final String USERNAME = "username123";

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerResource customerResource;

    @Test
    public void shouldGetCustomerResponseReturnNotFoundStatusWhenCustomerNotExists() throws Exception {
        // given
        when(customerRepository.find(USERNAME)).thenReturn(Optional.empty());
        // when
        Response response = customerResource.getCustomer(USERNAME);
        // then
        assertThat(response.getStatus()).isEqualTo(NOT_FOUND.getStatusCode());
    }

    @Test
    public void shouldGetCustomerResponseReturnCustomerEntity() throws Exception {
        // given
        Customer customer = Customer.builder().username(USERNAME).build();
        when(customerRepository.find(USERNAME)).thenReturn(Optional.of(customer));
        // when
        Response response = customerResource.getCustomer(USERNAME);
        // then
        assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());
        assertThat(response.getEntity())
                .isInstanceOf(Customer.class)
                .isSameAs(customer);
    }

    @Test
    public void shouldCreateCustomerResponseReturnConflictStatusWhenGivenUsernameIsNotAvailable() throws Exception {
        // given
        Customer customer = Customer.builder().username(USERNAME).build();
        when(customerRepository.find(USERNAME)).thenReturn(Optional.of(customer));
        // when
        Response response = customerResource.createCustomer(USERNAME);
        // then
        assertThat(response.getStatus()).isEqualTo(CONFLICT.getStatusCode());
    }

    @Test
    public void shouldCreateCustomerRequestPersistCustomerInRepository() throws Exception {
        // given
        when(customerRepository.find(USERNAME)).thenReturn(Optional.empty());
        // when
        customerResource.createCustomer(USERNAME);
        // then
        verify(customerRepository).save(any(Customer.class));
    }

    @Test
    public void shouldCreateCustomerResponseReturnCustomerEntity() throws Exception {
        // given
        when(customerRepository.find(USERNAME)).thenReturn(Optional.empty());
        // when
        Response response = customerResource.createCustomer(USERNAME);
        // then
        assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());
        assertThat(response.getEntity()).isInstanceOf(Customer.class);
        assertThat(((Customer) response.getEntity()).getUsername()).isEqualTo(USERNAME);
    }

    @Test
    public void shouldDeleteCustomerResponseReturnNotFoundStatusWhenCustomerNotExists() throws Exception {
        // given
        when(customerRepository.find(USERNAME)).thenReturn(Optional.empty());
        // when
        Response response = customerResource.deleteCustomer(USERNAME);
        // then
        assertThat(response.getStatus()).isEqualTo(NOT_FOUND.getStatusCode());
    }

    @Test
    public void shouldDeleteCustomerRequestDeleteCustomerFromRepository() throws Exception {
        // given
        Customer customer = Customer.builder().username(USERNAME).build();
        when(customerRepository.find(USERNAME)).thenReturn(Optional.of(customer));
        // when
        customerResource.deleteCustomer(USERNAME);
        // then
        verify(customerRepository).delete(USERNAME);
    }

    @Test
    public void shouldDeleteCustomerResponseReturnNoContentStatus() throws Exception {
        // given
        Customer customer = Customer.builder().username(USERNAME).build();
        when(customerRepository.find(USERNAME)).thenReturn(Optional.of(customer));
        // when
        Response response = customerResource.deleteCustomer(USERNAME);
        // then
        assertThat(response.getStatus()).isEqualTo(NO_CONTENT.getStatusCode());
    }

}