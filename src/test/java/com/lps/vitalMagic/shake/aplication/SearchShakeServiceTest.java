package com.lps.vitalMagic.shake.aplication;


import com.lps.vitalMagic.inventory.domain.model.entity.Attribute;
import com.lps.vitalMagic.inventory.domain.model.entity.Item;
import com.lps.vitalMagic.inventory.domain.model.entity.ItemAttribute;
import com.lps.vitalMagic.inventory.domain.repository.AttributeRepository;
import com.lps.vitalMagic.inventory.domain.repository.ItemRepository;
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

import java.util.List;
import java.util.Set;

import static  org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class SearchShakeServiceTest {

    @InjectMocks
    private SearchShakeService searchShakeService;

    @Mock
    private ShakeRepository shakeRepository;

    @Mock
    private AttributeRepository attributeRepository;

    @Mock
    private ItemRepository itemRepository;


    @Test
    public void searchShakeWithQueryIsSuccessfully(){

        SearchShakeQuery query =
                new SearchShakeQuery(ShakeType.STANDARD, ShakeCategory.RARE);

        Attribute strength = new Attribute (1L, "Strength","dd","ddd");
        Attribute agility = new Attribute(2L, "Agility","dd","ddd");

        Item banana = Item.from(1L,
                "Banana",
                "banana",
                List.of(
                        ItemAttribute.from(1L, 10),
                        ItemAttribute.from(2L, 5)),
                true
        );

        Item strawberry =Item.from(
                2L,
                "Fresa",
                "Fresa",
                List.of(
                        ItemAttribute.from(1L, 3),
                        ItemAttribute.from(2L, 2)
                ),
                true
        );

        Item oats = Item.from(
                3L,
                "Avena",
                "Avena",
                List.of(
                        ItemAttribute.from(1L, 7)
                ),
                true
        );

        Shake shake = Shake.from(
                1L,
                "Batida Pirada",
                "Loca como Piru",
                ShakeType.STANDARD,
                ShakeCategory.RARE,
                List.of(
                         ShakeIngredient.from(1L, 1L,1),
                        ShakeIngredient.from(1L, 2L,1),
                        ShakeIngredient.from(1L, 3L,1)
                ),
                true
        );

        when(shakeRepository.searchAvailableShakes(query))
                .thenReturn(List.of(shake));

        when(itemRepository.findAllById(Set.of(1L, 2L, 3L)))
                .thenReturn(List.of(banana, strawberry, oats));

        when(attributeRepository.findAll())
                .thenReturn(List.of(strength, agility));

        List<ShakeView> result = searchShakeService.execute(query);

        assertEquals(1, result.size());

        ShakeView view = result.get(0);

        assertEquals("Batida Pirada", view.name());
        assertEquals(ShakeType.STANDARD, view.shakeType());
        assertEquals(ShakeCategory.RARE, view.shakeCategory());

        assertEquals(3, view.ingredients().size());

        assertEquals(2, view.attributes().size());

        assertTrue(
                view.attributes().stream()
                        .anyMatch(a ->
                                a.attributeId().equals(1L)
                                        && a.value() == 20)
        );

        assertTrue(
                view.attributes().stream()
                        .anyMatch(a ->
                                a.attributeId().equals(2L)
                                        && a.value() == 7)
        );


    }

    @Test
    public void  searchShakeReturnsEmptyListWhenNoResultsFound(){
        SearchShakeQuery query =
                new SearchShakeQuery(ShakeType.STANDARD, ShakeCategory.RARE);

        when(shakeRepository.searchAvailableShakes(query))
                .thenReturn(List.of());

        List<ShakeView> result = searchShakeService.execute(query);

        assertTrue(result.isEmpty());


    }

}
