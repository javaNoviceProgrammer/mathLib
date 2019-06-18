package mathLib.fem;

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

import mathLib.fem.core.Element;
import mathLib.fem.core.Mesh;
import mathLib.fem.core.Node;
import mathLib.fem.util.container.ElementList;
import mathLib.fem.util.container.NodeList;
import mathLib.matrix.algebra.intf.Vector;
import mathLib.plot.util.ColorMap;
import mathLib.plot.util.ColorMap.ColorMapName;
import mathLib.util.MathUtils;


public class MeshPlot2D {

	JFreeChart chart ;
	Mesh mesh ;
	Vector func ;
	float dx = 0.001f, dy=0.001f ;
	ColorMapName colorMapName ;
	int meshDensity = 10 ;

    public MeshPlot2D(Mesh mesh, Vector func) {
    	this.mesh = mesh ;
    	this.func = func ;
//        chart = createChart(createDataset()) ;
        this.colorMapName = ColorMapName.Rainbow ;
    }
    
    public MeshPlot2D(Mesh mesh, Vector func, ColorMapName colorMapName) {
    	this.mesh = mesh ;
    	this.func = func ;
    	this.colorMapName = colorMapName ;
//        chart = createChart(createDataset()) ;
    }
    
    public void setColorMap(ColorMapName name) {
    	this.colorMapName = name ;
//    	chart = createChart(createDataset()) ;
    }
    
    public void setPlotDensity(int density) {
    	this.meshDensity = density ;
    }
    
    public void setBlockRenderSize(double dx, double dy) {
    	this.dx = (float) dx ;
    	this.dy = (float) dy ;
    }

    public void run(boolean systemExit){
        JFrame frame = new JFrame();
        if(systemExit)
        	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        else
        	frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        ChartPanel chartPanel = new ChartPanel(createChart(createDataset())) {

			private static final long serialVersionUID = -8231653378192714530L;

			@Override
            public Dimension getPreferredSize() {
                return new Dimension(640, 480);
            }
        };
        
        chartPanel.setMouseZoomable(true, false);
        frame.add(chartPanel);
        Image image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/mathLib/plot/extra/presentation.png"));
        frame.setIconImage(image);
        frame.setTitle("Plot Viewer v1.0");
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
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
        
        System.out.println("Funtion range: " + range[0] + " , " + range[1]);
        
        SpectrumPaintScale ps = new SpectrumPaintScale(range[0], range[1]);
        r.setPaintScale(ps);
        
//        dx = 0.001f ; 
//        dy = 0.001f ;
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

    	DefaultXYZDataset dataset = new DefaultXYZDataset();
    	ElementList elements = mesh.getElementList() ;
    	for(Element e: elements) {
    		addInterpolationOnElement(dataset, e);
    	}
        return dataset ;
    }
    
    private void addInterpolationOnElement(DefaultXYZDataset dataset, Element e) {
    	
    	int coords = mesh.getNodeList().at(1).dim() ;
    	
    	// bilinear interpolation over a rectangular element
//		if(e.vertices().size() == 4 && coords==2) {
//			double[] coef = Utils.computeBilinearFunctionCoef(e.nodes.toArray(new Point[0]), f);
//			//f(x,y) = a1 + a2*x + a3*y + a4*x*y
//			double x = coord[0];
//			double y = coord[1];
//			double interpValue = coef[0] + coef[1]*x + coef[2]*y + coef[3]*x*y;
//
//		}
		
		// linear interpolation over a triangular element
		if(e.vertices().size() == 3 && coords==2) {
			NodeList nodes = e.nodes ;
			Node n1 = nodes.at(1) ;
			Node n2 = nodes.at(2) ;
			Node n3 = nodes.at(3) ;
			double x1 = n1.coord(1) ;
			double y1 = n1.coord(2) ;
			double x2 = n2.coord(1) ;
			double y2 = n2.coord(2) ;
			double x3 = n3.coord(1) ;
			double y3 = n3.coord(2) ;
			double val1 = func.get(n1.globalIndex) ;
			double val2 = func.get(n2.globalIndex) ;
			double val3 = func.get(n3.globalIndex) ;
			
			double[] lambda1 = MathUtils.linspace(0.0, 1.0, meshDensity) ;
			double[] lambda2 = MathUtils.linspace(0.0, 1.0, meshDensity) ;
			double lambda3 = 0.0 ;
			double[] xVals = new double[meshDensity*meshDensity] ;
			double[] yVals = new double[meshDensity*meshDensity] ;
			double[] funcVals = new double[meshDensity*meshDensity] ;
			for(int i=0; i<lambda1.length; i++) {
				for(int j=0; j<lambda2.length; j++) {
					lambda3 = 1-lambda1[i]-lambda2[j] ;
					if(lambda3 >= 0.0) {
						xVals[j+i*lambda1.length] = lambda1[i]*x1 + lambda2[j]*x2 + lambda3*x3 ;
						yVals[j+i*lambda1.length] = lambda1[i]*y1 + lambda2[j]*y2 + lambda3*y3 ;
						funcVals[j+i*lambda1.length] = lambda1[i]*val1 + lambda2[j]*val2 + lambda3*val3 ;
					}
				}
			}
			
			int index = e.globalIndex ;
			dataset.addSeries("series "+ index, new double[][] {xVals, yVals, funcVals});
			
			// create renderer for each element
			// TODO
		}
			
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
