package com.lps.vitalMagic.sale.aplication;

import com.lps.vitalMagic.inventory.domain.model.entity.InventoryTransaction;
import com.lps.vitalMagic.product.domain.model.entity.Product;
import com.lps.vitalMagic.product.domain.model.enums.ProductType;
import com.lps.vitalMagic.product.domain.repository.ProductRepository;
import com.lps.vitalMagic.product.domain.service.ProductAvailabilityService;
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
    public ProductRepository productRepository;


    @Mock
    public ProductAvailabilityService productAvailabilityService;






    @Test
    public void whenRegisterSaleIsSuccessfully(){

        List<CreateSaleItemCommand> itemCommands= new ArrayList<>();

        itemCommands.add(new CreateSaleItemCommand(1L,3));

        CreateSaleCommand saleCommand = new CreateSaleCommand(itemCommands);

        BigDecimal totalExpected=new BigDecimal("2251.5");



        Product product = Product.from(itemCommands.get(0).productId(),7L,
                ProductType.SHAKE,"Batida zapote criptoniano",new BigDecimal("750.50"),true);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productAvailabilityService.checkAvailability(product,3)).thenReturn(Boolean.TRUE);



        Long result= registerSaleService.execute(saleCommand);

        ArgumentCaptor<Sale> saleCaptor = ArgumentCaptor.forClass(Sale.class);





        verify(saleRepository).save(saleCaptor.capture());
        Sale sale= saleCaptor.capture();
        assertEquals(1L,result);
        assertEquals(0, sale.getTotalAmount().compareTo(totalExpected));
        assertEquals(itemCommands.size(), sale.getItems().size());



    }

}
