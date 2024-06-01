package com.jovisco.spring6restmvc.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jovisco.spring6restmvc.mappers.BeerOrderMapper;
import com.jovisco.spring6restmvc.repositories.BeerOrderRepository;

import static com.jovisco.spring6restmvc.controllers.BeerControllerTest.jwtRequestPostProcessor;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.core.Is.is;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class BeerOrderControllerIT {

    @Autowired
    BeerOrderController beerOrderController;

    @Autowired
    WebApplicationContext wac;

    @Autowired
    BeerOrderRepository beerOrderRepository;
    
    @Autowired
    BeerOrderMapper beerOrderMapper;

     @Autowired
    ObjectMapper objectMapper;
    
    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .apply(springSecurity())
                .build();
    }

    @Test
    void testListOrders() throws Exception {
        mockMvc.perform(
                get(BeerOrderController.ORDERS_PATH)    
                .with(jwtRequestPostProcessor)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content.size()", greaterThan(0)));
    }

    @Test
    void testGetById() throws Exception {
        
        var order = beerOrderRepository.findAll().getFirst();

        mockMvc.perform(
                get(BeerOrderController.ORDERS_ID_PATH, order.getId())
                .with(jwtRequestPostProcessor)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", is(order.getId().toString())));
    }
}
