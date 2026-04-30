# Test cases - Reto Backend

## Alcance

Estos casos cubren el flujo principal y los negativos minimos para validar los microservicios bancarios:

- Clientes / Personas
- Cuentas
- Movimientos
- Reportes

## Pruebas automaticas

### Unitarias e integracion con Maven

Comando:

```bash
mvn test
```

Casos cubiertos:

| ID | Tipo | Caso | Resultado esperado |
| --- | --- | --- | --- |
| TC-AUTO-01 | Unitario | Crear entidad `Cliente` con datos de persona | El dominio conserva `clienteId`, `nombre` y `estado` |
| TC-AUTO-02 | Controller | `GET /clientes` | Retorna lista HTTP 200 |
| TC-AUTO-03 | Controller | `POST /clientes` valido | Retorna HTTP 201 |
| TC-AUTO-04 | Unitario | Deposito en cuenta | Actualiza saldo disponible |
| TC-AUTO-05 | Unitario | Retiro sin saldo | Lanza `Saldo no disponible` |
| TC-AUTO-06 | Integracion | Crear cuenta, crear/consultar/actualizar movimiento, retiro invalido y reporte | Respuestas HTTP esperadas y reporte con saldo correcto |

### Karate end-to-end

Comando PowerShell:

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
| TC-KARATE-01 | `clientes.feature` | Crear y listar cliente con ID dinamico | HTTP 201 y luego HTTP 200 con el cliente creado |
| TC-KARATE-02 | `clientes.feature` | Cliente con nombre vacio | HTTP 400 con mensaje de validacion |
| TC-KARATE-03 | `clientes.feature` | Cliente duplicado | HTTP 409 con `El clienteId ya existe` |
| TC-KARATE-04 | `cuentas-movimientos.feature` | Crear cuenta, deposito, consulta/actualizacion de movimiento, reporte y retiro sin saldo | HTTP 201/200/400 segun cada paso |
| TC-KARATE-05 | `cuentas-movimientos.feature` | Cuenta duplicada | HTTP 409 con `El numero de cuenta ya existe` |
| TC-KARATE-06 | `cuentas-movimientos.feature` | Movimiento sobre cuenta inexistente | HTTP 404 con `Cuenta no encontrada` |
| TC-KARATE-07 | `cuentas-movimientos.feature` | Reporte con rango invalido | HTTP 400 con mensaje de rango |

## Pruebas manuales recomendadas

| ID | Servicio | Pasos | Resultado esperado |
| --- | --- | --- | --- |
| TC-MAN-01 | Customer | Crear cliente desde Postman y consultar `GET /clientes` | El cliente aparece en la lista |
| TC-MAN-02 | Customer | Actualizar cliente con `PUT /clientes/{id}` | Cambios persistidos |
| TC-MAN-03 | Customer | Eliminar cliente con `DELETE /clientes/{id}` | HTTP 204 |
| TC-MAN-04 | Account | Crear cuenta con saldo inicial 100 | Saldo disponible inicia en 100 |
| TC-MAN-05 | Account | Registrar deposito de 50 | Saldo disponible queda en 150 |
| TC-MAN-06 | Account | Consultar `GET /movimientos/{id}` con el ID creado | Retorna el movimiento y su cuenta |
| TC-MAN-07 | Account | Actualizar movimiento a 80 con `PUT /movimientos/{id}` | Saldo disponible queda en 180 |
| TC-MAN-08 | Account | Registrar retiro de -300 | HTTP 400 y mensaje `Saldo no disponible` |
| TC-MAN-09 | Account | Consultar `/reportes?fecha=2020-01-01,2030-12-31&cliente=<clienteId>` | Retorna movimientos del cliente |
| TC-MAN-10 | Docker | Levantar `docker compose up --build` desde WSL | Servicios, PostgreSQL y RabbitMQ disponibles |
