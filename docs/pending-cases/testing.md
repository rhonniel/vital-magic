# Testing aplazado

## TEST-001 - Cobertura MVC de busqueda exitosa de ventas

- Estado: aplazado explicitamente por el propietario.
- Referencia: `src/test/java/com/lps/vitalMagic/sale/application/SaleControllerTest.java`, metodo `searchSaleWithQueryIsSuccessfully`.
- Situacion verificada: envia `from`, `to`, `productId`, `page` y `size`; solo comprueba HTTP 200. No configura `SearchSaleUseCase.execute`, por lo que el mock devuelve `null` y el test puede pasar sin cuerpo de respuesta.
- Pendiente autorizado para conservar: cuando se retome, devolver un `PageResult<SaleView>` representativo, verificar el query enviado al use case y el contenido/paginacion de la respuesta.
- Alcance de esta ejecucion: este metodo queda sin cambios. Las correcciones aprobadas de parametros y JSON pertenecen a otros metodos de la clase.
