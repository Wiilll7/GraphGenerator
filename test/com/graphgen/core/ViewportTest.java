package com.graphgen.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class ViewportTest {

    // Margem de tolerancia para calculos
    private static final double DELTA = 0.0001;

    @Test
    public void construtor_DeveCalcularMargensEAreasCorretamente() {
        // Arrange
        int width = 1000;
        int height = 1000;
        double xMin = -10, xMax = 10, yMin = -10, yMax = 10;

        // Act
        Viewport vp = new Viewport(width, height, xMin, xMax, yMin, yMax);

        // Assert
        // A margem deve ser 5% do tamanho total
        assertEquals(50, vp.margemX, "A margem X deve ser 50 pixels");
        assertEquals(50, vp.margemY, "A margem Y deve ser 50 pixels");
        
        // A area util é o tamanho total menos as duas margens
        assertEquals(900, vp.widthArea, "A largura util deve ser 900 pixels");
        assertEquals(900, vp.heightArea, "A altura util deve ser 900 pixels");
    }

    @Test
    public void construtor_ComDominioSimetrico_DeveCentralizarOrigemNoMeioDaTela() {
        // Arrange
        Viewport vp = new Viewport(1000, 1000, -10, 10, -10, 10);

        // Assert
        // A origem (0,0) matematica deve estar bem no centro da area util (900 / 2 = 450)
        assertEquals(450, vp.origemX, "A origem X deve estar no centro da area util (450)");
        assertEquals(450, vp.origemY, "A origem Y deve estar no centro da area util (450)");
        
        // Total de X e Y deve ser a distancia entre min e max
        assertEquals(20.0, vp.totalX, DELTA, "O total X deve ser 20 unidades");
        assertEquals(20.0, vp.totalY, DELTA, "O total Y deve ser 20 unidades");
        
        // Os pixels por unidade (area util / unidades totais)
        assertEquals(45.0, vp.xPixels, DELTA, "Cada unidade de X deve ocupar 45 pixels");
        assertEquals(45.0, vp.yPixels, DELTA, "Cada unidade de Y deve ocupar 45 pixels");
    }

    @Test
    public void construtor_DeveCalcularLimitesDaTelaCorretamente() {
        // Arrange
        Viewport vp = new Viewport(1000, 1000, -10, 10, -10, 10);

        // Assert
        // Limite esquerdo: -origemX - margemX
        assertEquals(-500, vp.leftLimit, "O limite esquerdo deve ser -500 a partir da origem");
        
        // Limite direito: (widthArea - origemX) + margemX
        assertEquals(500, vp.rightLimit, "O limite direito deve ser 500 a partir da origem");
        
        // Limite superior
        assertEquals(-500, vp.upLimit, "O limite superior deve ser -500 a partir da origem");
        assertEquals(500, vp.downLimit, "O limite inferior deve ser 500 a partir da origem");
    }

    @Test
    public void construtor_ComDominioApenasPositivo_DeveAjustarOrigemParaAEsquerda() {
        // Arrange 
    	// Dominio X de 0 a 10
        Viewport vp = new Viewport(1000, 1000, 0, 10, -10, 10);

        // Assert
        // Como o grafico vai de 0 a 10, o 0 fica grudado na margem esquerda da area util
        assertEquals(0, vp.origemX, "A origem X deve ser 0 na area util se o xMin for 0");
        
        // Pixels por unidade: área / totalX pixels por unidade
        assertEquals(90.0, vp.xPixels, DELTA, "Cada unidade de X agora deve ocupar 90 pixels");
    }
}