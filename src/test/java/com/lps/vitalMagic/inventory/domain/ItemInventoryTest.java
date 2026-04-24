package com.lps.vitalMagic.inventory.domain;

import com.lps.vitalMagic.inventory.domain.exception.InvalidItemInventoryException;
import com.lps.vitalMagic.inventory.domain.model.entity.*;
import com.lps.vitalMagic.inventory.domain.model.input.AttributeValue;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ItemInventoryTest {

    @Test
    public void createValidItemInventory() {
        int minStock=77;
        Long itemId= 55L;


        ItemInventory newItem=ItemInventory.create(itemId,minStock);

        assertNotNull(newItem);
        assertTrue(newItem.isActive());
        assertEquals(minStock, newItem.getMinStock());
        assertEquals(0,newItem.getCurrentStock());
        assertEquals(BigDecimal.ZERO,newItem.getUnitCost());

    }

    @Test
    public void shouldNotAllowMinStockLessThanZero(){

        assertThrows(InvalidItemInventoryException.class,()-> ItemInventory.create(4L,-1));

    }


    private Item createValidItem() {
        List<AttributeValue> attributes =new ArrayList<>();
        attributes.add(new AttributeValue(1L,3));
        attributes.add(new AttributeValue(2L,5));
        return Item.create("test", "test descri", attributes);
    }
}
