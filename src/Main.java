public class Main {

	public static void main(String[] args) {
		
		Function func = new ConcreteFunction();
		int largura = 1000;
        int altura = 1000;
        String nomeArquivo = "meu_grafico.png";
        
        GenerateGraph.gerarPng(largura, altura, nomeArquivo, func);
	}

}
