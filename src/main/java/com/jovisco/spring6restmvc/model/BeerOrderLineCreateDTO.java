package com.jovisco.spring6restmvc.model;

import java.util.UUID;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BeerOrderLineCreateDTO {

    @NotNull
    private UUID beerId;

    @Min(value = 1, message = "Quantity on hand must be greater than 0")
    private Integer orderQuantity;
}