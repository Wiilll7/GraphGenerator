package com.graphgen.api;

import java.awt.Color;

/**
 * Classe que encapsula uma função matemática e suas propriedades visuais.
 * Armazena a lógica matemática, a cor da linha a ser desenhada 
 * e o texto que aparecerá na legenda do gráfico. Utiliza o padrão Fluent API 
 * para facilitar a configuração em uma única linha.
 * @author Willian Moretti
 * @version 1.0
 */
public class Function {

	/** Atributos */
	private IFunction function;
	private Color color;
	private String text = "";
	
	
	/**
	 * Construtor padrão. Cria uma função vazia.
	 * Recomenda-se encadear os métodos {@code setFunction}, {@code setColor} e {@code setSubtitles} 
	 * logo após a instanciação.
	 */
	public Function() {}
	
	/**
	 * Construtor de cópia privado. Utilizado internamente pelo método clone().
	 * @param func O objeto Function a ser copiado.
	 */
	private Function(Function func) {
		if (func != null) {
			this.function = func.function;
			this.color = func.color;
			this.text = func.text;
		}
	}

	
	/**
	 * Cria e retorna uma cópia exata deste objeto Function.
	 * Essencial para que a classe Graph armazene o estado atual da função 
	 * no momento do desenho, evitando que alterações futuras na mesma variável 
	 * sobrescrevam a função original na memória.
	 * @return Uma nova instância de Function com os mesmos atributos.
	 */
	public Function clone() {
		return new Function(this);
	}
	
	
	/**
	 * Define a expressão matemática que esta função irá representar.
	 * @param function A interface IFunction contendo a regra matemática (uma expressão Lambda).
	 * @return A própria instância de Function (para encadeamento Fluent API).
	 */
	public Function setFunction(IFunction function) {
		this.function = function;
		return this;
	}
	
	/**
	 * Define a cor da linha com a qual esta função será desenhada no gráfico.
	 * Se não for definida, o gráfico utilizará a cor padrão configurada no Theme.
	 * @param color A cor desejada (java.awt.Color).
	 * @return A própria instância de Function (para encadeamento Fluent API).
	 */
	public Function setColor(Color color) {
		this.color = color;
		return this;
	}
	
	/**
	 * Define o texto da legenda para esta função.
	 * Se for mantido vazio (""), a função será desenhada, mas não aparecerá na caixa de legendas.
	 * @param text O texto da legenda.
	 * @return A própria instância de Function (para encadeamento Fluent API).
	 */
	public Function setSubtitles(String text) {
		this.text = text;
		return this;
	}
	
	/**
	 * Obtém a interface funcional matemática armazenada.
	 * @return O objeto IFunction interno.
	 */
	public IFunction getFunction() {
		return function;
	}
	
	/**
	 * Obtém a cor configurada para o desenho desta função.
	 * @return A cor da função (pode ser null se não foi configurada).
	 */
	public Color getColor() {
		return color;
	}
	
	/**
	 * Obtém o texto da legenda desta função.
	 * @return O texto da legenda (retorna uma String vazia se não configurado).
	 */
	public String getText() {
		return text;
	}
	
	/**
	 * Atalho que delega o cálculo matemático para a interface interna.
	 * @param x O valor do domínio.
	 * @return O valor resultante da imagem no eixo Y.
	 */
	public double function(double x) {
		return function.function(x);
	}
	
}