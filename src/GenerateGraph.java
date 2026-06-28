import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class GenerateGraph {

	
    public static void gerarPng(int width, int height, String filename, Function func) {

    	BufferedImage imagem = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    	
    	Graphics2D g2d = getBaseGraph(width, height, imagem);

        int domain_min = -10;
        int domain_max = 10;

        int range_min = -10;
        int range_max = 10;

        double domain_step = (double) (domain_max - domain_min)/width;
        
        for (int x = 0; x < width; x++) {
            double y1 = func.function(x * domain_step + domain_min);
            double y2 = func.function((x+1) * domain_step + domain_min);
            
            double normalized_y1 = (y1 - range_min) / (range_max - range_min);
            double normalized_y2 = (y2 - range_min) / (range_max - range_min);
            int target1 = height - 1 - (int) (normalized_y1 * (height - 1));
            int target2 = height - 1 - (int) (normalized_y2 * (height - 1));
            
            if (Calculus.abs(target1 - target2) < height * 0.8) {
            	g2d.drawLine(x, target1, x, target2);
            }
        }
        
        g2d.dispose();

        try {
            File arquivoSaida = new File(filename);
            ImageIO.write(imagem, "png", arquivoSaida);
            System.out.println("Sucesso! Imagem salva em: " + arquivoSaida.getAbsolutePath());
        } catch (Exception e) {
            System.err.println("Erro ao salvar o arquivo: " + e.getMessage());
        }
    }
    
    public static Graphics2D getBaseGraph(int width, int height, BufferedImage imagem){
    	Graphics2D g2d = imagem.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, width, height);
        
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
        
        return g2d;
    }
}