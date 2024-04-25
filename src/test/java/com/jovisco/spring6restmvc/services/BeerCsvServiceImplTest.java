package com.jovisco.spring6restmvc.services;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

import com.jovisco.spring6restmvc.model.BeerCSVRecord;

public class BeerCsvServiceImplTest {
 BeerCsvService beerCsvService = new BeerCsvServiceImpl();

    @Test
    void testConvertCSV() throws FileNotFoundException {

        File file = ResourceUtils.getFile("classpath:csvdata/beers.csv");

        List<BeerCSVRecord> recs = beerCsvService.convertCSV(file);

        System.out.println(recs.size());
        System.out.flush();

        assertThat(recs.size()).isGreaterThan(0);
    }}
