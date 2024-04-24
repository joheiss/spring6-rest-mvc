package com.jovisco.spring6restmvc.repositories;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.jovisco.spring6restmvc.entities.Beer;
import com.jovisco.spring6restmvc.model.BeerStyle;

import jakarta.validation.ConstraintViolationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

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

        beerRepository.flush();

        assertThat(savedBeer).isNotNull();
        assertThat(savedBeer.getId()).isNotNull();
    }

    @Test
    void testSaveBeerWithMissingMandatoryFields() {

        // try to save beer with missing mandatory attribute value for "price"
        beerRepository.save(Beer.builder()
            .name("Spaten Hell")
            .style(BeerStyle.PALE_ALE)
            .quantityOnHand(123)
            .upc("47119")
            .build()
        );

        // verify that save throws an exception
        assertThatExceptionOfType(ConstraintViolationException.class)
            .isThrownBy(() -> beerRepository.flush());
    }

    @Test
    void testSaveBeerWithInvalidFields() {

        // try to save beer with column "name" being longer than allowed 
        beerRepository.save(Beer.builder()
            .name("Spaten Hell ist das schlechteste Bier, das ich in meiner Jugend getrunken habe")
            // .name("Spaten Hell")
            .style(BeerStyle.PALE_ALE)
            .price(new BigDecimal(13.24))
            .quantityOnHand(123)
            .upc("47119")
            .build()
        );

        // verify that save throws an exception
        assertThatExceptionOfType(Exception.class)
            .isThrownBy(() -> beerRepository.flush());
    }

}
