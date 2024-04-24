package com.jovisco.spring6restmvc.controllers;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jovisco.spring6restmvc.model.BeerDTO;
import com.jovisco.spring6restmvc.services.BeerService;
import com.jovisco.spring6restmvc.services.BeerServiceImpl;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.core.Is.is;

@WebMvcTest(BeerController.class)
public class BeerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    BeerService beerService;
    
    BeerServiceImpl beerServiceImpl;

    @BeforeEach
    void setUp() {
        beerServiceImpl = new BeerServiceImpl();
    }

    @Test
    void testListBeers() throws Exception {
        // get list of beers to be returned by the test
        List<BeerDTO> testBeers = beerServiceImpl.listBeers();
       given(beerService.listBeers()).willReturn(testBeers);

       // mock the GET request
       mockMvc.perform(get(BeerController.BEERS_PATH)
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.length()", is(testBeers.size())));
    }

    @Test
    void testGetBeerById() throws Exception {
        // get 1st beer from list to return that as test data
        BeerDTO testBeer = beerServiceImpl.listBeers().get(0);
        given(beerService.getBeerById(testBeer.getId())).willReturn(Optional.of(testBeer));

        mockMvc.perform(get(BeerController.BEERS_PATH_ID, testBeer.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id", is(testBeer.getId().toString())))
            .andExpect(jsonPath("$.name", is(testBeer.getName())));
    }

    @Test
    void testGetBeerByIdNotFound() throws Exception {
        // prepare the NotFoundExcepion
        given(beerService.getBeerById(any(UUID.class))).willReturn(Optional.empty());

        mockMvc
            .perform(get(BeerController.BEERS_PATH_ID, UUID.randomUUID()))
            .andExpect(status().isNotFound());
    }

    @Test
    void testCreateBeer() throws Exception {
        // get 1st item from list of beers for mapping to jSON
        BeerDTO testBeer = beerServiceImpl.listBeers().get(0);
        testBeer.setId(null);
        testBeer.setVersion(null);
        testBeer.setCreatedAt(null);
        testBeer.setUpdatedAt(null);

        // this is needed to have a return value for the mocked "createBeer" within the beer controller
        given(beerService.createBeer(any(BeerDTO.class))).willReturn(beerServiceImpl.listBeers().get(1));

        mockMvc
            .perform(post(BeerController.BEERS_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testBeer))
                .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isCreated())
            .andExpect(header().exists("Location"));
    }

    @Test
    void testUpdateBeerById() throws Exception {
        // get 1st item on list for testing
        BeerDTO found = beerServiceImpl.listBeers().get(0);
        BeerDTO testBeer = BeerDTO.builder().build();
        BeanUtils.copyProperties(found, testBeer);
        testBeer.setPrice(new BigDecimal(11.22));

        given(beerService.updateBeer(any(), any())).willReturn(Optional.of(found));

        mockMvc
            .perform(put(BeerController.BEERS_PATH_ID, testBeer.getId())
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testBeer))
                .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isNoContent());

        // verify that BeerService was called ONCE with proper arguments
        verify(beerService, times(1)).updateBeer(testBeer.getId(), testBeer);
    }

    @Test
    void testDeleteBeerById() throws Exception {
        // get 1st item from list of beers
        BeerDTO testBeer = beerServiceImpl.listBeers().get(0);

        mockMvc
            .perform(delete(BeerController.BEERS_PATH_ID, testBeer.getId()))
            .andExpect(status().isNoContent());

        // verify that service method was called 
        verify(beerService, times(1)).deleteBeer(testBeer.getId());
    }

    @Test
    void testPatchBeerById() throws Exception {
        // get 1st item on list for testing
        BeerDTO found = beerServiceImpl.listBeers().get(0);
        BeerDTO testBeer = BeerDTO.builder()
            .id(found.getId())
            .price(new BigDecimal(11.33))
            .build();
    
        given(beerService.updateBeer(any(), any())).willReturn(Optional.of(found));

        mockMvc
            .perform(patch(BeerController.BEERS_PATH_ID, testBeer.getId())
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testBeer))
                .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isNoContent());

        // verify that BeerService was called ONCE with proper arguments
        verify(beerService, times(1)).updateBeer(testBeer.getId(), testBeer);
    }

}
