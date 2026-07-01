package com.graphgen.style;

import java.awt.Color;

/**
 * Classe responsável por gerenciar as configurações de estilo e paleta de cores do gráfico.
 * Permite a personalização completa dos elementos visuais, como plano de fundo, eixos, 
 * linhas de grade e legendas. Utiliza o padrão Fluent API para facilitar a configuração.
 * @author Willian Moretti
 * @version 1.0
 */
public class Theme {

	/** Atributos */
	private Color gridPrincipal;
	private Color subGrid;
	private Color numbers;
	private Color background;
	private Color defaultFunction;
	private Color subtitleBackground;
	private Color axisXY;
	
	/**
	 * Configuração de tema padrão com fundo claro e elementos escuros.
	 */
	public static final Theme WHITE_MODE = new Theme()
			.setAxisXY(Color.BLACK)
			.setBackground(Color.WHITE)
			.setDefaultFunction(new Color(173, 33, 52))
			.setGridPrincipal(new Color(89, 87, 88))
			.setNumbers(Color.BLACK)
			.setSubGrid(new Color(220, 220, 220))
			.setSubtitleBackground(new Color(230, 230, 230));
	
	/**
	 * Configuração de tema padrão com fundo escuro e elementos claros.
	 */
	public static final Theme DARK_MODE = new Theme()
			.setAxisXY(new Color(230, 230, 230))
			.setBackground(Color.BLACK)
			.setDefaultFunction(new Color(82, 247, 203))
			.setGridPrincipal(new Color(130, 130, 130))
			.setNumbers(new Color(230, 230, 230))
			.setSubGrid(new Color(50, 50, 50))
			.setSubtitleBackground(new Color(160, 160, 160));
	
	
	/**
	 * Construtor padrão. Cria um tema vazio sem cores definidas inicialmente.
	 * Recomenda-se o uso dos métodos setters para preencher as cores desejadas.
	 */
	public Theme() {}
	
	
	/**
	 * Obtém a cor atual das linhas da grade principal.
	 * @return A cor da grade principal.
	 */
	public Color getGridPrincipal() {
		return gridPrincipal;
	}

	/**
	 * Define a cor das linhas da grade principal.
	 * @param gridPrincipal A cor desejada.
	 * @return A própria instância de Theme (para encadeamento Fluent API).
	 */
	public Theme setGridPrincipal(Color gridPrincipal) {
		this.gridPrincipal = gridPrincipal;
		return this;
	}

	/**
	 * Obtém a cor atual das linhas da sub-grade.
	 * @return A cor da sub-grade.
	 */
	public Color getSubGrid() {
		return subGrid;
	}

	/**
	 * Define a cor das linhas da sub-grade.
	 * @param subGrid A cor desejada.
	 * @return A própria instância de Theme (para encadeamento Fluent API).
	 */
	public Theme setSubGrid(Color subGrid) {
		this.subGrid = subGrid;
		return this;
	}

	/**
	 * Obtém a cor atual utilizada para desenhar os números nos eixos.
	 * @return A cor dos números.
	 */
	public Color getNumbers() {
		return numbers;
	}

	/**
	 * Define a cor dos números desenhados ao longo dos eixos X e Y.
	 * @param numbers A cor desejada.
	 * @return A própria instância de Theme (para encadeamento Fluent API).
	 */
	public Theme setNumbers(Color numbers) {
		this.numbers = numbers;
		return this;
	}

	/**
	 * Obtém a cor atual do plano de fundo do gráfico.
	 * @return A cor do plano de fundo.
	 */
	public Color getBackground() {
		return background;
	}

	/**
	 * Define a cor do plano de fundo de toda a área do gráfico.
	 * @param background A cor desejada.
	 * @return A própria instância de Theme (para encadeamento Fluent API).
	 */
	public Theme setBackground(Color background) {
		this.background = background;
		return this;
	}

	/**
	 * Obtém a cor padrão utilizada para desenhar funções que não possuem cor específica.
	 * @return A cor padrão de função.
	 */
	public Color getDefaultFunction() {
		return defaultFunction;
	}

	/**
	 * Define a cor padrão que será atribuída a uma função caso o usuário não defina uma cor para ela.
	 * @param defaultFunction A cor desejada.
	 * @return A própria instância de Theme (para encadeamento Fluent API).
	 */
	public Theme setDefaultFunction(Color defaultFunction) {
		this.defaultFunction = defaultFunction;
		return this;
	}

	/**
	 * Obtém a cor atual do fundo da caixa de legendas.
	 * @return A cor de fundo da legenda.
	 */
	public Color getSubtitleBackground() {
		return subtitleBackground;
	}

	/**
	 * Define a cor do fundo da caixa de legendas gerada pelo gráfico.
	 * @param subtitleBackground A cor desejada.
	 * @return A própria instância de Theme (para encadeamento Fluent API).
	 */
	public Theme setSubtitleBackground(Color subtitleBackground) {
		this.subtitleBackground = subtitleBackground;
		return this;
	}

	/**
	 * Obtém a cor atual das linhas dos eixos centrais X e Y.
	 * @return A cor dos eixos X e Y.
	 */
	public Color getAxisXY() {
		return axisXY;
	}

	/**
	 * Define a cor das linhas principais que representam o eixo X e o eixo Y.
	 * @param axisXY A cor desejada.
	 * @return A própria instância de Theme (para encadeamento Fluent API).
	 */
	public Theme setAxisXY(Color axisXY) {
		this.axisXY = axisXY;
		return this;
	}
	
}