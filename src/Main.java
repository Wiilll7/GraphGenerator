import java.awt.Color;

public class Main {

	public static void main(String[] args) {
		
		Function func = new Function((x) -> (Calculus.sin(x)));
		int width = 1920;
        int height = 1920;
        
        Graph gg = new Graph(width, height, -10, 10, -10, 10);
        
        gg.addFunc(func.getFunction());
        
        func.setFunction((x) -> (Calculus.cos(x)));
        
        gg.addFunc(func.getFunction(), Color.BLUE);
        
        func.setFunction((x) -> (Calculus.tan(x)));
        
        gg.addFunc(func.getFunction(), Color.magenta);
        
        gg.generateGraphPng();
        
	}

}
