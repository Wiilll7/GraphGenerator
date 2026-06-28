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

        int domain_min = -11;
        int domain_max = 10;

        int range_min = -10;
        int range_max = 10;

        double domain_step = (double) (domain_max - domain_min)/width;

        double y = func.function(domain_min - domain_step);
        
        double normalized_y = Math.max(0.0, Math.min(1.0,(y - range_min) / (range_max - range_min)));
        int target = height - 1 - (int) (normalized_y * (height - 1));
        int y_proj_prev = target;
        
        for (int x = 0; x < width; x++) {
            y = func.function(x * domain_step + domain_min);
            
            normalized_y = Math.max(0.0, Math.min(1.0,(y - range_min) / (range_max - range_min)));
            target = height - 1 - (int) (normalized_y * (height - 1));

            
            if (!Double.isNaN(y) &&
            	!Double.isNaN(y_proj_prev) &&
            	!Double.isInfinite(y) &&
            	!Double.isInfinite(y_proj_prev) &&
            	y <= range_max &&
            	y >= range_min
            ) {
               g2d.drawLine(x, y_proj_prev, x, target);
            }
            y_proj_prev = target;
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