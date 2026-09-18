# E2E de backend

## Objetivo y alcance

Validar unos pocos flujos completos de alto valor:

```text
HTTP real → controller → application/service → domain services
→ repository adapters → Spring Data JPA/Hibernate → MySQL
```

Objetivo aproximado: 3 a 5 tests principales. Priorizar happy paths críticos; añadir uno o dos fallos solo si protegen una propiedad transversal importante. No replicar combinaciones, validaciones, filtros ni cálculos de unit, MVC o JPA tests. El checkout ya tiene `TransactionalRegistrationTest` para commits y rollbacks reales de ventas, compras y shakes.

## Configuración y HTTP

```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RegisterSaleE2ETest extends MySqlIntegrationTest {
    @Autowired private TestRestTemplate http;
}
```

Reutilizar `src/test/java/com/lps/vitalMagic/config/MySqlIntegrationTest.java`: es el nombre actual de la antigua `MySqlDataJpaTest`. Mantiene un `MySQLContainer` compartido (`mysql:8.4`), lo inicia manualmente en un bloque estático y expone las propiedades con `@DynamicPropertySource`. No reemplazarlo, renombrarlo por rutina ni introducir una segunda estrategia. No añadir `@Container`/`@Testcontainers` sobre el mismo recurso ni reiniciarlo entre tests.

`TestRestTemplate`, disponible con las dependencias actuales, envía HTTP real al puerto aleatorio. Invocar el endpoint; nunca sustituir la acción por llamadas directas a controllers, use cases o application services. Sin `@MockitoBean` ni mocks internos. `@WebMvcTest` no sustituye este flujo.

## Fixtures

Crear únicamente el estado inicial necesario, con repositories y helpers privados siguiendo los tests vecinos. Una venta simple requiere un `item`, un `item_inventory` con stock suficiente y un `product` de tipo `SIMPLE_PRODUCT` que referencia el item. Un shake estándar requiere al menos tres ingredientes según el dominio actual, con inventario que proporcione sus costes.

Preferir `src/test/java/com/lps/vitalMagic/e2e/{sale,purchase,shake}` para tests nuevos. Conservar el E2E de ventas existente en `sale/e2e` si moverlo no aporta valor. No crear frameworks de fixtures, builders genéricos ni jerarquías adicionales.

## Persistencia y assertions

Comprobar solamente:

1. Status HTTP.
2. Identificador/dato principal de respuesta.
3. Registro principal persistido.
4. Efectos secundarios críticos.

Son válidos Spring Data repositories (`existsById`, `findById`, consultas existentes) y `JdbcTemplate` para tablas o efectos persistidos. No añadir métodos ni queries de producción para un assertion.

El stock actual del checkout es `item_inventory.current_stock` más el saldo de movimientos pendientes (`PURCHASE` suma, `SALE` resta). Registrar una venta o compra no actualiza inmediatamente esa columna. Verificar el saldo persistido con los repositories existentes, sin cambiar este comportamiento.

## Relaciones LAZY

Normalmente no hay sesión JPA abierta alrededor de los assertions: navegar una colección `LAZY` después de `findById` puede producir legítimamente `LazyInitializationException`. Verificar hijos mediante repository o SQL; no cambiar mappings a `EAGER` ni modificar producción por comodidad del test.

## Transacciones y limpieza

No usar `@Transactional` para rollback automático del E2E: con `RANDOM_PORT`, HTTP se procesa en otro thread/transacción. La transacción real pertenece a los servicios de producción.

Las peticiones hacen commits reales. Limpiar explícitamente los datos creados, normalmente con `@AfterEach`, también cuando fallen los assertions. Eliminar hijos antes que padres: por ejemplo `sale_item → sale`, después movimientos, producto, inventario e item. Para shakes: producto, `shake_ingredient → shake`, inventario e items. Conservar catálogos de Flyway.

El borrado de tablas completas solo es apropiado en la BD desechable de Testcontainers, con ejecución secuencial y fixtures controlados, como en los tests actuales. No habilitar paralelismo sobre estos datos compartidos. No depender de rollback del test, de IDs fijos ni del orden entre clases. No reiniciar el contenedor.

## Patrón pequeño: venta real

Fragmento basado en `sale/e2e/RegisterSaleE2ETest.java`; los repositorios y helpers de fixtures/limpieza quedan en la clase. Es un patrón, no una plantilla rígida:

```java
@Test
void shouldRegisterSaleAndConsumeStock() {
    Long itemId = saveItem("Dragon Tail", 10, new BigDecimal("2.00"));
    Long productId = saveProduct(itemId, "Dragon Tail Potion");
    var request = new SaleController.CreateSaleRequest(List.of(
            new SaleController.CreateSaleItemRequest(productId, 3)));

    var response = http.postForEntity("/sale", request,
            SaleController.CreateSaleResponse.class);

    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertNotNull(response.getBody());
    Long saleId = response.getBody().saleId();
    assertNotNull(saleId);
    assertTrue(sales.existsById(saleId));
    assertEquals(3, jdbc.queryForObject(
            "SELECT quantity FROM sale_item WHERE sale_id = ? AND product_id = ?",
            Integer.class, saleId, productId));
    assertEquals(3, jdbc.queryForObject(
            "SELECT quantity FROM inventory_transaction WHERE source_id = ? AND type = 'SALE' AND item_id = ?",
            Integer.class, saleId, itemId));
    assertEquals(7, inventories.findByActiveTrueAndItemId(itemId).orElseThrow().getCurrentStock()
            + transactions.findTotalUnprocessedStocksByItemId(itemId));
}
```

## Ejecución

Ejecutar cada E2E nuevo individualmente y luego todos los E2E relevantes juntos para comprobar aislamiento y el contenedor compartido. Ver [comandos y diagnóstico](execution.md). Compilar sin ejecutar no demuestra que funciona el flujo.
