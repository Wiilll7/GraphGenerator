package com.graphgen.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class CalculusTest {

    // Margem de tolerancia para testes com ponto flutuante
    private static final double DELTA = 0.0001;

    @Test
    public void sin_ComValorZero_DeveRetornarZero() {
        // Arrange e Act
        double resultado = Calculus.sin(0);
        // Assert
        assertEquals(0.0, resultado, DELTA, "O seno de 0 deve ser 0");
    }

    @Test
    public void sin_ComPiSobreDois_DeveRetornarUm() {
        // Arrange e Act
        double resultado = Calculus.sin(Calculus.pi / 2);
        // Assert
        assertEquals(1.0, resultado, DELTA, "O seno de pi/2 deve ser 1");
    }

    @Test
    public void cos_ComValorZero_DeveRetornarUm() {
        // Arrange e Act
        double resultado = Calculus.cos(0);
        // Assert
        assertEquals(1.0, resultado, DELTA, "O cosseno de 0 deve ser 1");
    }

    @Test
    public void cos_ComPi_DeveRetornarMenosUm() {
        // Arrange e Act
        double resultado = Calculus.cos(Calculus.pi);
        // Assert
        assertEquals(-1.0, resultado, DELTA, "O cosseno de pi deve ser -1");
    }

    @Test
    public void pow_IntBasePositivaExpoentePositivo_DeveRetornarPotencia() {
        // Arrange e Act
        double resultado = Calculus.pow(2.0, 3);
        // Assert
        assertEquals(8.0, resultado, DELTA, "2 elevado a 3 deve ser 8");
    }

    @Test
    public void pow_DoubleBasePositivaExpoenteFracionado_DeveRetornarRaiz() {
        // Arrange e Act
        double resultado = Calculus.pow(4.0, 0.5);
        // Assert
        assertEquals(2.0, resultado, DELTA, "4 elevado a 0.5 deve ser 2");
    }

    @Test
    public void sqrt_ComNumeroQuadradoPerfeito_DeveRetornarRaizExata() {
        // Arrange e Act
        double resultado = Calculus.sqrt(9.0);
        // Assert
        assertEquals(3.0, resultado, DELTA, "A raiz de 9 deve ser 3");
    }

    @Test
    public void sqrt_ComNumeroNegativo_DeveRetornarNaN() {
        // Arrange e Act
        double resultado = Calculus.sqrt(-4.0);
        // Assert
        assertTrue(Double.isNaN(resultado), "A raiz de numero negativo deve retornar NaN");
    }

    @Test
    public void ln_DeUm_DeveRetornarZero() {
        // Arrange e Act
        double resultado = Calculus.ln(1.0);
        // Assert
        assertEquals(0.0, resultado, DELTA, "O logaritmo natural de 1 deve ser 0");
    }

    @Test
    public void ln_DaConstanteE_DeveRetornarUm() {
        // Arrange e Act
        double resultado = Calculus.ln(Calculus.e);
        // Assert
        assertEquals(1.0, resultado, DELTA, "O logaritmo natural de 'e' deve ser 1");
    }

    @Test
    public void abs_ComNumeroNegativo_DeveRetornarPositivo() {
        // Arrange e Act
        double resultado = Calculus.abs(-15.5);
        // Assert
        assertEquals(15.5, resultado, DELTA, "O valor absoluto de -15.5 deve ser 15.5");
    }

    @Test
    public void round_ComNumeroQuebradoParaBaixo_DeveArredondarParaBaixo() {
        // Arrange e Act
        int resultado = Calculus.round(2.4);
        // Assert
        assertEquals(2, resultado, "2.4 deve ser arredondado para 2");
    }

    @Test
    public void round_ComNumeroQuebradoParaCima_DeveArredondarParaCima() {
        // Arrange e Act
        int resultado = Calculus.round(2.6);
        // Assert
        assertEquals(3, resultado, "2.6 deve ser arredondado para 3");
    }
}