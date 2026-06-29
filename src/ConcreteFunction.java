public class ConcreteFunction implements Function {
	public double function(double x) {
		return Calculus.sin(x) + Calculus.sqrt(Calculus.cos(x));
	}
}
