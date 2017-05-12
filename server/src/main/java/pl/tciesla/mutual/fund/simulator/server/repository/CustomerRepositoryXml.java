package pl.tciesla.mutual.fund.simulator.server.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.tciesla.mutual.fund.simulator.server.model.Customer;

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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

@Singleton
@Startup
@SuppressWarnings("unused")
public class CustomerRepositoryXml implements CustomerRepository {

    private static final Logger logger = LoggerFactory.getLogger(CustomerRepositoryXml.class);

    private static final String XML = ".xml";

    private JAXBContext context;
    private Marshaller marshaller;
    private Unmarshaller unmarshaller;

    @PostConstruct
    public void initialize() {
        try {
            context = JAXBContext.newInstance(Customer.class);
        } catch (JAXBException e) {
            logger.error("error occur while initializing customer xml repository", e);
        }
    }

    @Override
    public Optional<Customer> find(String username) {
        checkNotNull(username, "username == null");
        checkArgument(!username.isEmpty(), "username is empty");

        Path customerFilename = Paths.get(username + XML);
        if (Files.notExists(customerFilename)) return Optional.empty();

        try {
            return Optional.of((Customer) unmarshaller().unmarshal(customerFilename.toFile()));
        } catch (JAXBException e) {
            logger.error("unmarshall customer {} failed", username, e);
            return Optional.empty();
        }
    }

    @Override
    public void save(Customer customer) {
        checkNotNull(customer, "customer == null");

        Path customerFilename = Paths.get(customer.getUsername() + XML);

        try {
            marshaller().marshal(customer, customerFilename.toFile());
            logger.info("customer[{}] has been persisted", customer.getUsername());
        } catch (JAXBException e) {
            logger.error("marshall customer {} failed", customer.getUsername(), e);
        }
    }

    @Override
    public void delete(String username) {
        checkNotNull(username, "username == null");
        checkArgument(!username.isEmpty(), "username is empty");

        try {
            Path customerFilename = Paths.get(username + XML);
            Files.deleteIfExists(customerFilename);
        } catch (IOException e) {
            logger.error("delete customer {} failed", username, e);
        }
    }

    private Marshaller marshaller() throws JAXBException {
        if (marshaller == null) {
            marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        }
        return marshaller;
    }

    private Unmarshaller unmarshaller() throws JAXBException {
        if (unmarshaller == null) {
            unmarshaller = context.createUnmarshaller();
        }
        return unmarshaller;
    }

    public void setMarshaller(Marshaller marshaller) {
        this.marshaller = marshaller;
    }

    public void setUnmarshaller(Unmarshaller unmarshaller) {
        this.unmarshaller = unmarshaller;
    }

}
