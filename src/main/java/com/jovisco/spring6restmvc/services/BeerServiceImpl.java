package com.jovisco.spring6restmvc.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.jovisco.spring6restmvc.model.Beer;
import com.jovisco.spring6restmvc.model.BeerStyle;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BeerServiceImpl implements BeerService {

    @Override
    public Beer getBeerById(UUID id) {
        
        log.debug("Service BeerService.getBeerById was called with id: " + id);
        
        return Beer.builder()
            .id(id)
            .version(1)
            .name("Paulaner Hell")
            .style(BeerStyle.PALE_ALE)
            .upc("12345")
            .price(new BigDecimal(12.34))
            .quantityOnHand(123)
            .createdAt(LocalDateTime.now())
            .updatedAt(LocalDateTime.now())
            .build();
    }
    
}
