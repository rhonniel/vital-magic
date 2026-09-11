# Ejecución y diagnóstico

## Entorno real

El proyecto usa Java 17 y Maven Wrapper. El `pom.xml` no declara una separación personalizada Surefire/Failsafe, perfiles de tests ni cobertura JaCoCo. En el árbol revisado no hay workflows de GitHub Actions. No inventes comandos como `-Ptest`, `-Pintegration` o `verify` con exclusiones implícitas.

JUnit, Mockito y Testcontainers no tienen versión explícita en el POM: no copies una versión de documentación reciente. Si necesitas resolverlas, inspecciona el POM efectivo o ejecuta `dependency:tree` en el checkout. El wrapper puede requerir red para descargar Maven/dependencias.

## Comandos desde la raíz de vital-magic

En PowerShell:

```powershell
.\mvnw.cmd -version
.\mvnw.cmd "-Dtest=FindItemServiceTest" test
.\mvnw.cmd "-Dtest=PurchaseControllerTest" test
.\mvnw.cmd "-Dtest=ItemInventoryJpaRepositoryTest" test
```

Para un método concreto:
` .\mvnw.cmd "-Dtest=FindItemServiceTest#shouldRejectMissingItem" test `
(el nombre corresponde a la plantilla, no al test antiguo).

Cuando se modifique la base MySQL o se investigue un fallo entre clases:

```powershell
.\mvnw.cmd "-Dtest=InventoryTransactionJpaRepositoryTest,ItemInventoryJpaRepositoryTest,PurchaseJpaRepositoryTest,SaleJpaRepositoryTest,ShakeEntityJpaRepositoryTest" test
```

Añade a la selección las nuevas clases JPA que existan en el checkout. La lista anterior representa el snapshot documentado.

Suite completa, cuando el alcance lo justifique:

```powershell
.\mvnw.cmd test
```

En Linux/macOS reemplaza `.\mvnw.cmd` por `./mvnw`; conserva entre comillas los selectores con comas, comodines o `#`.

Empieza por la clase afectada. Amplía a las clases relacionadas si cambió una dependencia compartida, y a la suite si el alcance lo requiere. Comprueba el número de tests ejecutados y los informes en `target/surefire-reports`; compilar o obtener cero tests no valida la conducta. `VitalMagicApplicationTests` tiene `@SpringBootTest` pero ningún método de test en el snapshot: no es evidencia de un smoke test ejecutado.

## Diagnóstico útil

| Síntoma | Primera comprobación |
| --- | --- |
| No arranca MySQL | Docker compatible accesible, logs del contenedor y descarga de mysql:8.4 |
| Pasa una clase JPA, falla el conjunto | Gestión @Container frente a datasource/contexto cacheado; leer la base actual |
| Fallo de Flyway | Primera migración fallida, esquema y compatibilidad del MySQL usado |
| Bean ausente en MVC | Falta @MockitoBean de una interfaz use-case del constructor |
| Adaptador ausente en JPA | Falta @Import del adaptador; no ampliar a SpringBootTest por reflejo |
| 400 en validación | Verificar JSON, binding y campo que falló; no basta el status |
| Test parametrizado falla antes del método | Aridad/tipos de MethodSource |
| Unnecessary stubbing | Fixture/stub que ese escenario no consume |
| Unit test de ausencia no ejercita la rama | Optional.empty frente a un mock que lanza directamente |

No actives el perfil QA para conectar a una base externa al resolver Docker. No habilites paralelismo como optimización rutinaria de esta base: la extensión JUnit de Testcontainers documenta limitaciones con ejecución paralela.

Al entregar, distingue: tests pasados; fallos reproducidos; bloqueos del entorno; comprobaciones no ejecutadas. No ocultes fallos mediante skips ni afirmes haber ejecutado ejemplos Markdown por haber validado la skill.

