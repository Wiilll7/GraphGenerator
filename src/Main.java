public class Main {

	public static void main(String[] args) {
		
		Function func = new ConcreteFunction();
		int width = 1920;
        int height = 1920;
        
        Graph gg = new Graph(width, height, 10, 10, -10, 10);
        
        gg.addFunc(func);
        
        gg.generateGraphPng();
        
	}

}
