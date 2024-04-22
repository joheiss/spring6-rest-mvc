package com.jovisco.spring6restmvc.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.jovisco.spring6restmvc.model.Beer;

public interface BeerService {
    List<Beer> listBeers();
    Optional<Beer> getBeerById(UUID id);
    Beer createBeer(Beer beer);
    Beer updateBeer(UUID id, Beer beer);
    void deleteBeer(UUID id);
}
