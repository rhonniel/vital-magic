package com.lps.vitalMagic.inventory.domain;

import com.lps.vitalMagic.inventory.domain.exception.InvalidAttributeException;
import com.lps.vitalMagic.inventory.domain.exception.InvalidItemException;
import com.lps.vitalMagic.inventory.domain.model.entity.Attribute;
import com.lps.vitalMagic.inventory.domain.model.entity.AttributeValue;
import com.lps.vitalMagic.inventory.domain.model.entity.Item;
import com.lps.vitalMagic.inventory.domain.model.entity.ItemAttribute;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;


import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ItemTest {


    @Test
    public void createValidItem() {
        String name = "test";
        String description = "test descri";
        List<AttributeValue> attributes =new ArrayList<>();
        attributes.add(new AttributeValue(1L,3));
        attributes.add(new AttributeValue(2L,5));

        Item newItem= Item.create(name,description,attributes);


        assertNotNull(newItem);
        assertTrue(newItem.isActive());
        assertEquals(attributes.size(), newItem.getAttributes().size());

    }



    @ParameterizedTest
    @NullAndEmptySource
    void shouldRejectInvalidName(String name) {
        List<AttributeValue> attributes =new ArrayList<>();
        attributes.add(new AttributeValue(1L,3));
        attributes.add(new AttributeValue(2L,5));


        assertThrows(InvalidItemException.class,
                () -> Item.create(name, "desc", attributes));
    }

    @ParameterizedTest
    @NullAndEmptySource
    void shouldRejectInvalidDescription(String description) {
        List<AttributeValue> attributes =new ArrayList<>();
        attributes.add(new AttributeValue(1L,3));
        attributes.add(new AttributeValue(2L,5));

        assertThrows(InvalidItemException.class,
                () -> Item.create("name", description, attributes));
    }



    @Test
    public void shouldNotAllowItemWithoutAttributes() {
        String name = "test";
        String description = "test descri";
        List<AttributeValue> attributes =new ArrayList<>();

        assertThrows(InvalidItemException.class,()-> Item.create(name,description,attributes));
    }



    @Test
    public void shouldAttributeItemNotAllowValueLessThanOne(){

        String name = "test";
        String description = "test descri";
        List<AttributeValue> attributes =new ArrayList<>();
        attributes.add(new AttributeValue(1L,0));
        attributes.add(new AttributeValue(2L,-2));


        assertThrows(InvalidItemException.class,()->Item.create(name,description,attributes));

    }



}
