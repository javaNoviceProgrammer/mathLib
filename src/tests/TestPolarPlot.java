package tests;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.TickUnit;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PolarAxisLocation;
import org.jfree.chart.plot.PolarPlot;
import org.jfree.chart.renderer.DefaultPolarItemRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 * @see http://en.wikipedia.org/wiki/Polar_coordinate_system
 * @see https://stackoverflow.com/questions/3458824
 * @see https://stackoverflow.com/questions/6540390
 * @see https://stackoverflow.com/questions/6576911
 * @see https://stackoverflow.com/a/10227275/230513
 */
public class TestPolarPlot extends JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 5323766583093532547L;
	private static final String title = "Archimedes' Spiral";

    public TestPolarPlot(String title) {
        super(title);
        JFreeChart chart = createChart(createDataset());
        ChartPanel panel = new ChartPanel(chart);
        panel.setPreferredSize(new Dimension(500, 500));
        panel.setMouseZoomable(false);
        this.add(panel);
    }

    private static XYDataset createDataset() {
        XYSeriesCollection result = new XYSeriesCollection();
        XYSeries series = new XYSeries(title);
        for (int t = 0; t <= 3 * 360; t++) {
            series.add(t, t);
        }
        result.addSeries(series);
        return result;
    }

    private static JFreeChart createChart(XYDataset dataset) {
        ValueAxis radiusAxis = new NumberAxis();
        radiusAxis.setTickLabelsVisible(false);
        DefaultPolarItemRenderer renderer = new DefaultPolarItemRenderer();
        renderer.setShapesVisible(true);
        PolarPlot plot = new PolarPlot(dataset, radiusAxis, renderer);
        plot.setCounterClockwise(true);
        plot.setAxisLocation(PolarAxisLocation.EAST_BELOW);
        plot.setAngleOffset(0);
        plot.setAngleTickUnit(new TickUnit(30) {
			private static final long serialVersionUID = 2098171735845412068L;});
        plot.setBackgroundPaint(Color.WHITE);
        plot.setRadiusGridlinePaint(Color.BLACK);
        plot.addCornerTextItem("r(θ) = θ; 0 < θ < 6π");
        JFreeChart chart = new JFreeChart(
            title, JFreeChart.DEFAULT_TITLE_FONT, plot, true);
        chart.setBackgroundPaint(Color.white);
        return chart;
    }

    public static void main(String[] args) {
        TestPolarPlot demo = new TestPolarPlot(title);
        demo.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        demo.pack();
        demo.setLocationRelativeTo(null);
        demo.setVisible(true);
    }
}