package com.jovisco.spring6restmvc.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import java.time.LocalDateTime;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.jovisco.spring6restmvc.mappers.CustomerMapper;
import com.jovisco.spring6restmvc.model.CustomerDTO;
import com.jovisco.spring6restmvc.repositories.CustomerRepository;

@SpringBootTest
public class CustomerControllerIT {

    @Autowired
    CustomerController customerController;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    CustomerMapper customerMapper;
    
    @Rollback
    @Transactional
    @Test
    void testCreateCustomer() {
        
        var now = LocalDateTime.now();

        // build a test customer
        var customer = CustomerDTO.builder()
            .name("Testkunde GbR")
            .createdAt(now)
            .updatedAt(now)
            .build();

        // call the controller method to create a beer
        var response = customerController.createCustomer(customer);

        // verify that creation worked well - status code 201 and location filled
        assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        var location = response.getHeaders().getLocation();
        assertThat(location).isNotNull();

        // get id from location & verify that the beer is really present on the database
        if (location != null) {
            var segments = location.getPath().split("/");
            var id = segments[segments.length - 1];
            assertThat(id).isNotNull();
            assertThat(customerRepository.existsById(UUID.fromString(id))).isTrue();
        }
    }

    @Rollback
    @Transactional
    @Test
    void testDeleteCustomerById() {
        var toBeDeleted = customerController.getAllCustomers().get(0);
        assertThat(toBeDeleted).isNotNull();

        var response = customerController.deleteCustomerById(toBeDeleted.getId());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        // verify that customer has been deleted from database
        assertThat(customerRepository.existsById(toBeDeleted.getId())).isFalse();
    }

    @Test
    void testDeleteCustomerByIdNotFound() {

        var response = customerController.deleteCustomerById(UUID.randomUUID());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));
    }

    @Test
    void testGetAllCustomers() {
        var customers = customerController.getAllCustomers();

        assertThat(customers.size()).isGreaterThan(0);
    }

    @Rollback
    @Transactional
    @Test
    void testGetAllCustomersEmptyDatabase() {
        customerRepository.deleteAll();
        var customers = customerController.getAllCustomers();

        assertThat(customers.size()).isEqualTo(0);
    }

    @Test
    void testGetCustomerbyId() {
        var customer = customerController.getAllCustomers().get(0);
        var found = customerController.getCustomerbyId(customer.getId());

        assertThat(found).isNotNull();
        assertThat(found.getId()).isEqualTo(customer.getId());
    }

    @Test
    void testGetCustomerbyIdNotFound() {

        assertThatExceptionOfType(NotFoundException.class)
            .isThrownBy(() -> customerController.getCustomerbyId(UUID.randomUUID()));
    }

    @Test
    void testPatchCustomerbyId() {

    }

    @Rollback
    @Transactional
    @Test
    void testUpdateCustomerbyId() {
        // get existing customer from database
        var found = customerRepository.findAll().get(0);
        assertThat(found).isNotNull();

        // modify values
        var toBeUpdated = customerMapper.customerToCustomerDto(found);
        toBeUpdated.setId(null);
        toBeUpdated.setVersion(null);
        toBeUpdated.setName(toBeUpdated.getName() + "*UPDATED*");

        // request controller to updatecustomer
        var response = customerController.updateCustomerById(found.getId(), toBeUpdated);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        // verify that values have been updated properly
        var updated = customerRepository.findById(found.getId()).get();
        assertThat(updated).isNotNull();
        assertThat(updated.getName()).isEqualTo(toBeUpdated.getName());
    }

    @Test
    void testUpdateBeerByIdNotFound() {

        // verify that calling the update with an invalid UUID throws an exception
        assertThatExceptionOfType(NotFoundException.class)
            .isThrownBy(() -> customerController.updateCustomerById(UUID.randomUUID(), CustomerDTO.builder().build()));
    }

}
