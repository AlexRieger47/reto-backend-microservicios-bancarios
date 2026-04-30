# Reto Backend - Microservicios bancarios

Monorepo Java/Spring Boot para resolver el reto técnico backend de banca. La solución implementa clientes, cuentas, movimientos, reportes, persistencia relacional, comunicación asíncrona, Docker Compose, documentación Swagger y pruebas automatizadas.

## 🎯 Alcance

- CRUD de clientes, con herencia `Cliente extends Persona`.
- CRUD de cuentas.
- CRUD de movimientos con actualización automática del saldo.
- Validación de retiros con el mensaje requerido: `Saldo no disponible`.
- Reporte de estado de cuenta por cliente y rango de fechas.
- Pruebas unitarias, integración y end-to-end con Karate.

## 🧰 Stack

- Java 21
- Spring Boot 3.5.x
- Maven
- Spring Web
- Spring Data JPA
- PostgreSQL
- RabbitMQ
- Swagger / OpenAPI con Springdoc
- JUnit 5 y Mockito
- Karate DSL
- Docker Compose

## 🗂️ Estructura

| Módulo | Responsabilidad |
| --- | --- |
| `customer-service` | CRUD de clientes/personas y publicación de eventos de cliente. |
| `account-service` | Cuentas, movimientos, validación de saldo, reportes y réplica simple de clientes. |
| `karate-tests` | Pruebas end-to-end sobre los endpoints reales. |
| `docs/TestCases.md` | Matriz de pruebas automáticas y manuales. |
| `BaseDatos.sql` | Script relacional base para la entrega. |
| `postman/reto-backend.postman_collection.json` | Colección Postman para validación manual. |

## 🐳 Ejecución con Docker

Desde la raíz de `RepoBackend`:

```bash
docker compose up -d --build
```

El Compose levanta:

| Servicio | URL / Puerto |
| --- | --- |
| `customer-service` | `http://127.0.0.1:8081` |
| `account-service` | `http://127.0.0.1:8082` |
| PostgreSQL clientes | `localhost:5433` |
| PostgreSQL cuentas | `localhost:5434` |
| RabbitMQ AMQP | `localhost:5672` |
| RabbitMQ Management | `http://127.0.0.1:15672` |

Las bases de datos y RabbitMQ tienen `healthcheck`; los microservicios esperan a que la infraestructura esté saludable antes de arrancar.

Para reiniciar desde cero:

```bash
docker compose down -v
docker compose up -d --build
```

## 🚀 Ejecución en desarrollo

Por defecto, los servicios usan H2 en memoria para desarrollo rápido. La integración con RabbitMQ queda desactivada localmente para permitir levantar cada servicio sin contenedores.

Terminal 1:

```bash
mvn -pl customer-service spring-boot:run
```

Terminal 2:

```bash
mvn -pl account-service spring-boot:run
```

## 🌐 Endpoints

Customer service:

| Método | Ruta |
| --- | --- |
| `GET` | `/clientes` |
| `GET` | `/clientes/{id}` |
| `POST` | `/clientes` |
| `PUT` | `/clientes/{id}` |
| `DELETE` | `/clientes/{id}` |

Account service:

| Método | Ruta |
| --- | --- |
| `GET` | `/cuentas` |
| `GET` | `/cuentas/{id}` |
| `POST` | `/cuentas` |
| `PUT` | `/cuentas/{id}` |
| `DELETE` | `/cuentas/{id}` |
| `GET` | `/movimientos` |
| `GET` | `/movimientos/{id}` |
| `POST` | `/movimientos` |
| `PUT` | `/movimientos/{id}` |
| `DELETE` | `/movimientos/{id}` |
| `GET` | `/reportes?fecha=yyyy-MM-dd,yyyy-MM-dd&cliente=CLI-001` |

Los movimientos recalculan el saldo disponible de la cuenta al crearse, actualizarse o eliminarse.

## 📖 Swagger

Con los servicios levantados:

- Customer service: `http://127.0.0.1:8081/swagger-ui.html`
- Account service: `http://127.0.0.1:8082/swagger-ui.html`

- Alternativamente, usar `localhost`.

## 📬 Postman

Importa la colección:

```text
postman/reto-backend.postman_collection.json
```

Flujo recomendado:

1. `Crear cliente`
2. `Crear cuenta`
3. `Registrar depósito`
4. `Consultar movimiento por ID`
5. `Actualizar movimiento`
6. `Retiro sin saldo`
7. `Reporte`

Los requests `Consultar movimiento por ID` y `Actualizar movimiento` usan la variable `{{movementId}}`. Ejecuta primero `Registrar depósito`, porque ese request guarda automáticamente el ID del movimiento creado. Si ejecutas esos requests de forma aislada, reemplaza `{{movementId}}` por un ID real existente.

## 🧪 Pruebas

Suite completa:

```bash
mvn test
```

`mvn test` ejecuta pruebas unitarias, integración y Karate. Para que Karate pueda probar los endpoints reales, los servicios deben estar levantados en `8081` y `8082`.

En Windows/WSL espejo, si `localhost` da problemas:

```powershell
mvn test "-Dcustomer.baseUrl=http://127.0.0.1:8081" "-Daccount.baseUrl=http://127.0.0.1:8082"
```

Solo unitarias e integración por servicio:

```bash
mvn -pl customer-service,account-service test
```

Solo Karate:

```powershell
mvn -pl karate-tests test
```

Reporte HTML de Karate:

```text
karate-tests/target/karate-reports/karate-summary.html
```

Las pruebas Karate generan IDs dinámicos con `UUID`, por lo que pueden repetirse contra H2 o PostgreSQL sin colisionar por `clienteId` o `numeroCuenta`.

## 🏗️ Decisiones técnicas

- Monorepo para simplificar revisión y ejecución sin perder independencia por servicio.
- Separación por capas: `api`, `application`, `domain` e `infrastructure`.
- Regla de saldo dentro del dominio `Cuenta`, no en el controlador.
- DTOs para no exponer entidades JPA directamente.
- Manejo centralizado de excepciones con respuestas JSON consistentes.
- Comunicación asíncrona con RabbitMQ: `customer-service` publica eventos y `account-service` mantiene una réplica simple de clientes.
- Springdoc para documentar endpoints con Swagger UI.
