package com.lps.vitalMagic.product.domain;

import com.lps.vitalMagic.product.domain.model.data.Composition;
import com.lps.vitalMagic.product.domain.model.data.IngredientComposition;
import com.lps.vitalMagic.product.domain.model.entity.Product;
import com.lps.vitalMagic.product.domain.model.enums.ProductType;
import com.lps.vitalMagic.product.domain.service.ProductCompositionService;
import com.lps.vitalMagic.shake.domain.model.entity.Shake;
import com.lps.vitalMagic.shake.domain.model.entity.ShakeIngredient;
import com.lps.vitalMagic.shake.domain.model.enums.ShakeCategory;
import com.lps.vitalMagic.shake.domain.model.enums.ShakeType;
import com.lps.vitalMagic.shake.domain.repository.ShakeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductCompositionServiceTest {

    @InjectMocks
    private ProductCompositionService productCompositionService;

    @Mock
    private ShakeRepository shakeRepository;


    @Test
    public void whenGetCompositionOfShakeIsSuccessfully(){

        Long productId=5L;
        int quantityProduct=2;

        Product product = Product.from(productId,7L,
                ProductType.SHAKE,"Batida zapote criptoniano",new BigDecimal("750.50"),true);
        List<ShakeIngredient> shakeIngredients= new ArrayList<>();
        shakeIngredients.add(ShakeIngredient.from(productId,1L,2));
        shakeIngredients.add(ShakeIngredient.from(productId,2L,1));
        shakeIngredients.add(ShakeIngredient.from(productId,3L,3));

        Shake shake= Shake.from(productId,"Pollo","Pollo", ShakeType.STANDARD, ShakeCategory.RARE,shakeIngredients,true);
        when(shakeRepository.findById(product.getReferenceNo())).thenReturn(Optional.of(shake));


       Composition composition= productCompositionService.getComposition(product,quantityProduct);

        int actualTotal = composition.items().stream()
                .mapToInt(IngredientComposition::quantity)
                .sum();

        assertEquals(shakeIngredients.size(),composition.items().size());
        assertEquals(4, composition.items().get(0).quantity());
        assertEquals(2, composition.items().get(1).quantity());
        assertEquals(6, composition.items().get(2).quantity());
        assertEquals(12, actualTotal);

    }

    @Test
    public void whenGetCompositionOfSimpleProductIsSuccessfully(){

        Long productId=5L;
        int quantityProduct=2;

        Product product = Product.from(productId,7L,
                ProductType.SIMPLE_PRODUCT,"Pocion de mana",new BigDecimal("750.50"),true);

        Composition composition= productCompositionService.getComposition(product,quantityProduct);


        assertEquals(1L,composition.items().size());
        assertEquals(quantityProduct, composition.items().get(0).quantity());

    }

    @Test
    public void whenShakeIsNotExists(){
        Long productId=5L;
        int quantityProduct =5;

        Product product = Product.from(productId,7L,
                ProductType.SHAKE,"Batida zapote criptoniano",new BigDecimal("750.50"),true);

        when(shakeRepository.findById(product.getReferenceNo())).thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class,
                () -> productCompositionService.getComposition(product,quantityProduct));


    }

}
