
public class Viewport {
	
	// Calcula a margem e espaco util para desenho
	public final int widthArea;
    public final int heightArea;
    public final int margemX;
    public final int margemY;
    
    // Usado na movimentação da origem
    public final double totalX;
    public final double totalY;
    public final int origemX;
    public final int origemY;
    
    // Limites maximos e minimos
    public final int leftLimit;
    public final int rightLimit;
    public final int upLimit;
    public final int downLimit;
    
    public final double xPixels; 
    public final double yPixels;

    public Viewport(int width, int height, double xMin, double xMax, double yMin, double yMax) {
        this.margemX = (int) (width * 0.05);
        this.margemY = (int) (height * 0.05);
        this.widthArea = width - (margemX * 2);
        this.heightArea = height - (margemY * 2);
        
        this.totalX = xMax - xMin;
        this.totalY = yMax - yMin;
        
        double proporcaoZeroX = -xMin / totalX;
        double proporcaoZeroY = yMax / totalY;
        
        this.origemX = (int) (proporcaoZeroX * widthArea);
        this.origemY = (int) (proporcaoZeroY * heightArea);
        
        this.leftLimit = -origemX - margemX;
        this.rightLimit = (widthArea - origemX) + margemX;
        this.upLimit = -origemY - margemY;
        this.downLimit = (heightArea - origemY) + margemY;
        
        // Calculado uma única vez
        this.xPixels = widthArea / totalX;
        this.yPixels = heightArea / totalY;
    }
    
}
