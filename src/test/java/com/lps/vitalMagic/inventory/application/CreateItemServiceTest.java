package com.lps.vitalMagic.inventory.application;

import com.lps.vitalMagic.inventory.application.command.CreateItemAttributeCommand;
import com.lps.vitalMagic.inventory.application.command.CreateItemCommand;
import com.lps.vitalMagic.inventory.application.service.CreateItemService;
import com.lps.vitalMagic.inventory.domain.model.entity.Item;
import com.lps.vitalMagic.inventory.domain.model.entity.ItemAttribute;
import com.lps.vitalMagic.inventory.domain.model.entity.ItemInventory;
import com.lps.vitalMagic.inventory.domain.repository.AttributeRepository;
import com.lps.vitalMagic.inventory.domain.repository.ItemInventoryRepository;
import com.lps.vitalMagic.inventory.domain.repository.ItemRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateItemServiceTest {

    @Mock
    AttributeRepository attributeRepository;

    @Mock
    ItemRepository itemRepository;

    @Mock
    ItemInventoryRepository itemInventoryRepository;

    @InjectMocks
    CreateItemService service;

    @Test
    void createItemSuccessfully() {

        CreateItemCommand command =  buildCommand();
        Item persisted = buildSavedItem();

        when(attributeRepository.existsById(anyLong()))
                .thenReturn(true);

        when(itemRepository.save(any())).thenReturn(persisted);




        Long itemId = service.execute(command);
        ArgumentCaptor<Item> itemCaptor = ArgumentCaptor.forClass(Item.class);
        ArgumentCaptor<ItemInventory> inventoryCaptor = ArgumentCaptor.forClass(ItemInventory.class);


        verify(itemRepository).save(itemCaptor.capture());
        verify(itemInventoryRepository).save(inventoryCaptor.capture());

        Item savedItem = itemCaptor.getValue();
        ItemInventory savedInventory = inventoryCaptor.getValue();


        assertEquals(persisted.getId(),itemId);
        assertEquals(command.attributes().size(), savedItem.getAttributes().size());
        assertEquals(command.minStock(), savedInventory.getMinStock());

    }

    @Test
    public void whenItemAttributeNotExists(){

        CreateItemCommand command =  buildCommand();

        when(attributeRepository.existsById(command.attributes().get(0).attributeId()))
                .thenReturn(true);

        when(attributeRepository.existsById(command.attributes().get(1).attributeId()))
                .thenReturn(false);

        assertThrows(EntityNotFoundException.class,() -> {service.execute(command);});


    }
    private CreateItemCommand buildCommand() {
        return new CreateItemCommand(
                "Cola de dragon",
                "Cola disecada de un dragon",
                List.of(
                        new CreateItemAttributeCommand(1L, 5),
                        new CreateItemAttributeCommand(2L, 7)
                ),
                7
        );
    }

    private Item buildSavedItem() {
        return Item.from(
                77L,
                "Cola de dragon",
                "Cola disecada de un dragon",
                List.of(
                        ItemAttribute.from(1L, 5),
                        ItemAttribute.from(2L, 7)
                ),
                true
        );
    }

}