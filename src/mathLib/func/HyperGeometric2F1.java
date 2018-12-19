package mathLib.func;

import mathLib.integral.Integral1D;
import mathLib.integral.intf.IntegralFunction1D;

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
		integral.setNumPoints(200);
		integral.setErrorBound(1e-12);
		return integral.getIntegral() ;
	}

	public double getValue(double x){
		return getCoeff()*getIntegral(x) ;
	}

	// for test
	public static void main(String[] args){
		double b = 3.0 ;
		HyperGeometric2F1 hg = new HyperGeometric2F1(0.5, (3*b-1)/(2*b), 1.5) ;
//		double[] x = MathUtils.linspace(-1, 10, 15) ;
//		double[] y = new double[x.length] ;
//		for(int i=0; i<x.length; i++){
//			y[i] = x[i] * hg.getValue(-x[i]*x[i]) ;
////			System.out.println(y[i]);
//		}
//		MatlabChart fig = new MatlabChart() ;
//		fig.plot(x, y);
//		fig.RenderPlot();
//		fig.run(true);

//		double x = 100.0 ;
//		System.out.println(x * hg.getValue(-x*x));

		System.out.println(hg.getValue(-10));

//		double[] b = MathUtils.linspace(1.1, 10, 10) ;
//		RealFunction func = new RealFunction() {
//
//			@Override
//			public double evaluate(double b) {
//				HyperGeometric2F1 hg = new HyperGeometric2F1(0.5, (3*b-1)/(2*b), 1.5) ;
//				double f = 500 ;
//				double y = f * hg.getValue(-f*f) ;
//				return y;
//			}
//		};
//		double[] y = ArrayFunc.apply(t -> func.evaluate(t), b) ;
//		MatlabChart fig = new MatlabChart() ;
//		fig.plot(b, y);
//		fig.RenderPlot();
//		fig.run(true);
	}


}
