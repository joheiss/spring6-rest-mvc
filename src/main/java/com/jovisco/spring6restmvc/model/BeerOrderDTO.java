package com.jovisco.spring6restmvc.model;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BeerOrderDTO {

    private UUID id;
    private Long version;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private String customerRef;

    @NotEmpty
    private CustomerDTO customer;

    @NotEmpty
    private Set<BeerOrderLineDTO> beerOrderLines;

    private BeerOrderShipmentDTO beerOrderShipment;
}
