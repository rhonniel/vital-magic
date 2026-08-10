package com.lps.vitalMagic.shake.aplication;

import com.lps.vitalMagic.common.exception.ResourceNotFoundException;
import com.lps.vitalMagic.shake.application.query.ShakeIngredientSource;
import com.lps.vitalMagic.shake.application.query.ShakeProductSource;
import com.lps.vitalMagic.shake.application.service.FindShakeProductSourceService;
import com.lps.vitalMagic.shake.domain.model.entity.Shake;
import com.lps.vitalMagic.shake.domain.model.entity.ShakeIngredient;
import com.lps.vitalMagic.shake.domain.repository.ShakeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindShakeProductSourceServiceTest {

    @Mock
    private ShakeRepository shakeRepository;

    @InjectMocks
    private FindShakeProductSourceService service;

    @Test
    void shouldFindShakeProductSource() {
        Shake shake = mock(Shake.class);
        ShakeIngredient firstIngredient = mock(ShakeIngredient.class);
        ShakeIngredient secondIngredient = mock(ShakeIngredient.class);

        when(firstIngredient.getItemId()).thenReturn(1L);
        when(firstIngredient.getQuantity()).thenReturn(2);

        when(secondIngredient.getItemId()).thenReturn(2L);
        when(secondIngredient.getQuantity()).thenReturn(3);

        when(shake.getId()).thenReturn(10L);
        when(shake.getName()).thenReturn("Dragon Shake");
        when(shake.getIngredients()).thenReturn(
                List.of(firstIngredient, secondIngredient)
        );

        when(shakeRepository.findById(10L))
                .thenReturn(Optional.of(shake));

        ShakeProductSource result = service.execute(10L);

        assertEquals(10L, result.shakeId());
        assertEquals("Dragon Shake", result.name());
        assertEquals(
                List.of(
                        new ShakeIngredientSource(1L, 2),
                        new ShakeIngredientSource(2L, 3)
                ),
                result.ingredients()
        );
    }

    @Test
    void shouldFailWhenShakeDoesNotExist() {
        when(shakeRepository.findById(10L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> service.execute(10L)
        );
    }
}