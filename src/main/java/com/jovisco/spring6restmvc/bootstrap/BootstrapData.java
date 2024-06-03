package com.jovisco.spring6restmvc.bootstrap;

import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.jovisco.spring6restmvc.entities.Beer;
import com.jovisco.spring6restmvc.entities.BeerOrder;
import com.jovisco.spring6restmvc.entities.BeerOrderLine;
import com.jovisco.spring6restmvc.entities.Customer;
import com.jovisco.spring6restmvc.model.BeerCSVRecord;
import com.jovisco.spring6restmvc.model.BeerStyle;
import com.jovisco.spring6restmvc.repositories.BeerOrderRepository;
import com.jovisco.spring6restmvc.repositories.BeerRepository;
import com.jovisco.spring6restmvc.repositories.CustomerRepository;
import com.jovisco.spring6restmvc.services.BeerCsvService;

import lombok.RequiredArgsConstructor;
import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class BootstrapData implements CommandLineRunner {

    private final BeerRepository beerRepository;
    private final BeerCsvService beerCsvService;
    private final CustomerRepository customerRepository;
    private final BeerOrderRepository beerOrderRepository;
    
    @Override
    public void run(String... args) throws Exception {

        log.debug("Bootstrapping is running");

        loadBeerData();
        loadBeerDataFromCsv();
        loadCustomerData();
        loadOrderData();
    }

    private void loadBeerData() {

        if (beerRepository.count() > 0) return;

        log.debug("... load test data for beers ...");

        Beer beer1 = Beer.builder()
            .name("Erdinger Weizen")
            .style(BeerStyle.WHEAT)
            .upc("12341")
            .price(new BigDecimal(9.87))
            .quantityOnHand(121)
            .build();

        Beer beer2 = Beer.builder()
            .name("Salvator Bock")
            .style(BeerStyle.STOUT)
            .upc("12342")
            .price(new BigDecimal(1.23))
            .quantityOnHand(122)
            .build();

        Beer beer3 = Beer.builder()
            .name("Paulaner Hell")
            .style(BeerStyle.PALE_ALE)
            .upc("12343")
            .price(new BigDecimal(12.34))
            .quantityOnHand(123)
            .build();

        beerRepository.saveAll(Arrays.asList(beer1, beer2, beer3));
    }
    
    private void loadCustomerData() {

        if (customerRepository.count() > 0) return;

        log.debug("... load test data for customers ...");

        Customer customer1 = Customer.builder()
            .name("Hampelmann AG")
            .build();

        Customer customer2 = Customer.builder()
            .name("Klosterfrau GmbH")
            .build();

        customerRepository.saveAll(Arrays.asList(customer1, customer2));
    }

    private void loadBeerDataFromCsv() throws FileNotFoundException {

        if (beerRepository.count() < 10) {
            
            log.debug("... load test data for beers from CSV file ...");

            // get CSV file
            // File file = ResourceUtils.getFile("classpath:csvdata/beers.csv");
            var file = BootstrapData.class.getResourceAsStream("/csvdata/beers.csv");
            log.debug("*** CSV file: " + file);
            
            // convert csv data to pojo
            List<BeerCSVRecord> records = beerCsvService.convertCSV(file);

            // map csv data to beer entity and save on database
            records.forEach(record -> beerRepository.save(buildBeerEntityFromCsvRecord(record)));
        }
    }

    private Beer buildBeerEntityFromCsvRecord(BeerCSVRecord record) {
        return Beer.builder()
            .name(StringUtils.abbreviate(record.getBeer(), 50))
            .style(mapToBeerStyle(record.getStyle()))
            .price(BigDecimal.TEN)
            .upc(record.getRow().toString())
            .quantityOnHand(record.getCount())
            .build();
    }

    private BeerStyle mapToBeerStyle(String style) {
        BeerStyle beerStyle = switch (style) {
            case "American Pale Lager" -> BeerStyle.LAGER;
            case "American Pale Ale (APA)", "American Black Ale", "Belgian Dark Ale", "American Blonde Ale" ->
                    BeerStyle.ALE;
            case "American IPA", "American Double / Imperial IPA", "Belgian IPA" -> BeerStyle.IPA;
            case "American Porter" -> BeerStyle.PORTER;
            case "Oatmeal Stout", "American Stout" -> BeerStyle.STOUT;
            case "Saison / Farmhouse Ale" -> BeerStyle.SAISON;
            case "Fruit / Vegetable Beer", "Winter Warmer", "Berliner Weissbier" -> BeerStyle.WHEAT;
            case "English Pale Ale" -> BeerStyle.PALE_ALE;
            default -> BeerStyle.PILSNER;
        };
        return beerStyle;
    }

    private void loadOrderData() {
        
        if (beerOrderRepository.count() == 0) {
            val customers = customerRepository.findAll();
            val beers = beerRepository.findAll();

            val beerIterator = beers.iterator();

            customers.forEach(customer -> {
                beerOrderRepository.save(BeerOrder.builder()
                    .customer(customer)
                    .beerOrderLines(Set.of(
                            BeerOrderLine.builder()
                                    .beer(beerIterator.next())
                                    .orderQuantity(1)
                                    .build(),
                            BeerOrderLine.builder()
                                    .beer(beerIterator.next())
                                    .orderQuantity(2)
                                    .build()
                        )
                    )
                    .build()
                );

                beerOrderRepository.save(BeerOrder.builder()
                    .customer(customer)
                    .beerOrderLines(Set.of(
                            BeerOrderLine.builder()
                                    .beer(beerIterator.next())
                                    .orderQuantity(1)
                                    .build(),
                            BeerOrderLine.builder()
                                    .beer(beerIterator.next())
                                    .orderQuantity(2)
                                    .build()
                        )
                    )
                    .build()
                );
            });
            beerOrderRepository.findAll();
        }
    }
}
