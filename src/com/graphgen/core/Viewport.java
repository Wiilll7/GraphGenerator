package com.graphgen.core;

public class Viewport {
	
	
	// Atributos
	public final int widthArea;
    public final int heightArea;
    public final int margemX;
    public final int margemY;
    
    public final double totalX;
    public final double totalY;
    public final int origemX;
    public final int origemY;
    
    public final int leftLimit;
    public final int rightLimit;
    public final int upLimit;
    public final int downLimit;
    
    public final double xPixels; 
    public final double yPixels;

    
    // Construtor
    public Viewport(int width, int height, double xMin, double xMax, double yMin, double yMax) {
    	// Calcula a margem e espaco util para desenho
        this.margemX = (int) (width * 0.05);
        this.margemY = (int) (height * 0.05);
        this.widthArea = width - (margemX * 2);
        this.heightArea = height - (margemY * 2);
        
        // Usado na movimentação da origem
        this.totalX = xMax - xMin;
        this.totalY = yMax - yMin;
        double proporcaoZeroX = -xMin / totalX;
        double proporcaoZeroY = yMax / totalY;
        this.origemX = (int) (proporcaoZeroX * widthArea);
        this.origemY = (int) (proporcaoZeroY * heightArea);
        
        // Limites maximos e minimos
        this.leftLimit = -origemX - margemX;
        this.rightLimit = (widthArea - origemX) + margemX;
        this.upLimit = -origemY - margemY;
        this.downLimit = (heightArea - origemY) + margemY;
        
        // Quantidade de pixels para cada eixo
        this.xPixels = widthArea / totalX;
        this.yPixels = heightArea / totalY;
    }
    
}
