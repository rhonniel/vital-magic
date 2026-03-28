package com.lps.vitalMagic.sales.domain.model.entity;

import com.lps.vitalMagic.sales.domain.exception.InvalidSaleException;
import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Entity
@Table
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @OneToMany(cascade = CascadeType.ALL , orphanRemoval = true)
    @JoinColumn(name = "sale_id")
    private List<SaleItem> items;

    @Column(name = "total_amount")
    @Getter
    private BigDecimal totalAmount;

    private Sale(List<SaleItem> items,BigDecimal totalAmount) {
        Objects.requireNonNull(items);

        if(items.isEmpty()){
            throw new InvalidSaleException("Sale Items shouldn't be empty");
        }
        for(SaleItem saleItem:items){
            this.items.add(Objects.requireNonNull(saleItem));
        }
        this.totalAmount = Objects.requireNonNull(totalAmount);

    }

    public static Sale create(List<SaleItem> items,BigDecimal totalAmount){
        return new Sale(items,totalAmount);
    }

    public List<SaleItem> getItems(){
        return Collections.unmodifiableList(items);
    }



}
