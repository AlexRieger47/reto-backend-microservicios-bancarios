Feature: Clientes API

  Background:
    * url karate.properties['customer.baseUrl'] || 'http://localhost:8081'

  Scenario: crear y listar clientes
    Given path 'clientes'
    And request
      """
      {
        "clienteId": "CLI-KARATE",
        "nombre": "Cliente Karate",
        "genero": "Masculino",
        "edad": 30,
        "identificacion": "9999999999",
        "direccion": "Direccion de prueba",
        "telefono": "0999999999",
        "contrasena": "1234",
        "estado": true
      }
      """
    When method post
    Then status 201
    And match response.clienteId == 'CLI-KARATE'

    Given path 'clientes'
    When method get
    Then status 200
    And match response[*].clienteId contains 'CLI-KARATE'
