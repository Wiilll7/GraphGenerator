import java.awt.Color;

public class Function {

	private IFunction function;
	private Color color = new Color(173, 33, 52);
	private String text = "";
	
	public Function() {}
	
	private Function(Function func) {
		if (func != null) {
			this.function = func.function;
			this.color = func.color;
			this.text = func.text;
		}
	}

	public Function clone() {
		return new Function(this);
	}
	
	public Function setFunction(IFunction function) {
		this.function = function;
		return this;
	}
	
	public Function setColor(Color color) {
		this.color = color;
		return this;
	}
	
	public Function setSubtitles(String text) {
		this.text = text;
		return this;
	}
	
	public IFunction getFunction() {
		return function;
	}
	
	public Color getColor() {
		return color;
	}
	
	public String getText() {
		return text;
	}
	
	public double function(double x) {
		return function.function(x);
	}
	
}
