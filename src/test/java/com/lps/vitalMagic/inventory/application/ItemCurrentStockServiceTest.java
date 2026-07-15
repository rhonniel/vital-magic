package com.lps.vitalMagic.inventory.application;

import com.lps.vitalMagic.inventory.application.service.ItemCurrentStockService;
import com.lps.vitalMagic.inventory.domain.model.entity.ItemInventory;
import com.lps.vitalMagic.inventory.domain.repository.InventoryTransactionRepository;
import com.lps.vitalMagic.inventory.domain.repository.ItemInventoryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ItemCurrentStockServiceTest {

    @InjectMocks
    private ItemCurrentStockService service;

    @Mock
    private InventoryTransactionRepository inventoryTransactionRepository;

    @Mock
    private ItemInventoryRepository itemInventoryRepository;



    @Test
    public void whenGetCurrentStockIsSuccessfully(){

        Long itemId=777L;
        Integer currentStock=150;
        Integer totalUnprocessedStocks=55;
        Integer totalStock=currentStock+totalUnprocessedStocks;
        ItemInventory itemInventory= ItemInventory.from(55L,itemId,0,150, BigDecimal.valueOf(700.50));

        when(itemInventoryRepository.findByActiveTrueAndItemId(itemId)).thenReturn(Optional.of(itemInventory));

        when(inventoryTransactionRepository.findTotalUnprocessedStocksByItemId(itemId)).thenReturn(55);

        assertEquals(totalStock,service.getCurrentStock(itemId));

    }

    @Test
    public void whenItemIdIsNotFound(){
        when(itemInventoryRepository.findByActiveTrueAndItemId(9L)).thenThrow(EntityNotFoundException.class);
        assertThrows(EntityNotFoundException.class,() -> {service.getCurrentStock(9L);});
    }
}
