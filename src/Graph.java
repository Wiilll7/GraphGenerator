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
	
    public void generateGraphPng(String filename, Function func, double xMin, double xMax) {
        BufferedImage imagem = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        
        int margemX = (int) (width * 0.05);
        int margemY = (int) (height * 0.05);
        
        int widthArea = width - (margemX * 2);
        int heightArea = height - (margemY * 2);
        
        Graphics2D g2d = getBaseGraph(imagem, xMin, xMax, widthArea, heightArea, margemX, margemY);

        printFunc(g2d, func, xMin, xMax, widthArea, heightArea, margemX);
        
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
    public Graphics2D getBaseGraph(BufferedImage imagem, double xMin, double xMax, int widthArea, int heightArea, int margemX, int margemY){
    	
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
        
        // Faz os calculos de proporcao necessarios para desenhar o grafico base
        double totalX = xMax - xMin;
        double proporcaoZero = -xMin / totalX;
        int origemX = (int) (proporcaoZero * widthArea);
        int origemY = heightArea / 2; 
        int leftLimit = -origemX - margemX;
        int rightLimit = (widthArea - origemX) + margemX;
        int upLimit = -origemY - margemY;
        int downLimit = (heightArea - origemY) + margemY;
        double xPixels = widthArea / totalX;
        double yPixels = heightArea / totalX;
        double step = totalX / 10.0; 
        
        // Seta o ponto (0, 0) do grafico
        g2d.translate(margemX + origemX, margemY + origemY);
        
        // Desenha o grid e subgrid
        drawSubGrid(g2d, step, xMax, xMin, xPixels, yPixels, upLimit, downLimit, leftLimit, rightLimit);
        drawGrid(g2d, step, xMax, xMin, xPixels, yPixels, upLimit, downLimit, leftLimit, rightLimit);
        
        // Desenha a linha principal X e Y do grafico
        g2d.drawLine(leftLimit, 0, rightLimit, 0); 
        g2d.drawLine(0, upLimit, 0, downLimit);
        
        // Inverte a escala de Y para positivos p/ cima e negativos p/ baixo
        g2d.scale(1.0, -1.0);
        
        return g2d;
    }
    
    // Desenha a funcao em cima do grafico base gerado
    public void printFunc(Graphics2D g2d, Function func, double xMin, double xMax, int widthArea, int heightArea, int margemX) {
    	g2d.setStroke(new BasicStroke(10.0f));
    	
        double totalX = xMax - xMin;
        double pixels = widthArea / totalX;

        double proporcaoZero = -xMin / totalX; 
        int origemX = (int) (proporcaoZero * widthArea);
        
        int startPixelX = -origemX - margemX;
        int endPixelX = (widthArea - origemX) + margemX;
        
        g2d.setColor(new Color(173, 33, 52));
        
        for (int pixelX = startPixelX; pixelX < endPixelX - 1; pixelX++) {
            
            double x1 = pixelX / pixels;
            double x2 = (pixelX + 1) / pixels;

            double y1 = func.function(x1);
            double y2 = func.function(x2);

            if (!Double.isNaN(y1) && Double.isNaN(y2)) {
                g2d.drawLine(pixelX, (int)(y1 * pixels), pixelX + 1, 0); 
                continue;
            }
            if (Double.isNaN(y1) && !Double.isNaN(y2)) {
                g2d.drawLine(pixelX, 0, pixelX + 1, (int)(y2 * pixels)); 
                continue;
            }
            if (Double.isNaN(y1) || Double.isNaN(y2)) continue;
            
            double pixelY1 = y1 * pixels;
            double pixelY2 = y2 * pixels;
            
            if (Calculus.abs(pixelY1 - pixelY2) > heightArea * 0.5) {
                if ((y1 > 0 && y2 < 0) || (y1 < 0 && y2 > 0)) {
                    continue; 
                }
            }
            g2d.drawLine(pixelX, (int)pixelY1, pixelX + 1, (int)pixelY2);
        }
    }
    
    // Desenha o grid
    public void drawGrid(Graphics2D g2d, double step, double xMax, double xMin, double xPixels, double yPixels,
    		int upLimit, int downLimit, int leftLimit, int rightLimit) {
    	
    	// Inicializacao de variaveis usadas nos loops
        FontMetrics metrics = g2d.getFontMetrics(g2d.getFont());
        BasicStroke bsNum = new BasicStroke(8.0f);
        BasicStroke bsGrid = new BasicStroke(4.0f);
        String textPos;
        String textNeg;
        int textSizePos;
        int textSizeNeg;
        int px;
        
    	// Calcula e desenha os numeros e grid para X positivos
        for (double x = step; x <= xMax; x += step) {
        	// Calcula pontos para os numeros de X positivos
        	px = (int) (x * xPixels);
        	
        	// Seta o texto para os positivos
            textPos = String.format("%.1f", x);
            textSizePos = metrics.stringWidth(textPos);
            
            // Seta o texto para os negativos
            textNeg = "-"+String.format("%.1f", x);
            textSizeNeg = metrics.stringWidth(textNeg);
        	
            // Seta as cores para o grid e desenha 
            g2d.setStroke(bsGrid);
            g2d.setColor(new Color(89, 87, 88));
            
            g2d.drawLine(px, upLimit, px, downLimit); // Positivo
            g2d.drawLine(-px, upLimit, -px, downLimit); // Negativo
            
            // Seta as cores para os numeros e desenha
        	g2d.setStroke(bsNum);
        	g2d.setColor(Color.BLACK);
        	
            g2d.drawLine(px, 8, px, -8); // Positivo
            g2d.drawString(textPos, px - (textSizePos / 2), metrics.getHeight()*3/2);
            
            g2d.drawLine(-px, 8, -px, -8); // Negativo
            g2d.drawString(textNeg, -px - (textSizeNeg + metrics.stringWidth("-"))/2, metrics.getHeight()*3/2);
        }
        
        // Calcula e desenha os numeros e grid para Y
        for (double x = step; x <= xMax; x += step) {
        	// Calcula pontos para os numeros de Y
            px = (int) (x * yPixels);
            
            // Seta o texto para os positivos
            textPos = String.format("%.1f", x);
            textSizePos = metrics.stringWidth(textPos);
            
            // Seta o texto para os negativos
            textNeg = "-"+String.format("%.1f", x);
            textSizeNeg = metrics.stringWidth(textNeg);
            
            
            // Seta as cores para o grid e desenha 
            g2d.setStroke(bsGrid);
            g2d.setColor(new Color(89, 87, 88));
            
            g2d.drawLine(leftLimit, -px, rightLimit, -px); // Positivo
            g2d.drawLine(leftLimit, px, rightLimit, px); // Negativo
            
            
            // Seta as cores para os numeros e desenha
        	g2d.setStroke(bsNum);
        	g2d.setColor(Color.BLACK);
        	
        	g2d.drawLine(8, -px, -8, -px); // Positivo
            g2d.drawString(textPos, -textSizePos - 25, -px + (metrics.getAscent() / 2));
            
            g2d.drawLine(8, px, -8, px); // Negativo
            g2d.drawString(textNeg, -textSizeNeg - 25, px + (metrics.getAscent() / 2));
        }
    }
    
    // Desenha o subgrid
    public void drawSubGrid(Graphics2D g2d, double step, double xMax, double xMin, double xPixels, double yPixels,
    		int upLimit, int downLimit, int leftLimit, int rightLimit) {
    	// Define a cor e espessura do subgrid
        g2d.setStroke(new BasicStroke(2f));
        g2d.setColor(new Color(220, 220, 220));
        double stepSubGrid = step / 5.0;

        // Faz o subgrid para o X
        for (double x = stepSubGrid; x <= xMax; x += stepSubGrid) {
            int px = (int) (x * xPixels);
            g2d.drawLine(px, upLimit, px, downLimit); // Positivo
            g2d.drawLine(-px, upLimit, -px, downLimit); // Negativo
        }
        // Faz o subgrid para o Y
        for (double x = stepSubGrid; x <= xMax; x += stepSubGrid) {
            int py = (int) (x * yPixels);
            g2d.drawLine(leftLimit, -py, rightLimit, -py); // Positivo
            g2d.drawLine(leftLimit, py, rightLimit, py); // Negativo
        }
    }
    
    
}