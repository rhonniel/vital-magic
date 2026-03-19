package com.lps.vitalMagic.product.domain.model.entity;

import com.lps.vitalMagic.product.domain.model.enums.ProductType;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table
public class Product {
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

}
