package com.lps.vitalMagic.inventory.application;


import com.lps.vitalMagic.inventory.application.service.RegisterPurchaseTransactionService;
import com.lps.vitalMagic.inventory.domain.exception.InventoryTransactionException;
import com.lps.vitalMagic.inventory.domain.model.entity.InventoryTransaction;
import com.lps.vitalMagic.inventory.domain.model.enums.InventoryTransactionType;
import com.lps.vitalMagic.inventory.domain.repository.InventoryTransactionRepository;
import com.lps.vitalMagic.inventory.domain.repository.ItemRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class RegisterPurchaseTransactionServiceTest {

    @InjectMocks
    private RegisterPurchaseTransactionService registerPurchaseTransactionService;

    @Mock
    private InventoryTransactionRepository inventoryTransactionRepository;

    @Mock
    private ItemRepository itemRepository;



    @Test
    public void whenRegisterPurchaseTransactionIsSuccessfully(){

        Long purchaseId= 5L;
        Long itemId= 7L;

        when(itemRepository.existsById(itemId)).thenReturn(Boolean.TRUE);

        registerPurchaseTransactionService
                .registerPurchase(purchaseId,itemId,10, BigDecimal.valueOf(700.00));

        ArgumentCaptor<InventoryTransaction> transactionArgumentCaptor = ArgumentCaptor.forClass(InventoryTransaction.class);
        verify(inventoryTransactionRepository).save(transactionArgumentCaptor.capture());
        InventoryTransaction inventoryTransaction= transactionArgumentCaptor.getValue();

        assertEquals(inventoryTransaction.getType(), InventoryTransactionType.PURCHASE);

    }

    @Test
    public void whenItemNotExistsThrowAException(){
        Long purchaseId= 5L;
        Long itemId= 7L;
        when(itemRepository.existsById(itemId)).thenReturn(Boolean.FALSE);

        assertThrows( EntityNotFoundException.class,()->   registerPurchaseTransactionService
                .registerPurchase(purchaseId,itemId,10, BigDecimal.valueOf(700.00)));
    }

}
