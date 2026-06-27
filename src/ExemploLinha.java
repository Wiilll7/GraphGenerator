import java.util.ArrayList;
import java.util.List;
import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.XYSeries;
import org.knowm.xchart.style.markers.SeriesMarkers;

public class ExemploLinha {
    public static void main(String[] args) {
        // 1. Criar dados
        List<Double> xData = new ArrayList<>();
        List<Double> yData = new ArrayList<>();
        for (int i = 0; i <= 30; i++) {
            xData.add((double) i);
            yData.add(Math.pow(i, 2));
        }

        // 2. Criar o Gráfico
        XYChart chart = new XYChartBuilder()
            .width(800)
            .height(600)
            .title("Exemplo de Gráfico XY")
            .xAxisTitle("X")
            .yAxisTitle("Y")
            .build();

        // 3. Adicionar Série ao Gráfico
        XYSeries series = chart.addSeries("x^2", xData, yData);
        series.setMarker(SeriesMarkers.CIRCLE);

        // 4. Salvar ou exibir o gráfico
        try {
            BitmapEncoder.saveBitmap(chart, "./ExemploGrafico.png", BitmapEncoder.BitmapFormat.PNG);
            System.out.println("Gráfico gerado com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
