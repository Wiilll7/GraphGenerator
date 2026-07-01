import java.awt.Color;

public class Theme {

	private Color gridPrincipal;
	private Color subGrid;
	private Color numbers;
	private Color background;
	private Color defaultFunction;
	private Color subtitleBackground;
	private Color axisXY;
	
	public static final Theme WHITE_MODE = new Theme()
			.setAxisXY(Color.BLACK)
			.setBackground(Color.WHITE)
			.setDefaultFunction(new Color(173, 33, 52))
			.setGridPrincipal(new Color(89, 87, 88))
			.setNumbers(Color.BLACK)
			.setSubGrid(new Color(220, 220, 220))
			.setSubtitleBackground(new Color(230, 230, 230));
	
	public static final Theme DARK_MODE = new Theme()
			.setAxisXY(Color.WHITE)
			.setBackground(Color.BLACK)
			.setDefaultFunction(new Color(173, 33, 52))
			.setGridPrincipal(new Color(130, 130, 130))
			.setNumbers(Color.WHITE)
			.setSubGrid(new Color(50, 50, 50))
			.setSubtitleBackground(new Color(160, 160, 160));
			
	public Theme() {}
	
	public Color getGridPrincipal() {
		return gridPrincipal;
	}
	public Theme setGridPrincipal(Color gridPrincipal) {
		this.gridPrincipal = gridPrincipal;
		return this;
	}
	public Color getSubGrid() {
		return subGrid;
	}
	public Theme setSubGrid(Color subGrid) {
		this.subGrid = subGrid;
		return this;
	}
	public Color getNumbers() {
		return numbers;
	}
	public Theme setNumbers(Color numbers) {
		this.numbers = numbers;
		return this;
	}
	public Color getBackground() {
		return background;
	}
	public Theme setBackground(Color background) {
		this.background = background;
		return this;
	}
	public Color getDefaultFunction() {
		return defaultFunction;
	}
	public Theme setDefaultFunction(Color defaultFunction) {
		this.defaultFunction = defaultFunction;
		return this;
	}
	public Color getSubtitleBackground() {
		return subtitleBackground;
	}
	public Theme setSubtitleBackground(Color subtitleBackground) {
		this.subtitleBackground = subtitleBackground;
		return this;
	}
	public Color getAxisXY() {
		return axisXY;
	}
	public Theme setAxisXY(Color axisXY) {
		this.axisXY = axisXY;
		return this;
	}
	
}
