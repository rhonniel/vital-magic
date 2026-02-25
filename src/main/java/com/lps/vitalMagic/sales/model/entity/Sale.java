package com.lps.vitalMagic.sales.model.entity;

import com.lps.vitalMagic.core.entity.Auditory;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL , orphanRemoval = true)
    @JoinColumn(name = "sale_id")
    private List<SaleItem> items= new ArrayList<>();

    @Column(name = "total_amount")
    private BigDecimal totalAmount;

    @Embedded
    private Auditory auditory;
}
