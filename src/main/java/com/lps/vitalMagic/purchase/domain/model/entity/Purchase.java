package com.lps.vitalMagic.purchase.domain.model.entity;


import com.lps.vitalMagic.purchase.domain.exception.InvalidPurchaseException;
import com.lps.vitalMagic.purchase.domain.model.input.PurchaseItemInput;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


public class Purchase {
    @Getter
    private Long id;


    private List<PurchaseItem> items= new ArrayList<>();


    @Getter
    private BigDecimal totalAmount;

    private Purchase() {
    }

    public static Purchase create(List<PurchaseItemInput> items){

        Purchase purchase= new Purchase();

        Objects.requireNonNull(items);

        if(items.isEmpty()){
            throw  new InvalidPurchaseException("Purchase should have least one item");
        }

        for(PurchaseItemInput input:items){
            Objects.requireNonNull(input);
            purchase.items.add(new PurchaseItem(purchase.id,input.itemSnapshot(),input.quantity()));
        }

        purchase.calculateTotal();

        return purchase;
    }

    public static Purchase from(Long id, List<PurchaseItem> items, BigDecimal totalAmount) {
        Purchase purchase= new Purchase();
        purchase.id=id;
        purchase.items=items;
        purchase.totalAmount=totalAmount;
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
