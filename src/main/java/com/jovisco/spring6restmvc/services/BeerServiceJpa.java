package com.jovisco.spring6restmvc.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.jovisco.spring6restmvc.entities.Beer;
import com.jovisco.spring6restmvc.mappers.BeerMapper;
import com.jovisco.spring6restmvc.model.BeerDTO;
import com.jovisco.spring6restmvc.repositories.BeerRepository;

import lombok.RequiredArgsConstructor;

@Service
@Primary
@RequiredArgsConstructor
public class BeerServiceJpa implements BeerService {

    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;

    @Override
    public List<BeerDTO> listBeers() {
        return beerRepository.findAll()
            .stream()
            .map(beerMapper::beerToBeerDto)
            .collect(Collectors.toList());
    }

    @Override
    public Optional<BeerDTO> getBeerById(UUID id) {
        return Optional.ofNullable(
            beerMapper.beerToBeerDto(beerRepository
                .findById(id)
                .orElse(null)
            )
        );
    }

    @Override
    public BeerDTO createBeer(BeerDTO beer) {
        Beer toBeCreated = beerMapper.beerDtoToBeer(beer);
        
        return beerMapper.beerToBeerDto(beerRepository.save(toBeCreated));
    }

    @Override
    public Optional<BeerDTO> updateBeer(UUID id, BeerDTO beer) {

        Beer found = beerRepository.findById(id).orElse(null);

        if (found == null) return Optional.ofNullable(beerMapper.beerToBeerDto(found));

        if (beer.getName() != null) found.setName(beer.getName());
        if (beer.getStyle() != null) found.setStyle(beer.getStyle());
        if (beer.getPrice() != null) found.setPrice(beer.getPrice());
        if (beer.getQuantityOnHand() != null) found.setQuantityOnHand((beer.getQuantityOnHand()));
        if (beer.getUpc() != null) found.setUpc(beer.getUpc());
        found.setUpdatedAt(LocalDateTime.now());
            
        return Optional.ofNullable(beerMapper.beerToBeerDto(beerRepository.save(found)));
    }

    @Override
    public void deleteBeer(UUID id) {

        beerRepository.deleteById(id);
    }
    
}
