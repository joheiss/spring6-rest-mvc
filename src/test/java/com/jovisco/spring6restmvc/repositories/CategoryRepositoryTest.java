package com.jovisco.spring6restmvc.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.jovisco.spring6restmvc.entities.Beer;
import com.jovisco.spring6restmvc.entities.Category;


@SpringBootTest
public class CategoryRepositoryTest {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    BeerRepository beerRepository;

    Beer testBeer;

    @BeforeEach
    void setUp() {
        testBeer = beerRepository.findAll().getFirst();
    }

    @Rollback
    @Transactional
    @Test
    void testCreateCategory() {
        var savedCategory = categoryRepository.save(
            Category.builder().description("Test Category").build()
        );
        testBeer.addCategory(savedCategory);
        var savedBeer = beerRepository.save(testBeer);

        assertThat(savedBeer.getCategories().contains(savedCategory));

        System.out.println(savedBeer);
        System.out.flush();
    }
}
