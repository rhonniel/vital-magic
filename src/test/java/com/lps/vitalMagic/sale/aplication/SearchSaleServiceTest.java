package com.lps.vitalMagic.sale.aplication;

import com.lps.vitalMagic.sales.application.pagination.PageResult;
import com.lps.vitalMagic.sales.application.pagination.Pagination;
import com.lps.vitalMagic.sales.application.query.SearchSaleQuery;
import com.lps.vitalMagic.sales.application.service.SearchSaleService;
import com.lps.vitalMagic.sales.application.view.SaleItemView;
import com.lps.vitalMagic.sales.application.view.SaleView;
import com.lps.vitalMagic.sales.domain.repository.SaleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class SearchSaleServiceTest {

    @InjectMocks
    private SearchSaleService searchSaleService;

    @Mock
    private SaleRepository saleRepository;



    @Test
    public void searchSaleIsSuccessfully(){

        SearchSaleQuery query = new SearchSaleQuery(LocalDate.now(),LocalDate.now(),777L,new Pagination(1,10));
        List<SaleView> sales = new ArrayList<>();
        List<SaleItemView> saleItems = new ArrayList<>();
        saleItems.add( new SaleItemView(11L,"Batida Adrenalina",
              2,  BigDecimal.valueOf(450.00),BigDecimal.valueOf(900.00)));
        sales.add(new SaleView(1L,LocalDateTime.now(),BigDecimal.valueOf(900.00),saleItems));


        PageResult<SaleView> salePageResult= new PageResult<>(sales,query.pagination().page(),query.pagination().size(),1,1);


        when(saleRepository.searchAvailableShakes(query)).thenReturn(salePageResult);

        PageResult<SaleView> result= searchSaleService.execute(query);

       assertEquals(sales.size(),result.content().size());


    }
}
