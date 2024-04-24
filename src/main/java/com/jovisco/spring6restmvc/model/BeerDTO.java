package com.jovisco.spring6restmvc.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class BeerDTO {
    private UUID id;
    private Integer version;
    @NotBlank @NotNull private String name;
    @NotNull private BeerStyle style;
    @NotBlank @NotNull private String upc;
    private Integer quantityOnHand;
    @NotNull private BigDecimal price;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
