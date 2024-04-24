package com.jovisco.spring6restmvc.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.jovisco.spring6restmvc.model.BeerDTO;

public interface BeerService {
    List<BeerDTO> listBeers();
    Optional<BeerDTO> getBeerById(UUID id);
    BeerDTO createBeer(BeerDTO beer);
    Optional<BeerDTO> updateBeer(UUID id, BeerDTO beer);
    void deleteBeer(UUID id);
}
