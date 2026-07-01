package com.graphgen.api;
import java.awt.Color;

public class Function {

	// Atributos
	private IFunction function;
	private Color color;
	private String text = "";
	
	
	// Construtores
	public Function() {}
	
	private Function(Function func) {
		if (func != null) {
			this.function = func.function;
			this.color = func.color;
			this.text = func.text;
		}
	}

	
	// Metodo de clone de objeto
	public Function clone() {
		return new Function(this);
	}
	
	
	// Setters e getters
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
