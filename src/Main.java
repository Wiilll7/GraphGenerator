public class Main {

	public static void main(String[] args) {
		
		Function func = new ConcreteFunction();
		int width = 1600;
        int height = 1600;
        String nomeArquivo = "meu_grafico.png";
        
        Graph gg = new Graph(width, height);
        
        gg.generateGraphPng(nomeArquivo, func);
        
	}

}
