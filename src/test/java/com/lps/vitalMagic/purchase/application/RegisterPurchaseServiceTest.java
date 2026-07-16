package com.lps.vitalMagic.purchase.application;

import com.lps.vitalMagic.inventory.application.dto.ItemInfo;
import com.lps.vitalMagic.inventory.application.service.FindItemService;
import com.lps.vitalMagic.inventory.application.service.RegisterPurchaseTransactionService;
import com.lps.vitalMagic.purchase.application.command.CreatePurchaseCommand;
import com.lps.vitalMagic.purchase.application.command.CreatePurchaseItemCommand;
import com.lps.vitalMagic.purchase.application.service.RegisterPurchaseService;
import com.lps.vitalMagic.purchase.domain.model.entity.Purchase;
import com.lps.vitalMagic.purchase.domain.repository.PurchaseRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RegisterPurchaseServiceTest {

    @InjectMocks
    private RegisterPurchaseService registerPurchaseService;

    @Mock
    private RegisterPurchaseTransactionService registerPurchaseTransactionService ;

    @Mock
    private PurchaseRepository purchaseRepository;

    @Mock
    private FindItemService findItemService;




    @Test
    public void whenRegisterPurchaseIsSuccessfully(){
        List<CreatePurchaseItemCommand> purchaseItemCommand = new ArrayList<>();
        purchaseItemCommand.add(new CreatePurchaseItemCommand(7L,50, BigDecimal.valueOf(60.50)));
        CreatePurchaseCommand purchaseCommand = new CreatePurchaseCommand(purchaseItemCommand);
        ItemInfo  itemInfo= new ItemInfo(7L,"Pochoclo");

        ArgumentCaptor<Purchase> purchaseArgumentCaptor= ArgumentCaptor.forClass(Purchase.class);

        when(findItemService.getItemInfo(7L)).thenReturn(itemInfo);
        when(purchaseRepository.save(any()))
                .thenAnswer(invocation -> {
                    Purchase purchase = invocation.getArgument(0);

                    return Purchase.from(
                            1L,
                            purchase.getItems(),
                            purchase.getTotalAmount(),
                            LocalDateTime.now()
                    );
                });



        Long result=registerPurchaseService.execute(purchaseCommand);



        verify(registerPurchaseTransactionService).registerPurchase(1L,7L,50,BigDecimal.valueOf(60.50));
        verify(purchaseRepository).save(purchaseArgumentCaptor.capture());

        Purchase purchase = purchaseArgumentCaptor.getValue();

        assertEquals(1L,result);
        assertEquals(purchaseItemCommand.size(),purchase.getItems().size());

    }

    @Test
    public void whenItemNotExistsRegisterPurchaseThrowException(){
        List<CreatePurchaseItemCommand> purchaseItemCommand = new ArrayList<>();
        purchaseItemCommand.add(new CreatePurchaseItemCommand(7L,50, BigDecimal.valueOf(60.50)));
        CreatePurchaseCommand purchaseCommand = new CreatePurchaseCommand(purchaseItemCommand);


        when(findItemService.getItemInfo(7L)).thenThrow(EntityNotFoundException.class);


        assertThrows(EntityNotFoundException.class,() ->  registerPurchaseService.execute(purchaseCommand));


    }



}
