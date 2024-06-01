package com.jovisco.spring6restmvc.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jovisco.spring6restmvc.entities.BeerAudit;

public interface BeerAuditRepository extends JpaRepository<BeerAudit, UUID> {

}
