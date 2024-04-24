package com.jovisco.spring6restmvc.bootstrap;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.jovisco.spring6restmvc.entities.Beer;
import com.jovisco.spring6restmvc.entities.Customer;
import com.jovisco.spring6restmvc.model.BeerStyle;
import com.jovisco.spring6restmvc.repositories.BeerRepository;
import com.jovisco.spring6restmvc.repositories.CustomerRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class BootstrapData implements CommandLineRunner {

    private final BeerRepository beerRepository;
    private final CustomerRepository customerRepository;

    @Override
    public void run(String... args) throws Exception {

        log.debug("Bootstrapping is running");

        loadBeerData();
        loadCustomerData();
    }

    private void loadBeerData() {

        if (beerRepository.count() > 0) return;

        log.debug("... load test data for beers ...");

        var now = LocalDateTime.now();
        
        Beer beer1 = Beer.builder()
            .name("Erdinger Weizen")
            .style(BeerStyle.WHEAT)
            .upc("12341")
            .price(new BigDecimal(9.87))
            .quantityOnHand(121)
            .createdAt(now)
            .updatedAt(now)
            .build();

        Beer beer2 = Beer.builder()
            .name("Salvator Bock")
            .style(BeerStyle.STOUT)
            .upc("12342")
            .price(new BigDecimal(1.23))
            .quantityOnHand(122)
            .createdAt(now)
            .updatedAt(now)
            .build();

        Beer beer3 = Beer.builder()
            .name("Paulaner Hell")
            .style(BeerStyle.PALE_ALE)
            .upc("12343")
            .price(new BigDecimal(12.34))
            .quantityOnHand(123)
            .createdAt(now)
            .updatedAt(now)
            .build();

        beerRepository.saveAll(Arrays.asList(beer1, beer2, beer3));
    }
    
    private void loadCustomerData() {

        if (customerRepository.count() > 0) return;

        log.debug("... load test data for customers ...");

        var now = LocalDateTime.now();

        Customer customer1 = Customer.builder()
            .name("Hampelmann AG")
            .createdAt(now)
            .updatedAt(now)
            .build();

        Customer customer2 = Customer.builder()
            .name("Klosterfrau GmbH")
            .createdAt(now)
            .updatedAt(now)
            .build();

        customerRepository.saveAll(Arrays.asList(customer1, customer2));
    }

}
