import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Graph {

	// Atributos
	private int width;
	private int height;
	private double xMin;
	private double xMax;
	private double yMin;
	private double yMax;
	private BufferedImage imagem;
	private Graphics2D g2d;
	private ArrayList<Function> functions = new ArrayList<Function>();
	
	
	// Construtor
    public Graph(int width, int height, double xMin, double xMax, double yMin, double yMax) {
    	if (xMin == xMax || yMin == yMax) throw new IllegalArgumentException("Os valores de mínimo e máximo não podem ser iguais");
    	if (xMin > xMax) throw new IllegalArgumentException("O valor de X mínimo não pode ser maior que o valor de X máximo");
    	if (yMin > yMax) throw new IllegalArgumentException("O valor de Y mínimo não pode ser maior que o valor de Y máximo");
		this.width = width;
		this.height = height;
		this.xMin = xMin;
		this.xMax = xMax;
		this.yMin = yMin;
		this.yMax = yMax;
		this.imagem = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        this.g2d = getBaseGraph();
	}

    
    // Gera o png do grafico final 
	public void generateGraphPng() {
		
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
    public Graphics2D getBaseGraph(){
    	
    	// Inicializa o Graphics2D e seta algumas configurações de estilos para ele
        g2d = imagem.createGraphics();
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, width, height);
        g2d.setStroke(new BasicStroke(8.0f));
        
        // Aplica uma fonte
        try {
            File fonte = new File("assets/VCR_OSD_MONO.ttf");
            float fontSize = Calculus.min(width, height) * 0.0215f;
            
            if (fontSize < 12f) {
            	fontSize = 12f;
            } 
            
            Font font = Font.createFont(Font.TRUETYPE_FONT, fonte).deriveFont(fontSize);
            g2d.setFont(font);
            g2d.setColor(Color.BLACK);
        } catch(Exception e) {
            e.printStackTrace();
        }
        
        // Desenha o grid e subgrid
        drawSubGrid();
        drawGrid();
        
        return g2d;
    }
    
    // Desenha a funcao em cima do grafico base gerado
    public void drawFunction(Function func) {
    	
    	// Inverte a escala de Y para positivos p/ cima e negativos p/ baixo
        g2d.scale(1.0, -1.0);
        
    	if (func.getFunction() == null) return;
    	
    	g2d.setStroke(new BasicStroke(10.0f));
        g2d.setColor(func.getColor());
    	
    	// Calcula a margem e espaco util para desenho
    	int margemX = (int) (width * 0.05);
        int margemY = (int) (height * 0.05);
        int widthArea = width - (margemX * 2);
        int heightArea = height - (margemY * 2);
    	
        // Usado na movimentação da origem
        double totalX = xMax - xMin;
        double totalY = yMax - yMin;
        double proporcaoZeroX = -xMin / totalX;
        int origemX = (int) (proporcaoZeroX * widthArea);
        
        double pixelsX = widthArea / totalX;
        double pixelsY = heightArea / totalY;
        int startPixelX = -origemX - margemX;
        int endPixelX = (widthArea - origemX) + margemX;
        
        
        for (int pixelX = startPixelX; pixelX < endPixelX - 1; pixelX++) {
            
            double x1 = pixelX / pixelsX;
            double x2 = (pixelX + 1) / pixelsX;
            double y1 = func.function(x1);
            double y2 = func.function(x2);
            
            if (Double.isNaN(y1) || Double.isNaN(y2)) continue;
            
            double pixelY1 = y1 * pixelsY;
            double pixelY2 = y2 * pixelsY;
            
            if (Calculus.abs(pixelY1 - pixelY2) > heightArea * 0.5) {
                if ((y1 > 0 && y2 < 0) || (y1 < 0 && y2 > 0)) {
                    continue; 
                }
            }
            g2d.drawLine(pixelX, (int)pixelY1, pixelX + 1, (int)pixelY2);
        }
        // Inverte a escala de Y para positivos p/ cima e negativos p/ baixo
        g2d.scale(1.0, -1.0);
        functions.add(func.clone());
    }
    
    public void drawSubtitles() {
    	FontMetrics metrics = g2d.getFontMetrics(g2d.getFont());
    	int totalHeight = 0;
    	int maxWidth = 0;
    	
    	for (Function f : functions) {
    		if (maxWidth < metrics.stringWidth(f.getText())) {
    			maxWidth = metrics.stringWidth(f.getText());
    		}
    		totalHeight += metrics.getAscent();
    	}
    	
    	if (maxWidth <= 0) return;
    	
    	int margemX = (int) (width * 0.05);
        int margemY = (int) (height * 0.05);
        int widthArea = width - (margemX * 2);
        int heightArea = height - (margemY * 2);
    	
    	double totalX = xMax - xMin;
        double totalY = yMax - yMin;
        double proporcaoZeroX = -xMin / totalX;
        double proporcaoZeroY = yMax / totalY;
    	int origemX = (int) (proporcaoZeroX * widthArea);
        int origemY = (int) (proporcaoZeroY * heightArea);
    	
        int rightLimit = (widthArea - origemX) + margemX;
        int downLimit = (heightArea - origemY) + margemY;
    	
    	g2d.setColor(Color.lightGray);
    	
    	for (int x = rightLimit - 30; x > (rightLimit - maxWidth - 30); x--) {
    		g2d.drawLine(x, downLimit, x, downLimit - totalHeight);
    	}
    	
    	System.out.println(maxWidth);
    	g2d.setColor(functions.get(0).getColor());
    	g2d.drawString(functions.get(0).getText(), 0, 0);
    }
    
    // Desenha o grid
    public void drawGrid() {
    	
    	// Inicializacao de variaveis usadas nos loops
        FontMetrics metrics = g2d.getFontMetrics(g2d.getFont());
        BasicStroke bsNum = new BasicStroke(8.0f);
        BasicStroke bsGrid = new BasicStroke(4.0f);
        Color gray_color = new Color(89, 87, 88);
        String text;
        int textSize;
        int px;
        
        // Calcula a margem e espaco util para desenho
        int margemX = (int) (width * 0.05);
        int margemY = (int) (height * 0.05);
        int widthArea = width - (margemX * 2);
        int heightArea = height - (margemY * 2);
        
        // Usado na movimentação da origem
        double totalX = xMax - xMin;
        double totalY = yMax - yMin;
        double proporcaoZeroX = -xMin / totalX;
        double proporcaoZeroY = yMax / totalY;
        int origemX = (int) (proporcaoZeroX * widthArea);
        int origemY = (int) (proporcaoZeroY * heightArea);
        
        // Limites maximos e minimos
        int leftLimit = -origemX - margemX;
        int rightLimit = (widthArea - origemX) + margemX;
        int upLimit = -origemY - margemY;
        int downLimit = (heightArea - origemY) + margemY;
        
        double xPixels = widthArea / totalX;
        double yPixels = heightArea / totalY;
        double stepX = totalX / 10.0;
        double stepY = totalY / 10.0;
        
        int alturaTexto = metrics.getHeight();
        int ascentTexto = metrics.getAscent();
        
    	// Calcula e desenha os numeros e grid para X negativo
        for (double x = -stepX; x >= xMin - stepX; x -= stepX) {
        	// Calcula pontos para os numeros de X
        	px = (int) (x * xPixels);
        	
            // Define as cores para o grid e desenha 
            g2d.setStroke(bsGrid);
            g2d.setColor(gray_color);
            g2d.drawLine(px, upLimit, px, downLimit);

            
            // Define o tamanho e as cores para os numeros e desenha
            text = String.format("%.1f", x);
            textSize = metrics.stringWidth(text);
            
        	g2d.setStroke(bsNum);
        	g2d.setColor(Color.BLACK);
        	
            g2d.drawLine(px, 8, px, -8);
            
            // Se o texto corta pra fora da tela ele nao escreve
            int xTexto = px - (textSize / 2);
            if (xTexto < leftLimit + 10) {
            	continue;
            }
            int yTexto = alturaTexto * 3 / 2;
            if (yTexto > downLimit - 5) {
                yTexto = downLimit - alturaTexto - 5;
            } else if (yTexto - ascentTexto < upLimit + 5) {
                yTexto = upLimit + ascentTexto + 5;
            }
            
            g2d.drawString(text, xTexto, yTexto);
        }
        
        // Calcula e desenha os numeros e grid para X positivo
        for (double x = stepX; x <= xMax + stepX; x += stepX) {
        	// Calcula pontos para os numeros de X
        	px = (int) (x * xPixels);
        	
            // Define as cores para o grid e desenha 
            g2d.setStroke(bsGrid);
            g2d.setColor(gray_color);
            g2d.drawLine(px, upLimit, px, downLimit);
            
            // Define o tamanho e as cores para os numeros e desenha
            text = String.format("%.1f", x);
            textSize = metrics.stringWidth(text);
            
        	g2d.setStroke(bsNum);
        	g2d.setColor(Color.BLACK);
        	
            g2d.drawLine(px, 8, px, -8);
            
            // Se o texto corta pra fora da tela ele nao escreve
            int xTexto = px - (textSize / 2);
            if (xTexto + textSize > rightLimit - 10) {
            	continue;
            }
            int yTexto = alturaTexto * 3 / 2;
            if (yTexto > downLimit - 5) {
                yTexto = downLimit - alturaTexto - 5;
            } else if (yTexto - ascentTexto < upLimit + 5) {
                yTexto = upLimit + ascentTexto + 5;
            }
            
            g2d.drawString(text, xTexto, yTexto);
        }
        
        // Calcula e desenha os numeros e grid para Y negativo
        for (double y = -stepY; y >= yMin - stepY; y -= stepY) {
        	// Calcula pontos para os numeros de Y
            px = (int) (y * yPixels);
            
            // Seta o texto para os positivos
            text = String.format("%.1f", y);
            textSize = metrics.stringWidth(text);
            
            // Seta as cores para o grid e desenha 
            g2d.setStroke(bsGrid);
            g2d.setColor(gray_color);
            
            g2d.drawLine(leftLimit, -px, rightLimit, -px);
            
            // Seta as cores para os numeros e desenha
        	g2d.setStroke(bsNum);
        	g2d.setColor(Color.BLACK);
        	
        	g2d.drawLine(8, -px, -8, -px);
            
        	// Se o texto corta pra fora da tela ele nao escreve
        	int xTexto = -textSize - 25;
            if (xTexto < leftLimit + 10) {
                xTexto = leftLimit + 10;
            } else if (xTexto + textSize > rightLimit - 10) {
                xTexto = rightLimit - textSize - 10;
            }
            
            int yTexto = -px + (ascentTexto / 2);
            if (yTexto > downLimit - 5) {
                continue;
            }
            
            g2d.drawString(text, xTexto, yTexto + 20);
        }
        
        // Calcula e desenha os numeros e grid para Y positivo
        for (double y = stepY; y <= yMax + stepY; y += stepY) {
        	// Calcula pontos para os numeros de Y
            px = (int) (y * yPixels);
            
            // Seta o texto para os positivos
            text = String.format("%.1f", y);
            textSize = metrics.stringWidth(text);
            
            // Seta as cores para o grid e desenha 
            g2d.setStroke(bsGrid);
            g2d.setColor(gray_color);
            
            g2d.drawLine(leftLimit, -px, rightLimit, -px);
            
            // Seta as cores para os numeros e desenha
        	g2d.setStroke(bsNum);
        	g2d.setColor(Color.BLACK);
        	
        	g2d.drawLine(8, -px, -8, -px);
            
        	// Se o texto corta pra fora da tela ele nao escreve
        	int xTexto = -textSize - 25;
            if (xTexto < leftLimit + 10) {
                xTexto = leftLimit + 10;
            } else if (xTexto + textSize > rightLimit - 10) {
                xTexto = rightLimit - textSize - 10;
            }
            
            int yTexto = -px + (ascentTexto / 2);
            if (yTexto - ascentTexto < upLimit + 5) {
            	continue;
            }
            
            g2d.drawString(text, xTexto, yTexto + 20);
        }
        
        // Desenha a linha principal X e Y do grafico
        g2d.drawLine(leftLimit, 0, rightLimit, 0); 
        g2d.drawLine(0, upLimit, 0, downLimit);
        
    }
    
    // Desenha o subgrid
    public void drawSubGrid() {
    	
    	// Define a cor e espessura do subgrid
        g2d.setStroke(new BasicStroke(2f));
        g2d.setColor(new Color(220, 220, 220));
        
        // Calcula a margem e espaco util para desenho
        int margemX = (int) (width * 0.05);
        int margemY = (int) (height * 0.05);
        int widthArea = width - (margemX * 2);
        int heightArea = height - (margemY * 2);
        
        // Usado na movimentação da origem
        double totalX = xMax - xMin;
        double totalY = yMax - yMin;
        double proporcaoZeroX = -xMin / totalX;
        double proporcaoZeroY = yMax / totalY;
        int origemX = (int) (proporcaoZeroX * widthArea);
        int origemY = (int) (proporcaoZeroY * heightArea);
        
        // Limites maximos e minimos
        int leftLimit = -origemX - margemX;
        int rightLimit = (widthArea - origemX) + margemX;
        int upLimit = -origemY - margemY;
        int downLimit = (heightArea - origemY) + margemY;
        
        double xPixels = widthArea / totalX;
        double yPixels = heightArea / totalY;
        double stepX = totalX / 10.0;
        double stepY = totalY / 10.0;
        
        double stepSubX = stepX/5.0;

        // Seta o ponto (0, 0) do grafico
        g2d.translate(margemX + origemX, margemY + origemY);
        
        // Calcula e desenha os numeros e grid para X negativo
        for (double x = 0; x >= xMin - stepSubX * 5; x -= stepSubX) {
        	int px = (int) (x * xPixels);
            g2d.drawLine(px, upLimit, px, downLimit);
        }
        
        // Calcula e desenha os numeros e grid para X positivo
        for (double x = stepSubX; x <= xMax + stepSubX * 5; x += stepSubX) {
        	int px = (int) (x * xPixels);
            g2d.drawLine(px, upLimit, px, downLimit);
        }
        
        double stepSubY = stepY/5.0;
        
        // Calcula e desenha os numeros e grid para Y negativo
        for (double y = -stepSubY; y >= yMin - stepSubY * 5; y -= stepSubY) {
        	int py = (int) (y * yPixels);
            g2d.drawLine(leftLimit, -py, rightLimit, -py);
        }
        
        // Calcula e desenha os numeros e grid para Y positivo
        for (double y = stepSubY; y <= yMax + stepSubX * 5; y += stepSubY) {
        	int py = (int) (y * yPixels);
            g2d.drawLine(leftLimit, -py, rightLimit, -py);
        }
    }
    
}