Feature: Cuentas y movimientos API

  Background:
    * url karate.properties['account.baseUrl'] || 'http://localhost:8082'

  Scenario: crear cuenta, registrar deposito y rechazar retiro sin saldo
    Given path 'cuentas'
    And request
      """
      {
        "numeroCuenta": "KARATE-001",
        "tipoCuenta": "Ahorros",
        "saldoInicial": 100,
        "estado": true,
        "clienteId": "CLI-KARATE",
        "clienteNombre": "Cliente Karate"
      }
      """
    When method post
    Then status 201

    Given path 'movimientos'
    And request { numeroCuenta: 'KARATE-001', valor: 50 }
    When method post
    Then status 201
    And match response.saldo == 150

    Given path 'movimientos'
    And request { numeroCuenta: 'KARATE-001', valor: -300 }
    When method post
    Then status 400
    And match response.message == 'Saldo no disponible'
