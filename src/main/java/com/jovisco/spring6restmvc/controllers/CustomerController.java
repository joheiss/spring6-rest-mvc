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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jovisco.spring6restmvc.model.Customer;
import com.jovisco.spring6restmvc.services.CustomerService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("api/v1/customers")
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping
    List<Customer> getAllCustomers() {
        log.debug("CustomerComtroller.getAllCustomers was called.");
        return customerService.getAllCustomers();
    }
    
    @GetMapping("/{id}")
    Customer getCustomerbyId(@PathVariable UUID id) {
        log.debug("CustomerController.getCustomerById was called with id: " + id);
        return customerService.getCustomerById(id);
    }

    @PostMapping
    ResponseEntity<HttpStatus> createCustomer(@RequestBody Customer customer) {
        // create new customer from request body
        Customer savedCustomer = customerService.createCustomer(customer);
        // set Location header
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/v1/customers/" + savedCustomer.getId());
        // return HTTP status
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    ResponseEntity<HttpStatus> updateCustomerbyId(@PathVariable UUID id, @RequestBody Customer customer) {
        // update existing customer from request body
        customerService.updateCustomer(id, customer);

        // return HTTP status
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<HttpStatus> deleteCustomerById(@PathVariable UUID id) {
        // delete existing customer
        customerService.deleteCustomer(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{id}")
    ResponseEntity<HttpStatus> patchCustomerbyId(@PathVariable UUID id, @RequestBody Customer customer) {
        // update existing customer from request body
        customerService.updateCustomer(id, customer);

        // return HTTP status
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
