package com.jovisco.spring6restmvc.services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.jovisco.spring6restmvc.mappers.BeerOrderMapper;
import com.jovisco.spring6restmvc.model.BeerOrderDTO;
import com.jovisco.spring6restmvc.repositories.BeerOrderRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class BeerOrderServiceJpa implements BeerOrderService {

    private final BeerOrderRepository beerOrderRepository;
    private final BeerOrderMapper beerOrderMapper;

    @Cacheable(cacheNames = "ordersCache")
    @Override
    public Page<BeerOrderDTO> listOrders(Integer pageSize, Integer pageNumber) {

        var pageRequest = BeerPageRequestBuilder.build(null, null);
        var pageableSize = Pageable.ofSize(pageRequest.getPageSize());

        return beerOrderRepository.findAll(pageableSize).map(beerOrderMapper::orderToDto);
    }

    @Cacheable(cacheNames = "orderCache")
    @Override
    public Optional<BeerOrderDTO> getById(UUID id) {

        return Optional.ofNullable(
            beerOrderMapper.orderToDto(beerOrderRepository.findById(id).orElse(null))
        );
    }

}
