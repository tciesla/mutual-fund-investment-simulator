package pl.tciesla.simulator.server.dao;

import pl.tciesla.simulator.commons.domain.Customer;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.logging.Logger;

/**
 * Xml implementation of Customer Data Access Object
 */
@Singleton
@Startup
public class CustomerDaoXml implements CustomerDao {

    private static final Logger log = Logger.getLogger(CustomerDaoXml.class.getName());

    private static final String STORAGE_FILES_EXTENSION = ".xml";

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
            log.info("Error occur while initializing customer dao.");
        }
    }

    @Override
    public void persist(Customer customer) {
        try {
            marshaller.marshal(customer, new File(customer.getUsername() + STORAGE_FILES_EXTENSION));
            log.info("Customer[" + customer.getUsername() + "] data has been persisted.");
        } catch (JAXBException e) {
            log.info("Error occur while persisting user data.");
        }
    }

    @Override
    public Customer fetch(String customerUsername) {
        try {
            File customerFile = new File(customerUsername + STORAGE_FILES_EXTENSION);
            if (!customerFile.exists()) { return null; }
            return  (Customer) unmarshaller.unmarshal(customerFile);
        } catch (JAXBException e) {
            log.info("Error occur while retrieving customer data from file.");
        }
        return null;
    }

    @Override
    public boolean remove(String customerUsername) {
        File customerFile = new File(customerUsername + STORAGE_FILES_EXTENSION);
        return customerFile.delete();
    }
}
