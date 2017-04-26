package pl.tciesla.mutual.fund.simulator.server.repository;

import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.tciesla.mutual.fund.simulator.server.domain.Customer;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Singleton
@Startup
@SuppressWarnings("unused")
public class CustomerRepositoryXml implements CustomerRepository {

    private static final Logger logger = LoggerFactory.getLogger(CustomerRepositoryXml.class);

    private static final String XML_FILE_EXTENSION = ".xml";

    private Marshaller marshaller;
    private Unmarshaller unmarshaller;

    @PostConstruct
    public void initialize() {
        try {
            JAXBContext context = JAXBContext.newInstance(Customer.class);
            marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            unmarshaller = context.createUnmarshaller();
        } catch (JAXBException e) {
            logger.error("error occur while initializing customer xml repository", e);
        }
    }

    @Override
    public Optional<Customer> find(String username) {
        Preconditions.checkNotNull(username, "username == null");
        Preconditions.checkArgument(!username.isEmpty(), "username is empty");

        try {
            Path customerFilename = Paths.get(username + XML_FILE_EXTENSION);
            if (Files.notExists(customerFilename)) return Optional.empty();
            return Optional.of( (Customer) unmarshaller.unmarshal(customerFilename.toFile()) );
        } catch (JAXBException e) {
            logger.error("error occur while retrieving customer from file", e);
            return Optional.empty();
        }
    }

    @Override
    public void save(Customer customer) {
        Preconditions.checkNotNull(customer, "customer == null");

        try {
            Path customerFilename = Paths.get(customer.getUsername() + XML_FILE_EXTENSION);
            marshaller.marshal(customer, customerFilename.toFile());
            logger.info("customer[" + customer.getUsername() + "] has been persisted");
        } catch (JAXBException e) {
            logger.error("error occur while persisting customer to file", e);
        }
    }

    @Override
    public void delete(String username) {
        Preconditions.checkNotNull(username, "username == null");
        Preconditions.checkArgument(!username.isEmpty(), "username is empty");

        try {
            Path customerFilename = Paths.get(username + XML_FILE_EXTENSION);
            Files.delete(customerFilename);
        } catch (IOException e) {
            logger.error("error occur while deleting customer file", e);
        }
    }

}
