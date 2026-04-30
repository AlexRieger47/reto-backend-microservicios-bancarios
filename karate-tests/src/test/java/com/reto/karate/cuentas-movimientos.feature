Feature: Cuentas y movimientos API

  Background:
    * url karate.properties['account.baseUrl'] || 'http://localhost:8082'
    * def uuid = java.util.UUID.randomUUID() + ''
    * def suffix = uuid.substring(0, 8)
    * def numeroCuenta = 'KRT-' + suffix
    * def clienteId = 'CLI-' + suffix

  Scenario: crear cuenta, registrar deposito, consultar reporte y rechazar retiro sin saldo
    Given path 'cuentas'
    And request { numeroCuenta: '#(numeroCuenta)', tipoCuenta: 'Ahorros', saldoInicial: 100, estado: true, clienteId: '#(clienteId)', clienteNombre: 'Cliente Karate' }
    When method post
    Then status 201

    Given path 'movimientos'
    And request { numeroCuenta: '#(numeroCuenta)', valor: 50 }
    When method post
    Then status 201
    And match response.saldo == 150
    * def movimientoId = response.id

    Given path 'movimientos', movimientoId
    When method get
    Then status 200
    And match response.id == movimientoId
    And match response.numeroCuenta == numeroCuenta

    Given path 'movimientos', movimientoId
    And request { numeroCuenta: '#(numeroCuenta)', valor: 80 }
    When method put
    Then status 200
    And match response.valor == 80
    And match response.saldo == 180

    Given path 'reportes'
    And param fecha = '2020-01-01,2030-12-31'
    And param cliente = clienteId
    When method get
    Then status 200
    And match response[0].cliente == 'Cliente Karate'
    And match response[0].saldoDisponible == 180

    Given path 'movimientos'
    And request { numeroCuenta: '#(numeroCuenta)', valor: -300 }
    When method post
    Then status 400
    And match response.message == 'Saldo no disponible'

  Scenario: rechazar cuenta duplicada
    Given path 'cuentas'
    And request { numeroCuenta: '#(numeroCuenta)', tipoCuenta: 'Corriente', saldoInicial: 200, estado: true, clienteId: '#(clienteId)', clienteNombre: 'Cliente Karate' }
    When method post
    Then status 201

    Given path 'cuentas'
    And request { numeroCuenta: '#(numeroCuenta)', tipoCuenta: 'Corriente', saldoInicial: 200, estado: true, clienteId: '#(clienteId)', clienteNombre: 'Cliente Karate' }
    When method post
    Then status 409
    And match response.message == 'El numero de cuenta ya existe'

  Scenario: rechazar movimiento para cuenta inexistente
    Given path 'movimientos'
    And request { numeroCuenta: '#("NO-" + suffix)', valor: 25 }
    When method post
    Then status 404
    And match response.message == 'Cuenta no encontrada'

  Scenario: rechazar reporte con rango de fechas invalido
    Given path 'reportes'
    And param fecha = '2026-01-01'
    And param cliente = clienteId
    When method get
    Then status 400
    And match response.message contains 'rango de fecha'
