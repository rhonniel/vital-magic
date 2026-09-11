# Tests MVC

## Convención existente

Los cuatro controllers cubiertos (`ItemControllerTest`, `ShakeControllerTest`, `SaleControllerTest`, `PurchaseControllerTest`) usan `@WebMvcTest(Controller.class)`, `@Autowired MockMvc` y `@MockitoBean` sobre interfaces use-case.

Import vigente del mock Spring:
`org.springframework.test.context.bean.override.mockito.MockitoBean`.

No sustituyas este patrón por `@MockBean`, ni por `@Mock` con `@InjectMocks`: estos últimos no registran colaboradores en el slice. No añadas Docker ni repositorios JPA al test HTTP.

## Plantilla adaptada de PurchaseControllerTest

Añade o adapta métodos en la clase existente cuando corresponda.

```java
package com.lps.vitalMagic.purchase.application;

import com.lps.vitalMagic.purchase.application.controller.PurchaseController;
import com.lps.vitalMagic.purchase.application.usecase.RegisterPurchaseUseCase;
import com.lps.vitalMagic.purchase.application.usecase.SearchPurchaseUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PurchaseController.class)
class PurchaseControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private RegisterPurchaseUseCase registerPurchaseUseCase;
    @MockitoBean
    private SearchPurchaseUseCase searchPurchaseUseCase;

    @Test
    void shouldRegisterPurchase() throws Exception {
        when(registerPurchaseUseCase.execute(any())).thenReturn(77L);

        mockMvc.perform(post("/purchase")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"items":[
                                  {"itemId":1,"quantity":5,"unitCost":35.50}
                                ]}
                                """))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location",
                        "http://localhost/purchase/77"))
                .andExpect(jsonPath("$.purchaseId").value(77));
    }

    @Test
    void shouldRejectEmptyItems() throws Exception {
        mockMvc.perform(post("/purchase")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"items":[]}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Invalid request"));

        verifyNoInteractions(registerPurchaseUseCase);
    }
}
```

Cuando se prueba la conversión de entrada, amplía el caso válido con `verify(useCase).execute(expectedCommand)` o un captor. El test original de compras muestra la comparación de records `CreatePurchaseCommand` y `SearchPurchasesQuery`.

## Cobertura según el cambio

- **Creación:** código HTTP, identificador con nombre real del response y `Location` si lo expone. `/purchase` devuelve `purchaseId`; `/shake` devuelve `id`. No uniformes contratos diferentes.
- **Binding y validación:** JSON sintácticamente válido para probar campos; casos separados para JSON mal formado, enum desconocido, nulidad y restricciones numéricas. Lee las anotaciones reales; `unitCost` de compras usa `@DecimalMin(... inclusive=false)`.
- **Búsqueda:** stubea un `PageResult` real, comprueba contenido y metadata relevante, y verifica el query creado. No uses solo un 200 con un mock que devuelve null como prueba de serialización.
- **Parámetros:** fechas ISO; `page >= 0`; `1 <= size <= 100` en los requests revisados. Usa `@ParameterizedTest` + `@MethodSource`, con una única entrada inválida por fila.
- **Errores del servicio:** stubea la excepción para probar su traducción HTTP. El rango de fechas invertido se valida en `SearchPurchaseService`; el test MVC prueba la traducción de su `IllegalArgumentException`, no esa lógica de negocio.

`GlobalExceptionHandler` devuelve `ProblemDetail`. En el snapshot: ResourceNotFoundException → 404 / "Resource not found"; IllegalArgumentException → 400 / "Invalid operation input"; cuerpo ilegible → 400 / "Malformed request body". Revisa el handler antes de fijar `title`, `detail` o `errors`. Normalmente el advice se descubre en el slice; impórtalo explícitamente solo si la configuración concreta lo excluye.

## No copiar defectos de los ejemplos

En `ShakeControllerTest`, el método de búsqueda exitosa carece de `@Test`. `RARE` sí existe en `ShakeCategory`; `MAGIC` no. En `SaleControllerTest`, el test parametrizado de búsqueda usa nombres de parámetros de shake y filas con aridad inconsistente; un caso de productId ausente contiene JSON mal formado. Reescribe esos fixtures desde el controller de ventas si trabajas en ellos.

Estas observaciones describen el snapshot; verifica el checkout antes de asumir que siguen pendientes.

