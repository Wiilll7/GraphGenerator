package com.graphgen.util;

/**
 * Classe utilitária que fornece métodos para cálculos matemáticos complexos.
 * Implementa funções trigonométricas, logarítmicas e algébricas utilizando 
 * aproximações por Séries de Taylor e o método de Newton-Raphson, sem depender 
 * da biblioteca Math padrão do Java.
 * @author Willian Moretti
 * @version 1.0
 */
public class Calculus {

	/**
	 * Valor aproximado da constante matemática Pi.
	 */
	public final static double pi = 3.141592653589793238462643383279;
	
	/**
	 * Valor aproximado da constante de Euler (Base do logaritmo natural).
	 */
	public final static double e = 2.7182818284590452353602874713526;
	
	/**
	 * Construtor privado para evitar a instanciação desta classe utilitária.
	 */
	private Calculus() {}
	
	/**
	 * Calcula o seno de um ângulo utilizando a aproximação por Séries de Taylor.
	 * @param num O ângulo em radianos.
	 * @return O valor do seno do ângulo.
	 */
	public static double sin(double num) {
		num = num % (2 * pi);
		
		if (num < 0) {
			num += 2 * pi;
		}
		
		boolean module = ((num > pi && num < 2*pi) || (num < 0 && num > -pi));
		
		num = num % pi;
		
		if (num > pi/2) {
			num -= pi;
			num = -num;
		} else if (num < -pi/2) {
			num += pi;
			num = -num;
		}
		
		double result = num - (pow(num, 3) / 6) + (pow(num, 5) / 120) - (pow(num, 7) / 5040) + (pow(num, 9) / 362880) - (pow(num, 11) / 3.99168E7) + (pow(num, 13) / 6.2270208E9) 
				- (pow(num, 15) / 1.307674368E12) + (pow(num, 17) / 3.55687428096E14) - (pow(num, 19) / 1.21645100408832E17);
		
		return module ? -result : result;
	}
	
	/**
	 * Calcula o cosseno de um ângulo utilizando a aproximação por Séries de Taylor.
	 * @param num O ângulo em radianos.
	 * @return O valor do cosseno do ângulo.
	 */
	public static double cos(double num) {
		boolean module = (((num % (2*pi)) > pi/2 && (num % (2*pi)) < 3*pi/2) || 
						 ((num % (2*pi)) < -pi/2 && (num % (2*pi)) > -3*pi/2));
		
		num = num % pi;
		
		if (num > pi/2) {
			num -= pi;
		} else if (num < -pi/2) {
			num += pi;
		}
		
		double result = 1 - (pow(num, 2) / 2) + (pow(num, 4) / 24) - (pow(num, 6) / 720) + (pow(num, 8) / 40320) - (pow(num, 10) / 3628800) + (pow(num, 12) / 4.790016E8)
				- (pow(num, 14) / 8.71782912E10) + (pow(num, 16) / 2.0922789888E13) - (pow(num, 18) / 6.402373705728E15);
		
		return module ? -result : result;
	}

	/**
	 * Calcula a tangente de um ângulo.
	 * @param num O ângulo em radianos.
	 * @return O valor da tangente do ângulo.
	 */
	public static double tan(double num) {
		return sin(num)/cos(num);
	}
	
	/**
	 * Calcula a secante de um ângulo.
	 * @param num O ângulo em radianos.
	 * @return O valor da secante do ângulo.
	 */
	public static double sec(double num) {
		return 1/cos(num);
	}
	
	/**
	 * Calcula a cossecante de um ângulo.
	 * @param num O ângulo em radianos.
	 * @return O valor da cossecante do ângulo.
	 */
	public static double cossec(double num) {
		return 1/sin(num);
	}
	
	/**
	 * Calcula a cotangente de um ângulo.
	 * @param num O ângulo em radianos.
	 * @return O valor da cotangente do ângulo.
	 */
	public static double cotan(double num) {
		return cos(num)/sin(num);
	}
	
	/**
	 * Calcula o arco seno de um valor utilizando o método de Newton-Raphson.
	 * @param num O valor do seno (deve estar entre -1 e 1).
	 * @return O ângulo correspondente em radianos, ou NaN se fora do domínio.
	 */
	public static double arcsin(double num) {
		if (num < -1 || num > 1) return 0.0/0.0;
		
		double y = num;
		
		for (int i = 0; i < 5; i++) {
			y = y - (sin(y) - num) / cos(y);
		}
		
		return y;
	}
	
	/**
	 * Calcula o arco cosseno de um valor utilizando o método de Newton-Raphson.
	 * @param num O valor do cosseno (deve estar entre -1 e 1).
	 * @return O ângulo correspondente em radianos, ou NaN se fora do domínio.
	 */
	public static double arccos(double num) {
		if (num < -1 || num > 1) return 0.0/0.0;
		
		double y = pi/2;
		
		for (int i = 0; i < 5; i++) {
			y = y + (cos(y) - num) / sin(y);
		}
		
		return y;
	}
	
	/**
	 * Calcula o arco tangente de um valor.
	 * @param num O valor da tangente.
	 * @return O ângulo correspondente em radianos.
	 */
	public static double arctan(double num) {
		return arcsin(num / sqrt(1 + num*num));
	}
	
	/**
	 * Calcula o arco secante de um valor.
	 * @param num O valor da secante.
	 * @return O ângulo correspondente em radianos, ou NaN se fora do domínio.
	 */
	public static double arcsec(double num) {
		return arccos(1/num);
	}
	
	/**
	 * Calcula o arco cossecante de um valor.
	 * @param num O valor da cossecante.
	 * @return O ângulo correspondente em radianos, ou NaN se fora do domínio.
	 */
	public static double arccossec(double num) {
		return arcsin(1/num);
	}
	
	/**
	 * Calcula o arco cotangente de um valor.
	 * @param num O valor da cotangente.
	 * @return O ângulo correspondente em radianos.
	 */
	public static double arccotan(double num) {
		return pi/2 - arctan(num);
	}
	
	/**
	 * Calcula a potência de uma base elevada a um expoente inteiro.
	 * @param num A base.
	 * @param exp O expoente inteiro.
	 * @return O resultado da potência.
	 */
	public static double pow(double num, int exp) {
		if (exp == 0) return 1;
		if (num == 0) return 0;
		
		double result = 1;
		
		for (int i = 0; i < exp; i++) {
			result *= num;
		}
		
		return result;
	}
	
	/**
	 * Calcula a potência de uma base elevada a um expoente fracionário (double).
	 * @param num A base (deve ser positiva para expoentes não inteiros).
	 * @param exp O expoente fracionário.
	 * @return O resultado da potência, ou NaN se a base for negativa.
	 */
	public static double pow(double num, double exp) {
		if (num == 0) return 0;
		if (exp == 0) return 1;
		if (num < 0) return 0.0/0.0;
		
		double x = ln(num) * exp;
		boolean isNegative = (x < 0);
		
		if (isNegative) {
			x = -x;
		}
		
		double tn = (x < 0) ? 0.5 : 2.0;
		
		for (int i = 0; i < 5; i++) {
			tn = tn*(1.0 + x - ln(tn));
		}
		
		return isNegative ? 1/tn : tn;
	}
	
	/**
	 * Calcula a raiz quadrada de um número.
	 * @param num O valor do qual se deseja extrair a raiz.
	 * @return A raiz quadrada do número, ou NaN se o número for negativo.
	 */
	public static double sqrt(double num) {
		if (num < 0.0001 && num >= -0.0001) return 0;
		
		if (num < -0.0001) return 0/0f;
		
		double initial = (num+1)/2;
		double result = 0;
		double variation = 0;
		
		for (int i = 0; i < 15; i++) {
			result = 0.5 * (initial + (num/initial));
			
			variation = (initial - result) < 0 ? -(initial - result): (initial - result);
			if (variation < 0.0000000001) {
				break;
			}
			
			initial = result;
		}
		
		return result;
	}
	
	/**
	 * Retorna o valor absoluto de um número.
	 * @param num O valor de entrada.
	 * @return O valor absoluto, sempre positivo ou zero.
	 */
	public static double abs(double num) {
		return (num < 0) ? -num : num;
	}
	
	/**
	 * Calcula o logaritmo natural de um número utilizando Séries de Taylor.
	 * @param num O valor sobre o qual calcular o logaritmo.
	 * @return O logaritmo natural do número, ou NaN se o número for menor ou igual a zero.
	 */
	public static double ln(double num) {
		if (num <= 0) return 0.0/0.0;
		double z = (num-1)/(num+1);
		return 2*((z) + pow(z, 3)/3.0 + pow(z, 5)/5.0 + pow(z, 7)/7.0 + pow(z, 9)/9.0 + pow(z, 11)/11.0 + pow(z, 13)/13.0 + pow(z, 15)/15.0
		+ pow(z, 17)/17.0 + pow(z, 19)/19.0 + pow(z, 21)/21.0 + pow(z, 23)/23.0);
	}
	
	/**
	 * Arredonda um número fracionário para o número inteiro mais próximo.
	 * Valores com fração exata de 0.5 são arredondados para cima.
	 * @param num O número a ser arredondado.
	 * @return O valor inteiro correspondente.
	 */
	public static int round(double num) {
		return (num % 1.0 < 0.5) ? (int) num : (int) num + 1;
	}
	
	/**
	 * Compara se dois valores de ponto flutuante são aproximadamente iguais 
	 * dentro de uma margem de tolerância.
	 * @param a O primeiro valor.
	 * @param b O segundo valor.
	 * @param factor A margem de tolerância permitida para a igualdade.
	 * @return true se a diferença entre os valores for menor que a margem, false caso contrário.
	 */
	public static boolean equals(double a, double b, double factor) {
		return a + factor > b && b > a - factor;
	}
	
	/**
	 * Retorna o menor entre dois valores inteiros.
	 * @param a O primeiro valor.
	 * @param b O segundo valor.
	 * @return O menor valor entre os dois informados.
	 */
	public static int min(int a, int b) {
		return (a <= b) ? a : b;
	}
	
	/**
	 * Retorna o maior entre dois valores de ponto flutuante.
	 * @param a O primeiro valor.
	 * @param b O segundo valor.
	 * @return O maior valor entre os dois informados.
	 */
	public static double max(double a, double b) {
		if ((a == 0.0d) && (b == 0.0d)) return b;
		
		return (a >= b) ? a : b;
	}
	
}