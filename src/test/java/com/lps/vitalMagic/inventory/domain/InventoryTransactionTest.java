package com.lps.vitalMagic.inventory.domain;

import com.lps.vitalMagic.inventory.domain.exception.InventoryTransactionException;
import com.lps.vitalMagic.inventory.domain.model.entity.*;
import com.lps.vitalMagic.inventory.domain.model.enums.InventoryTransactionType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class InventoryTransactionTest {
    @Test
    public void createValidSaleInventoryTransaction() {

        Long saleId=7777L;
        int quantity=7;

        InventoryTransaction sale= InventoryTransaction.createSale(createValidItemInventory(),saleId,quantity);


        assertNotNull(sale);
        assertNull(sale.getProcessAt());
        assertEquals(quantity, sale.getQuantity());
        assertEquals(InventoryTransactionType.SALE,sale.getType());

    }

    @Test
    public  void shouldInventoryTransactionQuantityBePositive(){

        Long saleId=7777L;
        int quantity=-7;

        assertThrows(InventoryTransactionException.class,()-> InventoryTransaction.createSale(createValidItemInventory(),saleId,quantity));

    }




    @Test
    public void createValidPurchaseInventoryTransaction() {

        Long purchaseId=7777L;
        int quantity=7;
        BigDecimal unitCost= new BigDecimal("777.77");

        InventoryTransaction purchase= InventoryTransaction.createPurchase(createValidItemInventory(),purchaseId,quantity,unitCost);


        assertNotNull(purchase);
        assertNull(purchase.getProcessAt());
        assertEquals(quantity, purchase.getQuantity());
        assertEquals(0, purchase.getUnitCost().compareTo(unitCost));
        assertEquals(InventoryTransactionType.PURCHASE,purchase.getType());

    }

    @Test
    public  void shouldInventoryTransactionUnitCostBePositive(){

        Long purchaseId=7777L;
        int quantity=7;
        BigDecimal unitCost= new BigDecimal("-777.77");

        assertThrows(InventoryTransactionException.class,()-> InventoryTransaction.createPurchase(createValidItemInventory(),purchaseId,quantity,unitCost));

    }



    private ItemInventory createValidItemInventory() {
        int minStock=77;
        Attribute strength = new Attribute(1L, "strength","test","STR");
        Attribute speed = new Attribute(2L, "speed","test","SPD");

        List<ItemAttribute> attributes = List.of(
                ItemAttribute.create(strength, 5),
                ItemAttribute.create(speed, 7)
        );


        return  ItemInventory.create(Item.create("test", "test descri", attributes),minStock);
    }
}
