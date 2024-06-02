package com.jovisco.spring6restmvc.model;

import java.util.Set;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BeerOrderUpdateDTO {
    private String customerRef;

    @NotNull
    private UUID customerId;

    private Set<BeerOrderLineUpdateDTO> beerOrderLines;

    private BeerOrderShipmentUpdateDTO beerOrderShipment;
}
