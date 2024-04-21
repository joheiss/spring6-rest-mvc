package com.jovisco.spring6restmvc.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.RestController;

import com.jovisco.spring6restmvc.model.Beer;
import com.jovisco.spring6restmvc.services.BeerService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("api/v1/beers")
public class BeerController {
    
    private final BeerService beerService;
    
    @GetMapping
    public List<Beer> listBeers() {
        
        log.debug("Controller BeerController.listBeers was called");

        return beerService.listBeers();
    }

    @GetMapping("/{id}")
    public Beer getBeerById(@PathVariable UUID id) {

        log.debug("Controller BeerController.getBeerById was called with id: " + id);

        return beerService.getBeerById(id);
    }

    @PostMapping
    public ResponseEntity<HttpStatus> createBeer(@RequestBody Beer beer) {
        // create beer object from request body
        Beer savedBeer = beerService.createBeer(beer);

        // set Location header
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", String.format("/api/v1/beers/%s", savedBeer.getId().toString()));

        // HTTP status = 201 CREATED
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HttpStatus> updateBeerById(@PathVariable UUID id, @RequestBody Beer beer) {
        // update beer object from request body
        beerService.updateBeer(id, beer);

        // return HTTP status
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteBeerById(@PathVariable UUID id) {
        // delete beer object
        beerService.deleteBeer(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> patchBeerById(@PathVariable UUID id, @RequestBody Beer beer) {
        // update beer object from request body
        beerService.updateBeer(id, beer);

        // return HTTP status
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
