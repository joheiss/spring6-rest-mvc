package com.jovisco.spring6restmvc.services;

import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.jovisco.spring6restmvc.controllers.NotFoundException;
import com.jovisco.spring6restmvc.entities.BeerOrder;
import com.jovisco.spring6restmvc.entities.BeerOrderLine;
import com.jovisco.spring6restmvc.entities.BeerOrderShipment;
import com.jovisco.spring6restmvc.mappers.BeerOrderMapper;
import com.jovisco.spring6restmvc.model.BeerOrderCreateDTO;
import com.jovisco.spring6restmvc.model.BeerOrderDTO;
import com.jovisco.spring6restmvc.model.BeerOrderUpdateDTO;
import com.jovisco.spring6restmvc.repositories.BeerOrderRepository;
import com.jovisco.spring6restmvc.repositories.BeerRepository;
import com.jovisco.spring6restmvc.repositories.CustomerRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class BeerOrderServiceJpa implements BeerOrderService {

    private final BeerOrderRepository beerOrderRepository;
    private final BeerOrderMapper beerOrderMapper;
    private final CustomerRepository customerRepository;
    private final BeerRepository beerRepository;
    private final CacheManager cacheManager;

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

    @Override
    public BeerOrder create(BeerOrderCreateDTO order) {

        var customer = customerRepository
            .findById(order.getCustomerId())
            .orElseThrow(NotFoundException::new);

        // evict list cache
        evictCache(null);

        var beerOrderLines = new HashSet<BeerOrderLine>();

        order.getBeerOrderLines().forEach(beerOrderLine -> {
            beerOrderLines.add(
                BeerOrderLine.builder()
                    .beer(beerRepository.findById(beerOrderLine.getBeerId()).orElseThrow(NotFoundException::new))
                    .orderQuantity(beerOrderLine.getOrderQuantity())
                    .build()
            );
        });

        return beerOrderRepository.save(BeerOrder.builder()
            .customer(customer)
            .beerOrderLines(beerOrderLines)
            .customerRef(order.getCustomerRef())
            .build());   
     }

    @Override
    public BeerOrderDTO update(UUID id, BeerOrderUpdateDTO beerOrderUpdateDTO) {

        var order = beerOrderRepository
            .findById(id)
            .orElseThrow(NotFoundException::new);

        order.setCustomer(customerRepository
            .findById(beerOrderUpdateDTO.getCustomerId())
            .orElseThrow(NotFoundException::new)
        );

        order.setCustomerRef(beerOrderUpdateDTO.getCustomerRef());

        beerOrderUpdateDTO.getBeerOrderLines().forEach(beerOrderLine -> {
            if (beerOrderLine.getBeerId() != null) {
                // update existing order line
                var found = order.getBeerOrderLines().stream()
                    .filter(beerOrderLine1 -> beerOrderLine1.getId().equals(beerOrderLine.getId()))
                    .findFirst().orElseThrow(NotFoundException::new);
                found.setBeer(beerRepository.findById(beerOrderLine.getBeerId()).orElseThrow(NotFoundException::new));
                found.setOrderQuantity(beerOrderLine.getOrderQuantity());
                found.setQuantityAllocated(beerOrderLine.getQuantityAllocated());
            } else {
                // add new order line
                order.getBeerOrderLines().add(BeerOrderLine.builder()
                    .beer(beerRepository.findById(beerOrderLine.getBeerId()).orElseThrow(NotFoundException::new))
                    .orderQuantity(beerOrderLine.getOrderQuantity())
                    .quantityAllocated(beerOrderLine.getQuantityAllocated())
                    .build()
                );
            }
        });

        // add or change shipment tracking number
        if (beerOrderUpdateDTO.getBeerOrderShipment() != null && beerOrderUpdateDTO.getBeerOrderShipment().getTrackingNumber() != null) {
            if (order.getBeerOrderShipment() == null) {
                order.setBeerOrderShipment(BeerOrderShipment.builder().trackingNumber(beerOrderUpdateDTO.getBeerOrderShipment().getTrackingNumber()).build());
            } else {
                order.getBeerOrderShipment().setTrackingNumber(beerOrderUpdateDTO.getBeerOrderShipment().getTrackingNumber());
            }
        }

        evictCache(id);

        return beerOrderMapper.orderToDto(beerOrderRepository.save(order));    
    }

    @Override
    public void delete(UUID id) {

        if (beerOrderRepository.findById(id).isPresent()) {
            evictCache(id);
            beerOrderRepository.deleteById(id);
        } else {
            throw new NotFoundException();
        }    
    }

    private void evictCache(UUID id) {
        if (id != null) {
            cacheManager.getCache("orderCache").evict(id);
        }
        cacheManager.getCache("ordersCache").clear();
    }
}
