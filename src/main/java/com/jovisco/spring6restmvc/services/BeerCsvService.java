package com.jovisco.spring6restmvc.services;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import com.jovisco.spring6restmvc.model.BeerCSVRecord;

public interface BeerCsvService {
    List<BeerCSVRecord> convertCSV(File csvFile);
    List<BeerCSVRecord> convertCSV(InputStream csvFile);
}
