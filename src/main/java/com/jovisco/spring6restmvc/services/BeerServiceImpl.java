package com.jovisco.spring6restmvc.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.jovisco.spring6restmvc.model.Beer;
import com.jovisco.spring6restmvc.model.BeerStyle;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BeerServiceImpl implements BeerService {

    private Map<UUID, Beer> beerMap;

    public BeerServiceImpl() {
        this.beerMap = initBeerMap();
    }

    @Override
    public List<Beer> listBeers() {

        log.debug("Service BeerService.listBeers was called");

        return new ArrayList<>(beerMap.values());
    }

    @Override
    public Beer getBeerById(UUID id) {
        
        log.debug("Service BeerService.getBeerById was called with id: " + id);

        return beerMap.get(id);
    }
    
    @Override
    public Beer createBeer(Beer beer) {
        Beer savedBeer = Beer.builder()
            .id(UUID.randomUUID())
            .version(1)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .name(beer.getName())
            .style(beer.getStyle())
            .price(beer.getPrice())
            .quantityOnHand(beer.getQuantityOnHand())
            .upc(beer.getUpc())
            .build();

        beerMap.put(savedBeer.getId(), savedBeer);

        return savedBeer;
    }

    @Override
    public Beer updateBeer(UUID id, Beer beer) {
        // get beer by id
        Beer found = getBeerById(id);
        if (found == null) throw new RuntimeException(String.format("Beer with id = %s not found", id));

        // replace values
        String name = beer.getName();
        if (name != null && name != found.getName()) {
            found.setName(name);
        }
        Integer version = beer.getVersion();
        if (version != null && version != found.getVersion()) {
            found.setVersion(version);
        }
        BeerStyle style = beer.getStyle();
        if (style != null && style != found.getStyle()) {
            found.setStyle(style);
        }
        String upc = beer.getUpc();
        if (upc != null && upc != found.getUpc()) {
            found.setUpc(upc);
        }
        BigDecimal price = beer.getPrice();
        if (price != null && price != found.getPrice()) {
            found.setPrice(price);
        }
        Integer quantityOnHand = beer.getQuantityOnHand();
        if (quantityOnHand != null && quantityOnHand != found.getQuantityOnHand()) {
            found.setQuantityOnHand(quantityOnHand);
        }
        found.setUpdatedAt(LocalDateTime.now());
        
        // store changes
        beerMap.put(found.getId(), found);

        return found;
    }

    @Override
    public void deleteBeer(UUID id) {
       beerMap.remove(id);
    }

    private Map<UUID, Beer> initBeerMap() {

        Map<UUID, Beer> beerMap = new HashMap<>();

        Beer beer1 = Beer.builder()
            .id(UUID.randomUUID())
            .version(1)
            .name("Erdinger Weizen")
            .style(BeerStyle.WHEAT)
            .upc("12341")
            .price(new BigDecimal(9.87))
            .quantityOnHand(121)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

        Beer beer2 = Beer.builder()
            .id(UUID.randomUUID())
            .version(1)
            .name("Salvator Bock")
            .style(BeerStyle.STOUT)
            .upc("12342")
            .price(new BigDecimal(1.23))
            .quantityOnHand(122)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

        Beer beer3 = Beer.builder()
            .id(UUID.randomUUID())
            .version(1)
            .name("Paulaner Hell")
            .style(BeerStyle.PALE_ALE)
            .upc("12343")
            .price(new BigDecimal(12.34))
            .quantityOnHand(123)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();

    beerMap.put(beer1.getId(), beer1);
    beerMap.put(beer2.getId(), beer2);
    beerMap.put(beer3.getId(), beer3);

    return beerMap;

    }

}
