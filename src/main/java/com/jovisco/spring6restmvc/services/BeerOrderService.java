package com.jovisco.spring6restmvc.services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;

import com.jovisco.spring6restmvc.entities.BeerOrder;
import com.jovisco.spring6restmvc.model.BeerOrderCreateDTO;
import com.jovisco.spring6restmvc.model.BeerOrderDTO;
import com.jovisco.spring6restmvc.model.BeerOrderUpdateDTO;

public interface BeerOrderService {
    Page<BeerOrderDTO> listOrders(Integer pageSize, Integer pageNumber);
    Optional<BeerOrderDTO> getById(UUID id);
    BeerOrder create(BeerOrderCreateDTO order);
    BeerOrderDTO update(UUID id, BeerOrderUpdateDTO beer);
    void delete(UUID id);

}
