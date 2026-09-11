# Evidencia y mantenimiento

## Snapshot

Repositorio: [rhonniel/vital-magic](https://github.com/rhonniel/vital-magic).
Rama consultada: `main`.
Commit fijado: [7bbe2090bd8edf6a1672cbecf4384eaf55dacccb](https://github.com/rhonniel/vital-magic/tree/7bbe2090bd8edf6a1672cbecf4384eaf55dacccb).

Se inspeccionó el árbol completo, se recuperaron los archivos Java bajo `src/test` y se contrastaron los patrones con POM, base MySQL, controllers, servicios, mapper y migraciones relevantes. Esto es una revisión estática: no se compiló ni ejecutó la suite al generar estos Markdown.

## Hechos de configuración

- Java 17; Spring Boot 3.5.9; Flyway 9.22.3.
- Maven con `mvnw` y `mvnw.cmd`.
- `spring-boot-starter-test`, `org.testcontainers:junit-jupiter` y `org.testcontainers:mysql`, todos con scope test. Driver `mysql-connector-j` con scope runtime.
- La base usa imagen `mysql:8.4`, propiedades dinámicas y gestión JUnit.
- Cinco clases JPA; cuatro clases MVC; unit tests de entidades y servicios en inventory, product, purchase, sale y shake.
- Sin `src/test/resources`, workflows de GitHub Actions ni configuración personalizada de Surefire/Failsafe en el snapshot.

No interpretar la versión "latest" de una documentación externa como la versión resuelta por este POM.

## Diferencias frente a la conversación previa

| Tema | Lo que contiene el snapshot | Tratamiento en la skill |
| --- | --- | --- |
| MySqlDataJpaTest | @Testcontainers + @Container static; sin start manual | Reutilizar base vigente; singleton como opción condicionada |
| ShakeMapper | Mapea todas las proyecciones a cada shake sin filtrar shakeId | Recomendar regresión multi-shake cuando se trabaje ese defecto |
| Tests nuevos del mapper/adaptador de shake | No hay ShakeMapperTest ni JpaShakeRepositoryTest | No presentarlos como plantillas ya existentes |

No se han aplicado cambios al código Java mediante este paquete.

## Ejemplos que requieren cuidado

- `FindItemServiceTest` simula IllegalStateException desde el puerto, pero la ausencia real en `FindItemService` llega como Optional.empty y produce ResourceNotFoundException.
- `ShakeControllerTest` contiene una búsqueda exitosa sin @Test. RARE es un enum válido; MAGIC no lo es.
- `SaleControllerTest` mezcla parámetros de shake con la búsqueda de ventas, tiene filas de MethodSource incompatibles con su firma y un cuerpo inválido sintácticamente en un caso de validación.
- `ShakeEntityJpaRepositoryTest` asigna Strength al ID 2 en su fixture; V4 usa Defense para ese ID.
- `VitalMagicApplicationTests` no tiene métodos anotados de test.

Son observaciones del commit enlazado, no normas para replicar ni resultados de ejecución. Antes de corregirlas, confirma que siguen presentes y que corresponden al alcance pedido.

## Fuentes del repositorio

Los enlaces fijan el commit para que la evidencia no cambie cuando avance main. Para trabajar, lee las rutas equivalentes del checkout actual.

| Uso | Archivo |
| --- | --- |
| Reglas y arquitectura | [AGENTS.md](https://github.com/rhonniel/vital-magic/blob/7bbe2090bd8edf6a1672cbecf4384eaf55dacccb/AGENTS.md) |
| Versiones y dependencias | [pom.xml](https://github.com/rhonniel/vital-magic/blob/7bbe2090bd8edf6a1672cbecf4384eaf55dacccb/pom.xml) |
| Base MySQL | [src/test/java/com/lps/vitalMagic/config/MySqlDataJpaTest.java](https://github.com/rhonniel/vital-magic/blob/7bbe2090bd8edf6a1672cbecf4384eaf55dacccb/src/test/java/com/lps/vitalMagic/config/MySqlDataJpaTest.java) |
| Dominio y parámetros | [src/test/java/com/lps/vitalMagic/inventory/domain/ItemTest.java](https://github.com/rhonniel/vital-magic/blob/7bbe2090bd8edf6a1672cbecf4384eaf55dacccb/src/test/java/com/lps/vitalMagic/inventory/domain/ItemTest.java) |
| Captors | [src/test/java/com/lps/vitalMagic/inventory/application/CreateItemServiceTest.java](https://github.com/rhonniel/vital-magic/blob/7bbe2090bd8edf6a1672cbecf4384eaf55dacccb/src/test/java/com/lps/vitalMagic/inventory/application/CreateItemServiceTest.java) |
| Mockito en dominio | [src/test/java/com/lps/vitalMagic/product/domain/ProductAvailabilityServiceTest.java](https://github.com/rhonniel/vital-magic/blob/7bbe2090bd8edf6a1672cbecf4384eaf55dacccb/src/test/java/com/lps/vitalMagic/product/domain/ProductAvailabilityServiceTest.java) |
| MVC y contratos | [src/test/java/com/lps/vitalMagic/purchase/application/PurchaseControllerTest.java](https://github.com/rhonniel/vital-magic/blob/7bbe2090bd8edf6a1672cbecf4384eaf55dacccb/src/test/java/com/lps/vitalMagic/purchase/application/PurchaseControllerTest.java) |
| Controller de compras | [src/main/java/com/lps/vitalMagic/purchase/application/controller/PurchaseController.java](https://github.com/rhonniel/vital-magic/blob/7bbe2090bd8edf6a1672cbecf4384eaf55dacccb/src/main/java/com/lps/vitalMagic/purchase/application/controller/PurchaseController.java) |
| Traducción HTTP | [src/main/java/com/lps/vitalMagic/common/exception/GlobalExceptionHandler.java](https://github.com/rhonniel/vital-magic/blob/7bbe2090bd8edf6a1672cbecf4384eaf55dacccb/src/main/java/com/lps/vitalMagic/common/exception/GlobalExceptionHandler.java) |
| Filtros de inventario | [src/test/java/com/lps/vitalMagic/inventory/infrastructure/ItemInventoryJpaRepositoryTest.java](https://github.com/rhonniel/vital-magic/blob/7bbe2090bd8edf6a1672cbecf4384eaf55dacccb/src/test/java/com/lps/vitalMagic/inventory/infrastructure/ItemInventoryJpaRepositoryTest.java) |
| Adaptador de compras | [src/test/java/com/lps/vitalMagic/purchase/infrastructure/PurchaseJpaRepositoryTest.java](https://github.com/rhonniel/vital-magic/blob/7bbe2090bd8edf6a1672cbecf4384eaf55dacccb/src/test/java/com/lps/vitalMagic/purchase/infrastructure/PurchaseJpaRepositoryTest.java) |
| Adaptador de ventas | [src/test/java/com/lps/vitalMagic/sale/infrastructure/SaleJpaRepositoryTest.java](https://github.com/rhonniel/vital-magic/blob/7bbe2090bd8edf6a1672cbecf4384eaf55dacccb/src/test/java/com/lps/vitalMagic/sale/infrastructure/SaleJpaRepositoryTest.java) |
| Proyecciones de shake | [src/test/java/com/lps/vitalMagic/shake/infrastructure/ShakeEntityJpaRepositoryTest.java](https://github.com/rhonniel/vital-magic/blob/7bbe2090bd8edf6a1672cbecf4384eaf55dacccb/src/test/java/com/lps/vitalMagic/shake/infrastructure/ShakeEntityJpaRepositoryTest.java) |
| Mapper de shake | [src/main/java/com/lps/vitalMagic/shake/infrastructure/persistence/mapper/ShakeMapper.java](https://github.com/rhonniel/vital-magic/blob/7bbe2090bd8edf6a1672cbecf4384eaf55dacccb/src/main/java/com/lps/vitalMagic/shake/infrastructure/persistence/mapper/ShakeMapper.java) |
| Adaptador de shake | [src/main/java/com/lps/vitalMagic/shake/infrastructure/persistence/repository/impl/JpaShakeRepository.java](https://github.com/rhonniel/vital-magic/blob/7bbe2090bd8edf6a1672cbecf4384eaf55dacccb/src/main/java/com/lps/vitalMagic/shake/infrastructure/persistence/repository/impl/JpaShakeRepository.java) |
| Ausencia en servicio | [src/main/java/com/lps/vitalMagic/inventory/application/service/FindItemService.java](https://github.com/rhonniel/vital-magic/blob/7bbe2090bd8edf6a1672cbecf4384eaf55dacccb/src/main/java/com/lps/vitalMagic/inventory/application/service/FindItemService.java) |
| Fixture antiguo de ausencia | [src/test/java/com/lps/vitalMagic/inventory/application/FindItemServiceTest.java](https://github.com/rhonniel/vital-magic/blob/7bbe2090bd8edf6a1672cbecf4384eaf55dacccb/src/test/java/com/lps/vitalMagic/inventory/application/FindItemServiceTest.java) |
| MVC de shake | [src/test/java/com/lps/vitalMagic/shake/aplication/ShakeControllerTest.java](https://github.com/rhonniel/vital-magic/blob/7bbe2090bd8edf6a1672cbecf4384eaf55dacccb/src/test/java/com/lps/vitalMagic/shake/aplication/ShakeControllerTest.java) |
| Enum de categorías | [src/main/java/com/lps/vitalMagic/shake/domain/model/enums/ShakeCategory.java](https://github.com/rhonniel/vital-magic/blob/7bbe2090bd8edf6a1672cbecf4384eaf55dacccb/src/main/java/com/lps/vitalMagic/shake/domain/model/enums/ShakeCategory.java) |
| MVC de ventas | [src/test/java/com/lps/vitalMagic/sale/application/SaleControllerTest.java](https://github.com/rhonniel/vital-magic/blob/7bbe2090bd8edf6a1672cbecf4384eaf55dacccb/src/test/java/com/lps/vitalMagic/sale/application/SaleControllerTest.java) |
| Catálogo inicial | [src/main/resources/db/migration/V4__init_data_attributes.sql](https://github.com/rhonniel/vital-magic/blob/7bbe2090bd8edf6a1672cbecf4384eaf55dacccb/src/main/resources/db/migration/V4__init_data_attributes.sql) |
| Perfil QA | [src/main/resources/application-qa.yml](https://github.com/rhonniel/vital-magic/blob/7bbe2090bd8edf6a1672cbecf4384eaf55dacccb/src/main/resources/application-qa.yml) |
| Clase sin tests | [src/test/java/com/lps/vitalMagic/VitalMagicApplicationTests.java](https://github.com/rhonniel/vital-magic/blob/7bbe2090bd8edf6a1672cbecf4384eaf55dacccb/src/test/java/com/lps/vitalMagic/VitalMagicApplicationTests.java) |

## Documentación oficial complementaria

- [Skills de Codex](https://learn.chatgpt.com/docs/build-skills): estructura SKILL.md y descubrimiento en .agents/skills.
- [Spring Boot 3.5: testing](https://docs.spring.io/spring-boot/3.5/reference/testing/spring-boot-applications.html): slices MVC/JPA e importación selectiva. La página sigue la rama 3.5 y puede mostrar un patch posterior a 3.5.9.
- [Spring Test: transacciones](https://docs.spring.io/spring-framework/reference/testing/testcontext-framework/tx.html): rollback de tests y sus límites.
- [Testcontainers: JUnit 5](https://java.testcontainers.org/test_framework_integration/junit_5/): ciclo por clase y limitaciones de paralelismo.
- [Testcontainers: singleton manual](https://java.testcontainers.org/test_framework_integration/manual_lifecycle_control/): arranque manual compartido.
- [Mockito Javadoc](https://javadoc.io/doc/org.mockito/mockito-core/latest/org.mockito/org/mockito/Mockito.html): consulta la versión resuelta por Maven al necesitar una API concreta.

## Cuándo actualizar

Actualiza esta referencia y el archivo afectado si cambian POM, base de tests, perfiles, contratos HTTP o rutas. Mantén separados hechos observados, propuestas y defectos conocidos. No copies toda la suite a la skill: conserva ejemplos pequeños y enlaces al código real.

