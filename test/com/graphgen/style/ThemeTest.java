package com.graphgen.style;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.awt.Color;
import org.junit.jupiter.api.Test;

public class ThemeTest {

    @Test
    public void whiteMode_DeveEstarTotalmenteConfiguradoSemCoresNulas() {
        // Arrange
        Theme white = Theme.WHITE_MODE;

        // Assert
        assertNotNull(white.getAxisXY(), "Cor do Eixo XY nao pode ser nula");
        assertNotNull(white.getBackground(), "Cor de fundo nao pode ser nula");
        assertNotNull(white.getDefaultFunction(), "Cor padrao da funcao nao pode ser nula");
        assertNotNull(white.getGridPrincipal(), "Cor do grid principal nao pode ser nula");
        assertNotNull(white.getNumbers(), "Cor dos numeros nao pode ser nula");
        assertNotNull(white.getSubGrid(), "Cor do subgrid nao pode ser nula");
        assertNotNull(white.getSubtitleBackground(), "Cor de fundo da legenda nao pode ser nula");
    }

    @Test
    public void darkMode_DeveEstarTotalmenteConfiguradoSemCoresNulas() {
        // Arrange
        Theme dark = Theme.DARK_MODE;

        // Assert
        assertNotNull(dark.getAxisXY(), "Cor do Eixo XY nao pode ser nula");
        assertNotNull(dark.getBackground(), "Cor de fundo nao pode ser nula");
        assertNotNull(dark.getDefaultFunction(), "Cor padrao da funcao nao pode ser nula");
        assertNotNull(dark.getGridPrincipal(), "Cor do grid principal nao pode ser nula");
        assertNotNull(dark.getNumbers(), "Cor dos numeros nao pode ser nula");
        assertNotNull(dark.getSubGrid(), "Cor do subgrid nao pode ser nula");
        assertNotNull(dark.getSubtitleBackground(), "Cor de fundo da legenda nao pode ser nula");
    }

    @Test
    public void setters_UsandoFluentApi_DevemModificarVariavelERetornarPropriaInstancia() {
        // Arrange
        Theme customTheme = new Theme();
        Color corTeste = Color.MAGENTA;

        // Act
        Theme retornoMetodo = customTheme.setBackground(corTeste);

        // Assert
        assertEquals(corTeste, customTheme.getBackground(), "O setter deve alterar a cor de fundo com sucesso");
        assertSame(customTheme, retornoMetodo, "O setter deve retornar a propria instancia do objeto");
    }
}