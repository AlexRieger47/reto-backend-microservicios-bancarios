package com.reto.karate;

import com.intuit.karate.junit5.Karate;

class KarateTest {

    @Karate.Test
    Karate runAll() {
        return Karate.run("clientes", "cuentas-movimientos").relativeTo(getClass());
    }
}
