package com.jovisco.spring6restmvc.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.jovisco.spring6restmvc.entities.Customer;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class CustomerRepositoryTest {

    @Autowired
    CustomerRepository customerRepository;

    @Test
    void testSaveCustomer() {
        Customer savedCustomer = customerRepository.save(Customer.builder()
            .name("BierDimpfel OHG")
            .build()
        );

        assertThat(savedCustomer).isNotNull();
        assertThat(savedCustomer.getId()).isNotNull();

    }
}
