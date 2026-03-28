package com.lps.vitalMagic.purchase.domain;

import com.lps.vitalMagic.purchase.domain.exception.InvalidPurchaseItemException;
import com.lps.vitalMagic.purchase.domain.model.entity.ItemSnapshot;
import com.lps.vitalMagic.purchase.domain.model.entity.PurchaseItem;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PurchaseItemTest {
    @Test
    public void createValidPurchaseItem(){
        Long itemId= 7L;
        String itemName="Solsettia";
        BigDecimal cost = new BigDecimal("777.77");
        ItemSnapshot itemSnapshot= new ItemSnapshot(itemId,itemName,cost);
        BigDecimal subTotal= new BigDecimal("777.77");
        int quantity=2;

        PurchaseItem purchaseItem= PurchaseItem.create(itemSnapshot,quantity,subTotal);

        assertNotNull(purchaseItem);
        assertEquals(quantity,purchaseItem.getQuantity());
    }


    @Test
    public void shouldPurchaseItemQuantityBeMoreThanZero(){
        Long itemId= 7L;
        String itemName="Solsettia";
        BigDecimal cost = new BigDecimal("777.77");
        ItemSnapshot itemSnapshot= new ItemSnapshot(itemId,itemName,cost);
        BigDecimal subTotal= new BigDecimal("777.77");
        int quantity=0;

        assertThrows(InvalidPurchaseItemException.class,()->PurchaseItem.create(itemSnapshot,quantity,subTotal));

    }


    @Test
    public void shouldPurchaseItemCostBeMoreThanZero(){
        Long productId= 7L;
        String productName="shake test";
        BigDecimal cost = BigDecimal.ZERO;

        assertThrows(InvalidPurchaseItemException.class,()-> new ItemSnapshot(productId,productName,cost));

    }

}
