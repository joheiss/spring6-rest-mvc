package com.jovisco.spring6restmvc.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jovisco.spring6restmvc.entities.BeerOrderLine;

@Repository
public interface BeerOrderLineRepository extends JpaRepository<BeerOrderLine, UUID> {

}
