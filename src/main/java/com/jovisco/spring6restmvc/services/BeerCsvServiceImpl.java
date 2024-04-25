package com.jovisco.spring6restmvc.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.jovisco.spring6restmvc.model.BeerCSVRecord;
import com.opencsv.bean.CsvToBeanBuilder;


@Primary
@Service
public class BeerCsvServiceImpl implements BeerCsvService{

    @Override
    public List<BeerCSVRecord> convertCSV(File csvFile) {

         try {
            List<BeerCSVRecord> beerCSVRecords = new CsvToBeanBuilder<BeerCSVRecord>(new FileReader(csvFile))
                    .withType(BeerCSVRecord.class)
                    .build()
                    .parse();
            return beerCSVRecords;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
