import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class Main2 {

	public static void main(String[]args) {
		int width = 1600;
        int height = 1600;
		
		BufferedImage imagem = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		
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
	        
	        File arquivoSaida = new File("grafico_base.png");
            ImageIO.write(imagem, "png", arquivoSaida);
            System.out.println("Sucesso! Imagem salva em: " + arquivoSaida.getAbsolutePath());
	        
        } catch(Exception e) {
        	e.printStackTrace();
        }
        
        g2d.dispose();
		    
	}
}
