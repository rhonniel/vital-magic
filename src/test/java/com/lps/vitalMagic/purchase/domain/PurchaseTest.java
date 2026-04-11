package com.lps.vitalMagic.purchase.domain;


import com.lps.vitalMagic.purchase.domain.exception.InvalidPurchaseException;
import com.lps.vitalMagic.purchase.domain.model.entity.ItemSnapshot;
import com.lps.vitalMagic.purchase.domain.model.entity.Purchase;
import com.lps.vitalMagic.purchase.domain.model.entity.PurchaseItem;
import com.lps.vitalMagic.purchase.domain.model.input.PurchaseItemInput;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PurchaseTest {
    @Test
    public void createValidPurchase(){

        BigDecimal total= new BigDecimal("1001.00");
        List<PurchaseItemInput> items=getValidPurchaseItem();
        Purchase purchase= Purchase.create(items);


        assertNotNull(purchase);
        assertEquals(items.size(),purchase.getItems().size());
        assertEquals(total,purchase.getTotalAmount());

    }

    @Test
    public void shouldSaleHaveALeastOnePurchaseItem(){

        List<PurchaseItemInput> items= new ArrayList<>();

        assertThrows(InvalidPurchaseException.class,()->Purchase.create(items));

    }


    public List<PurchaseItemInput> getValidPurchaseItem(){
        List<PurchaseItemInput> items =new ArrayList<>();

        ItemSnapshot itemSnapshot= new ItemSnapshot(7L,"shake test", new BigDecimal("500.50"));
        int quantity=2;

        PurchaseItemInput input = new PurchaseItemInput(itemSnapshot,quantity);
        items.add(input);

        return items;

    }


    @Test
    public void shouldPurchaseItemQuantityBeMoreThanZero(){
        Long itemId= 7L;
        String itemName="Solsettia";
        BigDecimal cost = new BigDecimal("777.77");
        ItemSnapshot itemSnapshot= new ItemSnapshot(itemId,itemName,cost);

        List<PurchaseItemInput> items=new ArrayList<>();
        items.add(new PurchaseItemInput(itemSnapshot,0));
        assertThrows(InvalidPurchaseException.class,()->  Purchase.create(items));

    }


    @Test
    public void shouldPurchaseItemCostBeMoreThanZero(){
        Long itemId= 7L;
        String itemName="Solsettia";
        BigDecimal cost = BigDecimal.ZERO;
        assertThrows(InvalidPurchaseException.class,()->   new ItemSnapshot(itemId,itemName,cost));

    }
}
