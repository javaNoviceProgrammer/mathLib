package mathLib.fitting;

import mathLib.fitting.lmse.LeastSquareFitter;
import mathLib.fitting.lmse.LeastSquareFunction;
import mathLib.fitting.lmse.MarquardtFitter;
import mathLib.polynom.Polynomial;
import mathLib.util.MathUtils;
import plotter.chart.MatlabChart;

public class PolynomialFitting {

	int degree ;
	Polynomial polyfit = null ;
	double[] valX, valY ;
	double[][] xData ;
	
	public PolynomialFitting(int degree) {
		this.degree = degree ;
	}
	
	public void setData(double[] valX, double[] valY) {
		this.valX = valX ;
		this.valY = valY ;
		xData = new double[valX.length][1] ;
		for(int i=0; i<valX.length; i++)
			xData[i][0] = valX[i] ;
	}
	
	public void fit() {
		// step 1: Least square function
		LeastSquareFunction func = new LeastSquareFunction() {
			
			@Override
			public int getNParameters() {
				return degree + 1 ;
			}
			
			@Override
			public int getNInputs() {
				return 1;
			}
			
			@Override
			public double evaluate(double[] values, double[] parameters) {
				double x = values[0] ;
				polyfit = new Polynomial(parameters) ;
				return polyfit.evaluate(x);
			}
		};
		
		// setting up the least square fitting
		LeastSquareFitter fit = new MarquardtFitter(func) ;
		fit.setParameters(MathUtils.Arrays.setValue(1.0, degree+1));
		fit.setData(xData, valY);
		fit.fitData();
		polyfit = new Polynomial(fit.getParameters()) ;
	}
	
	public double interpolate(double var) {
		return polyfit.evaluate(var) ;
	}
	
	public Polynomial getPolynomial() {
		return polyfit ;
	}
	
	
	// for test
	public static void main(String[] args) {
		double[] x = MathUtils.linspace(0.0, 2.0, 100) ;
		double[] y = MathUtils.Arrays.Functions.sinc(x) ;
		PolynomialFitting pFit = new PolynomialFitting(6) ;
		pFit.setData(x, y);
		pFit.fit();
		
		MatlabChart fig = new MatlabChart() ;
		fig.plot(x, y, "b");
		
		double[] z = new double[x.length] ;
		for(int i=0; i<z.length; i++)
			z[i] = pFit.interpolate(x[i]) ;
		
		fig.plot(x, z, "r");
		fig.RenderPlot();
		fig.markerON();
		fig.run(true);
		
		System.out.println(pFit.getPolynomial());
	}
	
}
