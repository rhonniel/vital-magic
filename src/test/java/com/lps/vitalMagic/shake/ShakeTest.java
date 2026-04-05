package com.lps.vitalMagic.shake;

import com.lps.vitalMagic.shake.domain.model.entity.IngredientQuantity;
import com.lps.vitalMagic.shake.domain.model.entity.Shake;
import com.lps.vitalMagic.shake.domain.model.enums.ShakeCategory;
import com.lps.vitalMagic.shake.domain.model.exception.InvalidShakeException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ShakeTest {

    @Test
    public void createValidStandardShake(){
        String name="Egida Fresada";
        String description=" Batido de fresas que potencia la defensa fisica";
        List<IngredientQuantity> ingredients = new ArrayList<>();
        ingredients.add(new IngredientQuantity(777L,2));
        ingredients.add(new IngredientQuantity(555L,4));
        ingredients.add(new IngredientQuantity(464L,3));

        Shake shake = Shake.createStandardShake(name,description, ShakeCategory.DEFENSIVE,ingredients);

        assertNotNull(shake);
        assertTrue(shake.isActive());
        assertEquals(ingredients.size(),shake.getIngredients().size());

    }

    @Test
    public void shouldShakeHaveMoreThanTwoIngredients(){
        String name="Egida Fresada";
        String description=" Batido de fresas que potencia la defensa fisica";
        List<IngredientQuantity> ingredients = new ArrayList<>();
        ingredients.add(new IngredientQuantity(777L,2));
        ingredients.add(new IngredientQuantity(555L,4));

        assertThrows(InvalidShakeException.class,()->  Shake.createStandardShake(name,description, ShakeCategory.DEFENSIVE,ingredients));

    }

    @Test
    public void shouldIngredientsQuantityBeMoreThanZero(){
        String name="Egida Fresada";
        String description=" Batido de fresas que potencia la defensa fisica";
        List<IngredientQuantity> ingredients = new ArrayList<>();
        ingredients.add(new IngredientQuantity(777L,0));
        ingredients.add(new IngredientQuantity(555L,-2));
        ingredients.add(new IngredientQuantity(557L,4));
        assertThrows(InvalidShakeException.class,()->  Shake.createStandardShake(name,description, ShakeCategory.DEFENSIVE,ingredients));

    }
}
