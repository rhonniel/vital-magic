# Decisiones humanas pendientes

## DEC-001 - Filtros parciales de fechas

- Estado: pendiente de decision del propietario.
- Contexto: las busquedas de Purchase y Sale pueden recibir solo `from` o solo `to`.
- Opciones: aceptar limite inferior independiente; aceptar limite superior independiente; aceptar ambos limites independientes; exigir ambos o ninguno.
- Tradeoff: flexibilidad de consulta frente a un contrato de validacion mas restrictivo.
- Decision requerida: definir las combinaciones validas y su semantica antes de fijarlas en pruebas. No se ha elegido una opcion.

## DEC-002 - Estados HTTP para excepciones de dominio

- Estado: pendiente de decision del propietario.
- Contexto: falta un contrato HTTP acordado para `SaleDomainException`, `InventoryTransactionException` e `InvalidShakeException`.
- Opciones: 400, 409 o 422, segun el significado de cada error; no asumir una correspondencia global.
- Tradeoff: distinguir entrada invalida, conflicto con el estado y rechazo semantico afecta el comportamiento de clientes REST.
- Decision requerida: acordar respuesta y estado por significado de error antes de implementar handlers o pruebas definitivas.

## DEC-003 - Semantica de SaleItemView.itemId

- Estado: pendiente de decision del propietario.
- Contexto: no esta definido si `SaleItemView.itemId` representa el ID del detalle de venta, el ID del producto u otro identificador de dominio.
- Opciones: definir explicitamente una de esas identidades y documentar su contrato.
- Tradeoff: identificar el detalle facilita referirse a una linea de venta; identificar el producto facilita relacionarlo con el catalogo. No son identidades intercambiables.
- Decision requerida: determinar el identificador expuesto antes de escribir pruebas definitivas de mapping/API. La implementacion actual no se toma como decision.
