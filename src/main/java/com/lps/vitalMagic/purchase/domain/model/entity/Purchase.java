package com.lps.vitalMagic.purchase.domain.model.entity;


import com.lps.vitalMagic.purchase.domain.exception.InvalidPurchaseException;
import com.lps.vitalMagic.purchase.domain.model.input.PurchaseItemInput;
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

    public Purchase() {
    }

    public static Purchase create(List<PurchaseItemInput> items){

        Purchase purchase= new Purchase();

        Objects.requireNonNull(items);

        if(items.isEmpty()){
            throw  new InvalidPurchaseException("Purchase should have least one item");
        }

        for(PurchaseItemInput input:items){
            Objects.requireNonNull(input);
            purchase.items.add(new PurchaseItem(purchase,input.itemSnapshot(),input.quantity()));
        }

        purchase.calculateTotal();

        return purchase;
    }

    private void calculateTotal() {
        BigDecimal total = BigDecimal.ZERO;

        for (PurchaseItem item : items) {
            total = total.add(item.getSubtotal());
        }

        this.totalAmount = total;
    }


    public List<PurchaseItem> getItems(){
        return Collections.unmodifiableList(items);
    }

}
