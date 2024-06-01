package com.jovisco.spring6restmvc.services;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.jovisco.spring6restmvc.entities.Beer;
import com.jovisco.spring6restmvc.mappers.BeerMapper;
import com.jovisco.spring6restmvc.model.BeerDTO;
import com.jovisco.spring6restmvc.model.BeerStyle;
import com.jovisco.spring6restmvc.repositories.BeerRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Primary
@RequiredArgsConstructor
public class BeerServiceJpa implements BeerService {

    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;

    // use CacheManager to solve eviction issue
    private final CacheManager cacheManager;


    @Cacheable(cacheNames = "beersCache")
    @Override
    public Page<BeerDTO> getAllBeers() {

        log.info("Get all beers- in service (JPA)");

        var pageRequest = BeerPageRequestBuilder.build(null, null);
        var pageableSize = Pageable.ofSize(pageRequest.getPageSize());

        return beerRepository.findAll(pageableSize).map(beerMapper::beerToBeerDto);
    }


    @Cacheable(cacheNames = "beersCache")
    @Override
    public Page<BeerDTO> listBeers(String name, BeerStyle style, Boolean showInventory, Integer pageSize, Integer pageNumber) {

        log.info("Get beers by query - in service (JPA)");

        var pageRequest = BeerPageRequestBuilder.build(pageSize, pageNumber);

        Page<Beer> beers;

        if (StringUtils.hasText(name) && style != null) {
            beers = beerRepository.findByNameContainingIgnoreCaseAndStyle(name, style, pageRequest);
        } else if (StringUtils.hasText(name)) {
            beers = beerRepository.findByNameContainingIgnoreCase(name, pageRequest);
        } else if (style != null) {
            beers = beerRepository.findByStyle(style, pageRequest);
        } else {
            beers = beerRepository.findAll(pageRequest);
        }

        if (showInventory != null && !showInventory) {
            beers.forEach(beer -> beer.setQuantityOnHand(null));
        }

        return beers.map(beerMapper::beerToBeerDto);
    }

    @Cacheable(cacheNames = "beerCache", key = "#id")
    @Override
    public Optional<BeerDTO> getBeerById(UUID id) {

        log.info("Get beer by id - in service (JPA)");

        return Optional.ofNullable(
            beerMapper.beerToBeerDto(beerRepository
                .findById(id)
                .orElse(null)
            )
        );
    }

    @Override
    public BeerDTO createBeer(BeerDTO beer) {

        // use cache manager to evict data from list cache
        cacheManager.getCache("beersCache").clear();
        
        Beer toBeCreated = beerMapper.beerDtoToBeer(beer);
        
        return beerMapper.beerToBeerDto(beerRepository.save(toBeCreated));
    }

    
    @Override
    public Optional<BeerDTO> updateBeer(UUID id, BeerDTO beer) {

        Beer found = beerRepository.findById(id).orElse(null);

        if (found == null) return Optional.ofNullable(beerMapper.beerToBeerDto(found));

        // evict cache for changed beer
        evictCache(id);

        if (beer.getName() != null) found.setName(beer.getName());
        if (beer.getStyle() != null) found.setStyle(beer.getStyle());
        if (beer.getPrice() != null) found.setPrice(beer.getPrice());
        if (beer.getQuantityOnHand() != null) found.setQuantityOnHand((beer.getQuantityOnHand()));
        if (beer.getUpc() != null) found.setUpc(beer.getUpc());
        found.setUpdatedAt(LocalDateTime.now());
            
        return Optional.ofNullable(beerMapper.beerToBeerDto(beerRepository.save(found)));
    }

    /* --- EVICTION ON "beersCache" WLIL NOT WORK because of AOP (aspect oriented programming in Spring) */
    // @Caching(evict = {
    //     @CacheEvict(cacheNames = "beerCache", key = "#id"),
    //     @CacheEvict(cacheNames = "beersCache")
    // })
    @Override
    public void deleteBeer(UUID id) {

        // use cache manager to evict data from caches
        cacheManager.getCache("beerCache").evict(id);
        cacheManager.getCache("beersCache").clear();

        beerRepository.deleteById(id);
    }

    private void evictCache(UUID id) {

        // use cache manager to evict data from caches
        cacheManager.getCache("beerCache").evict(id);
        cacheManager.getCache("beersCache").clear();
    }
}
