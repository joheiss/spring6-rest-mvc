package com.jovisco.spring6restmvc.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jovisco.spring6restmvc.mappers.BeerOrderMapper;
import com.jovisco.spring6restmvc.model.BeerOrderCreateDTO;
import com.jovisco.spring6restmvc.model.BeerOrderLineCreateDTO;
import com.jovisco.spring6restmvc.model.BeerOrderLineUpdateDTO;
import com.jovisco.spring6restmvc.model.BeerOrderShipmentUpdateDTO;
import com.jovisco.spring6restmvc.model.BeerOrderUpdateDTO;
import com.jovisco.spring6restmvc.repositories.BeerOrderRepository;
import com.jovisco.spring6restmvc.repositories.BeerRepository;
import com.jovisco.spring6restmvc.repositories.CustomerRepository;

import jakarta.transaction.Transactional;

import static com.jovisco.spring6restmvc.controllers.BeerControllerTest.jwtRequestPostProcessor;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashSet;
import java.util.Set;

@SpringBootTest
public class BeerOrderControllerIT {

    @Autowired
    BeerOrderController beerOrderController;

    @Autowired
    WebApplicationContext wac;

    @Autowired
    BeerOrderRepository beerOrderRepository;
    
    @Autowired
    BeerRepository beerRepository;

    @Autowired
    CustomerRepository customerRepository;
    
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

    @Test
    void testCreateBeerOrder() throws Exception {

        var customer = customerRepository.findAll().getFirst();
        var beer = beerRepository.findAll().getFirst();

        var beerOrderCreateDTO = BeerOrderCreateDTO.builder()
            .customerId(customer.getId())
            .beerOrderLines(Set.of(
                BeerOrderLineCreateDTO.builder()
                    .beerId(beer.getId())
                    .orderQuantity(1)
                    .build()
                )
            )
            .build();

        mockMvc.perform(post(BeerOrderController.ORDERS_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(beerOrderCreateDTO))
                .with(jwtRequestPostProcessor)
            )
            .andExpect(status().isCreated())
            .andExpect(header().exists("Location"));
    }

    @Transactional
    @Test
    void testUpdateOrder() throws Exception {

        var order = beerOrderRepository.findAll().getFirst();

        var lines = new HashSet<BeerOrderLineUpdateDTO>();

        order.getBeerOrderLines().forEach(beerOrderLine -> {
            lines.add(BeerOrderLineUpdateDTO.builder()
                .id(beerOrderLine.getId())
                .beerId(beerOrderLine.getBeer().getId())
                .orderQuantity(beerOrderLine.getOrderQuantity())
                .quantityAllocated(beerOrderLine.getQuantityAllocated())
                .build()
            );
        });

        var beerOrderUpdateDTO = BeerOrderUpdateDTO.builder()
                .customerId(order.getCustomer().getId())
                .customerRef("TestRef")
                .beerOrderLines(lines)
                .beerOrderShipment(BeerOrderShipmentUpdateDTO.builder()
                    .trackingNumber("123456")
                    .build()
                )
                .build();

        mockMvc.perform(
            put(BeerOrderController.ORDERS_ID_PATH, order.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(beerOrderUpdateDTO))
                .with(jwtRequestPostProcessor)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.customerRef", is("TestRef")));
    }

    @Transactional
    @Test
    void testDelete() throws Exception {

        var order = beerOrderRepository.findAll().getFirst();

        mockMvc.perform(
            delete(BeerOrderController.ORDERS_ID_PATH, order.getId())
            .with(jwtRequestPostProcessor)
        )
        .andExpect(status().isNoContent());

        assertTrue(beerOrderRepository.findById(order.getId()).isEmpty());

        // mockMvc.perform(
        //     delete(BeerOrderController.ORDERS_ID_PATH, order.getId())
        //     .with(jwtRequestPostProcessor)
        // )
        // .andExpect(status().isNotFound());
    }

}
