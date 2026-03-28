package com.lps.vitalMagic.purchase.domain.model.entity;


import com.lps.vitalMagic.purchase.domain.exception.InvalidPurchaseException;
import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Entity
@Table
public class Purchase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL , orphanRemoval = true)
    @JoinColumn(name = "purchase_id")
    private List<PurchaseItem> items= new ArrayList<>();

    @Column(name = "total_amount")
    @Getter
    private BigDecimal totalAmount;

    public Purchase(List<PurchaseItem> items, BigDecimal totalAmount) {
        Objects.requireNonNull(items);

        if(items.isEmpty()){
           throw  new InvalidPurchaseException("Purchase should have least one item");
        }

        for(PurchaseItem purchaseItem:items){
            this.items.add(Objects.requireNonNull(purchaseItem));
        }

        this.totalAmount=Objects.requireNonNull(totalAmount);;
    }

    public static Purchase create(List<PurchaseItem> items, BigDecimal totalAmount){
        return new Purchase(items,totalAmount);
    }


    public List<PurchaseItem> getItems(){
        return Collections.unmodifiableList(items);
    }

}
