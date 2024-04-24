package com.jovisco.spring6restmvc.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.jovisco.spring6restmvc.model.CustomerDTO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CustomerServiceImpl implements CustomerService {

    private Map<UUID, CustomerDTO> customersMap;
    
    
    public CustomerServiceImpl() {
        this.customersMap = initCustomersMap();
    }

    @Override
    public List<CustomerDTO> getAllCustomers() {
        log.debug("CustomerService.getAllCustomers was called.");
        return new ArrayList<>(customersMap.values());
    }

    @Override
    public Optional<CustomerDTO> getCustomerById(UUID id) {
        log.debug("CustomerService.getCustomerById was called with ID: "+ id);
        return Optional.of(customersMap.get(id));
    }
    
     @Override
    public CustomerDTO createCustomer(CustomerDTO customer) {
        CustomerDTO savedCustomer = CustomerDTO.builder()
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
    public Optional<CustomerDTO> updateCustomer(UUID id, CustomerDTO customer) {
       // get existing customer
       CustomerDTO found = getCustomerById(id).orElse(null);
       if (found == null) return Optional.ofNullable(found);

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

       return Optional.ofNullable(found);
    }

    @Override
    public void deleteCustomer(UUID id) {
        customersMap.remove(id);
    }


    private Map<UUID, CustomerDTO> initCustomersMap() {
        Map<UUID, CustomerDTO> customersMap = new HashMap<>();
        
        CustomerDTO customer1 = CustomerDTO.builder()
            .id(UUID.randomUUID())
            .name("Hampelmann AG")
            .version(1)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

        CustomerDTO customer2 = CustomerDTO.builder()
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
