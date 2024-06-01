package com.jovisco.spring6restmvc.bootstrap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import com.jovisco.spring6restmvc.repositories.BeerOrderRepository;
import com.jovisco.spring6restmvc.repositories.BeerRepository;
import com.jovisco.spring6restmvc.repositories.CustomerRepository;
import com.jovisco.spring6restmvc.services.BeerCsvService;
import com.jovisco.spring6restmvc.services.BeerCsvServiceImpl;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(BeerCsvServiceImpl.class)
public class BootstrapDataTest {

    @Autowired
    BeerRepository beerRepository;

    @Autowired
    BeerCsvService beerCsvService;

    @Autowired
    CustomerRepository customerRepository;
    
    @Autowired
    BeerOrderRepository beerOrderRepository;

    BootstrapData bootstrapData;

    @BeforeEach
    void setUp() {
        bootstrapData = new BootstrapData(beerRepository, beerCsvService, customerRepository, beerOrderRepository);
    }

    @Test
    void testRun() throws Exception {
        bootstrapData.run();

        assertThat(beerRepository.count()).isGreaterThan(0);
        assertThat(customerRepository.count()).isGreaterThan(0);
        assertThat(beerOrderRepository.count()).isGreaterThan(0);
    }
}
