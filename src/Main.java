public class Main {

	public static void main(String[] args) {
		
		Function func = new ConcreteFunction();
		int width = 1000;
        int height = 1920;
        String nomeArquivo = "meu_grafico.png";
        
        Graph gg = new Graph(width, height);
        
        gg.generateGraphPng(nomeArquivo, func, -15, 15);
        
	}

}
