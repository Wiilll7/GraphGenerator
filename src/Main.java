import java.awt.Color;

public class Main {

	public static void main(String[] args) {
		
		Function func = new Function().setFunction((x) -> (Calculus.sin(x))).setSubtitles("AAA");
		int width = 1920;
        int height = 1920;
        
        Graph gg = new Graph(width, height, -10, 10, -10, 10);
        
        gg.drawFunction(func);
        
        func.setFunction((x) -> (Calculus.cos(x))).setColor(Color.black).setSubtitles("BBB");
        
        gg.drawFunction(func);
        
        func.setFunction((x) -> (1 - Calculus.sin(x))/Calculus.cos(x)).setColor(new Color(86, 28, 153));
        
        gg.drawFunction(func);
        
        gg.drawSubtitles();
        
        gg.generateGraphPng();
        
        
	}

}
