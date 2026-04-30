# Reto Backend - Microservicios bancarios

Monorepo para la prueba tecnica backend de arquitectura de microservicios.

## Objetivo

Implementar dos microservicios Spring Boot para el dominio bancario solicitado:

- Cliente / Persona
- Cuenta / Movimientos / Reportes

La solucion contempla API REST, JPA, base de datos relacional, comunicacion asincronica, Docker Compose y pruebas.

## Stack previsto

- Java 21 como target de compilacion
- Spring Boot 3.5.x
- Maven
- Spring Data JPA
- PostgreSQL
- RabbitMQ
- JUnit 5 / Mockito
- Karate DSL
- Docker Compose

## Estructura

- `customer-service`: CRUD de clientes/personas y publicacion de eventos.
- `account-service`: cuentas, movimientos, validacion de saldo y reportes.
- `karate-tests`: pruebas end-to-end con Karate.
- `docs/TestCases.md`: matriz de pruebas automaticas y manuales.
- `BaseDatos.sql`: script relacional base para la entrega.
- `postman/reto-backend.postman_collection.json`: coleccion de prueba manual.

## Endpoints

Customer service (`http://localhost:8081`):

- `GET /clientes`
- `GET /clientes/{id}`
- `POST /clientes`
- `PUT /clientes/{id}`
- `DELETE /clientes/{id}`

Account service (`http://localhost:8082`):

- `GET /cuentas`
- `GET /cuentas/{id}`
- `POST /cuentas`
- `PUT /cuentas/{id}`
- `DELETE /cuentas/{id}`
- `GET /movimientos`
- `GET /movimientos/{id}`
- `POST /movimientos`
- `PUT /movimientos/{id}`
- `DELETE /movimientos/{id}`
- `GET /reportes?fecha=yyyy-MM-dd,yyyy-MM-dd&cliente=CLI-001`

Los movimientos recalculan el saldo disponible de la cuenta al crearse, actualizarse o eliminarse. Si un retiro supera el saldo disponible, la API retorna `Saldo no disponible`.

## Swagger

Con los servicios levantados, la documentacion interactiva queda disponible en:

- Customer service: `http://127.0.0.1:8081/swagger-ui.html`
- Account service: `http://127.0.0.1:8082/swagger-ui.html`

En Windows con WSL en modo espejo, `127.0.0.1` suele ser mas confiable que `localhost`.

## Ejecutar en desarrollo

Desde la raiz de `RepoBackend`:

```bash
mvn -pl customer-service spring-boot:run
```

En otra terminal:

```bash
mvn -pl account-service spring-boot:run
```

Por defecto los servicios usan H2 en memoria para desarrollo rapido. La integracion asincronica con RabbitMQ queda desactivada localmente para que los servicios puedan levantar sin contenedores.

## Ejecutar con Docker

```bash
docker compose up --build
```

El Compose levanta PostgreSQL para cada servicio, RabbitMQ, `customer-service` en el puerto `8081` y `account-service` en el puerto `8082`.

## Pruebas

Suite completa:

```bash
mvn test
```

`mvn test` ejecuta unitarias, integracion y Karate. Para que Karate pueda probar los endpoints reales, `customer-service` y `account-service` deben estar levantados en `8081` y `8082`.

En Windows/WSL espejo, si `localhost` da problemas, ejecuta:

```powershell
mvn test "-Dcustomer.baseUrl=http://127.0.0.1:8081" "-Daccount.baseUrl=http://127.0.0.1:8082"
```

Solo unitarias e integracion por servicio, sin Karate:

```bash
mvn -pl customer-service,account-service test
```

Karate solamente:

```powershell
mvn -pl karate-tests test
```

Cuando la suite se ejecuta, Karate genera el reporte HTML en:

```text
karate-tests/target/karate-reports/karate-summary.html
```

Nota: la documentacion de apoyo mencionaba `karate-junit5:2.0.6`, pero esa coordenada no esta publicada en Maven Central. Por eso se usa `io.karatelabs:karate-junit5:1.5.2`.

Las pruebas Karate generan IDs dinamicos con `UUID`, por lo que pueden repetirse contra H2 o PostgreSQL sin colisionar por `clienteId` o `numeroCuenta`.

## Decisiones de arquitectura

- Monorepo para simplificar revision y ejecucion del reto sin perder independencia por servicio.
- Separacion por capas: `api`, `application`, `domain`, `infrastructure`.
- Regla de saldo dentro del dominio `Cuenta`, no en el controlador.
- Manejo centralizado de excepciones con respuestas JSON consistentes.
- Comunicacion asincronica mediante RabbitMQ: cliente publica cambios y cuentas mantiene una replica simple.
