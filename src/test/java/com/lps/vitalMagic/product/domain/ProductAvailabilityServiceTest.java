package com.lps.vitalMagic.product.domain;


import com.lps.vitalMagic.inventory.application.service.ItemCurrentStockService;
import com.lps.vitalMagic.product.domain.model.entity.Product;
import com.lps.vitalMagic.product.domain.model.enums.ProductType;
import com.lps.vitalMagic.product.domain.service.ProductAvailabilityService;
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
public class ProductAvailabilityServiceTest {

    @InjectMocks
    private ProductAvailabilityService productAvailabilityService;

    @Mock
    private ItemCurrentStockService itemCurrentStockService;

    @Mock
    private ShakeRepository shakeRepository;




    @Test
    public void whenCheckAvailabilityShakeIsSuccessfully(){
        Long productId=8L;
        int quantity =2;

        Product product = Product.from(productId,7L,
                ProductType.SHAKE,"Batida zapote criptoniano",new BigDecimal("750.50"),true);

        List<ShakeIngredient> shakeIngredients= new ArrayList<>();
        shakeIngredients.add(ShakeIngredient.from(productId,1L,2));
        shakeIngredients.add(ShakeIngredient.from(productId,2L,1));
        shakeIngredients.add(ShakeIngredient.from(productId,3L,3));

        Shake shake= Shake.from(productId,"Pollo","Pollo", ShakeType.STANDARD, ShakeCategory.RARE,shakeIngredients,true);

        when(shakeRepository.findById(product.getReferenceNo())).thenReturn(Optional.of(shake));
        when(itemCurrentStockService.getCurrentStock(1L)).thenReturn(10);
        when(itemCurrentStockService.getCurrentStock(2L)).thenReturn(2);
        when(itemCurrentStockService.getCurrentStock(3L)).thenReturn(20);

        boolean result =productAvailabilityService.checkAvailability(product,quantity);


        verify(itemCurrentStockService).getCurrentStock(1L);

        verify(itemCurrentStockService).getCurrentStock(2L);

        verify(itemCurrentStockService).getCurrentStock(3L);
        assertTrue(result);

    }


    @Test
    public void whenCheckAvailabilitySimpleProductIsSuccessfully(){
        Long productId=5L;
        int quantity =5;
        Product product = Product.from(productId,20L,
                ProductType.SIMPLE_PRODUCT,"Potion de mana",new BigDecimal("200.00"),true);

        when(itemCurrentStockService.getCurrentStock(product.getReferenceNo())).thenReturn(10);
        
        boolean result =productAvailabilityService.checkAvailability(product,quantity);

        verify(itemCurrentStockService).getCurrentStock(product.getReferenceNo());
        assertTrue(result);

    }
    
    @Test
    public void whenAvailabilityShakeIsNotEnough(){
        Long productId=8L;
        int quantity =2;

        Product product = Product.from(productId,7L,
                ProductType.SHAKE,"Batida zapote criptoniano",new BigDecimal("750.50"),true);

        List<ShakeIngredient> shakeIngredients= new ArrayList<>();
        shakeIngredients.add(ShakeIngredient.from(productId,1L,2));
        shakeIngredients.add(ShakeIngredient.from(productId,2L,1));
        shakeIngredients.add(ShakeIngredient.from(productId,3L,3));

        Shake shake= Shake.from(productId,"Pollo","Pollo", ShakeType.STANDARD, ShakeCategory.RARE,shakeIngredients,true);

        when(shakeRepository.findById(product.getReferenceNo())).thenReturn(Optional.of(shake));
        when(itemCurrentStockService.getCurrentStock(1L)).thenReturn(4);
        when(itemCurrentStockService.getCurrentStock(2L)).thenReturn(2);
        when(itemCurrentStockService.getCurrentStock(3L)).thenReturn(2);

        boolean result =productAvailabilityService.checkAvailability(product,quantity);

        assertFalse(result);

    }

    @Test
    public void whenAvailabilitySimpleProductIsNotEnough(){
        Long productId=5L;
        int quantity =5;
        Product product = Product.from(productId,20L,
                ProductType.SIMPLE_PRODUCT,"Potion de mana",new BigDecimal("200.00"),true);

        when(itemCurrentStockService.getCurrentStock(product.getReferenceNo())).thenReturn(4);

        boolean result =productAvailabilityService.checkAvailability(product,quantity);

        verify(itemCurrentStockService).getCurrentStock(product.getReferenceNo());

        assertFalse(result);

    }


    @Test
    public void whenShakeIsNotExists(){
        Long productId=5L;
        int quantity =5;

        Product product = Product.from(productId,7L,
                ProductType.SHAKE,"Batida zapote criptoniano",new BigDecimal("750.50"),true);

        when(shakeRepository.findById(product.getReferenceNo())).thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class,
                () -> productAvailabilityService.checkAvailability(product,quantity));


    }

}
