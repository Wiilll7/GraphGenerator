package com.graphgen.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.awt.Color;
import org.junit.jupiter.api.Test;

public class FunctionTest {

    // Margem de tolerancia para testes matematicos
    private static final double DELTA = 0.0001;

    @Test
    public void clone_DeveCriarNovaInstanciaComOsMesmosValores() {
        // Arrange
        IFunction lambda = (x) -> x * 2;
        Function original = new Function()
                .setFunction(lambda)
                .setColor(Color.BLUE)
                .setSubtitles("2x");

        // Act
        Function clonada = original.clone();

        // Assert
        // O clone deve ser um objeto diferente na memoria
        assertNotSame(original, clonada, "O objeto clonado deve ser uma nova instancia na memoria");
        
        // Os valores dentro dele devem ser exatamente iguais aos do original
        assertEquals(original.getFunction(), clonada.getFunction(), "A funcao deve ser copiada");
        assertEquals(original.getColor(), clonada.getColor(), "A cor deve ser copiada");
        assertEquals(original.getText(), clonada.getText(), "A legenda deve ser copiada");
    }

    @Test
    public void function_DeveDelegarCalculoParaOLambdaInterno() {
        // Arrange
        // Funcao que soma 5 ao x
        Function func = new Function().setFunction((x) -> x + 5);

        // Act
        // Avalia x = 10
        double resultado = func.function(10.0);

        // Assert
        assertEquals(15.0, resultado, DELTA, "A classe deve repassar o calculo corretamente para a interface interna");
    }

    @Test
    public void setters_UsandoFluentApi_DevemModificarVariaveisERetornarPropriaInstancia() {
        // Arrange
        Function func = new Function();
        Color corTeste = Color.RED;
        String legendaTeste = "sin(x)";

        // Act
        Function retornoMetodo = func.setColor(corTeste).setSubtitles(legendaTeste);

        // Assert
        // Verifica se os valores foram salvos
        assertEquals(corTeste, func.getColor(), "A cor deve ser salva corretamente");
        assertEquals(legendaTeste, func.getText(), "A legenda deve ser salva corretamente");
        
        // Verifica se retornou o this
        assertSame(func, retornoMetodo, "Os setters devem retornar a propria instancia");
    }
}