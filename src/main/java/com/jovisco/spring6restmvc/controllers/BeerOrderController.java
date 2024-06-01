package com.jovisco.spring6restmvc.controllers;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jovisco.spring6restmvc.model.BeerOrderDTO;
import com.jovisco.spring6restmvc.services.BeerOrderService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class BeerOrderController {

    public static final String ORDERS_PATH = "/api/v1/orders";
    public static final String ORDERS_ID_PATH = "/api/v1/orders/{id}";

    private final BeerOrderService beerOrderService;

    @GetMapping(ORDERS_PATH)
    public Page<BeerOrderDTO> listOrders(
        @RequestParam(required = false) Integer pageSize,
        @RequestParam(required = false) Integer pageNumber
    ) {
        return beerOrderService.listOrders(pageSize, pageNumber);
    }

    @GetMapping(ORDERS_ID_PATH)
    public BeerOrderDTO getById(@PathVariable UUID id) {
        return beerOrderService.getById(id).orElseThrow(NotFoundException::new);
    }
}
