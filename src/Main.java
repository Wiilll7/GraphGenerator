public class Main {

	public static void main(String[] args) {
		
		ConcreteFunction func = new ConcreteFunction((x) -> (Calculus.sin(x)));
		int width = 1920;
        int height = 1920;
        
        Graph gg = new Graph(width, height, -5, 10, -10, 10);
        
        gg.addFunc(func.getFunction());
        
        func.setFunction((x) -> Calculus.cos(x));
        
        gg.addFunc(func.getFunction());
        
        gg.generateGraphPng();
        
	}

}
