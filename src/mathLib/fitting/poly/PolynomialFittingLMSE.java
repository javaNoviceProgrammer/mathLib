package mathLib.fitting.poly;

import mathLib.fitting.lmse.LeastSquareFitter;
import mathLib.fitting.lmse.LeastSquareFunction;
import mathLib.fitting.lmse.MarquardtFitter;
import mathLib.plot.MatlabChart;
import mathLib.polynom.Polynomial;
import mathLib.util.MathUtils;
import mathLib.util.Timer;

public class PolynomialFittingLMSE {

	int degree ;
	Polynomial polyfit = null ;
	double[] valX, valY ;
	double[][] xData ;
	
	public PolynomialFittingLMSE(int degree) {
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
//		LeastSquareFitter fit = new NonLinearSolver(func) ;
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
	
	@Override
	public String toString() {
		return polyfit ;
	}

	// for test
	public static void main(String[] args) {
		double[] x = MathUtils.linspace(0.0, Math.PI, 100) ;
		double[] y = MathUtils.Arrays.Functions.sin(x) ;
		
		Timer timer = new Timer() ;
		timer.start();
		
		PolynomialFittingLMSE pFit = new PolynomialFittingLMSE(2) ;
		pFit.setData(x, y);
		pFit.fit();
		
		timer.stop();
		System.out.println(timer);
		
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
