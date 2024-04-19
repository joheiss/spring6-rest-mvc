package com.jovisco.spring6restmvc.controllers;

import java.util.UUID;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.jovisco.spring6restmvc.model.Beer;

@SpringBootTest
public class BeerControllerTest {

    @Autowired
    BeerController controller;

    @Test
    void testGetBeerById() {
        Beer beer = controller.getBeerById(UUID.randomUUID());
        System.out.println("The beer: " + beer);
        System.out.flush();
        Assertions.assertThat(beer.getName()).contains("Paulaner");
    }
}
