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

        int domain_min = -100;
        int domain_max = 100;

        int range_min = -100;
        int range_max = 100;

        double domain_step = (double) (domain_max - domain_min)/width;

        int y_before = height;
        int y_proj = range_max + 1;
        
        for (int x = 0; x < width; x++) {
            double y = func.function(x * domain_step + domain_min);

            if (y != Double.NaN) {
            	System.out.println("y: "+ y +" // range_min: "+ range_min +" // range_max: "+ range_max);
            	y_proj = (int) (Math.floor((y - range_min)/(range_max - range_min) * height));
            	if (y_proj == height) y_proj = height - 1;
            	
            	if (y > range_max || y == Double.POSITIVE_INFINITY) {
            		for (int i = y_before; i < height; i++) {
                		imagem.setRGB(x, i, corBranca);
                    }
            	
            	} else if(y < range_min || y == Double.NEGATIVE_INFINITY) {
            		for (int i = y_before; i >= 0; i--) {
                		imagem.setRGB(x, i, corBranca);
                    }
            		
            	} else {
	                if (y_before >= height || y_before < 0) {
	                	imagem.setRGB(x, y_proj, corBranca);
	                } else if (y_proj > y_before) {
	                	for (int i = y_before; i <= y_proj; i++) {
	                		imagem.setRGB(x, i, corBranca);
	                    }
	                } else {
	                	for (int i = y_before; i >= y_proj; i--) {
	                		imagem.setRGB(x, i, corBranca);
	                    }
	                } 
            	}
            }
            y_before = y_proj;
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