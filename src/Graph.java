import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class Graph {

	private int width;
	private int height;
	
	public Graph(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
    public void generateGraphPng(String filename, Function func, double xMin, double xMax, double yMin, double yMax) {
        BufferedImage imagem = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        
        int margemX = (int) (width * 0.05);
        int margemY = (int) (height * 0.05);
        
        int widthArea = width - (margemX * 2);
        int heightArea = height - (margemY * 2);
        
        Graphics2D g2d = getBaseGraph(imagem, xMin, xMax, yMin, yMax, widthArea, heightArea, margemX, margemY);

        printFunc(g2d, func, xMin, xMax, yMin, yMax, widthArea, heightArea, margemX);
        
        g2d.dispose();

        try {
            File file = new File(filename);
            ImageIO.write(imagem, "png", file);
            System.out.println("Sucesso! Imagem salva em: " + file.getAbsolutePath());
        } catch (Exception e) {
            System.err.println("Erro ao salvar o arquivo: " + e.getMessage());
        }
    }
    
    // Desenha o grafico base (linhas X e Y, escala e grid)
    public Graphics2D getBaseGraph(BufferedImage imagem, double xMin, double xMax, double yMin, double yMax, int widthArea, int heightArea, int margemX, int margemY){
    	
    	// Inicializa o Graphics2D e seta algumas configurações de estilos para ele
        Graphics2D g2d = imagem.createGraphics();
        
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
        
        
        
        // Usado na movimentação da origem
        double totalX = xMax - xMin;
        double totalY = yMax - yMin;
        double proporcaoZeroX = -xMin / totalX;
        double proporcaoZeroY = yMax / totalY;
        int origemX = (int) (proporcaoZeroX * widthArea);
        int origemY = (int) (proporcaoZeroY * heightArea);
        
        // Limites máximos e mínimos
        int leftLimit = -origemX - margemX;
        int rightLimit = (widthArea - origemX) + margemX;
        int upLimit = -origemY - margemY;
        int downLimit = (heightArea - origemY) + margemY;
        
        double xPixels = widthArea / totalX;
        double yPixels = heightArea / totalY;
        double stepX = totalX / 10.0;
        double stepY = totalY / 10.0;
        
        
        // Seta o ponto (0, 0) do grafico
        g2d.translate(margemX + origemX, margemY + origemY);
        
        // Desenha o grid e subgrid
        drawSubGrid(g2d, stepX, stepY, xMax, xMin, yMax, yMin, xPixels, yPixels, upLimit, downLimit, leftLimit, rightLimit);
        drawGrid(g2d, xMax, xMin, yMax, yMin, xPixels, yPixels, stepX, stepY, upLimit, downLimit, leftLimit, rightLimit);
        
        
        // Desenha a linha principal X e Y do grafico
        g2d.drawLine(leftLimit, 0, rightLimit, 0); 
        g2d.drawLine(0, upLimit, 0, downLimit);
        
        // Inverte a escala de Y para positivos p/ cima e negativos p/ baixo
        g2d.scale(1.0, -1.0);
        
        return g2d;
    }
    
    // Desenha a funcao em cima do grafico base gerado
    public void printFunc(Graphics2D g2d, Function func, double xMin, double xMax, double yMin, double yMax, int widthArea, int heightArea, int margemX) {
    	g2d.setStroke(new BasicStroke(10.0f));
    	
        double totalX = xMax - xMin;
        double totalY = yMax - yMin;
        double pixelsX = widthArea / totalX;
        double pixelsY = heightArea / totalY;

        double proporcaoZeroX = -xMin / totalX;
        double proporcaoZeroY = yMax / totalY;
        int origemX = (int) (proporcaoZeroX * widthArea);
        int origemY = (int) (proporcaoZeroY * heightArea);
        
        int startPixelX = -origemX - margemX;
        int endPixelX = (widthArea - origemX) + margemX;
        
        g2d.setColor(new Color(173, 33, 52));
        
        
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

    
    
}
    
    
    // Desenha o grid
    public void drawGrid(
    		Graphics2D g2d,
    		double xMax, double xMin,
    		double yMax, double yMin,
    		double xPixels, double yPixels,
    		double stepX, double stepY,
    		int upLimit, int downLimit,
    		int leftLimit, int rightLimit
    	) {
    	
    	// Inicializacao de variaveis usadas nos loops
        FontMetrics metrics = g2d.getFontMetrics(g2d.getFont());
        BasicStroke bsNum = new BasicStroke(8.0f);
        BasicStroke bsGrid = new BasicStroke(4.0f);
        Color gray_color = new Color(89, 87, 88);
        String text;
        int textSize;
        int px;
        
    	// Calcula e desenha os numeros e grid para X negativo
        for (double x = 0; x >= xMin - stepX; x -= stepX) {
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
            g2d.drawString(text, px - (textSize / 2), metrics.getHeight()*3/2);
        }
        
        // Calcula e desenha os numeros e grid para X positivo
        for (double x = stepX; x <= xMax; x += stepX) {
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
            g2d.drawString(text, px - (textSize / 2), metrics.getHeight()*3/2);
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
        	
        	g2d.drawLine(8, -px, -8, -px); // Positivo
            g2d.drawString(text, -textSize - 25, -px + (metrics.getAscent()));
        }
        
        // Calcula e desenha os numeros e grid para Y positivo
        for (double y = stepY; y <= yMax; y += stepY) {
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
        	
        	g2d.drawLine(8, -px, -8, -px); // Positivo
            g2d.drawString(text, -textSize - 25, -px + (metrics.getAscent()));
        }
        
    }
    
    // Desenha o subgrid
    public void drawSubGrid(
    		Graphics2D g2d,
    		double stepX, double stepY,
    		double xMax, double xMin,
    		double yMax, double yMin,
    		double xPixels, double yPixels,
    		int upLimit, int downLimit, int leftLimit, int rightLimit) {
    	// Define a cor e espessura do subgrid
        g2d.setStroke(new BasicStroke(2f));
        g2d.setColor(new Color(220, 220, 220));
        double stepSubX = stepX/5.0;

        
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