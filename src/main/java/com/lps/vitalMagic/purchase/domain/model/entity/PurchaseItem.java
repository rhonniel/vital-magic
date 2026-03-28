package com.lps.vitalMagic.purchase.domain.model.entity;

import com.lps.vitalMagic.purchase.domain.exception.InvalidPurchaseItemException;
import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name ="purchase_item")
@Getter
public class PurchaseItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "purchase_id")
    private Long purchaseId;

    @Embedded
    private final ItemSnapshot item;

    @Column
    private int quantity;


    @Column
    private BigDecimal subtotal;

    private PurchaseItem(ItemSnapshot item, int quantity, BigDecimal subtotal) {

        if(quantity<=0){
            throw new InvalidPurchaseItemException("Purchase item quantity should be more than zero");
        }

        this.item= Objects.requireNonNull(item);
        this.quantity = quantity;
        this.subtotal =  Objects.requireNonNull(subtotal);
    }

    public static PurchaseItem create(ItemSnapshot item, int quantity, BigDecimal subtotal){
        return new PurchaseItem(item,quantity,subtotal);
    }
}
