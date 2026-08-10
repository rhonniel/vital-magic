package com.lps.vitalMagic.product.domain;


import com.lps.vitalMagic.product.domain.exception.InvalidProductException;
import com.lps.vitalMagic.product.domain.model.data.IngredientCost;
import com.lps.vitalMagic.product.domain.model.data.ShakeProductData;
import com.lps.vitalMagic.product.domain.model.entity.Product;
import com.lps.vitalMagic.product.domain.model.enums.ProductType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ProductTest {

    @Test
    public void createValidProductFromStandardShake(){
        List<IngredientCost> ingredientCosts = List.of(
                new IngredientCost(
                        777L,
                        3,
                        new BigDecimal("770.50")
                ),
                new IngredientCost(
                        500L,
                        2,
                        new BigDecimal("450.50")
                )
        );

        ShakeProductData shakeProductData = new ShakeProductData(
                111L,
                "Thunder Shake",
                ingredientCosts
        );

        BigDecimal totalCost = new BigDecimal("3212.50");

        Product product =
                Product.createShakeProduct(shakeProductData);

        assertNotNull(product);
        assertEquals(ProductType.SHAKE, product.getProductType());
        assertTrue(product.getPrice().compareTo(BigDecimal.ZERO)>0);
        assertTrue(product.getPrice().compareTo(totalCost)>0);
    }



    @Test
    void shouldRejectIngredientCostWithZeroQuantity() {
        assertThrows(
                InvalidProductException.class,
                () -> new IngredientCost(
                        777L,
                        0,
                        new BigDecimal("10.00")
                )
        );
    }

    @Test
    void shouldRejectIngredientCostWithNegativeQuantity() {
        assertThrows(
                InvalidProductException.class,
                () -> new IngredientCost(
                        777L,
                        -1,
                        new BigDecimal("10.00")
                )
        );
    }

    @Test
    void shouldRejectIngredientCostEqualToZero() {
        assertThrows(
                InvalidProductException.class,
                () -> new IngredientCost(
                        777L,
                        3,
                        BigDecimal.ZERO
                )
        );
    }

    @Test
    void shouldRejectNegativeIngredientCost() {
        assertThrows(
                InvalidProductException.class,
                () -> new IngredientCost(
                        777L,
                        3,
                        new BigDecimal("-10.00")
                )
        );
    }

    @Test
    void shouldCalculateIngredientTotalCost() {
        IngredientCost ingredientCost = new IngredientCost(
                777L,
                3,
                new BigDecimal("10.50")
        );

        assertEquals(
                new BigDecimal("31.50"),
                ingredientCost.totalCost()
        );
    }
    @Test
    void shouldRejectBlankProductName() {
        IngredientCost ingredientCost = new IngredientCost(
                777L,
                3,
                new BigDecimal("10.00")
        );

        assertThrows(
                InvalidProductException.class,
                () -> new ShakeProductData(
                        111L,
                        " ",
                        List.of(ingredientCost)
                )
        );
    }

    @Test
    void shouldRejectShakeProductWithoutIngredients() {
        assertThrows(
                InvalidProductException.class,
                () -> new ShakeProductData(
                        111L,
                        "Thunder Shake",
                        List.of()
                )
        );
    }

    @Test
    void shouldCreateValidShakeProductData() {
        IngredientCost ingredientCost = new IngredientCost(
                777L,
                3,
                new BigDecimal("10.00")
        );

        ShakeProductData data = new ShakeProductData(
                111L,
                "Thunder Shake",
                List.of(ingredientCost)
        );

        assertEquals(111L, data.referenceNo());
        assertEquals("Thunder Shake", data.name());
        assertEquals(List.of(ingredientCost), data.ingredients());
    }
}
