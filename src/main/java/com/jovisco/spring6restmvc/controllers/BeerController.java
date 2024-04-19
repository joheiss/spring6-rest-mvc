package com.jovisco.spring6restmvc.controllers;

import java.util.UUID;

import org.springframework.stereotype.Controller;

import com.jovisco.spring6restmvc.model.Beer;
import com.jovisco.spring6restmvc.services.BeerService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@AllArgsConstructor
@Controller
public class BeerController {
    
    private final BeerService beerService;
    
    public Beer getBeerById(UUID id) {

        log.debug("Controller BeerController.getBeerById was called with id: " + id);

        return beerService.getBeerById(id);
    }
}
