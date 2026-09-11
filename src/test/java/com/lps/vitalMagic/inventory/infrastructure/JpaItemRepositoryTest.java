package com.lps.vitalMagic.inventory.infrastructure;

import com.lps.vitalMagic.config.MySqlDataJpaTest;
import com.lps.vitalMagic.common.pagination.Pagination;
import com.lps.vitalMagic.inventory.application.query.SearchItemsQuery;
import com.lps.vitalMagic.inventory.application.view.ItemView;
import com.lps.vitalMagic.inventory.infrastructure.persistence.entity.ItemEntity;
import com.lps.vitalMagic.inventory.infrastructure.persistence.repository.ItemJpaRepository;
import com.lps.vitalMagic.inventory.infrastructure.persistence.repository.impl.JpaAttributeRepository;
import com.lps.vitalMagic.inventory.infrastructure.persistence.repository.impl.JpaItemRepository;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@Import({JpaItemRepository.class, JpaAttributeRepository.class})
class JpaItemRepositoryTest extends MySqlDataJpaTest {
    @Autowired private JpaItemRepository repository;
    @Autowired private ItemJpaRepository items;

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = "DRAGON")
    void shouldSearchOnlyActiveItems(String name) {
        var active = items.saveAndFlush(new ItemEntity("Dragon tail", "Active", true));
        items.saveAndFlush(new ItemEntity("Dragon scale", "Inactive", false));
        if (name != null) {
            items.saveAndFlush(new ItemEntity("Potion", "Unrelated active item", true));
        }

        var result = repository.searchAvailableItems(new SearchItemsQuery(name, new Pagination(0, 10)));

        assertEquals(List.of(active.getId()), result.content().stream().map(ItemView::id).toList());
        assertEquals(1, result.totalElements());
    }
}
