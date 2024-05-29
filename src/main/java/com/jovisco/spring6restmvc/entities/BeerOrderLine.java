package com.jovisco.spring6restmvc.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Version;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@Entity
public class BeerOrderLine {


    public BeerOrderLine(UUID id, Long version, LocalDateTime createdAt, LocalDateTime updatedAt, Integer orderQuantity,
            Integer quantityAllocated, Beer beer, BeerOrder beerOrder) {
        this.id = id;
        this.version = version;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.orderQuantity = orderQuantity;
        this.quantityAllocated = quantityAllocated;
        this.beer = beer;
        setBeerOrder(beerOrder);
    }

    @Id
    @GeneratedValue(generator = "UUID")
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(length = 36, columnDefinition = "varchar(36)", updatable = false, nullable = false )
    private UUID id;

    @Version
    private Long version;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public boolean isNew() {
        return this.id == null;
    }

    private Integer orderQuantity;
    private Integer quantityAllocated;

    @ManyToOne
    private Beer beer;

    @ManyToOne
    private BeerOrder beerOrder;

    public void setBeerOrder(BeerOrder beerOrder) {
        this.beerOrder = beerOrder;
        beerOrder.getBeerOrderLines().add(this);
    }
}
