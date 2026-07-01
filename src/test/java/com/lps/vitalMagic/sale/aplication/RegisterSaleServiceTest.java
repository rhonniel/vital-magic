package com.lps.vitalMagic.sale.aplication;

import com.lps.vitalMagic.inventory.domain.service.RegisterSaleTransactionService;
import com.lps.vitalMagic.product.domain.model.data.Composition;
import com.lps.vitalMagic.product.domain.model.data.IngredientComposition;
import com.lps.vitalMagic.product.domain.model.data.ProductComposition;
import com.lps.vitalMagic.product.domain.model.entity.Product;
import com.lps.vitalMagic.product.domain.model.enums.ProductType;
import com.lps.vitalMagic.product.domain.repository.ProductRepository;
import com.lps.vitalMagic.product.domain.service.ProductAvailabilityService;
import com.lps.vitalMagic.product.domain.service.ProductCompositionService;
import com.lps.vitalMagic.sales.application.command.CreateSaleCommand;
import com.lps.vitalMagic.sales.application.command.CreateSaleItemCommand;
import com.lps.vitalMagic.sales.application.service.RegisterSaleService;
import com.lps.vitalMagic.sales.domain.model.entity.Sale;
import com.lps.vitalMagic.sales.domain.repository.SaleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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

        // Ajusta construccion de este objeto que composision agrege su item
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
                            sale.getTotalAmount()
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

}
