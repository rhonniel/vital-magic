package com.lps.vitalMagic.product.model.entity;

import com.lps.vitalMagic.core.entity.Auditory;
import com.lps.vitalMagic.product.model.enums.ProductType;
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

    @Embedded
    private Auditory auditory;

    @Column
    private boolean active;

}
