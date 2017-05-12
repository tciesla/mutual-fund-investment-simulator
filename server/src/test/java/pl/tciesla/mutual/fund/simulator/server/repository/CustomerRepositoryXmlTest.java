package pl.tciesla.mutual.fund.simulator.server.repository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import pl.tciesla.mutual.fund.simulator.server.model.Customer;

import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CustomerRepositoryXmlTest {

    private final String username = "somebody";

    @Mock
    private Marshaller marshaller;

    @Mock
    private Unmarshaller unmarshaller;

    @InjectMocks
    private CustomerRepositoryXml customerRepositoryXml;

    @Before
    public void setUp() throws Exception {
        customerRepositoryXml = new CustomerRepositoryXml();
        customerRepositoryXml.setMarshaller(marshaller);
        customerRepositoryXml.setUnmarshaller(unmarshaller);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionWhenUsernameIsNullWhileFinding() throws Exception {
        // when
        customerRepositoryXml.find(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenUsernameIsEmptyWhileFinding() throws Exception {
        // given
        String emptyUsername = "";
        // when
        customerRepositoryXml.find(emptyUsername);
    }

    @Test
    public void shouldReturnEmptyOptionalWhenCustomerNotExistsWhileFinding() throws Exception {
        // when
        Optional<Customer> customer = customerRepositoryXml.find(username);
        // then
        assertThat(customer.isPresent()).isFalse();
    }

    @Test
    public void shouldReturnEmptyOptionalWhenUnmarshallCustomerWithException() throws Exception {
        // given
        createCustomerXmlFile(username);
        doThrow(JAXBException.class).when(unmarshaller).unmarshal(any(File.class));
        // when
        Optional<Customer> customer = customerRepositoryXml.find(username);
        // then
        assertThat(customer.isPresent()).isFalse();
        deleteCustomerXmlFile(username);
    }

    @Test
    public void shouldUnmarshallCustomerFromXmlFile() throws Exception {
        // given
        createCustomerXmlFile(username);
        Customer customer = Customer.builder().username(username).build();
        doReturn(customer).when(unmarshaller).unmarshal(any(File.class));
        // when
        Optional<Customer> result = customerRepositoryXml.find(username);
        // then
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get().getUsername()).isEqualTo(username);
        deleteCustomerXmlFile(username);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionWhenCustomerIsNullWhileSaving() throws Exception {
        // when
        customerRepositoryXml.save(null);
    }

    @Test
    public void shouldMarshallerCustomerToXmlFile() throws Exception {
        // given
        Customer customer = Customer.builder().username(username).build();
        // when
        customerRepositoryXml.save(customer);
        // then
        verify(marshaller).marshal(any(Customer.class), any(File.class));
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowExceptionWhenUsernameIsNullWhileDeleting() throws Exception {
        // when
        customerRepositoryXml.delete(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenUsernameIsEmptyWhileDeleting() throws Exception {
        // given
        String emptyUsername = "";
        // when
        customerRepositoryXml.delete(emptyUsername);
    }

    @Test
    public void shouldDeleteCustomerXmlFile() throws Exception {
        createCustomerXmlFile(username);
        // when
        customerRepositoryXml.delete(username);
        // then
        assertThat(Files.exists(Paths.get(username + ".xml"))).isFalse();
    }

    private void createCustomerXmlFile(String username) throws IOException {
        Path path = Paths.get(username + ".xml");
        Files.deleteIfExists(path);
        Files.createFile(path);
    }

    private void deleteCustomerXmlFile(String username) throws IOException {
        Files.deleteIfExists(Paths.get(username + ".xml"));
    }
}