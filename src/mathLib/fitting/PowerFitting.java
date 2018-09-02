package mathLib.fitting;

import mathLib.fitting.lmse.LeastSquareFitter;
import mathLib.fitting.lmse.LeastSquareFunction;
import mathLib.fitting.lmse.MarquardtFitter;
import mathLib.util.MathUtils;
import mathLib.util.Timer;
import plotter.chart.MatlabChart;

public class PowerFitting {

	double[] valX, valY ;
	double[][] xData ;
	double a , b ;
	
	/**
	 * single term: a * x^b
	 * 
	 */
	
	public PowerFitting() {
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
				return 2 ;
			}
			
			@Override
			public int getNInputs() {
				return 1;
			}
			
			@Override
			public double evaluate(double[] values, double[] parameters) {
				double x = values[0] ;
				double a = parameters[0] ;
				double b = parameters[1] ;
				return a*Math.pow(x, b);
			}
		};
		
		// setting up the least square fitting
		LeastSquareFitter fit = new MarquardtFitter(func) ;
//		LeastSquareFitter fit = new NonLinearSolver(func) ;
		fit.setParameters(new double[] {1.0, 1.0});
		fit.setData(xData, valY);
		fit.fitData();
		a = fit.getParameters()[0] ;
		b = fit.getParameters()[1] ;
	}
	
	public double interpolate(double var) {
		return a*Math.pow(var, b) ;
	}
	
	@Override
	public String toString() {
		return a + " * x^" + b + ")";
	}

	// for test
	public static void main(String[] args) {
		double[] x = MathUtils.linspace(0.0, 1*Math.PI, 100) ;
		double[] y = MathUtils.Arrays.Functions.pow(x, 5) ;
		
		Timer timer = new Timer() ;
		timer.start();
		PowerFitting pFit = new PowerFitting() ;
		pFit.setData(x, y);
		pFit.fit();
		timer.stop();
		System.out.println(timer);
		
		System.out.println(pFit);

		MatlabChart fig = new MatlabChart() ;
		fig.plot(x, y, "b");

		double[] z = new double[x.length] ;
		for(int i=0; i<z.length; i++)
			z[i] = pFit.interpolate(x[i]) ;

		fig.plot(x, z, "r");
		fig.RenderPlot();
		fig.markerON();
		fig.run(true);

		
	}
	
}
