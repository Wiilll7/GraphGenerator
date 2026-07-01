package com.graphgen.core;

/**
 * Classe responsável por realizar o mapeamento entre o plano cartesiano matemático 
 * e as coordenadas de pixel da tela. Calcula áreas úteis, margens, posição da origem 
 * e os fatores de escala necessários para desenhar o gráfico sem distorções.
 * * @author Willian Moretti
 * @version 1.0
 */
public class Viewport {
	
	/** Largura da área útil de desenho em pixels. */
	public final int widthArea;
	
	/** Altura da área útil de desenho em pixels. */
    public final int heightArea;
    
    /** Tamanho da margem horizontal em pixels. */
    public final int margemX;
    
    /** Tamanho da margem vertical em pixels. */
    public final int margemY;
    
    /** Distância matemática total do eixo X. */
    public final double totalX;
    
    /** Distância matemática total do eixo Y. */
    public final double totalY;
    
    /** Posição X do ponto de origem (0,0) relativo a área útil. */
    public final int origemX;
    
    /** Posição Y do ponto de origem (0,0) relativo a área útil. */
    public final int origemY;
    
    /** Limite máximo de pixels para a esquerda a partir da origem. */
    public final int leftLimit;
    
    /** Limite máximo de pixels para a direita a partir da origem. */
    public final int rightLimit;
    
    /** Limite máximo de pixels para cima a partir da origem. */
    public final int upLimit;
    
    /** Limite máximo de pixels para baixo a partir da origem. */
    public final int downLimit;
    
    /** Quantidade de pixels que representa 1 unidade matemática no eixo X. */
    public final double xPixels; 
    
    /** Quantidade de pixels que representa 1 unidade matemática no eixo Y. */
    public final double yPixels;

    
    /**
     * Construtor do Viewport. Realiza todos os cálculos de proporção baseados no tamanho 
     * físico da imagem e nos limites matemáticos solicitados pelo usuário.
     * * @param width A largura total da imagem em pixels.
     * @param height A altura total da imagem em pixels.
     * @param xMin O valor mínimo matemático do eixo X.
     * @param xMax O valor máximo matemático do eixo X.
     * @param yMin O valor mínimo matemático do eixo Y.
     * @param yMax O valor máximo matemático do eixo Y.
     */
    public Viewport(int width, int height, double xMin, double xMax, double yMin, double yMax) {
        this.margemX = (int) (width * 0.05);
        this.margemY = (int) (height * 0.05);
        this.widthArea = width - (margemX * 2);
        this.heightArea = height - (margemY * 2);
        
        this.totalX = xMax - xMin;
        this.totalY = yMax - yMin;
        double proporcaoZeroX = -xMin / totalX;
        double proporcaoZeroY = yMax / totalY;
        this.origemX = (int) (proporcaoZeroX * widthArea);
        this.origemY = (int) (proporcaoZeroY * heightArea);
        
        this.leftLimit = -origemX - margemX;
        this.rightLimit = (widthArea - origemX) + margemX;
        this.upLimit = -origemY - margemY;
        this.downLimit = (heightArea - origemY) + margemY;
        
        this.xPixels = widthArea / totalX;
        this.yPixels = heightArea / totalY;
    }
    
}