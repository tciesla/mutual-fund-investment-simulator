package pl.tciesla.simulator.server.dao;

import pl.tciesla.simulator.server.domain.Customer;

/**
 * Data Access Object for Customer
 */
public interface CustomerDao {
    void persist(Customer customer);
    Customer fetch(String customerName);
    boolean remove(String username);
}
