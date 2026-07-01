package com.graphgen.api;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import com.graphgen.style.Theme;

public class GraphTest {

    @Test
    public void construtor_ComLimitesIguais_DeveLancarExcecao() {
        // Assert
    	// Verifica se o Java lança IllegalArgumentException quando xMin e xMax sao iguais
        assertThrows(IllegalArgumentException.class, () -> {
            new Graph(1000, 1000, 5, 5, -10, 10);
        }, "Deve proibir xMin igual a xMax");

        assertThrows(IllegalArgumentException.class, () -> {
            new Graph(1000, 1000, -10, 10, 0, 0);
        }, "Deve proibir yMin igual a yMax");
    }

    @Test
    public void construtor_ComMinimoMaiorQueMaximo_DeveLancarExcecao() {
        // Assert
    	// Verifica se a trava de logica invertida funciona
        assertThrows(IllegalArgumentException.class, () -> {
            new Graph(1000, 1000, 10, -10, -10, 10);
        }, "Deve proibir xMin maior que xMax");

        assertThrows(IllegalArgumentException.class, () -> {
            new Graph(1000, 1000, -10, 10, 20, 5);
        }, "Deve proibir yMin maior que yMax");
    }

    @Test
    public void desenharEGeraGrafico_ComFluxoCompleto_NaoDeveDarCrash() {
        // Arrange
        Graph gg = new Graph(800, 800, -5, 5, -5, 5);
        Function func = new Function().setFunction(x -> x * 2).setSubtitles("2x");
        
        // Act e Assert
        // assertDoesNotThrow garante que o codigo roda do comeco ao fim sem estourar NullPointerException ou erros de fonte
        assertDoesNotThrow(() -> {
            gg.setTheme(Theme.DARK_MODE);
            gg.drawFunction(func);
            gg.drawSubtitles();
            gg.generateGraphPng();
        }, "A esteira completa de desenho e geracao nao deve lancar excecoes");
    }

    @Test
    public void generateGraphPng_DeveCriarOArquivoFisicoNoComputador() {
        // Arrange
        Graph gg = new Graph(500, 500, -10, 10, -10, 10);
        Function func = new Function().setFunction(x -> x * x);
        gg.drawFunction(func);
        
        // Act
        gg.generateGraphPng();
        
        // Assert
        File arquivoGerado = new File("Grafico.png");
        assertTrue(arquivoGerado.exists(), "O arquivo Grafico.png deveria ter sido criado no diretório");
    }

    @AfterEach
    public void limparArquivosGerados() {
        File arquivo = new File("Grafico.png");
        if (arquivo.exists()) {
            arquivo.delete();
        }
    }
}