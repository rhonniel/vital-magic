# DataJpaTest con MySQL y Testcontainers

## Checkout actual

La base vigente es `MySqlIntegrationTest`: arranca MySQL manualmente en un bloque estático y publica propiedades mediante `@DynamicPropertySource`, sin `@Container` ni `@Testcontainers`. Reutilizarla tanto en JPA como en E2E; no reemplazar su ciclo de vida. El resto de observaciones históricas de esta referencia deben contrastarse con el checkout.

Cada clase JPA concreta declara `@DataJpaTest`; la base no incluye esa anotación. La base conserva `@AutoConfigureTestDatabase(replace = Replace.NONE)`.

No dupliques contenedores ni uses localhost/3306, credenciales de QA o H2 como sustituto para probar SQL de MySQL. No introduzcas `@ServiceConnection` como convención: el repo usa propiedades dinámicas.

## Plantilla para una consulta Spring Data

Adaptada de `ItemInventoryJpaRepositoryTest`; los imports y constructores corresponden al snapshot. Integra el método en la clase existente.

```java
package com.lps.vitalMagic.inventory.infrastructure;

import com.lps.vitalMagic.config.MySqlIntegrationTest;
import com.lps.vitalMagic.inventory.infrastructure.persistence.entity.ItemEntity;
import com.lps.vitalMagic.inventory.infrastructure.persistence.entity.ItemInventoryEntity;
import com.lps.vitalMagic.inventory.infrastructure.persistence.repository.ItemJpaRepository;
import com.lps.vitalMagic.inventory.infrastructure.persistence.repository.ItemInventoryJpaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@DataJpaTest
class ItemInventoryJpaRepositoryTest extends MySqlIntegrationTest {
    @Autowired
    private ItemJpaRepository itemRepository;
    @Autowired
    private ItemInventoryJpaRepository inventoryRepository;

    @Test
    void shouldExcludeInactiveInventory() {
        ItemEntity item = itemRepository.saveAndFlush(
                new ItemEntity("Dragon Tail", "Description", true));
        inventoryRepository.saveAndFlush(new ItemInventoryEntity(
                null, item.getId(), new BigDecimal("25.00"), 5, 10, false));

        var result = inventoryRepository.findByActiveTrueAndItemId(item.getId());

        assertFalse(result.isPresent());
        assertEquals(1, inventoryRepository.findByItemIdIn(
                java.util.List.of(item.getId())).size());
    }
}
```

La comprobación adicional confirma que el inventario existe y que la exclusión se debe a su estado. Para probar filtros de colecciones crea filas incluidas y excluidas; conserva los IDs devueltos al guardar.

## Adaptador de repositorio

`PurchaseJpaRepositoryTest` y `SaleJpaRepositoryTest` añaden `@Import` del adaptador real. Para compras, la cabecera es:

```java
import com.lps.vitalMagic.purchase.infrastructure.persistance.repository.impl.JpaPurchaseRepository;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(JpaPurchaseRepository.class)
class PurchaseJpaRepositoryTest extends MySqlIntegrationTest {
    @Autowired
    private JpaPurchaseRepository repository;
}
```

Este fragmento muestra solo la configuración; añade escenarios de conducta, no un test vacío. Usa el repositorio Spring Data para fixtures relacionados y el adaptador como sujeto. No mockees el adaptador, el mapper ni la consulta que quieres integrar.

Para un nuevo test de `JpaShakeRepository`, el import real es
`com.lps.vitalMagic.shake.infrastructure.persistence.repository.impl.JpaShakeRepository`.
Ese test **no existe en el snapshot**: aplícale el patrón de compras/ventas cuando se requiera cobertura del adaptador.

## Fixtures, Flyway y transacciones

- Hay migraciones en `src/main/resources/db/migration`, incluidas V1–V19. Mantén Flyway activo cuando el objetivo sea comprobar persistencia contra el esquema real.
- Solo se encontró `application-qa.yml`; no hay `src/test/resources` ni `@ActiveProfiles` en la base. No atribuyas a todos los tests el `ddl-auto: validate` del perfil QA ni lo actives por costumbre.
- Los tests existentes usan `saveAndFlush` y `saveAllAndFlush`, factories de dominio y helpers privados. Persiste primero las filas referenciadas.
- Para verificar round-trip desde la BD y no solo el contexto de persistencia, usa `EntityManager.flush()` y `clear()` antes de volver a consultar. Para constraints, fuerza flush dentro del `assertThrows`.
- `@DataJpaTest` revierte normalmente la transacción de cada método. No requiere borrado masivo en `@AfterEach`. Ese rollback no cubre commits explícitos, transacciones independientes, otros hilos ni toda operación DDL. No supongas que reinicia secuencias/autoincrementos.
- V4 precarga atributos: Strength=1, Defense=2, Magic=3. El test de proyecciones de shake sobrescribe el ID 2 como Strength dentro de su fixture. No trates ese ID como catálogo global; usa datos de catálogo verificados o fixtures deliberados dentro de la transacción.
- Usa fechas fijas para filtros y paginación. Comprueba orden solo si lo define la implementación. Compras/ventas tienen ejemplos de páginas ordenadas y totales.
- Para agregaciones de shake prueba multiplicación cantidad × atributo, IDs incluidos/excluidos y pertenencia a cada shake. Una consulta correcta no garantiza un mapper correcto.

## Ciclo de vida compartido

El checkout usa un singleton manual por JVM. Mantener el arranque estático único sin gestión JUnit `@Container`/`@Testcontainers` sobre el mismo recurso; no detenerlo en `@AfterAll` de una clase hija. Si varias clases fallan juntas, revisar primero aislamiento de datos y contextos/datasources cacheados.

Compartir servidor no equivale a aislamiento de datos ni a `withReuse(true)` entre ejecuciones. Cada JVM de Maven tiene su propio contenedor. No añadir `@DirtiesContext`, esperas o reintentos globales como arreglo automático.

Cambiar la infraestructura se rige por `AGENTS.md`. Si se modifica la base, validar varias clases JPA juntas y los E2E relevantes; los E2E requieren [limpieza explícita](e2e-tests.md).
