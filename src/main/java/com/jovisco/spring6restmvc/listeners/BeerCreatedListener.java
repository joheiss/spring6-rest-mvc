package com.jovisco.spring6restmvc.listeners;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.jovisco.spring6restmvc.events.BeerCreatedEvent;
import com.jovisco.spring6restmvc.mappers.BeerMapper;
import com.jovisco.spring6restmvc.repositories.BeerAuditRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class BeerCreatedListener {

    private final BeerMapper beerMapper;
    private final BeerAuditRepository beerAuditRepository;
    
    @Async
    @EventListener
    public void listen(BeerCreatedEvent event) {

        // write change history for beer entity
        var beerAudit = beerMapper.beerToBeerAudit(event.getBeer());
        beerAudit.setAuditEventType("BEER_CREATED");
        if (event.getAuthentication() != null && event.getAuthentication().getName() != null) {
            beerAudit.setPrincipalName(event.getAuthentication().getName());
        }
        
        var saved = beerAuditRepository.save(beerAudit);
        
        log.debug("Beer audit record saved: " + saved.getId() + ", triggered by: " + saved.getPrincipalName());
    }
}
