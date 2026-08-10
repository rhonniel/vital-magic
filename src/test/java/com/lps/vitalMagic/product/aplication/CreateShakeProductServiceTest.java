package com.lps.vitalMagic.product.aplication;

import com.lps.vitalMagic.inventory.application.service.ItemCostProvider;
import com.lps.vitalMagic.product.aplication.command.CreateShakeProductCommand;
import com.lps.vitalMagic.product.aplication.exception.ProductAlreadyExistsException;
import com.lps.vitalMagic.product.aplication.service.CreateShakeProductService;
import com.lps.vitalMagic.product.domain.model.entity.Product;
import com.lps.vitalMagic.product.domain.model.enums.ProductType;
import com.lps.vitalMagic.product.domain.repository.ProductRepository;
import com.lps.vitalMagic.shake.application.query.ShakeIngredientSource;
import com.lps.vitalMagic.shake.application.query.ShakeProductSource;
import com.lps.vitalMagic.shake.application.usecase.FindShakeProductSourceUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateShakeProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private FindShakeProductSourceUseCase findShakeProductSourceUseCase;

    @Mock
    private ItemCostProvider itemCostProvider;

    @InjectMocks
    private CreateShakeProductService service;

    @Test
    void shouldCreateShakeProduct() {
        CreateShakeProductCommand command = new CreateShakeProductCommand(10L);

        ShakeProductSource shakeSource =
                new ShakeProductSource(
                        10L,
                        "Dragon Shake",
                        List.of(
                                new ShakeIngredientSource(1L, 2),
                                new ShakeIngredientSource(2L, 1)
                        )
                );

        Map<Long, BigDecimal> itemCosts = Map.of(
                1L, new BigDecimal("10.00"),
                2L, new BigDecimal("20.00")
        );

        when(productRepository.existsByReferenceNoAndProductType(
                10L,
                ProductType.SHAKE
        )).thenReturn(false);

        when(findShakeProductSourceUseCase.execute(10L))
                .thenReturn(shakeSource);

        when(itemCostProvider.findCostsByItemIds(Set.of(1L, 2L)))
                .thenReturn(itemCosts);

        when(productRepository.save(any(Product.class)))
                .thenAnswer(invocation -> {
                    Product product = invocation.getArgument(0);

                    return Product.from(
                            100L,
                            product.getReferenceNo(),
                            product.getProductType(),
                            product.getName(),
                            product.getPrice(),
                            product.isActive()
                    );
                });

        Long productId = service.execute(command);

        assertEquals(100L, productId);

        ArgumentCaptor<Product> productCaptor =
                ArgumentCaptor.forClass(Product.class);

        verify(productRepository).save(productCaptor.capture());

        Product savedProduct = productCaptor.getValue();

        assertEquals(10L, savedProduct.getReferenceNo());
        assertEquals(ProductType.SHAKE, savedProduct.getProductType());
        assertEquals("Dragon Shake", savedProduct.getName());
        assertEquals(new BigDecimal("52.00"), savedProduct.getPrice());
        assertTrue(savedProduct.isActive());
    }

    @Test
    void shouldRejectProductWhenItAlreadyExists() {
        CreateShakeProductCommand command = new CreateShakeProductCommand(10L);

        when(productRepository.existsByReferenceNoAndProductType(
                10L,
                ProductType.SHAKE
        )).thenReturn(true);

        assertThrows(
                ProductAlreadyExistsException.class,
                () -> service.execute(command)
        );

        verifyNoInteractions(
                findShakeProductSourceUseCase,
                itemCostProvider
        );

        verify(productRepository, never())
                .save(any(Product.class));
    }

    @Test
    void shouldFailWhenAnIngredientDoesNotHaveCost() {
        CreateShakeProductCommand command = new CreateShakeProductCommand(10L);

        ShakeProductSource shakeSource =
                new ShakeProductSource(
                        10L,
                        "Dragon Shake",
                        List.of(
                                new ShakeIngredientSource(1L, 2),
                                new ShakeIngredientSource(2L, 1)
                        )
                );

        when(productRepository.existsByReferenceNoAndProductType(
                10L,
                ProductType.SHAKE
        )).thenReturn(false);

        when(findShakeProductSourceUseCase.execute(10L))
                .thenReturn(shakeSource);


        when(itemCostProvider.findCostsByItemIds(Set.of(1L, 2L)))
                .thenReturn(Map.of(
                        1L, new BigDecimal("10.00")
                ));

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> service.execute(command)
        );

        assertEquals(
                "Item does not have cost",
                exception.getMessage()
        );

        verify(productRepository, never())
                .save(any(Product.class));
    }
}