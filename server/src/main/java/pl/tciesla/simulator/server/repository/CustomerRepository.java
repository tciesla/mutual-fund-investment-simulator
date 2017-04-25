package pl.tciesla.simulator.server.repository;

import pl.tciesla.simulator.server.domain.Customer;

import java.util.Optional;

public interface CustomerRepository {
    Optional<Customer> find(String username);
    void save(Customer customer);
    boolean delete(String username);
}
