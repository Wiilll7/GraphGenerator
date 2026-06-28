public class ConcreteFunction implements Function {
	public double function(double x) {
		return 1/(Calculus.sin(x)/Calculus.cos(x));
	}
}
