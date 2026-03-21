package com.lps.vitalMagic.inventory.domain;

import com.lps.vitalMagic.inventory.domain.exception.InvalidAttributeException;
import com.lps.vitalMagic.inventory.domain.exception.InvalidItemException;
import com.lps.vitalMagic.inventory.domain.model.entity.Attribute;
import com.lps.vitalMagic.inventory.domain.model.entity.Item;
import com.lps.vitalMagic.inventory.domain.model.entity.ItemAttribute;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ItemTest {


    @Test
    public void createValidItem() {
        String name = "test";
        String description = "test descri";
        List<ItemAttribute> attributes =new ArrayList<>();
        Attribute strength = new Attribute(1L, "strength","test","STR");
        Attribute speed = new Attribute(2L, "speed","test","SPD");
        attributes.add(ItemAttribute.create(strength,5));
        attributes.add(ItemAttribute.create(speed,7));

        Item newItem= Item.create(name,description,attributes);


        assertNotNull(newItem);
        assertTrue(newItem.isActive());
        assertEquals(attributes.size(), newItem.getAttributes().size());

    }




    @Test
    public void shouldNotAllowAddItemAttributeNull() {
        String name = "test";
        String description = "test descri";
        List<ItemAttribute> attributes =new ArrayList<>();
        Attribute strength = new Attribute(1L, "strength","test","STR");
        Attribute speed = new Attribute(2L, "speed","test","SPD");
        attributes.add(ItemAttribute.create(strength,5));
        attributes.add(ItemAttribute.create(speed,7));

        Item newItem= Item.create(name,description,attributes);


        assertThrows(InvalidItemException.class,()-> newItem.addAttribute(null));

    }



    @Test
    public void shouldNotAllowItemWithoutAttributes() {
        String name = "test";
        String description = "test descri";
        List<ItemAttribute> attributes =new ArrayList<>();
        assertThrows(InvalidItemException.class,()-> Item.create(name,description,attributes));
    }

}
