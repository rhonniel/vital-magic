package com.lps.vitalMagic.product.domain;


import com.lps.vitalMagic.product.domain.exception.InvalidProductException;
import com.lps.vitalMagic.product.domain.model.entity.Product;
import com.lps.vitalMagic.product.domain.model.enums.ProductType;
import com.lps.vitalMagic.shake.domain.model.entity.Shake;
import com.lps.vitalMagic.shake.domain.model.entity.ShakeIngredient;
import com.lps.vitalMagic.shake.domain.model.enums.ShakeCategory;
import com.lps.vitalMagic.shake.domain.model.enums.ShakeType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class ProductTest {

    @Test
    public void createValidProductFromStandardShake(){
        List<ShakeIngredient> shakeIngredients= new ArrayList<>();
        shakeIngredients.add(ShakeIngredient.from(111L,777L,3));
        shakeIngredients.add(ShakeIngredient.from(500L,500L,2));
        Shake shake= Shake.from(111L,"Thunder Shake",
                "Shake with lighning powers", ShakeType.STANDARD, ShakeCategory.RARE,shakeIngredients,true);
        Map<Long, BigDecimal> itemsCost = new HashMap<>();
        itemsCost.put(777L,new BigDecimal("770.50"));
        itemsCost.put(500L,new BigDecimal("450.50"));
        BigDecimal totalCost =
                new BigDecimal("3212.50");
        Product product= Product.createShakeProduct(shake,itemsCost);
        assertNotNull(product);
        assertEquals(ProductType.SHAKE, product.getProductType());
        assertTrue(product.getPrice().compareTo(BigDecimal.ZERO)>0);
        assertTrue(product.getPrice().compareTo(totalCost)>0);
    }


    @Test
    public void shouldProductPriceBeGreaterThanZero(){
        List<ShakeIngredient> shakeIngredients= new ArrayList<>();
        shakeIngredients.add(ShakeIngredient.from(111L,777L,3));
        Shake shake= Shake.from(111L,"Thunder Shake",
                "Shake with lighning powers", ShakeType.STANDARD, ShakeCategory.RARE,shakeIngredients,true);
        Map<Long, BigDecimal> itemsCost = new HashMap<>();
        itemsCost.put(777L,BigDecimal.ZERO);

        assertThrows(InvalidProductException.class,() -> Product.createShakeProduct(shake,itemsCost));

    }


    @Test
    public void shouldItemHaveUnitCost(){
        List<ShakeIngredient> shakeIngredients= new ArrayList<>();
        shakeIngredients.add(ShakeIngredient.from(111L,777L,3));
        Shake shake= Shake.from(111L,"Thunder Shake",
                "Shake with lighning powers", ShakeType.STANDARD, ShakeCategory.RARE,shakeIngredients,true);
        Map<Long, BigDecimal> itemsCost = new HashMap<>();
        itemsCost.put(555L,new BigDecimal("770.50"));

        assertThrows(InvalidProductException.class,() -> Product.createShakeProduct(shake,itemsCost));

    }



}
