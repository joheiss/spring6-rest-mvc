package com.jovisco.spring6restmvc.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.jovisco.spring6restmvc.model.CustomerDTO;

public interface CustomerService {
    List<CustomerDTO> getAllCustomers();
    Optional<CustomerDTO> getCustomerById(UUID id);
    CustomerDTO createCustomer(CustomerDTO customer);
    Optional<CustomerDTO> updateCustomer(UUID id, CustomerDTO customer);
    void deleteCustomer(UUID id);
}
