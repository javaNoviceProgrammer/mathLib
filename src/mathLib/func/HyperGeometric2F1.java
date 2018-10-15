package mathLib.func;

import mathLib.integral.Integral1D;
import mathLib.integral.intf.IntegralFunction1D;
import mathLib.plot.MatlabChart;
import mathLib.util.MathUtils;

public class HyperGeometric2F1 {

	double a, b, c ;

	public HyperGeometric2F1(double a, double b, double c) {
		this.a = a ;
		this.b = b ;
		this.c = c ;
	}

	private double getCoeff(){
		double coeff = GammaFunc.gamma(c)/(GammaFunc.gamma(b) * GammaFunc.gamma(c-b)) ;
		return coeff ;
	}

	private double getIntegral(double x){
		IntegralFunction1D func = new IntegralFunction1D() {
			public double function(double t) {
				return Math.pow(t, b-1)*Math.pow(1-t, c-b-1)*Math.pow(1-t*x, -a);
			}
		};
		Integral1D integral = new Integral1D(func, 0, 1) ;
		integral.setNumPoints(10);
		integral.setErrorBound(1e-12);
		return integral.getIntegral() ;
	}

	public double getValue(double x){
		return getCoeff()*getIntegral(x) ;
	}

	// for test
	public static void main(String[] args){
		HyperGeometric2F1 hg = new HyperGeometric2F1(0.5, (3*2.93-1)/(2*2.93), 1.5) ;
		double[] x = MathUtils.linspace(-1, 10, 100) ;
		double[] y = new double[x.length] ;
		for(int i=0; i<x.length; i++){
			y[i] = x[i] * hg.getValue(-x[i]*x[i]) ;
//			System.out.println(y[i]);
		}
		MatlabChart fig = new MatlabChart() ;
		fig.plot(x, y);
		fig.RenderPlot();
		fig.run(true);
	}

}
