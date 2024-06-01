package com.jovisco.spring6restmvc.model;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BeerOrderLineDTO {

     private UUID id;

    private Long version;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private BeerDTO beer;

    @Min(value = 1, message = "Quantity on hand must be greater than 0")
    private Integer orderQuantity;
    private Integer quantityAllocated;
}
