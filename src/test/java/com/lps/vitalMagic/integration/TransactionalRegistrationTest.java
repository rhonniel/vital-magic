package com.lps.vitalMagic.integration;

import com.lps.vitalMagic.config.MySqlDataJpaTest;
import com.lps.vitalMagic.inventory.application.service.FindItemService;
import com.lps.vitalMagic.inventory.application.service.ItemCostProvider;
import com.lps.vitalMagic.inventory.application.service.ItemCurrentStockService;
import com.lps.vitalMagic.inventory.application.service.RegisterPurchaseTransactionService;
import com.lps.vitalMagic.inventory.application.service.RegisterSaleTransactionService;
import com.lps.vitalMagic.inventory.domain.exception.InventoryTransactionException;
import com.lps.vitalMagic.inventory.infrastructure.persistence.entity.ItemEntity;
import com.lps.vitalMagic.inventory.infrastructure.persistence.entity.ItemInventoryEntity;
import com.lps.vitalMagic.inventory.infrastructure.persistence.repository.ItemJpaRepository;
import com.lps.vitalMagic.inventory.infrastructure.persistence.repository.ItemInventoryJpaRepository;
import com.lps.vitalMagic.inventory.infrastructure.persistence.repository.impl.JpaAttributeRepository;
import com.lps.vitalMagic.inventory.infrastructure.persistence.repository.impl.JpaInventoryTransactionRepository;
import com.lps.vitalMagic.inventory.infrastructure.persistence.repository.impl.JpaItemInventoryRepository;
import com.lps.vitalMagic.inventory.infrastructure.persistence.repository.impl.JpaItemRepository;
import com.lps.vitalMagic.product.aplication.service.CreateShakeProductService;
import com.lps.vitalMagic.product.domain.model.enums.ProductType;
import com.lps.vitalMagic.product.domain.service.ProductAvailabilityService;
import com.lps.vitalMagic.product.domain.service.ProductCompositionService;
import com.lps.vitalMagic.product.infrastructure.persistance.entity.ProductEntity;
import com.lps.vitalMagic.product.infrastructure.persistance.repository.ProductEntityJpaRepository;
import com.lps.vitalMagic.product.infrastructure.persistance.repository.impl.JpaProductRepository;
import com.lps.vitalMagic.purchase.application.command.CreatePurchaseCommand;
import com.lps.vitalMagic.purchase.application.command.CreatePurchaseItemCommand;
import com.lps.vitalMagic.purchase.application.service.RegisterPurchaseService;
import com.lps.vitalMagic.purchase.infrastructure.persistance.repository.impl.JpaPurchaseRepository;
import com.lps.vitalMagic.sales.application.command.CreateSaleCommand;
import com.lps.vitalMagic.sales.application.command.CreateSaleItemCommand;
import com.lps.vitalMagic.sales.application.service.RegisterSaleService;
import com.lps.vitalMagic.sales.infrastructure.persistence.repository.impl.JpaSaleRepository;
import com.lps.vitalMagic.shake.application.command.CreateShakeIngredientCommand;
import com.lps.vitalMagic.shake.application.command.CreateStandardShakeCommand;
import com.lps.vitalMagic.shake.application.service.CreateStandardShakeService;
import com.lps.vitalMagic.shake.application.service.FindShakeProductSourceService;
import com.lps.vitalMagic.shake.domain.model.enums.ShakeCategory;
import com.lps.vitalMagic.shake.infrastructure.persistence.repository.impl.JpaShakeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({RegisterSaleService.class, RegisterPurchaseService.class, CreateStandardShakeService.class,
        ProductAvailabilityService.class, ProductCompositionService.class, RegisterSaleTransactionService.class,
        FindItemService.class, RegisterPurchaseTransactionService.class, ItemCurrentStockService.class,
        CreateShakeProductService.class, FindShakeProductSourceService.class, ItemCostProvider.class,
        JpaSaleRepository.class, JpaPurchaseRepository.class, JpaShakeRepository.class, JpaProductRepository.class,
        JpaItemRepository.class, JpaAttributeRepository.class, JpaItemInventoryRepository.class,
        JpaInventoryTransactionRepository.class})
// The service proxy must own the transaction: an ambient test transaction would hide missing boundaries.
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class TransactionalRegistrationTest extends MySqlDataJpaTest {
    @Autowired private RegisterSaleService sales;
    @Autowired private RegisterPurchaseService purchases;
    @Autowired private CreateStandardShakeService shakes;
    @Autowired private ItemCurrentStockService stock;
    @Autowired private ItemJpaRepository items;
    @Autowired private ItemInventoryJpaRepository inventories;
    @Autowired private ProductEntityJpaRepository products;
    @Autowired private JdbcTemplate jdbc;

    @AfterEach
    void cleanCommittedFixtures() {
        // Only the disposable Testcontainers database; these tests intentionally commit.
        for (String table : List.of("sale_item", "sale", "purchase_item", "purchase",
                "inventory_transaction", "product", "shake_ingredient", "shake", "item_inventory", "item")) {
            jdbc.update("DELETE FROM " + table);
        }
    }

    @Test
    void shouldCommitSaleWhenCombinedConsumptionFitsStock() {
        Long itemId = saveItem("Shared ingredient", 7, new BigDecimal("2.00"));
        Long first = saveProduct(itemId, "First product");
        Long second = saveProduct(itemId, "Second product");

        Long saleId = sales.execute(saleCommand(first, second));

        assertEquals(1, count("sale"));
        assertEquals(Map.of(first, 3, second, 4), quantities("sale_item", "product_id", "sale_id", saleId));
        assertEquals(2, count("inventory_transaction"));
        assertEquals(7, jdbc.queryForObject(
                "SELECT SUM(quantity) FROM inventory_transaction WHERE source_id = ? AND type = 'SALE' AND item_id = ?",
                Integer.class, saleId, itemId));
        assertEquals(0, stock.getCurrentStock(itemId));
    }

    @Test
    void shouldRollbackSaleWhenCombinedConsumptionExceedsStock() {
        Long itemId = saveItem("Shared ingredient", 6, new BigDecimal("2.00"));
        Long first = saveProduct(itemId, "First product");
        Long second = saveProduct(itemId, "Second product");

        // Both prechecks pass (3 <= 6 and 4 <= 6). The second movement sees only 3 remaining.
        var error = assertThrows(InventoryTransactionException.class,
                () -> sales.execute(saleCommand(first, second)));

        assertEquals("The inventory don't have enough existences", error.getMessage());
        assertEquals(0, count("sale"));
        assertEquals(0, count("sale_item"));
        assertEquals(0, count("inventory_transaction"));
        assertEquals(6, stock.getCurrentStock(itemId));
    }

    @Test
    void shouldCommitPurchaseWithDifferentQuantitiesAndCosts() {
        Long first = saveItem("First ingredient", 0, new BigDecimal("2.00"));
        Long second = saveItem("Second ingredient", 0, new BigDecimal("4.00"));

        Long purchaseId = purchases.execute(purchaseCommand(first, second, new BigDecimal("4.50")));

        assertEquals(1, count("purchase"));
        assertEquals(Map.of(first, 2, second, 5), quantities("purchase_item", "item_id", "purchase_id", purchaseId));
        assertEquals(Map.of(first, 2, second, 5),
                quantities("inventory_transaction", "item_id", "source_id", purchaseId));
        assertEquals(2, jdbc.queryForObject(
                "SELECT COUNT(*) FROM inventory_transaction WHERE source_id = ? AND type = 'PURCHASE'", Integer.class, purchaseId));
        assertEquals(0, new BigDecimal("4.50").compareTo(jdbc.queryForObject(
                "SELECT unit_cost FROM inventory_transaction WHERE source_id = ? AND item_id = ?",
                BigDecimal.class, purchaseId, second)));
        assertEquals(2, stock.getCurrentStock(first));
        assertEquals(5, stock.getCurrentStock(second));
    }

    @Test
    void shouldRollbackPurchaseWhenLaterMovementCostExceedsDatabasePrecision() {
        Long first = saveItem("First ingredient", 0, new BigDecimal("2.00"));
        Long second = saveItem("Second ingredient", 0, new BigDecimal("4.00"));

        // Fits purchase_item DECIMAL(19,4), but not inventory_transaction DECIMAL(10,4).
        // IDENTITY inserts execute immediately, so the first movement has already been inserted.
        var error = assertThrows(DataIntegrityViolationException.class,
                () -> purchases.execute(purchaseCommand(first, second, new BigDecimal("1000000.00"))));

        assertTrue(error.getMostSpecificCause().getMessage().contains("unit_cost"));
        assertEquals(0, count("purchase"));
        assertEquals(0, count("purchase_item"));
        assertEquals(0, count("inventory_transaction"));
        assertEquals(0, stock.getCurrentStock(first));
    }

    @Test
    void shouldCommitStandardShakeAndProductReferencingShake() {
        Long first = saveItem("First ingredient", 0, new BigDecimal("2.00"));
        Long second = saveItem("Second ingredient", 0, new BigDecimal("4.00"));
        Long third = saveItem("Third ingredient", 0, new BigDecimal("3.00"));

        Long shakeId = shakes.execute(shakeCommand(first, second, third));

        assertEquals(1, count("shake"));
        assertEquals(Map.of(first, 2, second, 3, third, 1), quantities("shake_ingredient", "item_id", "shake_id", shakeId));
        assertEquals(1, count("product"));
        assertEquals(shakeId, jdbc.queryForObject(
                "SELECT reference_no FROM product WHERE product_type = 'SHAKE'", Long.class));
    }

    @Test
    void shouldRollbackStandardShakeWhenIngredientCostIsMissing() {
        Long first = saveItem("Costed ingredient", 0, new BigDecimal("2.00"));
        // Item exists, but no inventory row supplies its cost. Zero cost is not missing cost.
        Long second = items.saveAndFlush(new ItemEntity("Uncosted ingredient", "Description", true)).getId();
        Long third = saveItem("Another costed ingredient", 0, new BigDecimal("3.00"));

        var error = assertThrows(IllegalStateException.class,
                () -> shakes.execute(shakeCommand(first, second, third)));

        assertEquals("Item does not have cost", error.getMessage());
        assertEquals(0, count("shake"));
        assertEquals(0, count("shake_ingredient"));
        assertEquals(0, count("product"));
        assertTrue(items.existsById(second));
    }

    private Long saveItem(String name, int initialStock, BigDecimal cost) {
        Long id = items.saveAndFlush(new ItemEntity(name, "Description", true)).getId();
        inventories.saveAndFlush(new ItemInventoryEntity(null, id, cost, 0, initialStock, true));
        return id;
    }

    private Long saveProduct(Long itemId, String name) {
        return products.saveAndFlush(new ProductEntity(null, itemId, ProductType.SIMPLE_PRODUCT,
                name, new BigDecimal("10.00"), true)).getId();
    }

    private CreateSaleCommand saleCommand(Long first, Long second) {
        return new CreateSaleCommand(List.of(new CreateSaleItemCommand(first, 3), new CreateSaleItemCommand(second, 4)));
    }

    private CreatePurchaseCommand purchaseCommand(Long first, Long second, BigDecimal secondCost) {
        return new CreatePurchaseCommand(List.of(
                new CreatePurchaseItemCommand(first, 2, new BigDecimal("2.25")),
                new CreatePurchaseItemCommand(second, 5, secondCost)));
    }

    private CreateStandardShakeCommand shakeCommand(Long first, Long second, Long third) {
        return new CreateStandardShakeCommand("Standard shake", "Description", ShakeCategory.RARE,
                List.of(new CreateShakeIngredientCommand(first, 2), new CreateShakeIngredientCommand(second, 3),
                        new CreateShakeIngredientCommand(third, 1)));
    }

    private int count(String table) {
        return jdbc.queryForObject("SELECT COUNT(*) FROM " + table, Integer.class);
    }

    private Map<Long, Integer> quantities(String table, String itemColumn, String parentColumn, Long parentId) {
        return jdbc.query("SELECT " + itemColumn + ", quantity FROM " + table + " WHERE " + parentColumn + " = ?",
                rs -> {
                    Map<Long, Integer> result = new java.util.HashMap<>();
                    while (rs.next()) {
                        assertNull(result.put(rs.getLong(1), rs.getInt(2)), "Unexpected duplicate item");
                    }
                    return result;
                }, parentId);
    }
}
