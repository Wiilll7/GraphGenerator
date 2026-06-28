import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Graph {

	private int width;
	private int height;
	
	
	public Graph(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
    public void generateGraphPng(String filename, Function func) {
    	BufferedImage imagem = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    	
    	Graphics2D g2d = getBaseGraph(imagem);

        printFunc(g2d, func);
        
        g2d.dispose();

        try {
            File arquivoSaida = new File(filename);
            ImageIO.write(imagem, "png", arquivoSaida);
            System.out.println("Sucesso! Imagem salva em: " + arquivoSaida.getAbsolutePath());
        } catch (Exception e) {
            System.err.println("Erro ao salvar o arquivo: " + e.getMessage());
        }
    }
    
    public Graphics2D getBaseGraph(BufferedImage imagem){
    	Graphics2D g2d = imagem.createGraphics();
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, width, height);
        
        g2d.setStroke(new BasicStroke(5.0f));
        
        try {
	        File fonte = new File("assets/VCR_OSD_MONO.ttf");
	        Font font = Font.createFont(Font.TRUETYPE_FONT, fonte).deriveFont(50f);
	        
	        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(font);
	        
	        g2d.setFont(font);
	        g2d.setColor(Color.BLACK);
	        
        } catch(Exception e) {
        	e.printStackTrace();
        }
        
        g2d.translate(width/2, height/2);
        g2d.scale(1.0, -1.0);
        
        g2d.drawLine(-width/2, 0, width/2, 0);
        g2d.drawLine(0, -height/2, 0, height/2);
        
        return g2d;
    }
    
    public void printFunc(Graphics2D g2d, Function func) {
    	double xMin = -10.0;
        double xMax = 10.0;
        double yMin = -10.0;
        double yMax = 10.0;
        
        ArrayList<Integer> listStepNeg = new ArrayList<Integer>();
    	ArrayList<Integer> listStepPos = new ArrayList<Integer>();
    	
        double stepNeg = xMin/5;
        double stepPos = xMax/5;
        
        for (int i = 0; i > xMin; i += stepNeg) {
        	listStepNeg.add(i);
        }
        for (int i = 0; i < xMax; i += stepPos) {
        	listStepPos.add(i);
        }
        
        double scaleX = (width / 2.0) / xMax;
        double scaleY = (height / 2.0) / yMax;

        double step = (xMax - xMin) / width;

        int halfWidth = width / 2;
        
        for (int pixelX = -halfWidth; pixelX < halfWidth - 1; pixelX++) {
            
            double x1 = pixelX * step;
            double x2 = (pixelX + 1) * step;

            double y1 = func.function(x1);
            double y2 = func.function(x2);

            if (Double.isNaN(y1) || Double.isNaN(y2)) continue;

            if (listStepNeg.contains(x1) || listStepPos.contains(x1)) {
            	g2d.drawLine(pixelX, 4, pixelX, -4);
            }
            
            double pixelY1 = y1 * scaleY;
            double pixelY2 = y2 * scaleY;

            if (Calculus.abs(pixelY1 - pixelY2) < height * 0.8) {
                g2d.drawLine(pixelX, (int)pixelY1, pixelX + 1, (int)pixelY2);
            }
        }
    }
}