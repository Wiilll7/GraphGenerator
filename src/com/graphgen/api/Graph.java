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

/**
 * Classe principal do framework responsável por gerenciar a área de desenho,
 * processar as funções matemáticas e gerar a imagem final do gráfico.
 * Esta classe utiliza o Viewport para cálculos de proporção, o Theme para estilização
 * e a renderização de pixels via Java 2D API.
 * @author Willian Moretti
 * @version 1.0
 */
public class Graph {


	/** Lista que armazena todas as funções adicionadas ao gráfico para renderização e legenda. */
	private ArrayList<Function> functions = new ArrayList<Function>();
	
	/** O tema visual atual aplicado ao gráfico. */
	private Theme theme;
	
	/** O buffer de imagem na memória onde os pixels do gráfico são desenhados. */
	private BufferedImage imagem;
	
	/** O gerenciador de mapeamento entre coordenadas matemáticas e pixels da tela. */
	private Viewport vp;
	
	/** O contexto gráfico 2D utilizado para desenhar formas e textos na imagem. */
	private Graphics2D g2d;
	
	/** A largura total da imagem gerada em pixels. */
	private int width;
	
	/** A altura total da imagem gerada em pixels. */
	private int height;
	
	/** O limite mínimo do eixo X no plano cartesiano. */
	private double xMin;
	
	/** O limite máximo do eixo X no plano cartesiano. */
	private double xMax;
	
	/** O limite mínimo do eixo Y no plano cartesiano. */
	private double yMin;
	
	/** O limite máximo do eixo Y no plano cartesiano. */
	private double yMax;
	

	/**
	 * Construtor principal da classe Graph. Inicializa a imagem em branco e o mapeamento da tela 
	 * com base nas dimensões físicas e nos limites matemáticos fornecidos.
	 * @param width A largura total da imagem gerada em pixels.
	 * @param height A altura total da imagem gerada em pixels.
	 * @param xMin O valor mínimo do eixo X no plano cartesiano.
	 * @param xMax O valor máximo do eixo X no plano cartesiano.
	 * @param yMin O valor mínimo do eixo Y no plano cartesiano.
	 * @param yMax O valor máximo do eixo Y no plano cartesiano.
	 * @throws IllegalArgumentException Se os valores mínimos e máximos forem iguais, ou se o mínimo for maior que o máximo.
	 */
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

    /**
     * Define o tema visual do gráfico.
     * Recomenda-se chamar este método logo após a criação do objeto Graph, antes de desenhar as funções.
     * @param theme O objeto Theme contendo a paleta de cores desejada.
     */
    public void setTheme(Theme theme) {
    	this.theme = theme;
    }
    
    /**
     * Finaliza os desenhos e exporta a imagem do gráfico para um arquivo físico.
     * O arquivo será salvo automaticamente com o nome "Grafico.png" na pasta raiz de execução do projeto.
     * Libera os recursos de memória do Graphics2D após a geração.
     */
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
    
    /**
     * Adiciona e renderiza uma nova função matemática sobre o gráfico.
     * A função é avaliada pixel a pixel da esquerda para a direita, ignorando automaticamente 
     * saltos abruptos, como assíntotas verticais. A função desenhada será clonada e salva internamente 
     * para geração futura da legenda.
     * @param func O objeto Function contendo a expressão matemática e suas propriedades visuais.
     */
    public void drawFunction(Function func) {
    	
    	if (g2d == null) {
	        g2d = getBaseGraph();
		}
    	
    	for (Function f : functions) {
    		if (f.getFunction() == func.getFunction()) {
    			return;
    		}
    	}
    	
    	// Inverte a escala de Y para positivos para cima e negativos para baixo
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
    
    /**
     * Desenha a caixa de legendas flutuante no canto inferior direito do gráfico.
     * A legenda irá listar a cor e o texto de todas as funções que foram adicionadas 
     * através do método {@code drawFunction}. Se nenhuma função possuir texto definido, a legenda não será gerada.
     */
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

    /**
     * Inicializa o contexto gráfico 2D, aplica as configurações de anti-aliasing 
     * e desenha os elementos de fundo do gráfico, cor de fundo, malhas de grade e eixos.
     * Este método é de uso estritamente interno e serve como preparação da "tela" antes do desenho das funções.
     * @return O objeto Graphics2D configurado e pronto para uso.
     */
    private Graphics2D getBaseGraph(){
    	
    	// Inicializa o Graphics2D e define algumas configurações de estilos para ele
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
    
    /**
     * Calcula e desenha a grade principal do gráfico e a 
     * numeração ao longo dos eixos X e Y. A escala numérica é calculada automaticamente baseada 
     * nos limites da tela definidos no Viewport e no estilo definido pelo Theme.
     */
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
    
    /**
     * Calcula e desenha a sub-grade do gráfico que ficam entre as marcações principais da grade.
     */
    private void drawSubGrid() {
    	
    	// Define a cor e espessura do subgrid
    	float numSubgrid = (float) (Calculus.sqrt(vp.heightArea * vp.widthArea) * 0.001);
        g2d.setStroke(new BasicStroke(numSubgrid));
        g2d.setColor(theme.getSubGrid());
        
        double stepX = vp.totalX / 10.0;
        double stepY = vp.totalY / 10.0;
        
        double stepSubX = stepX/5.0;

        // Define o ponto (0, 0) do grafico
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