public class ConcreteFunction implements Function {
	public double function(double x) {
		return Calculus.sin(x)*Calculus.sin(2*x);
	}
}
