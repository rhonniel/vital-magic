package com.lps.vitalMagic.purchase.domain;


import com.lps.vitalMagic.purchase.domain.exception.InvalidPurchaseException;
import com.lps.vitalMagic.purchase.domain.model.entity.ItemSnapshot;
import com.lps.vitalMagic.purchase.domain.model.entity.Purchase;
import com.lps.vitalMagic.purchase.domain.model.entity.PurchaseItem;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PurchaseTest {
    @Test
    public void createValidPurchase(){

        BigDecimal total= new BigDecimal("777.77");
        List<PurchaseItem> items=getValidPurchaseItem();
        Purchase purchase= Purchase.create(items,total);


        assertNotNull(purchase);
        assertEquals(items.size(),purchase.getItems().size());
        assertEquals(total,purchase.getTotalAmount());

    }

    @Test
    public void shouldSaleHaveALeastOnePurchaseItem(){

        BigDecimal total= new BigDecimal("777.77");
        List<PurchaseItem> items= new ArrayList<>();

        assertThrows(InvalidPurchaseException.class,()->Purchase.create(items,total));

    }


    public List<PurchaseItem> getValidPurchaseItem(){
        List<PurchaseItem> items =new ArrayList<>();

        ItemSnapshot itemSnapshot= new ItemSnapshot(7L,"shake test", new BigDecimal("777.77"));
        BigDecimal subTotal = new BigDecimal("777.77");
        PurchaseItem purchaseItem= PurchaseItem.create(itemSnapshot,2,subTotal);
        items.add(purchaseItem);

        return items;

    }
}
