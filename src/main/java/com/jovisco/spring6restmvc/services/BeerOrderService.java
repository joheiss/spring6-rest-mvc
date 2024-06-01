package com.jovisco.spring6restmvc.services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;

import com.jovisco.spring6restmvc.model.BeerOrderDTO;

public interface BeerOrderService {
    Page<BeerOrderDTO> listOrders(Integer pageSize, Integer pageNumber);
    Optional<BeerOrderDTO> getById(UUID id);
}
