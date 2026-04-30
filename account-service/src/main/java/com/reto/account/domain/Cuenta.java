package com.reto.account.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cuentas")
public class Cuenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_cuenta", nullable = false, unique = true)
    private String numeroCuenta;

    @Column(name = "tipo_cuenta", nullable = false)
    private String tipoCuenta;

    @Column(name = "saldo_inicial", nullable = false, precision = 19, scale = 2)
    private BigDecimal saldoInicial;

    @Column(name = "saldo_disponible", nullable = false, precision = 19, scale = 2)
    private BigDecimal saldoDisponible;

    @Column(nullable = false)
    private Boolean estado;

    @Column(name = "cliente_id", nullable = false)
    private String clienteId;

    @Column(name = "cliente_nombre", nullable = false)
    private String clienteNombre;

    @OneToMany(mappedBy = "cuenta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Movimiento> movimientos = new ArrayList<>();

    protected Cuenta() {
    }

    public Cuenta(
            String numeroCuenta,
            String tipoCuenta,
            BigDecimal saldoInicial,
            Boolean estado,
            String clienteId,
            String clienteNombre
    ) {
        this.numeroCuenta = numeroCuenta;
        this.tipoCuenta = tipoCuenta;
        this.saldoInicial = saldoInicial;
        this.saldoDisponible = saldoInicial;
        this.estado = estado;
        this.clienteId = clienteId;
        this.clienteNombre = clienteNombre;
    }

    public Long getId() {
        return id;
    }

    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta(String numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public String getTipoCuenta() {
        return tipoCuenta;
    }

    public void setTipoCuenta(String tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
    }

    public BigDecimal getSaldoInicial() {
        return saldoInicial;
    }

    public void setSaldoInicial(BigDecimal saldoInicial) {
        this.saldoInicial = saldoInicial;
    }

    public BigDecimal getSaldoDisponible() {
        return saldoDisponible;
    }

    public void setSaldoDisponible(BigDecimal saldoDisponible) {
        this.saldoDisponible = saldoDisponible;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public String getClienteId() {
        return clienteId;
    }

    public void setClienteId(String clienteId) {
        this.clienteId = clienteId;
    }

    public String getClienteNombre() {
        return clienteNombre;
    }

    public void setClienteNombre(String clienteNombre) {
        this.clienteNombre = clienteNombre;
    }

    public Movimiento registrarMovimiento(BigDecimal valor) {
        BigDecimal nuevoSaldo = saldoDisponible.add(valor);
        if (nuevoSaldo.signum() < 0) {
            throw new InsufficientBalanceException("Saldo no disponible");
        }

        String tipoMovimiento = valor.signum() >= 0 ? "Deposito" : "Retiro";
        saldoDisponible = nuevoSaldo;

        Movimiento movimiento = new Movimiento(this, tipoMovimiento, valor, nuevoSaldo);
        movimientos.add(movimiento);
        return movimiento;
    }

    public void actualizarMovimiento(Movimiento movimiento, BigDecimal nuevoValor) {
        BigDecimal diferencia = nuevoValor.subtract(movimiento.getValor());
        BigDecimal nuevoSaldo = saldoDisponible.add(diferencia);
        if (nuevoSaldo.signum() < 0) {
            throw new InsufficientBalanceException("Saldo no disponible");
        }

        saldoDisponible = nuevoSaldo;
        movimiento.update(nuevoValor, nuevoSaldo);
    }

    public void reversarMovimiento(Movimiento movimiento) {
        BigDecimal nuevoSaldo = saldoDisponible.subtract(movimiento.getValor());
        if (nuevoSaldo.signum() < 0) {
            throw new InsufficientBalanceException("Saldo no disponible");
        }

        saldoDisponible = nuevoSaldo;
    }
}
