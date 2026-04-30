Feature: Clientes API

  Background:
    * url karate.properties['customer.baseUrl'] || 'http://localhost:8081'
    * def uuid = java.util.UUID.randomUUID() + ''
    * def suffix = uuid.substring(0, 8)
    * def clienteId = 'CLI-' + suffix

  Scenario: crear y listar clientes
    Given path 'clientes'
    And request { clienteId: '#(clienteId)', nombre: 'Cliente Karate', genero: 'Masculino', edad: 30, identificacion: '#("ID-" + suffix)', direccion: 'Direccion de prueba', telefono: '0999999999', contrasena: '1234', estado: true }
    When method post
    Then status 201
    And match response.clienteId == clienteId

    Given path 'clientes'
    When method get
    Then status 200
    And match response[*].clienteId contains clienteId

  Scenario: rechazar cliente con campos obligatorios faltantes
    Given path 'clientes'
    And request { clienteId: '#(clienteId)', nombre: '', genero: 'Masculino', edad: 30, identificacion: '#("ID-" + suffix)', direccion: 'Direccion de prueba', telefono: '0999999999', contrasena: '1234', estado: true }
    When method post
    Then status 400
    And match response.message contains 'nombre'

  Scenario: rechazar cliente duplicado
    Given path 'clientes'
    And request { clienteId: '#(clienteId)', nombre: 'Cliente Duplicado', genero: 'Masculino', edad: 30, identificacion: '#("ID-" + suffix)', direccion: 'Direccion de prueba', telefono: '0999999999', contrasena: '1234', estado: true }
    When method post
    Then status 201

    Given path 'clientes'
    And request { clienteId: '#(clienteId)', nombre: 'Cliente Duplicado', genero: 'Masculino', edad: 30, identificacion: '#("ID-" + suffix)', direccion: 'Direccion de prueba', telefono: '0999999999', contrasena: '1234', estado: true }
    When method post
    Then status 409
    And match response.message == 'El clienteId ya existe'
