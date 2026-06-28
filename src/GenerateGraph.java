import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.util.function.BiFunction;

public class GenerateGraph {

	
    public static void gerarPng(int width, int height, String filename, Function func) {
        BufferedImage imagem = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        int corPreta = Color.BLACK.getRGB();
        int corBranca = Color.WHITE.getRGB();

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
                if (y_proj_prev < target) {
                	for (int i = y_proj_prev; i <= target; i++) {
                        imagem.setRGB(x, i, corBranca);
                    }
                } else {
                	for (int i = y_proj_prev; i >= target; i--) {
                        imagem.setRGB(x, i, corBranca);
                    }
                }
                
               
            }
            y_proj_prev = target;
        }

        try {
            File arquivoSaida = new File(filename);
            ImageIO.write(imagem, "png", arquivoSaida);
            System.out.println("Sucesso! Imagem salva em: " + arquivoSaida.getAbsolutePath());
        } catch (Exception e) {
            System.err.println("Erro ao salvar o arquivo: " + e.getMessage());
        }
    }
}