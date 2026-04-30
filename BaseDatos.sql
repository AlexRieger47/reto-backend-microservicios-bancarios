CREATE TABLE clientes (
    id BIGSERIAL PRIMARY KEY,
    cliente_id VARCHAR(80) NOT NULL UNIQUE,
    nombre VARCHAR(120) NOT NULL,
    genero VARCHAR(40) NOT NULL,
    edad INTEGER NOT NULL,
    identificacion VARCHAR(40) NOT NULL,
    direccion VARCHAR(200) NOT NULL,
    telefono VARCHAR(40) NOT NULL,
    contrasena VARCHAR(120) NOT NULL,
    estado BOOLEAN NOT NULL
);

CREATE TABLE clientes_replica (
    id BIGSERIAL PRIMARY KEY,
    cliente_id VARCHAR(80) NOT NULL UNIQUE,
    nombre VARCHAR(120) NOT NULL,
    estado BOOLEAN NOT NULL
);

CREATE TABLE cuentas (
    id BIGSERIAL PRIMARY KEY,
    numero_cuenta VARCHAR(80) NOT NULL UNIQUE,
    tipo_cuenta VARCHAR(40) NOT NULL,
    saldo_inicial NUMERIC(19, 2) NOT NULL,
    saldo_disponible NUMERIC(19, 2) NOT NULL,
    estado BOOLEAN NOT NULL,
    cliente_id VARCHAR(80) NOT NULL,
    cliente_nombre VARCHAR(120) NOT NULL
);

CREATE TABLE movimientos (
    id BIGSERIAL PRIMARY KEY,
    fecha DATE NOT NULL,
    tipo_movimiento VARCHAR(40) NOT NULL,
    valor NUMERIC(19, 2) NOT NULL,
    saldo NUMERIC(19, 2) NOT NULL,
    cuenta_id BIGINT NOT NULL REFERENCES cuentas(id)
);

INSERT INTO clientes (cliente_id, nombre, genero, edad, identificacion, direccion, telefono, contrasena, estado)
VALUES
    ('CLI-001', 'Jose Lema', 'Masculino', 32, '0102030405', 'Otavalo sn y principal', '0987654321', '1234', true),
    ('CLI-002', 'Marianela Montalvo', 'Femenino', 28, '0203040506', 'Amazonas y Naciones Unidas', '0991234567', '5678', true);

INSERT INTO cuentas (numero_cuenta, tipo_cuenta, saldo_inicial, saldo_disponible, estado, cliente_id, cliente_nombre)
VALUES
    ('478758', 'Ahorros', 2000.00, 2000.00, true, 'CLI-001', 'Jose Lema'),
    ('225487', 'Corriente', 100.00, 100.00, true, 'CLI-002', 'Marianela Montalvo');
