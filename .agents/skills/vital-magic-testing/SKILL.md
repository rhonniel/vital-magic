---
name: vital-magic-testing
description: Crear, corregir y mantener tests Java de vital-magic con JUnit 5, Mockito, WebMvcTest y DataJpaTest con MySQL/Testcontainers, siguiendo sus capas y convenciones. Usar al añadir cobertura o diagnosticar fallos de tests de este repositorio.
---

# Tests de vital-magic

## Antes de escribir

Lee el `AGENTS.md` aplicable, el código de producción afectado y el test vecino de la misma capa. Confirma `pom.xml` y, para persistencia, `src/test/java/com/lps/vitalMagic/config/MySqlDataJpaTest.java`. El checkout actual prevalece sobre esta guía: no des por aplicado un cambio mencionado en una conversación.

Base documentada: `rhonniel/vital-magic`, `main`, commit `7bbe2090bd8edf6a1672cbecf4384eaf55dacccb`. Consulta [fuentes y diferencias conocidas](references/repository-baseline.md) cuando necesites verificar versiones, rutas o ejemplos antiguos.

## Elegir la capa

| Comportamiento | Tipo | Referencia |
| --- | --- | --- |
| Invariantes y cálculos de entidades/value objects | JUnit 5 con objetos reales | [Unit tests y Mockito](references/unit-tests.md) |
| Orquestación de servicios, puertos y errores | JUnit 5 + MockitoExtension | [Unit tests y Mockito](references/unit-tests.md) |
| HTTP, JSON, binding, validación y traducción de excepciones | WebMvcTest + MockMvc + MockitoBean | [MVC](references/web-mvc-tests.md) |
| Consultas, filtros, proyecciones y SQL | DataJpaTest + MySqlDataJpaTest | [JPA/MySQL](references/jpa-tests.md) |
| Adaptador JPA y conversión a dominio/vistas | DataJpaTest + Import del adaptador real | [JPA/MySQL](references/jpa-tests.md) |
| Mapper que transforma datos ya cargados | JUnit 5 con entidades/proyecciones reales | [Unit tests y Mockito](references/unit-tests.md) |

Carga solo las referencias correspondientes. Un cambio puede necesitar dos capas si prueba contratos diferentes. No uses `@SpringBootTest` como plantilla general ni mezcles slices MVC/JPA en una clase.

## Convenciones compartidas

- Java 17, JUnit Jupiter y assertions de `org.junit.jupiter.api.Assertions`. En MVC usa los result matchers de MockMvc. No introduzcas otra librería de assertions como parte rutinaria de un test.
- Nombres `*Test`, métodos descriptivos en inglés. Coexisten `should...` y `when...`: sigue el estilo vecino sin renombrados masivos.
- Tests en `src/test/java/com/lps/vitalMagic/`, dentro del dominio y capa existentes. Conserva rutas reales, incluyendo `shake/aplication`, `product/aplication`, `purchase/.../persistance` y el contraste `sale` en tests / `sales` en producción.
- Crea fixtures pequeños con factories reales (`create`, `from`) o helpers privados como `saveItem`. Para probar validación de creación, usa la factory que valida; `from` puede ser reconstitución sin esas validaciones.
- Usa fechas fijas y `new BigDecimal("20.00")`. Compara importes con `compareTo` si la escala no forma parte del contrato.
- Cada caso inválido debe mantener válidos los demás campos. Comprueba el motivo del error cuando un simple 400 pueda ocultar otro fallo.
- En regresiones de filtros incluye datos que deben aparecer y datos que deben excluirse. No fijes el orden de resultados si la consulta no lo define.
- Conserva los límites del proyecto: dominio sin entidades JPA; controllers con interfaces use-case; persistencia en infrastructure. Las decisiones de arquitectura, negocio o infraestructura se rigen por el `AGENTS.md` vigente y por la autorización del usuario.

## Crear y mantener

1. Identifica la conducta esperada desde el código y la petición. Si el código contiene el defecto solicitado, escribe una regresión del comportamiento correcto; no consolides el defecto como expectativa.
2. Adapta el ejemplo real más cercano. Las plantillas de las referencias son puntos de partida, no archivos adicionales que haya que duplicar.
3. Asegura que los tests tienen `@Test` o `@ParameterizedTest` y que cada `MethodSource` coincide en cantidad y tipos de argumentos.
4. Ejecuta el conjunto mínimo indicado en [ejecución](references/execution.md). Si cambias la base MySQL, comprueba además varias clases JPA juntas.
5. Reporta cambios, comandos ejecutados, resultado y cualquier validación pendiente. Un fallo de Docker, red o JDK no demuestra un defecto de negocio.

Al evolucionar el proyecto, actualiza solo la referencia afectada y su evidencia. Revisa esta guía cuando cambien versiones, imports de testing, ciclo de vida del contenedor, perfiles o estructura de paquetes.

