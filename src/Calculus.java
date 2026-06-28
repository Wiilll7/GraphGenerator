public class Calculus {

	public final static double pi = 3.141592653589793238462643383279;
	public final static double e = 2.7182818284590452353602874713526;
	public final static double ln2 = 0.69314718055994530941723212145;
	
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

	public static double pow(double num, int exp) {
		if (exp == 0) return 1;
		
		double result = 1;
		
		for (int i = 0; i < exp; i++) {
			result *= num;
		}
		
		return result;
	}
	
	public static double sqrt(double a) {
		double initial = (a+1)/2;
		double result = 0;
		double variation = 0;
		
		for (int i = 0; i < 15; i++) {
			result = 0.5 * (initial + (a/initial));
			
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
	
	public static double ln(double x) {
		if (x <= 0) return 0.0/0.0;
		return 2*(((x-1)/(x+1)) + 1.0/3.0*pow((x-1)/(x+1), 3) + 1.0/5.0*pow((x-1)/(x+1), 5) + 1.0/7.0*pow((x-1)/(x+1), 7) 
		+ 1.0/9.0*pow((x-1)/(x+1), 9) + 1.0/11.0*pow((x-1)/(x+1), 11) + 1.0/13.0*pow((x-1)/(x+1), 13) + 1.0/15.0*pow((x-1)/(x+1), 15)
		+ 1.0/17.0*pow((x-1)/(x+1), 17) + 1.0/19.0*pow((x-1)/(x+1), 19) + 1.0/21.0*pow((x-1)/(x+1), 21) + 1.0/23.0*pow((x-1)/(x+1), 23));
	}
	
	public static double exp(double num, double exp) {
		double x = ln(num) * exp;
		return 1 + x + (pow(x, 2)/2.0) + (pow(x, 3)/6.0) + (pow(x, 4)/24.0) + (pow(x, 5)/120.0) + (pow(x, 6)/720.0) + (pow(x, 7)/5040.0) + (pow(x, 8)/40320.0)
				 + (pow(x, 9)/362880.0) + (pow(x, 10)/3628800.0) + (pow(x, 11)/3.99168E7) + (pow(x, 12)/4.790016E8) + (pow(x, 13)/6.2270208E9);
		//double result = 
	}
	
	public static int round(double num) {
		return (num % 1.0 < 0.5) ? (int) num : (int) num + 1;
	}
	
	public static boolean equals(double a, double b, double factor) {
		return a + factor > b && b > a - factor;
	}
	
	public static void main(String[] args) {
		/*double result = 1;
		for (int i = 2; i < 15; i++) {
			result *= i;
			System.out.println(+i+"! = "+result);
		}*/
		System.out.println(round(3.499999));
	}
}
