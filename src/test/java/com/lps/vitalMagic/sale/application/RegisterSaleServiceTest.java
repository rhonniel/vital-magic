package com.lps.vitalMagic.sale.application;

import com.lps.vitalMagic.inventory.application.service.RegisterSaleTransactionService;
import com.lps.vitalMagic.inventory.domain.exception.InventoryTransactionException;
import com.lps.vitalMagic.product.domain.model.data.Composition;
import com.lps.vitalMagic.product.domain.model.data.IngredientComposition;
import com.lps.vitalMagic.product.domain.model.data.ProductComposition;
import com.lps.vitalMagic.product.domain.model.entity.Product;
import com.lps.vitalMagic.product.domain.model.enums.ProductType;
import com.lps.vitalMagic.product.domain.service.ProductAvailabilityService;
import com.lps.vitalMagic.product.domain.service.ProductCompositionService;
import com.lps.vitalMagic.sales.application.command.CreateSaleCommand;
import com.lps.vitalMagic.sales.application.command.CreateSaleItemCommand;
import com.lps.vitalMagic.sales.application.service.RegisterSaleService;
import com.lps.vitalMagic.sales.domain.exception.SaleDomainException;
import com.lps.vitalMagic.sales.domain.model.entity.Sale;
import com.lps.vitalMagic.sales.domain.repository.SaleRepository;
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
public class RegisterSaleServiceTest {

    @InjectMocks
    public RegisterSaleService registerSaleService;


    @Mock
    public SaleRepository saleRepository;


    @Mock
    public ProductAvailabilityService productAvailabilityService;

    @Mock
    public ProductCompositionService productCompositionService;

    @Mock
    public RegisterSaleTransactionService saleTransactionService;





    @Test
    public void whenRegisterSaleIsSuccessfully(){

        List<CreateSaleItemCommand> itemCommands= new ArrayList<>();

        itemCommands.add(new CreateSaleItemCommand(1L,3));

        CreateSaleCommand saleCommand = new CreateSaleCommand(itemCommands);

        BigDecimal totalExpected=new BigDecimal("2251.5");



        Product product = Product.from(itemCommands.get(0).productId(),7L,
                ProductType.SHAKE,"Batida zapote criptoniano",new BigDecimal("750.50"),true);


        List<IngredientComposition> ingredientCompositions= new ArrayList<>();
        Composition composition= new Composition(ingredientCompositions);
        composition.items().add(new IngredientComposition(1L,6));
        composition.items().add(new IngredientComposition(2L,3));
        composition.items().add(new IngredientComposition(3L,6));

        ProductComposition productComposition =new ProductComposition(product,composition);

        when(productCompositionService.getProductComposition(1L,3)).thenReturn(productComposition);
        when(productAvailabilityService.checkAvailability(composition)).thenReturn(Boolean.TRUE);
        when(saleRepository.save(any()))
                .thenAnswer(invocation -> {
                    Sale sale = invocation.getArgument(0);

                    return Sale.from(
                            1L,
                            sale.getItems(),
                            sale.getTotalAmount(),
                            LocalDateTime.now()
                    );
                });




        Long result= registerSaleService.execute(saleCommand);

        ArgumentCaptor<Sale> saleCaptor = ArgumentCaptor.forClass(Sale.class);




        verify(saleRepository).save(saleCaptor.capture());

        verify(saleTransactionService).registerInventoryTransaction(1L,1L,6);
        verify(saleTransactionService).registerInventoryTransaction(1L,2L,3);
        verify(saleTransactionService).registerInventoryTransaction(1L,3L,6);



        Sale sale = saleCaptor.getValue();
        assertEquals(1L,result);
        assertEquals(0, sale.getTotalAmount().compareTo(totalExpected));
        assertEquals(itemCommands.size(), sale.getItems().size());


    }


    @Test
    public void whenProductNotExists(){
        Long productId=777777777L;
        int quantity=99999;

        List<CreateSaleItemCommand> itemCommands= new ArrayList<>();
        itemCommands.add(new CreateSaleItemCommand(productId,quantity));
        CreateSaleCommand saleCommand = new CreateSaleCommand(itemCommands);

        when(productCompositionService.getProductComposition(productId,quantity)).thenThrow(EntityNotFoundException.class);

        assertThrows(EntityNotFoundException.class,() -> registerSaleService.execute(saleCommand));


    }

    @Test
    public void whenProductIsNotAvailable(){
        List<CreateSaleItemCommand> itemCommands= new ArrayList<>();

        itemCommands.add(new CreateSaleItemCommand(1L,3));

        CreateSaleCommand saleCommand = new CreateSaleCommand(itemCommands);


        Product product = Product.from(itemCommands.get(0).productId(),7L,
                ProductType.SHAKE,"Batida zapote criptoniano",new BigDecimal("750.50"),true);


        List<IngredientComposition> ingredientCompositions= new ArrayList<>();
        Composition composition= new Composition(ingredientCompositions);
        composition.items().add(new IngredientComposition(1L,6));
        composition.items().add(new IngredientComposition(2L,3));
        composition.items().add(new IngredientComposition(3L,6));

        ProductComposition productComposition =new ProductComposition(product,composition);

        when(productCompositionService.getProductComposition(1L,3)).thenReturn(productComposition);
        when(productAvailabilityService.checkAvailability(composition)).thenReturn(Boolean.FALSE);

        assertThrows(SaleDomainException.class,() -> registerSaleService.execute(saleCommand));

    }

    @Test
    public void whenInventoryTransactionFailed(){
        List<CreateSaleItemCommand> itemCommands= new ArrayList<>();

        itemCommands.add(new CreateSaleItemCommand(1L,3));

        CreateSaleCommand saleCommand = new CreateSaleCommand(itemCommands);


        Product product = Product.from(itemCommands.get(0).productId(),7L,
                ProductType.SHAKE,"Batida zapote criptoniano",new BigDecimal("750.50"),true);


        List<IngredientComposition> ingredientCompositions= new ArrayList<>();
        Composition composition= new Composition(ingredientCompositions);
        composition.items().add(new IngredientComposition(1L,6));
        composition.items().add(new IngredientComposition(2L,3));
        composition.items().add(new IngredientComposition(3L,6));

        ProductComposition productComposition =new ProductComposition(product,composition);

        when(productCompositionService.getProductComposition(1L,3)).thenReturn(productComposition);
        when(productAvailabilityService.checkAvailability(composition)).thenReturn(Boolean.TRUE);
        when(saleRepository.save(any()))
                .thenAnswer(invocation -> {
                    Sale sale = invocation.getArgument(0);

                    return Sale.from(
                            1L,
                            sale.getItems(),
                            sale.getTotalAmount(),
                            LocalDateTime.now()
                    );
                });

        doThrow(InventoryTransactionException.class).when(saleTransactionService)
                .registerInventoryTransaction(1L, 1L, 6);


       assertThrows(InventoryTransactionException.class,()-> registerSaleService.execute(saleCommand));

    }

}
