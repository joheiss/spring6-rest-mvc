package com.jovisco.spring6restmvc.bootstrap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.jovisco.spring6restmvc.repositories.BeerRepository;
import com.jovisco.spring6restmvc.repositories.CustomerRepository;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class BootstrapDataTest {

    @Autowired
    BeerRepository beerRepository;

    @Autowired
    CustomerRepository customerRepository;
    
    BootstrapData bootstrapData;

    @BeforeEach
    void setUp() {
        bootstrapData = new BootstrapData(beerRepository, customerRepository);
    }

    @Test
    void testRun() throws Exception {
        bootstrapData.run();

        assertThat(beerRepository.count()).isGreaterThan(0);
        assertThat(customerRepository.count()).isGreaterThan(0);
    }
}
