package com.lps.vitalMagic.inventory.domain;

import com.lps.vitalMagic.inventory.domain.exception.InvalidItemInventoryException;
import com.lps.vitalMagic.inventory.domain.model.entity.Attribute;
import com.lps.vitalMagic.inventory.domain.model.entity.Item;
import com.lps.vitalMagic.inventory.domain.model.entity.ItemAttribute;
import com.lps.vitalMagic.inventory.domain.model.entity.ItemInventory;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ItemInventoryTest {

    @Test
    public void createValidItemInventory() {
        int minStock=77;


        ItemInventory newItem=ItemInventory.create(createValidItem(),minStock);

        assertNotNull(newItem);
        assertTrue(newItem.isActive());
        assertEquals(minStock, newItem.getMinStock());
        assertEquals(0,newItem.getCurrentStock());
        assertEquals(BigDecimal.ZERO,newItem.getUnitCost());

    }

    @Test
    public void shouldNotAllowMinStockLessThanZero(){

        assertThrows(InvalidItemInventoryException.class,()-> ItemInventory.create(createValidItem(),-1));

    }


    private Item createValidItem() {
        Attribute strength = new Attribute(1L, "strength","test","STR");
        Attribute speed = new Attribute(2L, "speed","test","SPD");

        List<ItemAttribute> attributes = List.of(
                ItemAttribute.create(strength, 5),
                ItemAttribute.create(speed, 7)
        );

        return Item.create("test", "test descri", attributes);
    }
}
