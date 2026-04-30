# Casos de prueba - Reto Backend

Este documento resume las pruebas automáticas y manuales recomendadas para validar los microservicios bancarios.

## Alcance

- Clientes y personas.
- Cuentas.
- Movimientos.
- Reportes de estado de cuenta.
- Integración básica mediante RabbitMQ.

## Pruebas automáticas

### Maven

Comando:

```bash
mvn test
```

Casos cubiertos:

| ID | Tipo | Caso | Resultado esperado |
| --- | --- | --- | --- |
| TC-AUTO-01 | Unitario | Crear entidad `Cliente` con datos de persona | El dominio conserva `clienteId`, `nombre` y `estado`. |
| TC-AUTO-02 | Controller | `GET /clientes` | Retorna HTTP 200 con una lista. |
| TC-AUTO-03 | Controller | `POST /clientes` válido | Retorna HTTP 201. |
| TC-AUTO-04 | Unitario | Registrar depósito en cuenta | Actualiza el saldo disponible. |
| TC-AUTO-05 | Unitario | Registrar retiro sin saldo | Lanza `Saldo no disponible`. |
| TC-AUTO-06 | Integración | Crear cuenta, crear/consultar/actualizar movimiento, validar retiro inválido y consultar reporte | Respuestas HTTP esperadas y saldo correcto. |

### Karate end-to-end

Comando:

```powershell
mvn -pl karate-tests test
```

Reporte:

```text
karate-tests/target/karate-reports/karate-summary.html
```

Casos cubiertos:

| ID | Feature | Caso | Resultado esperado |
| --- | --- | --- | --- |
| TC-KARATE-01 | `clientes.feature` | Crear y listar cliente con ID dinámico | HTTP 201 y luego HTTP 200 con el cliente creado. |
| TC-KARATE-02 | `clientes.feature` | Crear cliente con nombre vacío | HTTP 400 con mensaje de validación. |
| TC-KARATE-03 | `clientes.feature` | Crear cliente duplicado | HTTP 409 con `El clienteId ya existe`. |
| TC-KARATE-04 | `cuentas-movimientos.feature` | Crear cuenta, registrar depósito, consultar/actualizar movimiento, consultar reporte y rechazar retiro sin saldo | HTTP 201, 200 y 400 según corresponda. |
| TC-KARATE-05 | `cuentas-movimientos.feature` | Crear cuenta duplicada | HTTP 409 con `El número de cuenta ya existe`. |
| TC-KARATE-06 | `cuentas-movimientos.feature` | Registrar movimiento sobre cuenta inexistente | HTTP 404 con `Cuenta no encontrada`. |
| TC-KARATE-07 | `cuentas-movimientos.feature` | Consultar reporte con rango de fechas inválido | HTTP 400 con mensaje de rango. |

## Pruebas manuales recomendadas

| ID | Servicio | Pasos | Resultado esperado |
| --- | --- | --- | --- |
| TC-MAN-01 | Customer | Crear cliente desde Postman y consultar `GET /clientes` | El cliente aparece en la lista. |
| TC-MAN-02 | Customer | Actualizar cliente con `PUT /clientes/{id}` | Los cambios quedan persistidos. |
| TC-MAN-03 | Customer | Eliminar cliente con `DELETE /clientes/{id}` | Retorna HTTP 204. |
| TC-MAN-04 | Account | Crear cuenta con saldo inicial 100 | El saldo disponible inicia en 100. |
| TC-MAN-05 | Account | Registrar depósito de 50 | El saldo disponible queda en 150. |
| TC-MAN-06 | Account | Consultar `GET /movimientos/{id}` con el ID creado | Retorna el movimiento y su cuenta. |
| TC-MAN-07 | Account | Actualizar movimiento a 80 con `PUT /movimientos/{id}` | El saldo disponible queda en 180. |
| TC-MAN-08 | Account | Registrar retiro de -300 | Retorna HTTP 400 y el mensaje `Saldo no disponible`. |
| TC-MAN-09 | Account | Consultar `/reportes?fecha=2020-01-01,2030-12-31&cliente=<clienteId>` | Retorna los movimientos del cliente dentro del rango. |
| TC-MAN-10 | Docker | Levantar `docker compose up -d --build` | Servicios, PostgreSQL y RabbitMQ quedan disponibles. |
| TC-MAN-11 | RabbitMQ | Crear o actualizar un cliente y revisar `clientes_replica` en la base de cuentas | La réplica refleja el cambio publicado por `customer-service`. |

## Datos de prueba

Las pruebas Karate generan IDs dinámicos con `UUID`, por lo que pueden repetirse sin colisionar por `clienteId` o `numeroCuenta`.

Para pruebas manuales, usa IDs propios o reinicia los volúmenes con:

```bash
docker compose down -v
docker compose up -d --build
```

Este comando borra los datos de PostgreSQL, así que conviene usarlo solo cuando se quiera iniciar una demo limpia.
