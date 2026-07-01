package com.graphgen.api;

import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.graphgen.core.Viewport;
import com.graphgen.style.Theme;
import com.graphgen.util.Calculus;

public class Graph {

	// Atributos
	private ArrayList<Function> functions = new ArrayList<Function>();
	private Theme theme;
	private BufferedImage imagem;
	private Viewport vp;
	private Graphics2D g2d;
	private int width;
	private int height;
	private double xMin;
	private double xMax;
	private double yMin;
	private double yMax;
	
	
	// Construtor
    public Graph(int width, int height, double xMin, double xMax, double yMin, double yMax) {
    	if (xMin == xMax || yMin == yMax) throw new IllegalArgumentException("Os valores de mínimo e máximo não podem ser iguais");
    	if (xMin > xMax) throw new IllegalArgumentException("O valor de X mínimo não pode ser maior que o valor de X máximo");
    	if (yMin > yMax) throw new IllegalArgumentException("O valor de Y mínimo não pode ser maior que o valor de Y máximo");
    	this.theme = Theme.WHITE_MODE;
		this.width = width;
		this.height = height;
		this.xMin = xMin;
		this.xMax = xMax;
		this.yMin = yMin;
		this.yMax = yMax;
        this.vp = new Viewport(width, height, xMin, xMax, yMin, yMax);
		this.imagem = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
	}

    // Define o tema (precisa ser definido antes de desenhar no grafico)
    public void setTheme(Theme theme) {
    	this.theme = theme;
    }
    
    // Gera o png do grafico final e limpa o g2d
	public void generateGraphPng() {
		
		if (g2d == null) {
	        g2d = getBaseGraph();
		}
		
        g2d.dispose();

        try {
            File file = new File("Grafico.png");
            ImageIO.write(imagem, "png", file);
            System.out.println("Sucesso! Imagem salva em: " + file.getAbsolutePath());
        } catch (Exception e) {
            System.err.println("Erro ao salvar o arquivo: " + e.getMessage());
        }
    }
    
    // Desenha o grafico base (linhas X e Y, escala e grid)
    private Graphics2D getBaseGraph(){
    	
    	// Inicializa o Graphics2D e seta algumas configurações de estilos para ele
        g2d = imagem.createGraphics();
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        
        g2d.setColor(theme.getBackground());
        g2d.fillRect(0, 0, width, height);
        
        // Aplica uma fonte
        try {
        	InputStream fonte = getClass().getResourceAsStream("/assets/vhs-gothic.ttf");
        	
            float fontSize = Calculus.min(width, height) * 0.0215f;
            
            if (fontSize < 12f) {
            	fontSize = 12f;
            } 
            
            Font font = Font.createFont(Font.TRUETYPE_FONT, fonte).deriveFont(fontSize);
            g2d.setFont(font);
            fonte.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
        
        drawSubGrid();
        drawGrid();
        
        return g2d;
    }
    
    // Desenha a funcao em cima do grafico base gerado
    public void drawFunction(Function func) {
    	
    	if (g2d == null) {
	        g2d = getBaseGraph();
		}
    	
    	for (Function f : functions) {
    		if (f.getFunction() == func.getFunction()) {
    			return;
    		}
    	}
    	
    	// Inverte a escala de Y para positivos p/ cima e negativos p/ baixo
        g2d.scale(1.0, -1.0);
        
    	if (func.getFunction() == null) return;
    	
    	// Define espessura e cor da linha
    	float tamLinha = (float) (Calculus.sqrt(vp.heightArea * vp.widthArea) * 0.005);
    	
    	if (tamLinha < 4) {
    		tamLinha = 4;
    	}
    	
    	if (func.getColor() == null) {
    		g2d.setColor(theme.getDefaultFunction());
    	} else {
            g2d.setColor(func.getColor());
    	}
    	
    	g2d.setStroke(new BasicStroke(tamLinha));
        
        int startPixelX = -vp.origemX - vp.margemX;
        int endPixelX = (vp.widthArea - vp.origemX) + vp.margemX;
        
        // Desenha a funcao
        for (int pixelX = startPixelX; pixelX < endPixelX - 1; pixelX++) {
            
            double x1 = pixelX / vp.xPixels;
            double x2 = (pixelX + 1) / vp.xPixels;
            double y1 = func.function(x1);
            double y2 = func.function(x2);
            
            if (Double.isNaN(y1) || Double.isNaN(y2)) continue;
            
            double pixelY1 = y1 * vp.yPixels;
            double pixelY2 = y2 * vp.yPixels;
            
            if (Calculus.abs(pixelY1 - pixelY2) > vp.heightArea * 0.5) {
                if ((y1 > 0 && y2 < 0) || (y1 < 0 && y2 > 0)) {
                    continue; 
                }
            }
            g2d.drawLine(pixelX, (int)pixelY1, pixelX + 1, (int)pixelY2);
        }
        
        // Reverte a escala de Y
        g2d.scale(1.0, -1.0);
        functions.add(func.clone());
    }
    
    // Desenha a leganda no grafico | se nenhuma funcao tiver apenas pula
    public void drawSubtitles() {
    	
    	if (g2d == null) {
	        g2d = getBaseGraph();
		}
    	
    	// Verifica se tem algum texto de legenda definido nas funcoes
    	FontMetrics metrics = g2d.getFontMetrics(g2d.getFont());
    	int totalHeight = 0;
    	int maxTextWidth = 0;
    	
    	for (Function f : functions) {
    		if (maxTextWidth < metrics.stringWidth(f.getText())) {
    			maxTextWidth = metrics.stringWidth(f.getText());
    		}
    		if (!f.getText().equals("")) {
    			totalHeight += metrics.getHeight() + (int)(vp.heightArea * 0.0036d);
    		}
    	}
    	
    	if (maxTextWidth <= 0) return;
    	
    	// Calculos de proporcao para desenhar o painel das legendas
    	int squareSize = (int) (metrics.getHeight() * 0.6);
    	int gap = (int) (vp.widthArea * 0.004);
    	int padding = (int) (Calculus.sqrt(vp.widthArea * vp.heightArea) * 0.005);
    	
    	int boxWidth = maxTextWidth + squareSize + gap + (padding * 2);
    	int boxHeight = totalHeight + padding; 
    	
    	g2d.setColor(theme.getSubtitleBackground());
    	
    	int margin = (int) (vp.widthArea * 0.018);
    	
    	// Calculos de proporcao para escrever as legendas
    	int boxRight = vp.rightLimit - margin;
    	int boxLeft = boxRight - boxWidth;
    	int boxBottom = vp.downLimit - margin;
    	int boxTop = boxBottom - boxHeight;
    	
    	g2d.fillRect(boxLeft, boxTop, boxWidth, boxHeight);
    	
    	int stepY = padding + metrics.getAscent(); 
    	
    	// Escreve as legendas
    	for (Function f : functions) {
    		if (!f.getText().equals("")) {
    			if (f.getColor() == null) {
    	    		g2d.setColor(theme.getDefaultFunction());
    	    	} else {
    	            g2d.setColor(f.getColor());
    	    	}
	    		
	    		int sqX = boxLeft + padding;
	    		int sqY = boxTop + stepY - metrics.getAscent() + ((metrics.getAscent() - squareSize)/2);
	    		
	    		g2d.fillRect(sqX, sqY, squareSize, squareSize);
	    		
	    		int textX = sqX + squareSize + gap;
	    		g2d.drawString(f.getText(), textX, boxTop + stepY);
	    		
	    		stepY += metrics.getHeight() + (int)(vp.heightArea * 0.0036d);
    		}
    	}
    }
    
    // Desenha o grid
    private void drawGrid() {
    	
    	// Inicializacao de variaveis usadas nos loops
        FontMetrics metrics = g2d.getFontMetrics(g2d.getFont());
        float tamNum = (float) (Calculus.sqrt(vp.heightArea * vp.widthArea) * 0.004);
        float tamGrid = (float) (Calculus.sqrt(vp.heightArea * vp.widthArea) * 0.002);
        BasicStroke bsNum = new BasicStroke(tamNum);
        BasicStroke bsGrid = new BasicStroke(tamGrid);
        String text;
        int textSize;
        int px;
        
        double stepX = vp.totalX / 10.0;
        double stepY = vp.totalY / 10.0;
        
        int alturaTexto = metrics.getHeight();
        int ascentTexto = metrics.getAscent();
        
    	// Calcula e desenha os numeros e grid para X negativo
        for (double x = -stepX; x >= xMin - stepX; x -= stepX) {
        	// Calcula pontos para os numeros de X
        	px = (int) (x * vp.xPixels);
        	
            // Define as cores para o grid e desenha 
            g2d.setStroke(bsGrid);
            g2d.setColor(theme.getGridPrincipal());
            g2d.drawLine(px, vp.upLimit, px, vp.downLimit);

            
            // Define o tamanho e as cores para os numeros e desenha
            text = String.format("%.1f", x);
            textSize = metrics.stringWidth(text);
            
        	g2d.setStroke(bsNum);
        	g2d.setColor(theme.getNumbers());
        	
            g2d.drawLine(px, (int) (vp.widthArea * 0.005), px, -(int) (vp.widthArea * 0.005));
            
            // Se o texto corta pra fora da tela ele nao escreve
            int xTexto = px - (textSize / 2);
            if (xTexto < vp.leftLimit + 10) {
            	continue;
            }
            int yTexto = alturaTexto * 3 / 2;
            if (yTexto > vp.downLimit - 5) {
                yTexto = vp.downLimit - alturaTexto - 5;
            } else if (yTexto - ascentTexto < vp.upLimit + 5) {
                yTexto = vp.upLimit + ascentTexto + 5;
            }
            
            g2d.drawString(text, xTexto, yTexto);
        }
        
        // Calcula e desenha os numeros e grid para X positivo
        for (double x = stepX; x <= xMax + stepX; x += stepX) {
        	// Calcula pontos para os numeros de X
        	px = (int) (x * vp.xPixels);
        	
            // Define as cores para o grid e desenha 
            g2d.setStroke(bsGrid);
            g2d.setColor(theme.getGridPrincipal());
            g2d.drawLine(px, vp.upLimit, px, vp.downLimit);
            
            // Define o tamanho e as cores para os numeros e desenha
            text = String.format("%.1f", x);
            textSize = metrics.stringWidth(text);
            
        	g2d.setStroke(bsNum);
        	g2d.setColor(theme.getNumbers());
        	
            g2d.drawLine(px, (int) (vp.widthArea * 0.005), px, -(int) (vp.widthArea * 0.005));
            
            // Se o texto corta pra fora da tela ele nao escreve
            int xTexto = px - (textSize / 2);
            if (xTexto + textSize > vp.rightLimit - 10) {
            	continue;
            }
            int yTexto = alturaTexto * 3 / 2;
            if (yTexto > vp.downLimit - 5) {
                yTexto = vp.downLimit - alturaTexto - 5;
            } else if (yTexto - ascentTexto < vp.upLimit + 5) {
                yTexto = vp.upLimit + ascentTexto + 5;
            }
            
            g2d.drawString(text, xTexto, yTexto);
        }
        
        // Calcula e desenha os numeros e grid para Y negativo
        for (double y = -stepY; y >= yMin - stepY; y -= stepY) {
        	// Calcula pontos para os numeros de Y
            px = (int) (y * vp.yPixels);
            
            // Define o texto para os positivos
            text = String.format("%.1f", y);
            textSize = metrics.stringWidth(text);
            
            // Define as cores para o grid e desenha 
            g2d.setStroke(bsGrid);
            g2d.setColor(theme.getGridPrincipal());
            
            g2d.drawLine(vp.leftLimit, -px, vp.rightLimit, -px);
            
            // Define as cores para os numeros e desenha
        	g2d.setStroke(bsNum);
        	g2d.setColor(theme.getNumbers());
        	
        	g2d.drawLine((int) (vp.heightArea * 0.005), -px, -(int) (vp.heightArea * 0.005), -px);
            
        	// Se o texto corta pra fora da tela ele nao escreve
        	int xTexto = -textSize - (int) (vp.widthArea * 0.013);
            if (xTexto < vp.leftLimit + 10) {
                xTexto = vp.leftLimit + 10;
            } else if (xTexto + textSize > vp.rightLimit - 10) {
                xTexto = vp.rightLimit - textSize - 10;
            }
            
            int yTexto = -px + (ascentTexto / 2);
            if (yTexto > vp.downLimit - 5) {
                continue;
            }
            
            g2d.drawString(text, xTexto, yTexto + (int) (vp.heightArea*0.01));
        }
        
        // Calcula e desenha os numeros e grid para Y positivo
        for (double y = stepY; y <= yMax + stepY; y += stepY) {
        	// Calcula pontos para os numeros de Y
            px = (int) (y * vp.yPixels);
            
            // Define o texto para os positivos
            text = String.format("%.1f", y);
            textSize = metrics.stringWidth(text);
            
            // Define as cores para o grid e desenha 
            g2d.setStroke(bsGrid);
            g2d.setColor(theme.getGridPrincipal());
            
            g2d.drawLine(vp.leftLimit, -px, vp.rightLimit, -px);
            
            // Define as cores para os numeros e desenha
        	g2d.setStroke(bsNum);
        	g2d.setColor(theme.getNumbers());
        	
        	g2d.drawLine((int) (vp.heightArea * 0.005), -px, -(int) (vp.heightArea * 0.005), -px);
            
        	// Se o texto corta pra fora da tela ele nao escreve
        	int xTexto = -textSize - (int) (vp.widthArea * 0.013);
            if (xTexto < vp.leftLimit + 10) {
                xTexto = vp.leftLimit + 10;
            } else if (xTexto + textSize > vp.rightLimit - 10) {
                xTexto = vp.rightLimit - textSize - 10;
            }
            
            int yTexto = -px + (ascentTexto / 2);
            if (yTexto - ascentTexto < vp.upLimit + 5) {
            	continue;
            }
            
            g2d.drawString(text, xTexto, yTexto + (int) (vp.heightArea*0.01));
        }
        
        g2d.setColor(theme.getAxisXY());
        
        // Desenha a linha principal X e Y do grafico
        g2d.drawLine(vp.leftLimit, 0, vp.rightLimit, 0); 
        g2d.drawLine(0, vp.upLimit, 0, vp.downLimit);
        
    }
    
    // Desenha o subgrid
    private void drawSubGrid() {
    	
    	// Define a cor e espessura do subgrid
    	float numSubgrid = (float) (Calculus.sqrt(vp.heightArea * vp.widthArea) * 0.001);
        g2d.setStroke(new BasicStroke(numSubgrid));
        g2d.setColor(theme.getSubGrid());
        
        double stepX = vp.totalX / 10.0;
        double stepY = vp.totalY / 10.0;
        
        double stepSubX = stepX/5.0;

        // Seta o ponto (0, 0) do grafico
        g2d.translate(vp.margemX + vp.origemX, vp.margemY + vp.origemY);
        
        // Calcula e desenha os numeros e grid para X negativo
        for (double x = 0; x >= xMin - stepSubX * 5; x -= stepSubX) {
        	int px = (int) (x * vp.xPixels);
            g2d.drawLine(px, vp.upLimit, px, vp.downLimit);
        }
        
        // Calcula e desenha os numeros e grid para X positivo
        for (double x = stepSubX; x <= xMax + stepSubX * 5; x += stepSubX) {
        	int px = (int) (x * vp.xPixels);
            g2d.drawLine(px, vp.upLimit, px, vp.downLimit);
        }
        
        double stepSubY = stepY/5.0;
        
        // Calcula e desenha os numeros e grid para Y negativo
        for (double y = -stepSubY; y >= yMin - stepSubY * 5; y -= stepSubY) {
        	int py = (int) (y * vp.yPixels);
            g2d.drawLine(vp.leftLimit, -py, vp.rightLimit, -py);
        }
        
        // Calcula e desenha os numeros e grid para Y positivo
        for (double y = stepSubY; y <= yMax + stepSubX * 5; y += stepSubY) {
        	int py = (int) (y * vp.yPixels);
            g2d.drawLine(vp.leftLimit, -py, vp.rightLimit, -py);
        }
    }
    
}