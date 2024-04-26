package com.jovisco.spring6restmvc.repositories;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.jovisco.spring6restmvc.bootstrap.BootstrapData;
import com.jovisco.spring6restmvc.entities.Beer;
import com.jovisco.spring6restmvc.model.BeerStyle;
import com.jovisco.spring6restmvc.services.BeerCsvServiceImpl;

import jakarta.validation.ConstraintViolationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@DataJpaTest
@Import({BootstrapData.class, BeerCsvServiceImpl.class})
public class BeerRepositoryTest {

    private Pageable pageSize = Pageable.ofSize(25);

    @Autowired
    BeerRepository beerRepository;

    @Test
    void testFindBeerByName() {

        // List<Beer> beers = beerRepository.findAllByNameIsLikeIgnoreCase("%IPA%");
        // assertThat(beers.size()).isEqualTo(336);

        Page<Beer> beers = beerRepository.findByNameContainingIgnoreCase("IPA", pageSize);

        assertThat(beers.getTotalElements()).isEqualTo(336);
    }

    @Test
    void testFindBeerByStyle() {

        Page<Beer> beers = beerRepository.findByStyle(BeerStyle.IPA, pageSize);

        assertThat(beers.getTotalElements()).isEqualTo(547);
    }

    @Test
    void testFindBeerByNameAndStyle() {

        Page<Beer> beers = beerRepository.findByNameContainingIgnoreCaseAndStyle("IPA", BeerStyle.IPA, pageSize);

        assertThat(beers.getTotalElements()).isEqualTo(310);
    }

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
