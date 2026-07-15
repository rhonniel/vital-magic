package com.lps.vitalMagic.inventory.application;

import com.lps.vitalMagic.inventory.application.dto.ItemInfo;
import com.lps.vitalMagic.inventory.application.service.FindItemService;
import com.lps.vitalMagic.inventory.domain.model.entity.Item;
import com.lps.vitalMagic.inventory.domain.model.entity.ItemAttribute;
import com.lps.vitalMagic.inventory.domain.repository.ItemRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class FindItemServiceTest {

    @InjectMocks
    private FindItemService findItemService;

    @Mock
    private ItemRepository itemRepository;


    @Test
    public void whenFindItemInfoIsSuccessfully(){
        List<ItemAttribute> attributes =new ArrayList<>();
        attributes.add(ItemAttribute.from(1L,3));
        attributes.add(ItemAttribute.from(2L,5));
        Item item=Item.from(777L,"Colirio","test",attributes,true);

        when(itemRepository.findById(777L)).thenReturn(Optional.of(item));

        ItemInfo itemInfo=findItemService.getItemInfo(777L);

        assertEquals(item.getId(),itemInfo.itemId());
        assertEquals(item.getName(),itemInfo.name());


    }

    @Test
    public void whenItemNotExistsFindItemThrowAException(){
        when(itemRepository.findById(777L)).thenThrow(EntityNotFoundException.class);
        assertThrows(EntityNotFoundException.class,() -> findItemService.getItemInfo(777L));

    }
}
