package com.graphgen.api;

/**
 * Interface funcional que representa uma expressão matemática f(x).
 * Por conter apenas um método e estar anotada com {@code @FunctionalInterface}, 
 * permite que o usuário crie e passe funções matemáticas de forma limpa 
 * utilizando expressões Lambda, como por exemplo: {@code (x) -> x * 2}.
 * * @author Willian Moretti
 * @version 1.0
 */
@FunctionalInterface
public interface IFunction {
	
	/**
	 * Avalia a função matemática para um determinado ponto no eixo X.
	 * * @param x O valor de entrada no eixo X.
	 * @return O valor resultante calculado para o eixo Y.
	 */
	public double function(double x);
	
}