public class ConcreteFunction {

	private Function function;
	
	public ConcreteFunction(Function function) {
		this.function = function;
	}

	public void setFunction(Function function) {
		this.function = function;
	}
	public Function getFunction() {
		return function;
	}
	
	public double function(double x) {
		return function.function(x);
	}
}
