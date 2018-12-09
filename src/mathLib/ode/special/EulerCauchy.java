package mathLib.ode.special;

import static mathLib.func.symbolic.FMath.cos;
import static mathLib.func.symbolic.FMath.log;
import static mathLib.func.symbolic.FMath.pow;
import static mathLib.func.symbolic.FMath.sin;
import static mathLib.func.symbolic.FMath.x;

import mathLib.func.ArrayFunc;
import mathLib.func.symbolic.intf.MathFunc;
import mathLib.plot.MatlabChart;
import mathLib.util.MathUtils;

/**
 * Euler-Cauchy ODE:
 * 					x^2 * y'' + a*x*y' + b*y = 0
 * 					general solution: y = x^m
 * 					auxiliary equation: m^2 + (a-1)*m + b = 0
 * 					Three cases:
 * 								1. Two distinct real roots: y = c1 * x^m1 + c2 * x^m2
 * 								2. double real roots: y = (c1 + c2*ln(x)) x^m
 * 								3. Two complex roots alpha +- j beta: y = (c1 * cos(beta*ln(x)) + c2 * sin(beta * ln(x)) * x^alpha ;
 * @author meisam
 *
 */

public class EulerCauchy {

	double a, b ;
	MathFunc sol1, sol2 ;

	public EulerCauchy(
			double a,
			double b
			) {
		this.a = a ;
		this.b = b ;
	}

	public void solve() {
		// step 1: test auxiliary equation
		double delta = (a-1)*(a-1) - 4*b ;
		double eps = 1e-12 ;
		if(delta > eps) {
			double m1 = (-(a-1) + Math.sqrt(delta))/2.0 ;
			sol1 = pow(x, m1) ;
			double m2 = (-(a-1) - Math.sqrt(delta))/2.0 ;
			sol2 = pow(x, m2) ;
		}
		else if(delta < -eps) {
			double real = -(a-1)/2.0 ;
			double imag = Math.sqrt(-delta)/2.0 ;
			sol1 = pow(x, real) * cos(imag*log(x)) ;
			sol2 = pow(x, real) * sin(imag*log(x)) ;
		}
		else {
			double m = -(a-1)/2.0 ;
			sol1 = pow(x, m) ;
			sol2 = log(x)*pow(x, m) ;
		}
	}

	public MathFunc getFirstSol(){
		return sol1 ;
	}

	public MathFunc getSecondSol() {
		return sol2 ;
	}

	public void plotSol1(double x0, double x1) {
		MatlabChart fig = new MatlabChart() ;
		double[] x = MathUtils.linspace(x0, x1, 1000) ;
		double[] y = ArrayFunc.apply(t -> sol1.apply(t), x) ;
		fig.plot(x, y);
		fig.renderPlot();
		fig.run(true);
	}

	public void plotSol2(double x0, double x1) {
		MatlabChart fig = new MatlabChart() ;
		double[] x = MathUtils.linspace(x0, x1, 1000) ;
		double[] y = ArrayFunc.apply(t -> sol2.apply(t), x) ;
		fig.plot(x, y);
		fig.renderPlot();
		fig.run(true);
	}

	public MathFunc getGeneralSol(double x0, double y0, double yPrime0) {
		double y1 = sol1.apply(x0) ;
		double y1Prime = sol1.diff("x").apply(x0) ;
		double y2 = sol2.apply(x0) ;
		double y2Prime = sol2.diff("x").apply(x0) ;
		double c1 = (y0*y2Prime - yPrime0 * y2)/(y1*y2Prime - y1Prime * y2) ;
		double c2 = (y1*yPrime0 - y0 * y1Prime)/(y1*y2Prime - y1Prime * y2) ;
		return c1*sol1+c2*sol2 ;
	}

	public void plotSol(MathFunc sol, double x0, double x1) {
		MatlabChart fig = new MatlabChart() ;
		double[] x = MathUtils.linspace(x0, x1, 1000) ;
		double[] y = ArrayFunc.apply(t -> sol.apply(t), x) ;
		fig.plot(x, y);
		fig.renderPlot();
		fig.run(true);
	}

	public String generalSol() {
		return "c1 " + sol1.getExpr() + " + c2 " + sol2.getExpr() ;
	}

	@Override
	public String toString() {
		String s1 = "x^2 y'' " ;
		String s2 = "" ;
		if(getSigned(a) != "")
			s2 = getSigned(a) + " x y' " ;
		String s3 = "" ;
		if(getSigned(b) != "")
			s3 = getSigned(b) + " y" ;

		return s1+s2+s3+" = 0" ;
	}

	private String getSigned(double t) {
		if(t>0)
			return "+ " + String.format("%.4f", Math.abs(t)) ;
		else if(t<0)
			return "- " + String.format("%.4f", Math.abs(t)) ;
		else
			return "" ;
	}

	// for test
	public static void main(String[] args) {
		double a = -3 ;
		double b = 10 ;
		EulerCauchy ec = new EulerCauchy(a, b) ;
		System.out.println(ec);
		ec.solve();

		System.out.println(ec.getFirstSol());
		System.out.println(ec.getSecondSol());

		System.out.println(ec.generalSol());

//		MathFunc sol = ec.getGeneralSol(1.0, 0, 1) ;
//		System.out.println(sol);
//
//		System.out.println(sol.apply(1));
//		System.out.println(sol.diff("x").apply(1));
//		ec.plotSol(sol, 0.05, 2);

	}

}
