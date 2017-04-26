package pl.tciesla.mutual.fund.simulator.server.repository;

import pl.tciesla.mutual.fund.simulator.server.domain.Customer;

import java.util.Optional;

public interface CustomerRepository {
    Optional<Customer> find(String username);
    void save(Customer customer);
    void delete(String username);
}
