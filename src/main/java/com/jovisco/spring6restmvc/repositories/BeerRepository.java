package com.jovisco.spring6restmvc.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jovisco.spring6restmvc.entities.Beer;

public interface BeerRepository extends JpaRepository<Beer, UUID>{

}
