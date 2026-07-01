package com.graphgen.util;
public class Calculus {

	// Definicao de valores constantes usados nos calculos
	public final static double pi = 3.141592653589793238462643383279;
	public final static double e = 2.7182818284590452353602874713526;
	
	
	// Metodos que realizam calculos matematicos
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

	public static double tan(double num) {
		return sin(num)/cos(num);
	}
	
	public static double sec(double num) {
		return 1/cos(num);
	}
	
	public static double cossec(double num) {
		return 1/sin(num);
	}
	
	public static double cotan(double num) {
		return cos(num)/sin(num);
	}
	
	public static double arcsin(double num) {
		if (num < -1 || num > 1) return 0.0/0.0;
		
		double y = num;
		
		for (int i = 0; i < 5; i++) {
			y = y - (sin(y) - num) / cos(y);
		}
		
		return y;
	}
	
	public static double arccos(double num) {
		if (num < -1 || num > 1) return 0.0/0.0;
		
		double y = pi/2;
		
		for (int i = 0; i < 5; i++) {
			y = y + (cos(y) - num) / sin(y);
		}
		
		return y;
	}
	
	public static double arctan(double num) {
		return arcsin(num / sqrt(1 + num*num));
	}
	
	public static double arcsec(double num) {
		return arccos(1/num);
	}
	
	public static double arccossec(double num) {
		return arcsin(1/num);
	}
	
	public static double arccotan(double num) {
		return pi/2 - arctan(num);
	}
	
	public static double pow(double num, int exp) {
		if (exp == 0) return 1;
		if (num == 0) return 0;
		
		double result = 1;
		
		for (int i = 0; i < exp; i++) {
			result *= num;
		}
		
		return result;
	}
	
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
	
	public static double abs(double num) {
		return (num < 0) ? -num : num;
	}
	
	public static double ln(double num) {
		if (num <= 0) return 0.0/0.0;
		double z = (num-1)/(num+1);
		return 2*((z) + pow(z, 3)/3.0 + pow(z, 5)/5.0 + pow(z, 7)/7.0 + pow(z, 9)/9.0 + pow(z, 11)/11.0 + pow(z, 13)/13.0 + pow(z, 15)/15.0
		+ pow(z, 17)/17.0 + pow(z, 19)/19.0 + pow(z, 21)/21.0 + pow(z, 23)/23.0);
	}
	
	public static int round(double num) {
		return (num % 1.0 < 0.5) ? (int) num : (int) num + 1;
	}
	
	public static boolean equals(double a, double b, double factor) {
		return a + factor > b && b > a - factor;
	}
	
	public static int min(int a, int b) {
        return (a <= b) ? a : b;
    }
	
	public static double max(double a, double b) {
	    if ((a == 0.0d) && (b == 0.0d)) return b;
	    
	    return (a >= b) ? a : b;
	}
	
}
