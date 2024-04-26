package com.jovisco.spring6restmvc.repositories;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import com.jovisco.spring6restmvc.entities.Beer;
import com.jovisco.spring6restmvc.model.BeerStyle;



public interface BeerRepository extends JpaRepository<Beer, UUID>{

    @NonNull Page<Beer> findAll(@NonNull Pageable pageable);
    Page<Beer> findAllByNameIsLikeIgnoreCase(String name, Pageable pageable);
    Page<Beer> findByNameContainingIgnoreCase(String name, Pageable pageable);
    Page<Beer> findByStyle(BeerStyle style, Pageable pageable);
    Page<Beer> findByNameContainingIgnoreCaseAndStyle(String name, BeerStyle style, Pageable pageable);
}
