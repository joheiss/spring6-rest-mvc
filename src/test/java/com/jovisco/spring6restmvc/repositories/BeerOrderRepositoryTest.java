package com.jovisco.spring6restmvc.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import com.jovisco.spring6restmvc.entities.Beer;
import com.jovisco.spring6restmvc.entities.BeerOrder;
import com.jovisco.spring6restmvc.entities.BeerOrderLine;
import com.jovisco.spring6restmvc.entities.BeerOrderShipment;
import com.jovisco.spring6restmvc.entities.Customer;

import jakarta.transaction.Transactional;

// @DataJpaTest -> use @SpringBootTest here to get data provided
@SpringBootTest
public class BeerOrderRepositoryTest {

    @Autowired
    BeerOrderRepository beerOrderRepository;

    @Autowired
    BeerOrderLineRepository beerOrderLineRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    BeerRepository beerRepository;

    Customer testCustomer;
    Beer testBeer;
    
    @BeforeEach
    void setUp() {
        testCustomer = customerRepository.findAll().getFirst();
        testBeer = beerRepository.findAll().getFirst();
    }

    @Test
    void testBeerOrdersTableIsEmpty() {
        assertThat(beerOrderRepository.count()).isEqualTo(0);
    }

    @Test
    void testCustomerAndTestBeerExist() {
        assertThat(testCustomer.getName()).isNotBlank();
        assertThat(testBeer.getName()).isNotBlank();
    }

    @Rollback
    @Transactional
    @Test
    void testCreateBeerOrder() {

        // Set<BeerOrderLine> orderLines = new HashSet<BeerOrderLine>();
        // orderLines.add(orderLine);

        BeerOrder order = BeerOrder.builder()
            .customer(testCustomer)
            .customerRef("PO 4711-123")
            .beerOrderShipment(BeerOrderShipment.builder().trackingNumber("20240429-1001").build())
            // .beerOrderLines(orderLines)
            .build();

        var savedBeerOrder = beerOrderRepository.save(order);

        BeerOrderLine orderLine = BeerOrderLine.builder()
            .beer(testBeer)
            .orderQuantity(10)
            .quantityAllocated(0)
            .beerOrder(savedBeerOrder)
            .build();

        var savedBeerOrderLine = beerOrderLineRepository.save(orderLine);

        // var found = beerOrderRepository.findById(savedBeerOrder.getId()).get();

        assertThat(savedBeerOrder).isNotNull();
        assertThat(savedBeerOrder.getId()).isNotNull();

        assertThat(savedBeerOrderLine).isNotNull();
        assertThat(savedBeerOrderLine.getId()).isNotNull();

    }
}
