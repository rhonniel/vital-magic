# DataJpaTest con MySQL y Testcontainers

## Configuración observada

Los cinco tests JPA heredan de `com.lps.vitalMagic.config.MySqlDataJpaTest`. Cada clase concreta declara `@DataJpaTest`; la base no incluye esa anotación.

La base contiene:

- `@Testcontainers`.
- `@AutoConfigureTestDatabase(replace = Replace.NONE)`.
- `@Container protected static final MySQLContainer<?> MYSQL = new MySQLContainer<>("mysql:8.4")`.
- `@DynamicPropertySource` que publica URL JDBC, usuario y contraseña del contenedor.

Reutiliza esta base en el checkout. No dupliques contenedores ni uses localhost/3306, credenciales de QA o H2 como sustituto para probar SQL de MySQL. No introduzcas `@ServiceConnection` como si fuera la convención actual: el repo usa propiedades dinámicas.

## Plantilla para una consulta Spring Data

Adaptada de `ItemInventoryJpaRepositoryTest`; los imports y constructores corresponden al snapshot. Integra el método en la clase existente.

```java
package com.lps.vitalMagic.inventory.infrastructure;

import com.lps.vitalMagic.config.MySqlDataJpaTest;
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
class ItemInventoryJpaRepositoryTest extends MySqlDataJpaTest {
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
class PurchaseJpaRepositoryTest extends MySqlDataJpaTest {
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

## Ciclo de vida: base actual frente a singleton

La base del snapshot **no es un singleton manual por JVM**. La extensión JUnit administra el campo `@Container static` por clase de test; la herencia no lo convierte en un arranque único de toda la suite. Si una clase funciona sola y varias fallan juntas, inspecciona el ciclo del contenedor y los contextos/datasources cacheados antes de culpar a las consultas.

Si el checkout ya adoptó un singleton manual, conserva ese patrón: arranque único en inicialización estática y sin gestión `@Container`/`@Testcontainers` sobre ese mismo recurso. No combines ambos propietarios del ciclo de vida ni lo detengas en `@AfterAll` de una clase hija.

El patrón singleton oficial permite compartir el arranque entre clases; normalmente Ryuk limpia el recurso al terminar. Reduce arranques, pero comparte servidor/estado y no equivale a aislamiento completo. Varias JVM de Maven siguen teniendo sus propios contenedores. No equivale a `withReuse(true)` entre ejecuciones.

Cambiar de la base actual al singleton es una propuesta de infraestructura, no parte implícita de escribir un test. Si la tarea lo incluye y está autorizado, modifica la base una sola vez y valida todas las clases JPA juntas. Respeta las decisiones y permisos del `AGENTS.md` vigente. No añadas `@DirtiesContext`, esperas o reintentos globales como arreglo automático.

Fuentes oficiales de este apartado: [JUnit 5 de Testcontainers](https://java.testcontainers.org/test_framework_integration/junit_5/), [singleton manual](https://java.testcontainers.org/test_framework_integration/manual_lifecycle_control/) y [transacciones de Spring Test](https://docs.spring.io/spring-framework/reference/testing/testcontext-framework/tx.html). Los imports y versiones del checkout prevalecen sobre ejemplos de versiones nuevas.

