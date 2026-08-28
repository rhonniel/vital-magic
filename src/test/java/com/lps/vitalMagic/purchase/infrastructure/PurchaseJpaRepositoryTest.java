package com.lps.vitalMagic.purchase.infrastructure;

import com.lps.vitalMagic.common.pagination.PageResult;
import com.lps.vitalMagic.common.pagination.Pagination;
import com.lps.vitalMagic.config.MySqlDataJpaTest;
import com.lps.vitalMagic.inventory.infrastructure.persistence.entity.ItemEntity;
import com.lps.vitalMagic.inventory.infrastructure.persistence.repository.ItemJpaRepository;
import com.lps.vitalMagic.purchase.application.query.SearchPurchasesQuery;
import com.lps.vitalMagic.purchase.application.view.PurchaseView;
import com.lps.vitalMagic.purchase.domain.model.entity.Purchase;
import com.lps.vitalMagic.purchase.domain.model.entity.PurchaseItem;
import com.lps.vitalMagic.purchase.infrastructure.persistance.repository.impl.JpaPurchaseRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@Import(JpaPurchaseRepository.class)
class PurchaseJpaRepositoryTest extends MySqlDataJpaTest {

    @Autowired
    private JpaPurchaseRepository repository;

    @Autowired
    private ItemJpaRepository itemRepository;

    @Test
    void shouldSaveAndFindPurchase() {
        ItemEntity item = saveItem("Dragon Tail");

        Purchase purchase = createPurchase(
                item,
                new BigDecimal("10.00"),
                2,
                LocalDateTime.of(2026, 8, 10, 10, 0)
        );

        Purchase savedPurchase = repository.save(purchase);

        Purchase result = repository.findById(savedPurchase.getId())
                .orElseThrow();

        assertNotNull(result.getId());
        assertEquals(1, result.getItems().size());
        assertEquals(
                item.getId(),
                result.getItems()
                        .get(0)
                        .getItem()
                        .itemId()
        );
        assertEquals(
                0,
                new BigDecimal("20.00")
                        .compareTo(result.getTotalAmount())
        );
    }

    @Test
    void shouldSearchPurchasesByDateAndItem() {
        ItemEntity selectedItem = saveItem("Dragon Tail");
        ItemEntity excludedItem = saveItem("Fairy Dust");

        Purchase selectedPurchase = repository.save(
                createPurchase(
                        selectedItem,
                        new BigDecimal("10.00"),
                        2,
                        LocalDateTime.of(2026, 8, 10, 10, 0)
                )
        );

        repository.save(
                createPurchase(
                        selectedItem,
                        new BigDecimal("10.00"),
                        1,
                        LocalDateTime.of(2026, 8, 20, 10, 0)
                )
        );

        repository.save(
                createPurchase(
                        excludedItem,
                        new BigDecimal("15.00"),
                        1,
                        LocalDateTime.of(2026, 8, 11, 10, 0)
                )
        );

        SearchPurchasesQuery query = new SearchPurchasesQuery(
                LocalDate.of(2026, 8, 9),
                LocalDate.of(2026, 8, 12),
                selectedItem.getId(),
                new Pagination(0, 10)
        );

        PageResult<PurchaseView> result = repository.search(query);

        assertEquals(1, result.content().size());
        assertEquals(
                selectedPurchase.getId(),
                result.content().get(0).id()
        );
    }

    @Test
    void shouldReturnPurchasesOrderedAndPaginated() {
        ItemEntity item = saveItem("Dragon Tail");

        repository.save(
                createPurchase(
                        item,
                        new BigDecimal("10.00"),
                        1,
                        LocalDateTime.of(2026, 8, 8, 10, 0)
                )
        );

        Purchase middlePurchase = repository.save(
                createPurchase(
                        item,
                        new BigDecimal("10.00"),
                        1,
                        LocalDateTime.of(2026, 8, 10, 10, 0)
                )
        );

        Purchase newestPurchase = repository.save(
                createPurchase(
                        item,
                        new BigDecimal("10.00"),
                        1,
                        LocalDateTime.of(2026, 8, 12, 10, 0)
                )
        );

        SearchPurchasesQuery query = new SearchPurchasesQuery(
                null,
                null,
                null,
                new Pagination(0, 2)
        );

        PageResult<PurchaseView> result = repository.search(query);

        assertEquals(2, result.content().size());
        assertEquals(
                newestPurchase.getId(),
                result.content().get(0).id()
        );
        assertEquals(
                middlePurchase.getId(),
                result.content().get(1).id()
        );
        assertEquals(3, result.totalElements());
        assertEquals(2, result.totalPages());
    }

    private ItemEntity saveItem(String name) {
        return itemRepository.saveAndFlush(
                new ItemEntity(
                        name,
                        name + " description",
                        true
                )
        );
    }

    private Purchase createPurchase(
            ItemEntity item,
            BigDecimal unitCost,
            int quantity,
            LocalDateTime createdAt
    ) {
        BigDecimal subtotal = unitCost.multiply(
                BigDecimal.valueOf(quantity)
        );

        PurchaseItem purchaseItem = PurchaseItem.from(
                null,
                item.getId(),
                item.getName(),
                unitCost,
                quantity,
                subtotal
        );

        return Purchase.from(
                null,
                List.of(purchaseItem),
                subtotal,
                createdAt
        );
    }
}