import java.awt.Color;

public class Main {

	public static void main(String[] args) {
		
		Function func = new Function().setFunction((x) -> (Calculus.sin(x))).setSubtitles("sin(x)");
		int width = 1920;
        int height = 1920;
        
        Graph gg = new Graph(width, height, -10, 10, -10, 10);
        
        gg.drawFunction(func);
        
        func.setFunction((x) -> (Calculus.cos(x))).setColor(Color.blue).setSubtitles("cos(x)");
        
        gg.drawFunction(func);
        
        func.setFunction((x) -> (1 - Calculus.sin(x))/Calculus.cos(x)).setColor(new Color(86, 28, 153)).setSubtitles("(1-sin(x))/cos(x)");
        
        gg.drawFunction(func);
        
        gg.drawSubtitles();
        
        gg.generateGraphPng();
        
        
	}

}
