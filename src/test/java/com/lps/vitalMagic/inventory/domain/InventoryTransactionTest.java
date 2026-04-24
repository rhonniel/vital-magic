package com.lps.vitalMagic.inventory.domain;

import com.lps.vitalMagic.inventory.domain.exception.InventoryTransactionException;
import com.lps.vitalMagic.inventory.domain.model.entity.*;
import com.lps.vitalMagic.inventory.domain.model.enums.InventoryTransactionType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class InventoryTransactionTest {
    @Test
    public void createValidSaleInventoryTransaction() {
        Long itemInventoryId=77L;
        Long saleId=7777L;
        int quantity=7;

        InventoryTransaction sale= InventoryTransaction.createSale(itemInventoryId,saleId,quantity);


        assertNotNull(sale);
        assertNull(sale.getProcessAt());
        assertEquals(quantity, sale.getQuantity());
        assertEquals(InventoryTransactionType.SALE,sale.getType());

    }

    @Test
    public  void shouldInventoryTransactionQuantityBePositive(){
        Long itemInventoryId=77L;
        Long saleId=7777L;
        int quantity=-7;

        assertThrows(InventoryTransactionException.class,()-> InventoryTransaction.createSale(itemInventoryId,saleId,quantity));

    }




    @Test
    public void createValidPurchaseInventoryTransaction() {
        Long itemInventoryId=77L;
        Long purchaseId=7777L;
        int quantity=7;
        BigDecimal unitCost= new BigDecimal("777.77");

        InventoryTransaction purchase= InventoryTransaction.createPurchase(itemInventoryId,purchaseId,quantity,unitCost);


        assertNotNull(purchase);
        assertNull(purchase.getProcessAt());
        assertEquals(quantity, purchase.getQuantity());
        assertEquals(0, purchase.getUnitCost().compareTo(unitCost));
        assertEquals(InventoryTransactionType.PURCHASE,purchase.getType());

    }

    @Test
    public  void shouldInventoryTransactionUnitCostBePositive(){
        Long itemInventoryId=77L;
        Long purchaseId=7777L;
        int quantity=7;
        BigDecimal unitCost= new BigDecimal("-777.77");

        assertThrows(InventoryTransactionException.class,()-> InventoryTransaction.createPurchase(itemInventoryId,purchaseId,quantity,unitCost));

    }


}
