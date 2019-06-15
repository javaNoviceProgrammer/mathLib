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

import mathLib.fem.core.Mesh;
import mathLib.fem.util.container.NodeList;
import mathLib.matrix.algebra.intf.Vector;
import mathLib.plot.util.ColorMap;
import mathLib.plot.util.ColorMap.ColorMapName;
import mathLib.util.MathUtils;


public class MeshPlot {

	JFreeChart chart ;
	Mesh mesh ;
	Vector func ;
	float dx, dy ;
	ColorMapName colorMapName ;

    public MeshPlot(Mesh mesh, Vector func) {
    	this.mesh = mesh ;
    	this.func = func ;
        chart = createChart(createDataset()) ;
        this.colorMapName = ColorMapName.Rainbow ;
    }
    
    public MeshPlot(Mesh mesh, Vector func, ColorMapName colorMapName) {
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
        Image image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/mathLib/plot/extra/presentation.png"));
        f.setIconImage(image);
        f.setTitle("Plot Viewer v1.0");
        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }

    private JFreeChart createChart(XYDataset dataset) {
    	NodeList nodes = mesh.getNodeList() ;
    	int m = nodes.size() ;
    	double[] x = new double[m] ;
    	double[] y = new double[m] ;
    	for(int i=0; i<m; i++) {
    		x[i] = nodes.at(i+1).coord(1) ;
    		y[i] = nodes.at(i+1).coord(2) ;
    	}
    
    	
    	double xmin = MathUtils.Arrays.FindMinimum.getValue(x) ;
    	double xmax = MathUtils.Arrays.FindMaximum.getValue(x) ;
    	double ymin = MathUtils.Arrays.FindMinimum.getValue(y) ;
    	double ymax = MathUtils.Arrays.FindMaximum.getValue(y) ;
    	
        NumberAxis xAxis = new NumberAxis("x Axis");
        NumberAxis yAxis = new NumberAxis("y Axis");
        xAxis.setAutoRangeIncludesZero(false);
        
        xAxis.setLowerBound(xmin);
        xAxis.setUpperBound(xmax);
        yAxis.setAutoRangeIncludesZero(false);
        yAxis.setLowerBound(ymin);
        yAxis.setUpperBound(ymax);
        
        XYPlot plot = new XYPlot(dataset, xAxis, yAxis, null);
        XYBlockRenderer r = new XYBlockRenderer();
        double[] range = getFuncMinMax() ;
        SpectrumPaintScale ps = new SpectrumPaintScale(range[0], range[1]);
        r.setPaintScale(ps);
        
        dx = 0.05f ; 
        dy = 0.05f ;
        r.setBlockHeight(dy);
        r.setBlockWidth(dx);
        
        plot.setRenderer(r);
        JFreeChart chart = new JFreeChart("", JFreeChart.DEFAULT_TITLE_FONT, plot, false);
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
    	NodeList nodes = mesh.getNodeList() ;
    	int m = nodes.size() ;
    	double[] x = new double[m] ;
    	double[] y = new double[m] ;
    	double[] funcVals = new double[m] ;
    	DefaultXYZDataset dataset = new DefaultXYZDataset();
    	for(int i=0; i<m; i++) {
    		x[i] = nodes.at(i+1).coord(1) ;
    		y[i] = nodes.at(i+1).coord(2) ;
    		funcVals[i] = func.get(i+1) ;
    		dataset.addSeries("series"+i, new double[][] {{x[i]}, {y[i]}, {funcVals[i]}} );
    	}
    	
        return dataset;
    }

    private double[] getFuncMinMax(){
    	int m = func.getDim() ;
    	double[] vals = new double[m] ;
    	for(int i=0; i<m; i++)
    		vals[i] = func.get(i+1) ;
    	double min = MathUtils.Arrays.FindMinimum.getValue(vals) ;
    	double max = MathUtils.Arrays.FindMaximum.getValue(vals) ;
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
