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

        int domain_min = -20;
        int domain_max = 20;

        int range_min = -20;
        int range_max = 20;

        double domain_step = (double) (domain_max - domain_min)/width;

        int y_before = range_max + 1;
        int y_proj;
        
        for (int x = 0; x < width; x++) {
            double y = func.function(x * domain_step + domain_min);

            if (y != Double.NaN && range_max >= y && y >= range_min) {
            	
                y_proj = (int) Math.floor((1-(y - range_min)/(range_max - range_min)) * height);
                
                if (x == 0) {
                	y_before = y_proj;
                }
                
                if (y_proj > y_before) {
                	for (int i = y_before; i <= y_proj; i++) {
                		imagem.setRGB(x, i, corBranca);
                    }
                } else {
                	for (int i = y_before; i >= y_proj; i--) {
                		imagem.setRGB(x, i, corBranca);
                    }
                }
                
                y_before = y_proj;
            }
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