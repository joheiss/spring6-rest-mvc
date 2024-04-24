package com.jovisco.spring6restmvc.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.RestController;

import com.jovisco.spring6restmvc.model.BeerDTO;
import com.jovisco.spring6restmvc.services.BeerService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@RequiredArgsConstructor
@RestController
public class BeerController {
    public static final String BEERS_PATH = "/api/v1/beers";
    public static final String BEERS_PATH_ID = "/api/v1/beers/{id}";
    public static final String BEER_PATH_ID = null;

    private final BeerService beerService;
    
    @GetMapping(BEERS_PATH)
    public List<BeerDTO> listBeers() {
        
        log.debug("Controller BeerController.listBeers was called");

        return beerService.listBeers();
    }

    @GetMapping(BEERS_PATH_ID)
    public BeerDTO getBeerById(@PathVariable UUID id) {

        log.debug("Controller BeerController.getBeerById was called with id: " + id);

        return beerService.getBeerById(id).orElseThrow(NotFoundException::new);
    }

    @PostMapping(BEERS_PATH)
    public ResponseEntity<HttpStatus> createBeer(@Validated @RequestBody BeerDTO beer) {
        // create beer object from request body
        BeerDTO savedBeer = beerService.createBeer(beer);

        // set Location header
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", BEERS_PATH + "/" + savedBeer.getId().toString());

        // HTTP status = 201 CREATED
        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @PutMapping(BEERS_PATH_ID)
    public ResponseEntity<HttpStatus> updateBeerById(@PathVariable UUID id, @Validated @RequestBody BeerDTO beer) {
        // update beer object from request body
        beerService.updateBeer(id, beer).orElseThrow(NotFoundException::new);

        // return HTTP status
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(BEERS_PATH_ID)
    public ResponseEntity<HttpStatus> deleteBeerById(@PathVariable UUID id) {
        // delete beer object
        beerService.deleteBeer(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping(BEERS_PATH_ID)
    public ResponseEntity<HttpStatus> patchBeerById(@PathVariable UUID id, @RequestBody BeerDTO beer) {
        // update beer object from request body
        beerService.updateBeer(id, beer).orElseThrow(NotFoundException::new);

        // return HTTP status
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
