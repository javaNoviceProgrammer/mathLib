package mathLib.plot;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.annotations.XYTitleAnnotation;
import org.jfree.chart.axis.LogarithmicAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RectangleAnchor;
import org.jfree.ui.RectangleEdge;

import javafx.embed.swing.SwingNode;
import mathLib.numbers.Complex;
import mathLib.util.MathUtils;

public class MatlabChart {

    Font font;
    JFreeChart chart = null ;
    LegendTitle legend;
    ArrayList<Color> colors;
    ArrayList<BasicStroke> strokes;
    XYSeriesCollection dataset;
    XYLineAndShapeRenderer plotRenderer ;
    ArrayList<String> specs ;

    int counter = 0 ;

    public MatlabChart() {
        font = JFreeChart.DEFAULT_TITLE_FONT;
        colors = new ArrayList<Color>();
        strokes = new ArrayList<BasicStroke>();
        dataset = new XYSeriesCollection();
        plotRenderer = new XYLineAndShapeRenderer() ;
        specs = new ArrayList<String>() ;
    }
    //*********************plotting************************************
    public void plot(double[] x, double[] y, String spec, float lineWidth, String title) {
        final XYSeries series = new XYSeries(title);
        for (int i = 0; i < x.length; i++)
            series.add(x[i],y[i]);
        dataset.addSeries(series);
        specs.add(spec) ;
        FindColor(spec,lineWidth);
    }

    public void plot(double[] x, double[] y, String spec, float lineWidth) {
    	String title = "fig" + counter ;
    	counter++ ;
        final XYSeries series = new XYSeries(title);
        for (int i = 0; i < x.length; i++)
            series.add(x[i],y[i]);
        dataset.addSeries(series);
        specs.add(spec) ;
        FindColor(spec,lineWidth);
    }

    public void plot(double[] x, double[] y, float lineWidth) {
    	String title = "fig" + counter ;
    	counter++ ;
        String spec = "-b" ;
        final XYSeries series = new XYSeries(title);
        for (int i = 0; i < x.length; i++)
            series.add(x[i],y[i]);
        dataset.addSeries(series);
        specs.add(spec) ;
        FindColor(spec,lineWidth);
    }

    public void plot(double[] x, double[] y, String spec) {
    	String title = "fig" + counter ;
    	counter++ ;
        float lineWidth = 1 ;
        final XYSeries series = new XYSeries(title);
        for (int i = 0; i < x.length; i++)
            series.add(x[i],y[i]);
        dataset.addSeries(series);
        specs.add(spec) ;
        FindColor(spec,lineWidth);
    }

    public void plot(double[] x, double[] y, int colorId) {
    	String title = "fig" + counter ;
    	counter++ ;
        final XYSeries series = new XYSeries(title);
        for (int i = 0; i < x.length; i++)
            series.add(x[i],y[i]);
        dataset.addSeries(series);
        specs.add("-") ;
        FindColor(colorId, 1.0f);
    }

    public void plot(double[] x, double[] y) {
    	String title = "fig" + counter ;
    	counter++ ;
        String spec = "-b" ;
        float lineWidth = 1 ;
        final XYSeries series = new XYSeries(title);
        for (int i = 0; i < x.length; i++)
            series.add(x[i],y[i]);
        dataset.addSeries(series);
        specs.add(spec) ;
        FindColor(spec,lineWidth);
    }

    public void plot(double[] x, Complex[] y) {
    	double[] yReal = MathUtils.Arrays.getReal(y) ;
    	double[] yImag = MathUtils.Arrays.getImag(y) ;
    	plot(x, yReal);
    	plot(x, yImag);
    }


    //*******************Rendering the figures**************************************
    public void RenderPlot() {
        // Create chart
    	if(chart == null){
            JFreeChart chart = null;
            if (dataset != null && dataset.getSeriesCount() > 0){
            	try {
            		ChartFactory.setChartTheme(StandardChartTheme.createLegacyTheme()); // in case legacy theme is supported by the OS
                	chart = ChartFactory.createXYLineChart(null,null,null,dataset,PlotOrientation.VERTICAL,true, true, false);
				} catch (Error e) {
					chart = ChartFactory.createXYLineChart(null,null,null,dataset,PlotOrientation.VERTICAL,false, false, false);
				}
            }
            else
                System.out.println(" [!] First create a chart and add data to it. The plot is empty now!");
            // Add customization options to chart
            XYPlot plot = chart.getXYPlot();
            for (int i = 0; i < colors.size(); i++) {
                // adding plot renderer
                plotRenderer.setSeriesStroke(i, strokes.get(i));
                plotRenderer.setSeriesPaint(i, colors.get(i));
                if(dataset.getSeries(i).getItemCount()>1){
                	plotRenderer.setSeriesShapesVisible(i, false);
                }
            }
            plot.setRenderer(plotRenderer);
            ((NumberAxis)plot.getDomainAxis()).setAutoRangeIncludesZero(false);
            ((NumberAxis)plot.getRangeAxis()).setAutoRangeIncludesZero(false);
            plot.setBackgroundPaint(Color.WHITE);
            legend = chart.getLegend();
            chart.removeLegend();
//            chart.setBackgroundPaint(new Color(228,233,238));
            chart.setBackgroundPaint(new Color(244,244,244));
            this.chart = chart;
    	}
    	else{
            // Add customization options to chart
            XYPlot plot = chart.getXYPlot();
            for (int i = counter-1; i < colors.size(); i++) {
                // adding plot renderer
                plotRenderer.setSeriesStroke(i, strokes.get(i));
                plotRenderer.setSeriesPaint(i, colors.get(i));
                if(dataset.getSeries(i).getItemCount()>1){
                	plotRenderer.setSeriesShapesVisible(i, false);
                }
            }
            plot.setRenderer(plotRenderer);
            ((NumberAxis)plot.getDomainAxis()).setAutoRangeIncludesZero(false);
            ((NumberAxis)plot.getRangeAxis()).setAutoRangeIncludesZero(false);
            plot.setBackgroundPaint(Color.WHITE);
            legend = chart.getLegend();
            chart.removeLegend();
    	}

    }

    //*********************************************************
    private void CheckExists() {
        if (chart == null) {
            throw new IllegalArgumentException("First plot something in the chart before you modify it.");
        }
    }

    public int getNumberOfFigures(){
    	return dataset.getSeriesCount() ;
    }

    // check this later
    public void clear(){
    	colors.clear();
    	strokes.clear();
    	specs.clear();
    	dataset = new XYSeriesCollection() ;
    	plotRenderer = new XYLineAndShapeRenderer() ;
    	chart = ChartFactory.createXYLineChart(null,getXLabel(),getYLabel(),dataset,PlotOrientation.VERTICAL,true, false, false);
    	counter = 0 ;
    	font(font.getName()) ;
    }

    //*********************************************************
    public void markerON(){
    	int numSeries = chart.getXYPlot().getSeriesCount() ;
    	for(int i=0; i<numSeries; i++){
    		plotRenderer.setSeriesShapesVisible(i, true);
    	}
    	chart.getXYPlot().setRenderer(plotRenderer);
    }

    public void markerON(int figNumber){
    	plotRenderer.setSeriesShapesVisible(figNumber, true);
    	chart.getXYPlot().setRenderer(plotRenderer);
    }

    public void markerOFF(){
    	int numSeries = chart.getXYPlot().getSeriesCount() ;
    	for(int i=0; i<numSeries; i++){
    		plotRenderer.setSeriesShapesVisible(i, false);
    	}
    	chart.getXYPlot().setRenderer(plotRenderer);
    }

    public void markerOFF(int figNumber){
    	plotRenderer.setSeriesShapesVisible(figNumber, false);
    	chart.getXYPlot().setRenderer(plotRenderer);
    }

    //*********************************************************

    public void setXAxis_to_Log(){
    	NumberAxis xLogAxis = new LogarithmicAxis(getXLabel()) ;
    	chart.getXYPlot().setDomainAxis(xLogAxis);
    }

    public void setXAxis_to_Linear(){
    	NumberAxis xLinearAxis = new NumberAxis(getXLabel()) ;
    	chart.getXYPlot().setDomainAxis(xLinearAxis);
    }

    public void setYAxis_to_Log(){
    	NumberAxis yLogAxis = new LogarithmicAxis(getYLabel()) ;
    	chart.getXYPlot().setRangeAxis(yLogAxis);
    }

    public void setYAxis_to_Linear(){
    	NumberAxis yLinearAxis = new LogarithmicAxis(getYLabel()) ;
    	chart.getXYPlot().setRangeAxis(yLinearAxis);
    }

    //*********************************************************
    public void font(String name, int fontSize) {
        CheckExists();
        font = new Font(name, Font.PLAIN, fontSize);
        chart.getXYPlot().getDomainAxis().setLabelFont(font);
        chart.getXYPlot().getDomainAxis().setTickLabelFont(font);
        chart.getXYPlot().getRangeAxis().setLabelFont(font);
        chart.getXYPlot().getRangeAxis().setTickLabelFont(font);
        legend.setItemFont(font);
    }

    public void font(String name) {
        CheckExists();
        font = new Font(name, Font.PLAIN, font.getSize());
        chart.getXYPlot().getDomainAxis().setLabelFont(font);
        chart.getXYPlot().getDomainAxis().setTickLabelFont(font);
        chart.getXYPlot().getRangeAxis().setLabelFont(font);
        chart.getXYPlot().getRangeAxis().setTickLabelFont(font);
        legend.setItemFont(font);
    }


    public void font(int fontSize) {
        CheckExists();
        font = new Font(font.getName(), Font.PLAIN, fontSize);
        chart.getXYPlot().getDomainAxis().setLabelFont(font);
        chart.getXYPlot().getDomainAxis().setTickLabelFont(font);
        chart.getXYPlot().getRangeAxis().setLabelFont(font);
        chart.getXYPlot().getRangeAxis().setTickLabelFont(font);
        legend.setItemFont(font);
    }

    public void setFontSize(int fontSize) {
        CheckExists();
        font = new Font(font.getName(), Font.PLAIN, fontSize);
        chart.getXYPlot().getDomainAxis().setLabelFont(font);
        chart.getXYPlot().getDomainAxis().setTickLabelFont(font);
        chart.getXYPlot().getRangeAxis().setLabelFont(font);
        chart.getXYPlot().getRangeAxis().setTickLabelFont(font);
        legend.setItemFont(font);
    }

    //*********************************************************
    public void title(String title) {
        CheckExists();
        chart.setTitle(title);
    }

    public String getTitle() {
        CheckExists();
        return chart.getTitle().getText() ;
    }

    public void setTitle(String title) {
        CheckExists();
        chart.setTitle(title);
    }

    public void titleOFF(){
        chart.setTitle("");
    }

    public void xlim(double l, double u) {
        CheckExists();
        chart.getXYPlot().getDomainAxis().setRange(l, u);
    }

    public void setXlim(double l, double u) {
        CheckExists();
        chart.getXYPlot().getDomainAxis().setRange(l, u);
    }

    public double[] getXlim() {
        CheckExists();
        double xMin = chart.getXYPlot().getDomainAxis().getRange().getLowerBound() ;
        double xMax = chart.getXYPlot().getDomainAxis().getRange().getUpperBound() ;
        return new double[] {xMin, xMax} ;
    }

    public void ylim(double l, double u) {
        CheckExists();
        chart.getXYPlot().getRangeAxis().setRange(l, u);
    }

    public void setYlim(double l, double u) {
        CheckExists();
        chart.getXYPlot().getRangeAxis().setRange(l, u);
    }

    public double[] getYlim() {
        CheckExists();
        double yMin = chart.getXYPlot().getRangeAxis().getRange().getLowerBound() ;
        double yMax = chart.getXYPlot().getRangeAxis().getRange().getUpperBound() ;
        return new double[] {yMin, yMax} ;
    }

    public void xlabel(String label) {
        CheckExists();
        chart.getXYPlot().getDomainAxis().setLabel(label);
    }

    public void setXLabel(String label) {
        CheckExists();
        chart.getXYPlot().getDomainAxis().setLabel(label);
    }

    public void ylabel(String label) {
        CheckExists();
        chart.getXYPlot().getRangeAxis().setLabel(label);
    }

    public void setYLabel(String label) {
        CheckExists();
        chart.getXYPlot().getRangeAxis().setLabel(label);
    }

    public String getXLabel(){
    	CheckExists();
    	return chart.getXYPlot().getDomainAxis().getLabel() ;
    }

    public String getYLabel(){
    	CheckExists();
    	return chart.getXYPlot().getRangeAxis().getLabel() ;
    }

    //***************Customization of the figures************
    public void setFigColor(int figNumber, Color color){
    	colors.set(figNumber, color) ;
    	plotRenderer.setSeriesPaint(figNumber, color);
    	chart.getXYPlot().setRenderer(plotRenderer);
    }

    public void setFigColor(int figNumber, javafx.scene.paint.Color colorfx){
    	Color color = new Color((float) colorfx.getRed(), (float) colorfx.getGreen(), (float) colorfx.getBlue(), (float) colorfx.getOpacity()) ;
    	colors.set(figNumber, color) ;
    	plotRenderer.setSeriesPaint(figNumber, color);
    	chart.getXYPlot().setRenderer(plotRenderer);
    }

//    public void setFigLineWidth(int figNumber, float lineWidth){
//    	BasicStroke stroke = new BasicStroke(lineWidth, strokes.get(figNumber).getEndCap(), strokes.get(figNumber).getLineJoin(), strokes.get(figNumber).getMiterLimit(), strokes.get(figNumber).getDashArray(), strokes.get(figNumber).getDashPhase()) ;
//    	strokes.set(figNumber, stroke) ;
//    	plotRenderer.setSeriesStroke(figNumber, stroke);
//    	chart.getXYPlot().setRenderer(plotRenderer);
//    }

    public void setFigLineWidth(int figNumber, float lineWidth){
    	if(lineWidth>0.1f){
        	BasicStroke stroke = new BasicStroke(lineWidth, strokes.get(figNumber).getEndCap(), strokes.get(figNumber).getLineJoin(), strokes.get(figNumber).getMiterLimit(), strokes.get(figNumber).getDashArray(), strokes.get(figNumber).getDashPhase()) ;
        	strokes.set(figNumber, stroke) ;
        	plotRenderer.setSeriesStroke(figNumber, stroke);
        	chart.getXYPlot().setRenderer(plotRenderer);
    	}
    	else{
    		float[] dash_array = new float[2];
    		dash_array[0] = Float.MIN_VALUE; //visible
    		dash_array[1] = Float.MAX_VALUE; //invisible
    		BasicStroke stroke = new BasicStroke(0.0f,
    		    BasicStroke.CAP_SQUARE,
    		    BasicStroke.JOIN_ROUND,
    		    10f,
    		    dash_array,
    		    0f);
    		plotRenderer.setSeriesStroke(figNumber, stroke);
    	}

    }

    public void setFigLineStyle(int figNumber, String lineStyle){
    	float lineWidth = strokes.get(figNumber).getLineWidth() ;
        float dash[] = {5.0f};
        float dot[] = {lineWidth};
    	BasicStroke stroke = strokes.get(figNumber) ;
        if (lineStyle.contains("-"))
            stroke = new BasicStroke(lineWidth);
        else if (lineStyle.contains(":"))
            stroke = new BasicStroke(lineWidth, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);
        else if (lineStyle.contains("."))
            stroke = new BasicStroke(lineWidth, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 2.0f, dot, 0.0f);
        strokes.set(figNumber, stroke) ;
        plotRenderer.setSeriesStroke(figNumber, stroke);
        chart.getXYPlot().setRenderer(plotRenderer);
    }

    //*********************************************************
    public void legend(String position) {
        CheckExists();
        legend.setItemFont(font);
        legend.setBackgroundPaint(Color.WHITE);
        legend.setFrame(new BlockBorder(Color.BLACK));
        if (position.toLowerCase().equals("northoutside")) {
            legend.setPosition(RectangleEdge.TOP);
            chart.addLegend(legend);
        } else if (position.toLowerCase().equals("eastoutside")) {
            legend.setPosition(RectangleEdge.RIGHT);
            chart.addLegend(legend);
        } else if (position.toLowerCase().equals("southoutside")) {
            legend.setPosition(RectangleEdge.BOTTOM);
            chart.addLegend(legend);
        } else if (position.toLowerCase().equals("westoutside")) {
            legend.setPosition(RectangleEdge.LEFT);
            chart.addLegend(legend);
        } else if (position.toLowerCase().equals("north")) {
            legend.setPosition(RectangleEdge.TOP);
            XYTitleAnnotation ta = new XYTitleAnnotation(0.50,0.98,legend,RectangleAnchor.TOP);
            chart.getXYPlot().addAnnotation(ta);
        } else if (position.toLowerCase().equals("northeast")) {
            legend.setPosition(RectangleEdge.TOP);
            XYTitleAnnotation ta = new XYTitleAnnotation(0.98,0.98,legend,RectangleAnchor.TOP_RIGHT);
            chart.getXYPlot().addAnnotation(ta);
        } else if (position.toLowerCase().equals("east")) {
            legend.setPosition(RectangleEdge.RIGHT);
            XYTitleAnnotation ta = new XYTitleAnnotation(0.98,0.50,legend,RectangleAnchor.RIGHT);
            chart.getXYPlot().addAnnotation(ta);
        } else if (position.toLowerCase().equals("southeast")) {
            legend.setPosition(RectangleEdge.BOTTOM);
            XYTitleAnnotation ta = new XYTitleAnnotation(0.98,0.02,legend,RectangleAnchor.BOTTOM_RIGHT);
            chart.getXYPlot().addAnnotation(ta);
        } else if (position.toLowerCase().equals("south")) {
            legend.setPosition(RectangleEdge.BOTTOM);
            XYTitleAnnotation ta = new XYTitleAnnotation(0.50,0.02,legend,RectangleAnchor.BOTTOM);
            chart.getXYPlot().addAnnotation(ta);
        } else if (position.toLowerCase().equals("southwest")) {
            legend.setPosition(RectangleEdge.BOTTOM);
            XYTitleAnnotation ta = new XYTitleAnnotation(0.02,0.02,legend,RectangleAnchor.BOTTOM_LEFT);
            chart.getXYPlot().addAnnotation(ta);
        } else if (position.toLowerCase().equals("west")) {
            legend.setPosition(RectangleEdge.LEFT);
            XYTitleAnnotation ta = new XYTitleAnnotation(0.02,0.50,legend,RectangleAnchor.LEFT);
            chart.getXYPlot().addAnnotation(ta);
        } else if (position.toLowerCase().equals("northwest")) {
            legend.setPosition(RectangleEdge.TOP);
            XYTitleAnnotation ta = new XYTitleAnnotation(0.02,0.98,legend,RectangleAnchor.TOP_LEFT);
            chart.getXYPlot().addAnnotation(ta);
        }
    }

	public void legendON() {
        CheckExists();
        legend.setItemFont(font);
        legend.setBackgroundPaint(Color.WHITE);
        legend.setFrame(new BlockBorder(Color.BLACK));
        legend.setPosition(RectangleEdge.TOP);
        XYTitleAnnotation ta = new XYTitleAnnotation(0.98,0.98,legend,RectangleAnchor.TOP_RIGHT);
        chart.getXYPlot().addAnnotation(ta);
    }

    public void legendOFF(){
        chart.removeLegend();
        chart.getXYPlot().clearAnnotations();
    }

    //*********************************************************
    public void grid(String xAxis, String yAxis) {
        CheckExists();
        if (xAxis.equalsIgnoreCase("on")){
            chart.getXYPlot().setDomainGridlinesVisible(true);
            chart.getXYPlot().setDomainMinorGridlinesVisible(true);
            chart.getXYPlot().setDomainGridlinePaint(Color.GRAY);
        } else {
            chart.getXYPlot().setDomainGridlinesVisible(false);
            chart.getXYPlot().setDomainMinorGridlinesVisible(false);
        }
        if (yAxis.equalsIgnoreCase("on")){
            chart.getXYPlot().setRangeGridlinesVisible(true);
            chart.getXYPlot().setRangeMinorGridlinesVisible(true);
            chart.getXYPlot().setRangeGridlinePaint(Color.GRAY);
        } else {
            chart.getXYPlot().setRangeGridlinesVisible(false);
            chart.getXYPlot().setRangeMinorGridlinesVisible(false);
        }
    }


    //*********************************************************

    public JFreeChart configureForSave(){
    	JFreeChart chart = new JFreeChart(getRawXYPlot());
		try {
			chart = (JFreeChart) this.chart.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
  	  	// background color
  	  	chart.setBackgroundPaint(Color.WHITE);
  	  	// set xy plot stroke and paint
  	  	chart.getXYPlot().setOutlinePaint(Color.BLACK);
  	  	chart.getXYPlot().setOutlineStroke(new BasicStroke(2));
  	  	// setting up the font
  	  	// setting up the font
  	  	Font font = JFreeChart.DEFAULT_TITLE_FONT;
  	  	int fontSize = 20 ;
        font = new Font(font.getName(), Font.PLAIN, fontSize);
        chart.getXYPlot().getDomainAxis().setLabelFont(font);
        chart.getXYPlot().getDomainAxis().setTickLabelFont(font);
        chart.getXYPlot().getRangeAxis().setLabelFont(font);
        chart.getXYPlot().getRangeAxis().setTickLabelFont(font);
        return chart ;
    }

    public void saveas(String fileName, int width, int height) {
        CheckExists();
        File file = new File(fileName);
        try {
            ChartUtilities.saveChartAsJPEG(file,this.chart,width,height);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveAsJPEG(String fileName, int width, int height) {
        CheckExists();
        File file = new File(fileName);
        try {
            ChartUtilities.saveChartAsJPEG(file, this.chart, width, height);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveAsPNG(String fileName, int width, int height) {
        CheckExists();
        File file = new File(fileName);
        try {
            ChartUtilities.saveChartAsPNG(file, this.chart, width, height);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //***************Colors and Specs of the plot**************************

    public void FindColor(String spec, float lineWidth) {
        float dash[] = {5.0f};
        float dot[] = {lineWidth};
        Color color = Color.RED;                    // Default color is red
        BasicStroke stroke = new BasicStroke(lineWidth); // Default stroke is line
        if (spec.contains("-"))
            stroke = new BasicStroke(lineWidth);
        else if (spec.contains(":"))
            stroke = new BasicStroke(lineWidth, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);
        else if (spec.contains("."))
            stroke = new BasicStroke(lineWidth, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 2.0f, dot, 0.0f);
        if (spec.contains("y"))
            color = Color.YELLOW;
        else if (spec.contains("m"))
            color = Color.MAGENTA;
        else if (spec.contains("c"))
            color = Color.CYAN;
        else if (spec.contains("r"))
            color = Color.RED;
        else if (spec.contains("g"))
            color = Color.GREEN;
        else if (spec.contains("b"))
            color = Color.BLUE;
        else if (spec.contains("k"))
            color = Color.BLACK;
        colors.add(color);
        strokes.add(stroke);
    }

    public void FindColor(int colorId, float lineWidth) {
    	int r = (int) (Math.random() * 255) ;
    	int g = (int) (Math.random() * 255) ;
    	int b = (int) (Math.random() * 255) ;
        Color color = new Color(r, g, b) ;
        BasicStroke stroke = new BasicStroke(lineWidth);
        colors.add(color);
        strokes.add(stroke);
    }

    // color from java.awt
    public void FindColor(String spec, float lineWidth, Color color) {
        float dash[] = {5.0f};
        float dot[] = {lineWidth};
        BasicStroke stroke = new BasicStroke(lineWidth); // Default stroke is line
        if (spec.contains("-"))
            stroke = new BasicStroke(lineWidth);
        else if (spec.contains(":"))
            stroke = new BasicStroke(lineWidth, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);
        else if (spec.contains("."))
            stroke = new BasicStroke(lineWidth, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 2.0f, dot, 0.0f);
        colors.add(color);
        strokes.add(stroke);
    }

    // color from javafx
    public void FindColor(String spec, float lineWidth, javafx.scene.paint.Color colorfx) {
        float dash[] = {5.0f};
        float dot[] = {lineWidth};
        BasicStroke stroke = new BasicStroke(lineWidth); // Default stroke is line
        if (spec.contains("-"))
            stroke = new BasicStroke(lineWidth);
        else if (spec.contains(":"))
            stroke = new BasicStroke(lineWidth, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);
        else if (spec.contains("."))
            stroke = new BasicStroke(lineWidth, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 2.0f, dot, 0.0f);
        Color color = new Color((float) colorfx.getRed(), (float) colorfx.getGreen(), (float) colorfx.getBlue(), (float) colorfx.getOpacity()) ;
        colors.add(color);
        strokes.add(stroke);
    }

    public void setAllColors(javafx.scene.paint.Color colorfx){
    	Color color = new Color((float) colorfx.getRed(), (float) colorfx.getGreen(), (float) colorfx.getBlue(), (float) colorfx.getOpacity()) ;
    	int M = colors.size() ;
    	for(int i=0; i<M; i++){
    		colors.set(i, color) ;
    	}
    	RenderPlot();
    }

    public void setAllColors(Color color){
    	int M = colors.size() ;
    	for(int i=0; i<M; i++){
    		colors.set(i, color) ;
    	}
    	RenderPlot();
    }

    public void setSpecificChartColor(int datasetNumber, javafx.scene.paint.Color colorfx){
    	Color color = new Color((float) colorfx.getRed(), (float) colorfx.getGreen(), (float) colorfx.getBlue(), (float) colorfx.getOpacity()) ;
    	colors.set(datasetNumber, color) ;
    }

    public void setSpecificChartColor(int datasetNumber, Color color){
    	colors.set(datasetNumber, color) ;
    }

    public void setLastChartColor(javafx.scene.paint.Color colorfx){
    	Color color = new Color((float) colorfx.getRed(), (float) colorfx.getGreen(), (float) colorfx.getBlue(), (float) colorfx.getOpacity()) ;
    	int M = colors.size() ;
    	colors.set(M-1, color) ;
    }

    //*********************************************************
    public JFreeChart getRawChart(){
        return chart ;
    }

    public JFreeChart getChart(){
        return this.chart ;
    }

    public XYPlot getRawXYPlot(){
        return chart.getXYPlot() ;
    }

//    public void run(){
//        JFrame chartFrame = new JFrame() ;
//        chartFrame.setSize(640, 450);
//        chartFrame.getContentPane().add(new ChartPanel(chart)) ;
//        Image image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/People/Meisam/GUI/Plotters/MatlabPlotter/Extras/presentation.png"));
//        chartFrame.setIconImage(image);
//        chartFrame.setTitle("Plot Viewer v1.0");
//        chartFrame.setVisible(true);
//    }
//
//    public void run(boolean systemExit){
//        JFrame chartFrame = new JFrame() ;
//        chartFrame.setSize(640, 450);
//        chartFrame.getContentPane().add(new ChartPanel(chart)) ;
//        Image image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/People/Meisam/GUI/Plotters/MatlabPlotter/Extras/presentation.png"));
//        chartFrame.setIconImage(image);
//        chartFrame.setTitle("Plot Viewer v1.0");
//        chartFrame.setVisible(true);
//        if(systemExit){
//        	chartFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        }
//    }

    public void run(){
        // look and feel
/*        try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
    	CustomChartPanel cpanel = new CustomChartPanel(this.chart, 640, 450, 640, 450, 640, 450, true, true, true, true, true, true) ;
	    JFrame chartFrame = new JFrame() ;
	    chartFrame.add(cpanel) ;
	    chartFrame.setSize(640, 450);
        Image image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/presentation.png"));
        chartFrame.setIconImage(image);
        chartFrame.setTitle("Plot Viewer v1.0");
        chartFrame.setVisible(true);
    }

    public void run(boolean systemExit){
/*        try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
    	CustomChartPanel cpanel = new CustomChartPanel(this.chart, 640, 450, 640, 450, 640, 450, true, true, true, true, true, true) ;
	    JFrame chartFrame = new JFrame() ;
	    chartFrame.add(cpanel) ;
	    chartFrame.setSize(640, 450);
        Image image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/mathLib/plot/extra/presentation.png"));
        chartFrame.setIconImage(image);
        chartFrame.setTitle("Plot Viewer v1.0");
        chartFrame.setVisible(true);
        if(systemExit){
        	chartFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }
    }

    public void show() {
    	this.run();
    }

    public void show(boolean systemExit) {
    	this.run(systemExit);
    }

    public JFrame getChartJFrame(int width, int height){
        JFrame chartFrame = new JFrame() ;
        chartFrame.setSize(width, height);
        chartFrame.getContentPane().add(new ChartPanel(chart)) ;
        return chartFrame ;
    }

    public JFrame getChartJFrame(){
        JFrame chartFrame = new JFrame() ;
        chartFrame.setSize(640, 450);
        chartFrame.getContentPane().add(new ChartPanel(chart)) ;
        return chartFrame ;
    }

    public ChartPanel getChartPanel(){
    	return new CustomChartPanel(this.chart, 640, 450, 640, 450, 640, 450, true, true, true, true, true, true) ;
//        return new ChartPanel(chart) ;
    }

    public ChartPanel getChartPanel(int width, int height){
        ChartPanel panel = new ChartPanel(chart) ;
        panel.setPreferredSize(new Dimension(width, height));
        return panel ;
    }


    public SwingNode getChartSwingNode(){
        SwingNode chartSwingNode = new SwingNode() ;
        chartSwingNode.setContent(new ChartPanel(chart));
        return chartSwingNode ;
    }

    public SwingNode getChartSwingNode(int width, int height){
        SwingNode chartSwingNode = new SwingNode() ;
        chartSwingNode.resize(width, height);
        chartSwingNode.setContent(getChartPanel(width, height));
        return chartSwingNode ;
    }

}