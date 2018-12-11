package mathLib.fitting;

import mathLib.fitting.lmse.LeastSquareFitter;
import mathLib.fitting.lmse.LeastSquareFunction;
import mathLib.fitting.lmse.MarquardtFitter;
import mathLib.func.ArrayFunc;
import mathLib.plot.MatlabChart;
import mathLib.polynom.Polynomial;
import mathLib.polynom.Rational;
import mathLib.util.ArrayUtils;
import mathLib.util.MathUtils;
import mathLib.util.Timer;

public class RationalFitting {

	double[] valX, valY ;
	double[][] xData ;
	int m, n ;
	Rational rational ;
	
	/**
	 * single term: p(x)/q(x)
	 * 
	 */
	
	public RationalFitting(int m, int n) {
		this.m = m ;
		this.n = n ;
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
				return (m+n+2) ;
			}
			
			@Override
			public int getNInputs() {
				return 1;
			}
			
			@Override
			public double evaluate(double[] values, double[] parameters) {
				double x = values[0] ;
				double[] pCoeff = new double[m+1] ;
				double[] qCoeff = new double[n+1] ;
				for(int i=0; i<m+1; i++)
					pCoeff[i] = parameters[i] ;
				for(int j=0; j<n+1; j++)
					qCoeff[j] = parameters[m+1+j] ;
				Polynomial numerator = new Polynomial(pCoeff) ;
				Polynomial denominator = new Polynomial(qCoeff) ;
				Rational r = new Rational(numerator, denominator) ;
				return r.evaluate(x) ;
			}
		};
		
		// setting up the least square fitting
		LeastSquareFitter fit = new MarquardtFitter(func) ;
//		LeastSquareFitter fit = new NonLinearSolver(func) ;
		fit.setParameters(ArrayUtils.setValue(1, m+n+2));
		fit.setData(xData, valY);
		fit.fitData();
		double[] params = fit.getParameters() ;
		double[] pCoeff = new double[m+1] ;
		double[] qCoeff = new double[n+1] ;
		for(int i=0; i<m+1; i++)
			pCoeff[i] = params[i] ;
		for(int j=0; j<n+1; j++)
			qCoeff[j] = params[m+1+j] ;
		Polynomial numerator = new Polynomial(pCoeff) ;
		Polynomial denominator = new Polynomial(qCoeff) ;
		rational = new Rational(numerator, denominator) ;
		
	}
	
	public double interpolate(double var) {
		return rational.evaluate(var) ;
	}
	
	@Override
	public String toString() {
		return rational;
	}

	// for test
	public static void main(String[] args) {
		double[] x = MathUtils.linspace(1, 2*Math.PI, 100) ;
		double[] y = ArrayFunc.apply(t -> 1/t*1/t, x) ;
		
		Timer timer = new Timer() ;
		timer.start();
		RationalFitting pFit = new RationalFitting(2, 3) ;
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
		fig.renderPlot();
		fig.markerON();
		fig.run(true);

		
	}
	
}
