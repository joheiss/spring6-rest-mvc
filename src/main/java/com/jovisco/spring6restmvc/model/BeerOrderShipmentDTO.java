package com.jovisco.spring6restmvc.model;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BeerOrderShipmentDTO {

    private UUID id;
    private Long version;

    @NotBlank
    private String trackingNumber;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
