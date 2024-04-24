package com.jovisco.spring6restmvc.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.byLessThan;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jovisco.spring6restmvc.mappers.BeerMapper;
import com.jovisco.spring6restmvc.model.BeerDTO;
import com.jovisco.spring6restmvc.repositories.BeerRepository;


@SpringBootTest
public class BeerControllerIT {

    @Autowired
    BeerController beerController;

    @Autowired
    BeerRepository beerRepository;

    @Autowired
    BeerMapper beerMapper;

    @Autowired
    ObjectMapper objectMapper;
    
    @Autowired
    WebApplicationContext wac;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }
    
    @Rollback
    @Transactional
    @Test
    void testCreateBeer() {

        var now = LocalDateTime.now();

        // build a test beer
        var beer = BeerDTO.builder()
            .name("TestBier")
            .price(new BigDecimal(7.89))
            .quantityOnHand(1)
            .upc("47118")
            .createdAt(now)
            .updatedAt(now)
            .build();

        // call the controller method to create a beer
        var response = beerController.createBeer(beer);

        // verify that creation worked well - status code 201 and location filled
        assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        var location = response.getHeaders().getLocation();
        assertThat(location).isNotNull();

        // get id from location & verify that the beer is really present on the database
        if (location != null) {
            var segments = location.getPath().split("/");
            var id = segments[segments.length - 1];
            assertThat(id).isNotNull();
            assertThat(beerRepository.existsById(UUID.fromString(id))).isTrue();
        }
    }

    @Rollback
    @Transactional
    @Test
    void testDeleteBeerById() {
        var toBeDeleted = beerController.listBeers().get(0);
        assertThat(toBeDeleted).isNotNull();

        var response = beerController.deleteBeerById(toBeDeleted.getId());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        // verify that beer has been deleted from database
        assertThat(beerRepository.existsById(toBeDeleted.getId())).isFalse();
    }

    @Test
    void testDeleteBeerByIdNotFound() {

        var response = beerController.deleteBeerById(UUID.randomUUID());
        assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));
    }

    @Test
    void testGetBeerById() {
        var beer = beerController.listBeers().get(0);
        var found = beerController.getBeerById(beer.getId());

        assertThat(found).isNotNull();
        assertThat(found.getId()).isEqualTo(beer.getId());
    }

    @Test
    void testGetBeerByIdNotFound() {

        assertThatExceptionOfType(NotFoundException.class)
            .isThrownBy(() -> beerController.getBeerById(UUID.randomUUID()));
    }

    @Test
    void testListBeers() {
       List<BeerDTO> beers = beerController.listBeers();

       assertThat(beers.size()).isGreaterThan(0);
    }

    @Rollback
    @Transactional
    @Test
    void testListBeersIsEmpty() {
        beerRepository.deleteAll();
       List<BeerDTO> beers = beerController.listBeers();

       assertThat(beers.size()).isEqualTo(0);
    }

    @Test
    void testPatchBeerByIdWithInvalidName() throws Exception {
        // get 1st item on list for testing
        var found = beerRepository.findAll().get(0);
        
        // modify values
         Map<String, Object> beerMap = new HashMap<>();
        beerMap.put("name", found.getName() + "*UPDATED12345678901234567890123456789012345678901234567890*");
        beerMap.put("updatedAt", LocalDateTime.now());

        var result = mockMvc
            .perform(patch(BeerController.BEERS_PATH_ID, found.getId())
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(beerMap))
                .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isBadRequest())
            .andReturn();
        
        System.out.println(result.getResponse().getContentAsString());
        System.out.flush();
    }

    @Rollback
    @Transactional
    @Test
    void testUpdateBeerById() {
        // get existing beer from database
        var found = beerRepository.findAll().get(0);
        assertThat(found).isNotNull();

        // modify values
        var toBeUpdated = beerMapper.beerToBeerDto(found);
        toBeUpdated.setId(null);
        toBeUpdated.setVersion(null);
        toBeUpdated.setName(toBeUpdated.getName() + "*UPDATED*");
        toBeUpdated.setPrice(toBeUpdated.getPrice().multiply(BigDecimal.TEN));
        toBeUpdated.setQuantityOnHand(toBeUpdated.getQuantityOnHand() - 1);
        toBeUpdated.setUpdatedAt(LocalDateTime.now());

        // request controller to update beer
        var response = beerController.updateBeerById(found.getId(), toBeUpdated);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        // verify that values have been updated properly
        var updated = beerRepository.findById(found.getId()).get();
        assertThat(updated).isNotNull();
        assertThat(updated.getName()).isEqualTo(toBeUpdated.getName());
        assertThat(updated.getQuantityOnHand()).isEqualTo(toBeUpdated.getQuantityOnHand());
        assertThat(updated.getPrice()).isCloseTo(toBeUpdated.getPrice(), byLessThan(new BigDecimal("0.1")));
    }

    @Test
    void testUpdateBeerByIdNotFound() {

        // verify that calling the update with an invalid UUID throws an exception
        assertThatExceptionOfType(NotFoundException.class)
            .isThrownBy(() -> beerController.updateBeerById(UUID.randomUUID(), BeerDTO.builder().build()));
    }
}
