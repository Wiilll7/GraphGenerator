public class Function {

	private IFunction function;
	
	public Function(IFunction function) {
		this.function = function;
	}

	public void setFunction(IFunction function) {
		this.function = function;
	}
	public IFunction getFunction() {
		return function;
	}
	
	public double function(double x) {
		return function.function(x);
	}
}
