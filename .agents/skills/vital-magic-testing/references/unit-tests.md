# Unit tests y Mockito

## Patrones del repo

Entidades como `ItemTest`, `ProductTest` y `ShakeTest` usan objetos reales y JUnit 5 sin Spring. Los servicios, incluidos servicios de dominio como `ProductAvailabilityServiceTest`, usan `@ExtendWith(MockitoExtension.class)`, `@Mock` para colaboradores y `@InjectMocks` para el sujeto.

Aísla puertos de repositorio/proveedores del dominio. No simules la entidad cuyo cálculo o invariante estás probando. Usa `@MockitoBean` solo si necesitas registrar el mock en un contexto Spring; un `@Mock` ordinario no hace eso.

## Plantilla de dominio

Ejemplo adaptado de `inventory/domain/ItemTest.java`. Se puede incorporar a esa clase; no crear un segundo `ItemTest` en el mismo package.

```java
package com.lps.vitalMagic.inventory.domain;

import com.lps.vitalMagic.inventory.domain.exception.InvalidItemException;
import com.lps.vitalMagic.inventory.domain.model.entity.Item;
import com.lps.vitalMagic.inventory.domain.model.input.AttributeValue;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ItemTest {
    @ParameterizedTest
    @NullAndEmptySource
    void shouldRejectInvalidName(String name) {
        var attributes = List.of(new AttributeValue(1L, 3));
        assertThrows(InvalidItemException.class,
                () -> Item.create(name, "Valid description", attributes));
    }
}
```

`@NullAndEmptySource` no incluye espacios en blanco. Añade un caso separado si esa frontera corresponde a la regla afectada. No asumas que todos los nulos lanzan una excepción de dominio: lee la factory.

## Plantilla de servicio

Ejemplo basado en `FindItemService`; corrige el escenario de ausencia del test antiguo para ejercitar realmente `Optional.empty()`.

```java
package com.lps.vitalMagic.inventory.application;

import com.lps.vitalMagic.common.exception.ResourceNotFoundException;
import com.lps.vitalMagic.inventory.application.service.FindItemService;
import com.lps.vitalMagic.inventory.domain.model.entity.Item;
import com.lps.vitalMagic.inventory.domain.model.entity.ItemAttribute;
import com.lps.vitalMagic.inventory.domain.repository.ItemRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindItemServiceTest {
    @Mock
    private ItemRepository itemRepository;
    @InjectMocks
    private FindItemService service;

    @Test
    void shouldReturnItemInfo() {
        Item item = Item.from(777L, "Colirio", "Description",
                List.of(ItemAttribute.from(1L, 3)), true);
        when(itemRepository.findById(777L)).thenReturn(Optional.of(item));

        var result = service.getItemInfo(777L);

        assertEquals(777L, result.itemId());
        assertEquals("Colirio", result.name());
    }

    @Test
    void shouldRejectMissingItem() {
        when(itemRepository.findById(777L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.getItemInfo(777L));
    }
}
```

## Uso de Mockito

- Configura solo los stubs consumidos por cada caso. No generalices `lenient()` ni añadas `reset()` para ocultar fixtures mal definidos.
- Usa argumentos exactos cuando sean parte de la conducta. Los ejemplos usan `any()`, pero eso por sí solo no prueba que el servicio construyó el comando correcto.
- Para objetos construidos dentro del servicio, captura el argumento de `save` con `ArgumentCaptor` y comprueba los campos esenciales. `CreateItemServiceTest` captura `Item` e `ItemInventory`.
- Usa `verify(..., never())` o `verifyNoInteractions(...)` cuando impedir una llamada sea parte de la regla. No impongas `verifyNoMoreInteractions` a todos los tests.
- Si usas matchers en una invocación de varios argumentos, expresa los demás con matchers compatibles, por ejemplo `eq(id)`.
- Un mock que lanza una excepción prueba propagación; no prueba que el servicio detecte ausencia. Devuelve `Optional.empty()`, listas vacías o el resultado real del puerto para ejercitar esa rama.
- Los unit tests con Mockito no comprueban rollback ni proxies transaccionales de Spring.

## Mappers

`ShakeMapper.toPageResult` es estático y transforma una página y proyecciones: se puede probar directamente, sin mock estático ni Spring. Usa dos shakes con IDs distintos, proyecciones de ambos y valores distinguibles; comprueba que cada vista contiene únicamente sus ingredientes y atributos, más la metadata de paginación relevante.

Este escenario es una **regresión propuesta**: el snapshot revisado no filtra las proyecciones por `shakeId` y no contiene `ShakeMapperTest`. Si el objetivo incluye corregir ese defecto, el test debe fallar antes de la corrección. Para validar además consulta y adaptador, consulta [JPA](jpa-tests.md).

