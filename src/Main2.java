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
		try {
	        // 1. Cria a imagem na memória (Ex: 400x200 pixels)
	        BufferedImage imagem = new BufferedImage(1600, 1600, BufferedImage.TYPE_INT_RGB);
	
	        // 2. Cria o "pincel" para desenhar na imagem
	        Graphics2D g2d = imagem.createGraphics();
	     
	        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
	        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
	        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
	
	        // 3. Pinta o fundo da imagem de branco (opcional, para não ficar tudo preto)
	        g2d.setColor(Color.WHITE);
	        g2d.fillRect(0, 0, 160, 1600);
	
	        
	        
	        Font fonta = new Font("Arial", Font.BOLD, 36);
	        
	        
	
	        String texto = "Olá, Java!";
	        int x = 100;
	        int y = 110; 
	        g2d.drawString(texto, x, y);
	
	        // 6. Muito importante: libera os recursos do pincel
	        g2d.dispose();
	        
            ImageIO.write(imagem, "png", new File("imagem_com_texto.png"));
            System.out.println("Imagem salva com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
        }
		    
	}
}
