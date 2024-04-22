package com.jovisco.spring6restmvc.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.jovisco.spring6restmvc.controllers.NotFoundException;
import com.jovisco.spring6restmvc.model.Customer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {

    private Map<UUID, Customer> customersMap;
    
    
    public CustomerServiceImpl() {
        this.customersMap = initCustomersMap();
    }

    @Override
    public List<Customer> getAllCustomers() {
        log.debug("CustomerService.getAllCustomers was called.");
        return new ArrayList<>(customersMap.values());
    }

    @Override
    public Optional<Customer> getCustomerById(UUID id) {
        log.debug("CustomerService.getCustomerById was called with ID: "+ id);
        return Optional.of(customersMap.get(id));
    }
    

     @Override
    public Customer createCustomer(Customer customer) {
        Customer savedCustomer = Customer.builder()
            .id(UUID.randomUUID())
            .version(1)
            .name(customer.getName())
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

        customersMap.put(savedCustomer.getId(), savedCustomer);

        return savedCustomer;
    } 

    @Override
    public Customer updateCustomer(UUID id, Customer customer) {
       // get existing customer
       Customer found = getCustomerById(id).orElseThrow(NotFoundException::new);

       // replace values
       Integer version = customer.getVersion();
       if (version != null && version != found.getVersion()) {
        found.setVersion(version);
       }
       String name = customer.getName();
       if (name != null && name != found.getName()) {
        found.setName(name);
       }
       found.setUpdatedAt(LocalDateTime.now());

       return found;
    }

    @Override
    public void deleteCustomer(UUID id) {
        customersMap.remove(id);
    }


    private Map<UUID, Customer> initCustomersMap() {
        Map<UUID, Customer> customersMap = new HashMap<>();
        
        Customer customer1 = Customer.builder()
            .id(UUID.randomUUID())
            .name("Hampelmann AG")
            .version(1)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

        Customer customer2 = Customer.builder()
            .id(UUID.randomUUID())
            .name("Klosterfrau GmbH")
            .version(1)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

        customersMap.put(customer1.getId(), customer1);
        customersMap.put(customer2.getId(), customer2);

        return customersMap;
    }

}
