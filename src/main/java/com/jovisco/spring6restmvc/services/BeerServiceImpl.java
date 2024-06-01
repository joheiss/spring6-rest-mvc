package com.jovisco.spring6restmvc.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.jovisco.spring6restmvc.model.BeerDTO;
import com.jovisco.spring6restmvc.model.BeerStyle;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BeerServiceImpl implements BeerService {

    private Map<UUID, BeerDTO> beerMap;

    public BeerServiceImpl() {
        this.beerMap = initBeerMap();
    }

    @Override
    public Page<BeerDTO> getAllBeers() {

        var pageRequest = BeerPageRequestBuilder.build(null, null);
        var size = this.beerMap.size();
        var from = Math.min(pageRequest.getPageSize() * pageRequest.getPageNumber(), size);;
        var to = Math.min(from + pageRequest.getPageSize(), size);

        return new PageImpl<BeerDTO>(new ArrayList<>(beerMap.values()).subList(from, to));
    }

    @Override
    public Page<BeerDTO> listBeers(String name, BeerStyle style, Boolean showInventory, Integer pageSize, Integer pageNumber) {

        var pageRequest = BeerPageRequestBuilder.build(null, null);
        var size = this.beerMap.size();
        var from = Math.min(pageRequest.getPageSize() * pageRequest.getPageNumber(), size);;
        var to = Math.min(from + pageRequest.getPageSize(), size);

        Page<BeerDTO> beers = getAllBeers();

        var beerStream = beers.stream();
        if (StringUtils.hasText(name)) {
            beerStream.filter(beer -> beer.getName().contains(name));
        }

        if (style != null) {
            beerStream.filter(beer -> beer.getStyle().equals(style));
        }

        if (showInventory != null && !showInventory) {
            beerStream.map(beer -> {
                beer.setQuantityOnHand(null);
                return beer;
            });
        }

        return new PageImpl<>(
            beerStream.collect(Collectors.toList())
                .subList(from, to)
        );
    }

    @Override
    public Optional<BeerDTO> getBeerById(UUID id) {
        
        log.debug("Service BeerService.getBeerById was called with id: " + id);

        return Optional.of(beerMap.get(id));
    }
    
    @Override
    public BeerDTO createBeer(BeerDTO beer) {

        BeerDTO savedBeer = BeerDTO.builder()
            .id(UUID.randomUUID())
            .version(1)
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
    public Optional<BeerDTO> updateBeer(UUID id, BeerDTO beer) {
        // get beer by id
        BeerDTO found = getBeerById(id).orElse(null);

        if (found == null) return Optional.ofNullable(found);

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
        
        // store changes
        beerMap.put(found.getId(), found);

        return Optional.ofNullable(found);
    }

    @Override
    public void deleteBeer(UUID id) {
       beerMap.remove(id);
    }

    private Map<UUID, BeerDTO> initBeerMap() {

        Map<UUID, BeerDTO> beerMap = new HashMap<>();

        BeerDTO beer1 = BeerDTO.builder()
            .id(UUID.randomUUID())
            .version(1)
            .name("Erdinger Weizen")
            .style(BeerStyle.WHEAT)
            .upc("12341")
            .price(new BigDecimal(9.87))
            .quantityOnHand(121)
            .build();

        BeerDTO beer2 = BeerDTO.builder()
            .id(UUID.randomUUID())
            .version(1)
            .name("Salvator Bock")
            .style(BeerStyle.STOUT)
            .upc("12342")
            .price(new BigDecimal(1.23))
            .quantityOnHand(122)
            .build();

        BeerDTO beer3 = BeerDTO.builder()
            .id(UUID.randomUUID())
            .version(1)
            .name("Paulaner Hell")
            .style(BeerStyle.PALE_ALE)
            .upc("12343")
            .price(new BigDecimal(12.34))
            .quantityOnHand(123)
            .build();

    beerMap.put(beer1.getId(), beer1);
    beerMap.put(beer2.getId(), beer2);
    beerMap.put(beer3.getId(), beer3);

    return beerMap;

    }

}
