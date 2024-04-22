package com.jovisco.spring6restmvc.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.jovisco.spring6restmvc.model.Customer;

public interface CustomerService {
    List<Customer> getAllCustomers();
    Optional<Customer> getCustomerById(UUID id);
    Customer createCustomer(Customer customer);
    Customer updateCustomer(UUID id, Customer customer);
    void deleteCustomer(UUID id);
}
