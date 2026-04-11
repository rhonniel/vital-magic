package com.lps.vitalMagic.sales.domain.model.entity;

import com.lps.vitalMagic.sales.domain.exception.InvalidSaleException;
import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;
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

    SaleItem(ProductSnapshot productSnapshot, int quantity) {

        if(quantity<=0){
            throw new InvalidSaleException("Sale item quantity should be more than zero");
        }

        this.productSnapshot= Objects.requireNonNull(productSnapshot);
        this.quantity = quantity;
        calculateSubtotal();
    }

    private void calculateSubtotal(){
        this.subtotal=productSnapshot.getUnitPrice().multiply(BigDecimal.valueOf(quantity)).setScale(2, RoundingMode.HALF_UP);
    }



}
