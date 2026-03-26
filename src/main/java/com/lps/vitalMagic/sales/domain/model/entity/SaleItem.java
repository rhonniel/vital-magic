package com.lps.vitalMagic.sales.domain.model.entity;

import com.lps.vitalMagic.inventory.domain.exception.InvalidAttributeException;
import com.lps.vitalMagic.inventory.domain.model.entity.Item;
import com.lps.vitalMagic.sales.domain.exception.InvalidSaleException;
import com.lps.vitalMagic.sales.domain.exception.InvalidSaleItemException;
import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "sale_item")
@Getter
public class SaleItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sale_id")
    private Long saleId;

    @Embedded
    private final ProductSnapshot productSnapshot;

    @Column
    private int quantity;

    @Column
    private BigDecimal subtotal;

    private SaleItem(ProductSnapshot productSnapshot, int quantity, BigDecimal subtotal) {

        if(quantity<=0){
            throw new InvalidSaleItemException("Sale item quantity should be more than zero");
        }

        this.productSnapshot= Objects.requireNonNull(productSnapshot);
        this.quantity = quantity;
        this.subtotal =  Objects.requireNonNull(subtotal);
    }

    public static SaleItem create(ProductSnapshot productSnapshot,int quantity, BigDecimal subtotal){
       return new SaleItem(productSnapshot,quantity,subtotal);
    }



}
