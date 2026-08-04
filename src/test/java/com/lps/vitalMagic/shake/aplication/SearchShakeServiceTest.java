package com.lps.vitalMagic.shake.aplication;


import com.lps.vitalMagic.common.presentation.pagination.PageResult;
import com.lps.vitalMagic.common.presentation.pagination.Pagination;
import com.lps.vitalMagic.inventory.application.view.ItemView;
import com.lps.vitalMagic.inventory.domain.model.entity.Attribute;
import com.lps.vitalMagic.inventory.domain.model.entity.Item;
import com.lps.vitalMagic.inventory.domain.model.entity.ItemAttribute;
import com.lps.vitalMagic.shake.application.query.SearchShakeQuery;
import com.lps.vitalMagic.shake.application.service.SearchShakeService;
import com.lps.vitalMagic.shake.application.view.ShakeView;
import com.lps.vitalMagic.shake.domain.model.entity.Shake;
import com.lps.vitalMagic.shake.domain.model.entity.ShakeIngredient;
import com.lps.vitalMagic.shake.domain.model.enums.ShakeCategory;
import com.lps.vitalMagic.shake.domain.model.enums.ShakeType;
import com.lps.vitalMagic.shake.domain.repository.ShakeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static  org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class SearchShakeServiceTest {

    @InjectMocks
    private SearchShakeService searchShakeService;

    @Mock
    private ShakeRepository shakeRepository;


    @Test
    public void searchShakeWithQueryIsSuccessfully(){

        SearchShakeQuery query =
                new SearchShakeQuery(ShakeType.STANDARD, ShakeCategory.RARE, new Pagination(0,1));


        PageResult<ShakeView> expected =
                new PageResult<>(List.of(), 0, 1, 0, 0);

        when(shakeRepository.searchAvailableShakes(query)).thenReturn(expected);

        PageResult<ShakeView> result = searchShakeService.execute(query);

        assertSame(expected,result);



    }


}
