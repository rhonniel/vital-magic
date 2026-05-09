package com.lps.vitalMagic.product.infrastructure.persistance.entity;

import com.lps.vitalMagic.product.domain.model.enums.ProductType;
import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;

@Entity
@Table(name = "product")
@Getter
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reference_no")
    private Long referenceNo;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_type")
    private ProductType productType;

    @Column
    private String name;

    @Column
    private BigDecimal price;


    @Column
    private boolean active;

    protected ProductEntity() {
    }

    public ProductEntity(Long id, Long referenceNo, ProductType productType, String name, BigDecimal price, boolean active) {
        this.id = id;
        this.referenceNo = referenceNo;
        this.productType = productType;
        this.name = name;
        this.price = price;
        this.active = active;
    }
}
