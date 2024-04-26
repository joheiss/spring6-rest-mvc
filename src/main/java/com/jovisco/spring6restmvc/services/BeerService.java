package com.jovisco.spring6restmvc.services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;

import com.jovisco.spring6restmvc.model.BeerDTO;
import com.jovisco.spring6restmvc.model.BeerStyle;

public interface BeerService {
    Page<BeerDTO> getAllBeers();
    Page<BeerDTO> listBeers(String name, BeerStyle style, Boolean showInventory, Integer pageSize, Integer pageNumber);
    Optional<BeerDTO> getBeerById(UUID id);
    BeerDTO createBeer(BeerDTO beer);
    Optional<BeerDTO> updateBeer(UUID id, BeerDTO beer);
    void deleteBeer(UUID id);
}
