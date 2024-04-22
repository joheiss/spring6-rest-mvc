package com.jovisco.spring6restmvc.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.hamcrest.core.Is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jovisco.spring6restmvc.model.Customer;
import com.jovisco.spring6restmvc.services.CustomerService;
import com.jovisco.spring6restmvc.services.CustomerServiceImpl;

@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;
    
    @MockBean
    CustomerService customerService;
    
    CustomerServiceImpl customerServiceImpl;

    @BeforeEach
    void setUp() {
        customerServiceImpl = new CustomerServiceImpl();
    }

    @Test
    void testGetAllCustomers() throws Exception {
        // get list of customers to be returned in the test
        List<Customer> testCustomers = customerServiceImpl.getAllCustomers();
        given(customerService.getAllCustomers()).willReturn(testCustomers);

        mockMvc.perform(get(CustomerController.CUSTOMERS_PATH)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.length()", Is.is(testCustomers.size()))
        );
    }

    @Test
    void testGetCustomerById() throws Exception {
        // get 1st item from list of customers to be returned in the test
        Customer testCustomer = customerServiceImpl.getAllCustomers().get(0);
        given(customerService.getCustomerById(testCustomer.getId())).willReturn(testCustomer);

        mockMvc.perform(get(CustomerController.CUSTOMERS_PATH_ID, testCustomer.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id", Is.is(testCustomer.getId().toString())))
            .andExpect(jsonPath("$.name", Is.is(testCustomer.getName()))
        );

    }

    @Test
    void testCreateCustomer() throws Exception {
        // get 1st item from list of customers to be used as test input
        Customer found = customerServiceImpl.getAllCustomers().get(0);
        Customer testCustomer = Customer.builder().build();
        BeanUtils.copyProperties(found, testCustomer);
        testCustomer.setId(null);
        testCustomer.setCreatedAt(null);
        testCustomer.setUpdatedAt(null);

        // provide a return value for the mocked "createCustomer" method
        given(customerService.createCustomer(any(Customer.class))).willReturn(customerServiceImpl.getAllCustomers().get(0));

        mockMvc
            .perform(post(CustomerController.CUSTOMERS_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testCustomer))
                .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isCreated())
            .andExpect(header().exists("Location"));
    }

    @Test
    void testUpdateCustomerById() throws Exception {
        // get 1st item from list of customers to be used as test input
        Customer found = customerServiceImpl.getAllCustomers().get(0);
        Customer testCustomer = Customer.builder().build();
        BeanUtils.copyProperties(found, testCustomer);
        testCustomer.setName(found.getName() + "**UPDATED**");

        mockMvc
            .perform(put(CustomerController.CUSTOMERS_PATH_ID, testCustomer.getId())
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testCustomer))
                .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isNoContent());

        // verify that service method was called with proper arguments
        verify(customerService, times(1)).updateCustomer(testCustomer.getId(), testCustomer);
    }

    @Test
    void testDeleteCustomerById() throws Exception {
        // get 1st item from list of customers
        Customer testCustomer = customerServiceImpl.getAllCustomers().get(0);

        mockMvc
            .perform(delete(CustomerController.CUSTOMERS_PATH_ID, testCustomer.getId()))
            .andExpect(status().isNoContent());

        // verify that service method was called 
        verify(customerService, times(1)).deleteCustomer(testCustomer.getId());
    }

    @Test
    void testPatchCustomerById() throws Exception {
        // get 1st item from list of customers to be used as test input
        Customer found = customerServiceImpl.getAllCustomers().get(0);
        Customer testCustomer = Customer.builder()
            .id(found.getId())
            .name(found.getName() + "**PATCHED**")
            .build();

        mockMvc
            .perform(patch(CustomerController.CUSTOMERS_PATH_ID, testCustomer.getId())
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testCustomer))
                .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isNoContent());

        // verify that service method was called with proper arguments
        verify(customerService, times(1)).updateCustomer(testCustomer.getId(), testCustomer);
    }

}
