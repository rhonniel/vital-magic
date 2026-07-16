package com.lps.vitalMagic.purchase.application;

import com.lps.vitalMagic.purchase.application.query.SearchPurchasesQuery;
import com.lps.vitalMagic.purchase.application.service.SearchPurchaseService;
import com.lps.vitalMagic.purchase.application.view.PurchaseItemView;
import com.lps.vitalMagic.purchase.application.view.PurchaseView;
import com.lps.vitalMagic.purchase.domain.repository.PurchaseRepository;
import com.lps.vitalMagic.sales.application.pagination.PageResult;
import com.lps.vitalMagic.sales.application.pagination.Pagination;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SearchPurchaseServiceTest {

    @InjectMocks
    private SearchPurchaseService searchPurchaseService;

    @Mock
    private PurchaseRepository purchaseRepository;


    @Test
    public void whenSearchPurchaseIsSuccessfully(){

        SearchPurchasesQuery query= new SearchPurchasesQuery(LocalDate.now(),LocalDate.now(),7L,new Pagination(1,1));
        List<PurchaseView> purchases= new ArrayList<>();
        List<PurchaseItemView> purchaseItemViews =new ArrayList<>();
        purchaseItemViews.add(new PurchaseItemView(7L,"Test",5,BigDecimal.valueOf(700.00),BigDecimal.valueOf(3500.00)));
        purchases.add(new PurchaseView(1L, LocalDateTime.now(), BigDecimal.valueOf(3500.00),purchaseItemViews));

        PageResult<PurchaseView> pageResult= new PageResult<>(purchases,query.pagination().page(),query.pagination().size(),1,1);

        when(purchaseRepository.search(query)).thenReturn(pageResult);
        PageResult<PurchaseView> result=searchPurchaseService.execute(query);

        assertEquals(purchases.size(),result.content().size());
    }

    @Test
    public void shouldRejectInvalidDateRange(){
        SearchPurchasesQuery query= new SearchPurchasesQuery(LocalDate.MAX,LocalDate.now(),7L,new Pagination(1,1));
        SearchPurchasesQuery queryNull= new SearchPurchasesQuery(null,null,7L,new Pagination(1,1));

        assertThrows(IllegalArgumentException.class,() -> searchPurchaseService.execute(query));
        assertThrows(IllegalArgumentException.class,() -> searchPurchaseService.execute(queryNull)) ;

    }

}
