package com.lps.vitalMagic.shake.aplication;

import com.lps.vitalMagic.inventory.domain.repository.ItemRepository;
import com.lps.vitalMagic.shake.application.command.CreateShakeIngredientCommand;
import com.lps.vitalMagic.shake.application.command.CreateStandardShakeCommand;
import com.lps.vitalMagic.shake.application.service.CreateStandardShakeService;
import com.lps.vitalMagic.shake.domain.model.entity.Shake;
import com.lps.vitalMagic.shake.domain.model.entity.ShakeIngredient;
import com.lps.vitalMagic.shake.domain.model.enums.ShakeCategory;

import com.lps.vitalMagic.shake.domain.model.enums.ShakeType;
import com.lps.vitalMagic.shake.domain.repository.ShakeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static  org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class CreateStandardShakeServiceTest {

    @Mock
    private ShakeRepository shakeRepository;

    @Mock
    private ItemRepository itemRepository;

    @InjectMocks
    private CreateStandardShakeService service;





    @Test
    public void whenCreateStandardShakeIsSuccessfully(){

        List<CreateShakeIngredientCommand> ingredientCommands= new ArrayList<>();
        ingredientCommands.add(new CreateShakeIngredientCommand(1L,2));
        ingredientCommands.add(new CreateShakeIngredientCommand(2L,1));
        ingredientCommands.add(new CreateShakeIngredientCommand(3L,1));

        CreateStandardShakeCommand shakeCommand= new CreateStandardShakeCommand("BatiOmega",
                "Batida con el poder cantar merengue con voz grave", ShakeCategory.RARE,ingredientCommands);

        Shake persisted = Shake.from(
                7L,
                shakeCommand.name(),
                shakeCommand.description(),
                ShakeType.STANDARD,
                shakeCommand.shakeCategory(),
                new ArrayList<>(),
                true
        );
        when(itemRepository.existsById(anyLong())).thenReturn(true);
        when(shakeRepository.save(any())).thenReturn(persisted);

        Long shakeId= service.execute(shakeCommand);
        ArgumentCaptor<Shake> captor =
                ArgumentCaptor.forClass(Shake.class);

        verify(shakeRepository).save(captor.capture());
        Shake saved = captor.getValue();
        assertEquals(ShakeType.STANDARD, saved.getShakeType());
        assertEquals(ingredientCommands.size(),saved.getIngredients().size());
        assertEquals(persisted.getId(),shakeId);
    }

    @Test
    public void whenIngredientDoesNotExistThrowsException(){
        List<CreateShakeIngredientCommand> ingredientCommands= new ArrayList<>();
        ingredientCommands.add(new CreateShakeIngredientCommand(1L,2));
        ingredientCommands.add(new CreateShakeIngredientCommand(2L,1));
        ingredientCommands.add(new CreateShakeIngredientCommand(3L,1));

        CreateStandardShakeCommand shakeCommand= new CreateStandardShakeCommand("BatiOmega",
                "Batida con el poder cantar merengue con voz grave", ShakeCategory.RARE,ingredientCommands);


        when(itemRepository.existsById(2L))
                .thenReturn(false);

        assertThrows(
                EntityNotFoundException.class,
                () -> service.execute(shakeCommand)
        );

        verify(shakeRepository, never()).save(any());
    }



}
