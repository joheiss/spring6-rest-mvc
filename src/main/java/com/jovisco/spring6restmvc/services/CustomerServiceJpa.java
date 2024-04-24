package com.jovisco.spring6restmvc.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.jovisco.spring6restmvc.entities.Customer;
import com.jovisco.spring6restmvc.mappers.CustomerMapper;
import com.jovisco.spring6restmvc.model.CustomerDTO;
import com.jovisco.spring6restmvc.repositories.CustomerRepository;

import lombok.RequiredArgsConstructor;

@Service
@Primary
@RequiredArgsConstructor
public class CustomerServiceJpa implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    public List<CustomerDTO> getAllCustomers() {
        return customerRepository.findAll()
            .stream()
            .map(customerMapper::customerToCustomerDto)
            .collect(Collectors.toList());
    }

    @Override
    public Optional<CustomerDTO> getCustomerById(UUID id) {
        return Optional.ofNullable(
            customerMapper.customerToCustomerDto(
                customerRepository.findById(id).orElse(null)
            )
        );
    }

    @Override
    public CustomerDTO createCustomer(CustomerDTO customer) {
        Customer toBeCreated = customerMapper.customerDtoToCustomer(customer);
        
        return customerMapper.customerToCustomerDto(customerRepository.save(toBeCreated));
    }

    @Override
    public Optional<CustomerDTO> updateCustomer(UUID id, CustomerDTO customer) {

        Customer found = customerRepository.findById(id).orElse(null);

        if (found == null) return Optional.ofNullable(customerMapper.customerToCustomerDto(found));

        if (customer.getName() != null) found.setName(customer.getName());
        found.setUpdatedAt(LocalDateTime.now());
            
        return Optional.ofNullable(customerMapper.customerToCustomerDto(customerRepository.save(found)));
    }

    @Override
    public void deleteCustomer(UUID id) {
        customerRepository.deleteById(id);
    }
    
}
