package com.lps.vitalMagic.purchase.domain.model.entity;

import com.lps.vitalMagic.purchase.domain.exception.InvalidPurchaseException;
import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

@Entity
@Table(name ="purchase_item")
@Getter
public class PurchaseItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @MapsId("purchaseId")
    private Purchase purchase;

    @Embedded
    private final ItemSnapshot item;

    @Column
    private int quantity;


    @Column
    private BigDecimal subtotal;

     PurchaseItem(Purchase purchase, ItemSnapshot item, int quantity) {

        if(quantity<=0){
            throw new InvalidPurchaseException("Purchase item quantity should be more than zero");
        }

        this.purchase=Objects.requireNonNull(purchase);
        this.item= Objects.requireNonNull(item);
        this.quantity = quantity;
        calculateSubtotal();
    }

    private void calculateSubtotal(){
         this.subtotal=item.getUnitCost().multiply(BigDecimal.valueOf(quantity)).setScale(2, RoundingMode.HALF_UP);
    }

}
