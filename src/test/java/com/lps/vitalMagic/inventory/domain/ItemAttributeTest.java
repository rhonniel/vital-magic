package com.lps.vitalMagic.inventory.domain;

import com.lps.vitalMagic.inventory.domain.exception.InvalidAttributeException;
import com.lps.vitalMagic.inventory.domain.model.entity.Attribute;
import com.lps.vitalMagic.inventory.domain.model.entity.ItemAttribute;
import org.junit.jupiter.api.Test;



import static org.junit.jupiter.api.Assertions.*;

public class ItemAttributeTest {

    @Test
    public void createValidItemAttribute() {

        Attribute strength = new Attribute(1L, "strength","test","STR");
        ItemAttribute itemAttribute= ItemAttribute.create(strength,5);

        assertNotNull(itemAttribute);
        assertEquals(5, itemAttribute.getValue());

    }

    @Test
    public void shouldNotAllowValueLessThanOne(){

        Attribute strength = new Attribute(1L, "strength","test","STR");

        assertThrows(InvalidAttributeException.class,()->ItemAttribute.create(strength,0));

    }


    @Test
    public void shouldNotAllowedNullAttribute(){

        assertThrows(InvalidAttributeException.class,()->ItemAttribute.create(null,7));

    }

}
