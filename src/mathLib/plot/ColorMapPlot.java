package mathLib.plot;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Toolkit;

import javax.swing.JFrame;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.PaintScale;
import org.jfree.chart.renderer.xy.XYBlockRenderer;
import org.jfree.chart.title.PaintScaleLegend;
import org.jfree.data.xy.DefaultXYZDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYZDataset;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;

import mathLib.plot.util.ColorMap;
import mathLib.plot.util.ColorMap.ColorMapName;
import mathLib.plot.util.MeshGrid;


public class ColorMapPlot {

	JFreeChart chart ;
	MeshGrid mesh ;
	double[][] func ;
	float dx, dy ;
	ColorMapName colorMapName ;

    public ColorMapPlot(MeshGrid mesh, double[][] func) {
    	this.mesh = mesh ;
    	this.func = func ;
        chart = createChart(createDataset()) ;
        this.colorMapName = ColorMapName.Rainbow ;
    }
    
    public ColorMapPlot(MeshGrid mesh, double[][] func, ColorMapName colorMapName) {
    	this.mesh = mesh ;
    	this.func = func ;
    	this.colorMapName = colorMapName ;
        chart = createChart(createDataset()) ;
    }
    
    public void setColorMap(ColorMapName name) {
    	this.colorMapName = name ;
    	chart = createChart(createDataset()) ;
    }

    public void run(boolean systemExit){
        JFrame f = new JFrame();
        if(systemExit)
        	f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        else
        	f.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        ChartPanel chartPanel = new ChartPanel(chart) {

			private static final long serialVersionUID = -8231653378192714530L;

			@Override
            public Dimension getPreferredSize() {
                return new Dimension(640, 480);
            }
        };
        chartPanel.setMouseZoomable(true, false);
        f.add(chartPanel);
        Image image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/presentation.png"));
        f.setIconImage(image);
        f.setTitle("Plot Viewer v1.0");
        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }

    private JFreeChart createChart(XYDataset dataset) {
        NumberAxis xAxis = new NumberAxis("x Axis");
        NumberAxis yAxis = new NumberAxis("y Axis");
        xAxis.setAutoRangeIncludesZero(false);
        xAxis.setLowerBound(mesh.getX(0, 0));
        xAxis.setUpperBound(mesh.getX(mesh.getXDim()-1, 0));
        yAxis.setAutoRangeIncludesZero(false);
        yAxis.setLowerBound(mesh.getY(0, 0));
        yAxis.setUpperBound(mesh.getY(0, mesh.getYDim()-1));
        XYPlot plot = new XYPlot(dataset, xAxis, yAxis, null);
        XYBlockRenderer r = new XYBlockRenderer();
        double[] range = getFuncMinMax() ;
        SpectrumPaintScale ps = new SpectrumPaintScale(range[0], range[1]);
        r.setPaintScale(ps);
        r.setBlockHeight(dy);
        r.setBlockWidth(dx);
        plot.setRenderer(r);
        JFreeChart chart = new JFreeChart("",
            JFreeChart.DEFAULT_TITLE_FONT, plot, false);
        NumberAxis scaleAxis = new NumberAxis("Scale");
        scaleAxis.setAxisLinePaint(Color.white);
        scaleAxis.setTickMarkPaint(Color.white);
        scaleAxis.setAutoRangeIncludesZero(false);
        PaintScaleLegend legend = new PaintScaleLegend(ps, scaleAxis);
        legend.setSubdivisionCount(128);
        legend.setAxisLocation(AxisLocation.TOP_OR_RIGHT);
        legend.setPadding(new RectangleInsets(5, 10, 40, 10));
        legend.setStripWidth(20);
        legend.setPosition(RectangleEdge.RIGHT);
        legend.setBackgroundPaint(Color.WHITE);
        chart.addSubtitle(legend);
        chart.setBackgroundPaint(Color.white);
        return chart;
    }

    private XYZDataset createDataset() {
    	dx = (float) (mesh.getX(1, 0) - mesh.getX(0, 0)) ;
    	dy = (float) (mesh.getY(0, 1) - mesh.getY(0, 0)) ;
        DefaultXYZDataset dataset = new DefaultXYZDataset();
        for (int i = 0; i < mesh.getXDim(); i++) {
            double[][] data = new double[3][mesh.getYDim()];
            for (int j = 0; j < mesh.getYDim(); j++) {
                data[0][j] = mesh.getX(i, j);
                data[1][j] = mesh.getY(i, j);
                data[2][j] = func[i][j];
            }
            dataset.addSeries("Series" + i, data);
        }
        return dataset;
    }

    private double[] getFuncMinMax(){
    	int rows = func.length ;
    	int columns = func[0].length ;
    	double min = Double.MAX_VALUE ;
    	double max = Double.MIN_VALUE ;
    	for(int i=0; i<rows; i++) {
    		for(int j=0; j<columns; j++){
    			if(min>func[i][j]) min = func[i][j] ;
    			if(max<func[i][j]) max = func[i][j] ;
    		}
    	}
    	return new double[] {min, max} ;
    }

    private class SpectrumPaintScale implements PaintScale {

        private final double lowerBound;
        private final double upperBound;

        public SpectrumPaintScale(double lowerBound, double upperBound) {
            this.lowerBound = lowerBound;
            this.upperBound = upperBound;
        }

        @Override
        public double getLowerBound() {
            return lowerBound;
        }

        @Override
        public double getUpperBound() {
            return upperBound;
        }

        @Override
        public Paint getPaint(double value) {
        	ColorMap colorMap = new ColorMap(getLowerBound(), getUpperBound(), colorMapName) ;
        	return colorMap.getColor(value) ;
        }
    }

}
