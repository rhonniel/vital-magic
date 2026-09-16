package com.lps.vitalMagic.e2e.purchase;

import com.lps.vitalMagic.config.MySqlIntegrationTest;
import com.lps.vitalMagic.inventory.infrastructure.persistence.entity.ItemEntity;
import com.lps.vitalMagic.inventory.infrastructure.persistence.entity.ItemInventoryEntity;
import com.lps.vitalMagic.inventory.infrastructure.persistence.repository.InventoryTransactionJpaRepository;
import com.lps.vitalMagic.inventory.infrastructure.persistence.repository.ItemInventoryJpaRepository;
import com.lps.vitalMagic.inventory.infrastructure.persistence.repository.ItemJpaRepository;
import com.lps.vitalMagic.purchase.application.controller.PurchaseController;
import com.lps.vitalMagic.purchase.infrastructure.persistance.repository.PurchaseJpaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RegisterPurchaseE2ETest extends MySqlIntegrationTest {
    @Autowired private TestRestTemplate http;
    @Autowired private ItemJpaRepository items;
    @Autowired private ItemInventoryJpaRepository inventories;
    @Autowired private InventoryTransactionJpaRepository transactions;
    @Autowired private PurchaseJpaRepository purchases;
    @Autowired private JdbcTemplate jdbc;

    @AfterEach
    void cleanCommittedFixtures() {
        // Disposable Testcontainers database; the HTTP request commits independently.
        jdbc.update("DELETE FROM purchase_item");
        jdbc.update("DELETE FROM purchase");
        jdbc.update("DELETE FROM inventory_transaction");
        jdbc.update("DELETE FROM item_inventory");
        jdbc.update("DELETE FROM item");
    }

    @Test
    void shouldRegisterPurchaseAndIncreaseStock() {
        Long itemId = items.saveAndFlush(new ItemEntity("Dragon Tail", "Description", true)).getId();
        inventories.saveAndFlush(new ItemInventoryEntity(
                null, itemId, new BigDecimal("2.00"), 0, 2, true));
        var request = new PurchaseController.CreatePurchaseRequest(List.of(
                new PurchaseController.CreatePurchaseItemRequest(itemId, 5, new BigDecimal("2.25"))));

        var response = http.postForEntity("/purchase", request, PurchaseController.CreatePurchaseResponse.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        Long purchaseId = response.getBody().purchaseId();
        assertNotNull(purchaseId);
        assertTrue(purchases.existsById(purchaseId));
        assertEquals(5, jdbc.queryForObject(
                "SELECT quantity FROM purchase_item WHERE purchase_id = ? AND item_id = ?",
                Integer.class, purchaseId, itemId));
        assertEquals(5, jdbc.queryForObject("""
                SELECT quantity FROM inventory_transaction
                WHERE source_id = ? AND type = 'PURCHASE' AND item_id = ?
                """, Integer.class, purchaseId, itemId));
        // Available stock includes committed movements not yet consolidated into item_inventory.
        assertEquals(7, inventories.findByActiveTrueAndItemId(itemId).orElseThrow().getCurrentStock()
                + transactions.findTotalUnprocessedStocksByItemId(itemId));
    }
}
