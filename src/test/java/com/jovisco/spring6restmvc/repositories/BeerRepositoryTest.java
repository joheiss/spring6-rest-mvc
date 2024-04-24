package com.jovisco.spring6restmvc.repositories;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.jovisco.spring6restmvc.entities.Beer;
import com.jovisco.spring6restmvc.model.BeerStyle;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class BeerRepositoryTest {

    @Autowired
    BeerRepository beerRepository;

    @Test
    void testSaveBeer() {
        Beer savedBeer = beerRepository.save(Beer.builder()
            .name("Spaten Hell")
            .style(BeerStyle.PALE_ALE)
            .price(new BigDecimal(13.24))
            .quantityOnHand(123)
            .upc("47119")
            .build()
        );

        assertThat(savedBeer).isNotNull();
        assertThat(savedBeer.getId()).isNotNull();
    }
}
