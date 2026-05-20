package com.lps.vitalMagic.inventory.application;

import com.lps.vitalMagic.inventory.application.query.SearchItemsQuery;
import com.lps.vitalMagic.inventory.application.service.SearchAvailableItemsService;
import com.lps.vitalMagic.inventory.application.view.ItemView;
import com.lps.vitalMagic.inventory.domain.model.entity.Item;
import com.lps.vitalMagic.inventory.domain.repository.ItemRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SearchAvailableItemsServiceTest {

    @InjectMocks
    private SearchAvailableItemsService service;

    @Mock
    private ItemRepository itemRepository;



    @Test
    public void shouldReturnAvailableItems(){

        SearchItemsQuery query = new SearchItemsQuery("Pelo");

        Item item = Item.from(
                7L,
                "Pelo de araña",
                "Hebras de cabello sacadas de un arácnido",
                List.of(),
                true
        );

        when(itemRepository.searchAvailableItems(query))
                .thenReturn(List.of(item));

        List<ItemView> result = service.execute(query);

        assertNotNull(result);
        assertEquals(1, result.size());

        ItemView itemView = result.get(0);

        assertEquals(7L, itemView.id());
        assertEquals("Pelo de araña", itemView.name());
        assertEquals(
                "Hebras de cabello sacadas de un arácnido",
                itemView.description()
        );


    }


}
