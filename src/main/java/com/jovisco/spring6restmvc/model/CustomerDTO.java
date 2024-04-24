package com.jovisco.spring6restmvc.model;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerDTO {
    private UUID id;
    private Integer version;
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
