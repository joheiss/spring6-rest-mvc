package com.jovisco.spring6restmvc.services;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.jovisco.spring6restmvc.repositories.BeerOrderRepository;

import jakarta.transaction.Transactional;

@SpringBootTest
public class BeerOrderServiceJpaTest {

    @Autowired
    BeerOrderRepository beerOrderRepository;

    @Autowired
    BeerOrderServiceJpa beerOrderService;

    @Test
    void testCreate() {

    }

    @Transactional
    @Test
    void testDelete() {

        var toBeDeleted = beerOrderRepository.findAll().getFirst();

        beerOrderService.delete(toBeDeleted.getId());

        assertTrue(beerOrderRepository.findById(toBeDeleted.getId()).isEmpty());
    }

    @Test
    void testGetById() {

    }

    @Test
    void testListOrders() {

    }

    @Test
    void testUpdate() {

    }
}
