package com.lps.vitalMagic.inventory.domain;


import com.lps.vitalMagic.inventory.domain.exception.InventoryTransactionException;
import com.lps.vitalMagic.inventory.domain.model.entity.InventoryTransaction;
import com.lps.vitalMagic.inventory.domain.model.enums.InventoryTransactionType;
import com.lps.vitalMagic.inventory.domain.repository.InventoryTransactionRepository;
import com.lps.vitalMagic.inventory.domain.service.ItemCurrentStockService;
import com.lps.vitalMagic.inventory.domain.service.RegisterSaleTransactionService;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class RegisterSaleTransactionServiceTest {

    @InjectMocks
    private RegisterSaleTransactionService saleTransactionService;

    @Mock
    private InventoryTransactionRepository inventoryTransactionRepository;

    @Mock
    private ItemCurrentStockService itemCurrentStockService;


    @Test
    public void whenRegisterSaleTransactionIsSuccessfully(){

        Long saleId= 5L;
        Long itemId= 7L;

        when(itemCurrentStockService.getCurrentStock(itemId)).thenReturn(10);

        saleTransactionService.registerInventoryTransaction(saleId,itemId,10);

        ArgumentCaptor<InventoryTransaction> transactionArgumentCaptor = ArgumentCaptor.forClass(InventoryTransaction.class);
        verify(inventoryTransactionRepository).save(transactionArgumentCaptor.capture());
        InventoryTransaction inventoryTransaction= transactionArgumentCaptor.getValue();

        assertEquals(inventoryTransaction.getType(), InventoryTransactionType.SALE);

    }

    @Test
    public void whenItemNotHaveEnoughStocks(){
        Long saleId= 5L;
        Long itemId= 7L;

        when(itemCurrentStockService.getCurrentStock(itemId)).thenReturn(10);

        assertThrows( InventoryTransactionException.class,()-> saleTransactionService.registerInventoryTransaction(saleId,itemId,11));
    }

}
