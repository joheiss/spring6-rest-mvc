package com.jovisco.spring6restmvc.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.jovisco.spring6restmvc.model.Customer;
import com.jovisco.spring6restmvc.services.CustomerService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
public class CustomerController {

    public static final String CUSTOMERS_PATH = "/api/v1/customers";
    public static final String CUSTOMERS_PATH_ID = "/api/v1/customers/{id}";

    private final CustomerService customerService;

    @GetMapping(CUSTOMERS_PATH)
    List<Customer> getAllCustomers() {
        log.debug("CustomerComtroller.getAllCustomers was called.");
        return customerService.getAllCustomers();
    }
    
    @GetMapping(CUSTOMERS_PATH_ID)
    Customer getCustomerbyId(@PathVariable UUID id) {
        log.debug("CustomerController.getCustomerById was called with id: " + id);
        return customerService.getCustomerById(id);
    }

    @PostMapping(CUSTOMERS_PATH)
    ResponseEntity<HttpStatus> createCustomer(@RequestBody Customer customer) {
        // create new customer from request body
        Customer savedCustomer = customerService.createCustomer(customer);
        // set Location header
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", CUSTOMERS_PATH + "/" + savedCustomer.getId());
        // return HTTP status
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @PutMapping(CUSTOMERS_PATH_ID)
    ResponseEntity<HttpStatus> updateCustomerbyId(@PathVariable UUID id, @RequestBody Customer customer) {
        // update existing customer from request body
        customerService.updateCustomer(id, customer);

        // return HTTP status
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(CUSTOMERS_PATH_ID)
    ResponseEntity<HttpStatus> deleteCustomerById(@PathVariable UUID id) {
        // delete existing customer
        customerService.deleteCustomer(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping(CUSTOMERS_PATH_ID)
    ResponseEntity<HttpStatus> patchCustomerbyId(@PathVariable UUID id, @RequestBody Customer customer) {
        // update existing customer from request body
        customerService.updateCustomer(id, customer);

        // return HTTP status
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
