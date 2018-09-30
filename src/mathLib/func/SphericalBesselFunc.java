package mathLib.func;

import static edu.uta.futureye.function.FMath.cosh;
import static edu.uta.futureye.function.FMath.exp;
import static edu.uta.futureye.function.FMath.sinh;
import static edu.uta.futureye.function.FMath.x;
import static java.lang.Math.cosh;
import static java.lang.Math.exp;
import static java.lang.Math.sinh;

import edu.uta.futureye.function.intf.MathFunc;
import mathLib.util.MathUtils;

public class SphericalBesselFunc {
	
	// first kind

	public static double in(int n, double x) {
		if(n<0)
			return in(-n, x)*MathUtils.minusOnePower(n) ;
		if(n==0)
			return sinh(x)/x ;
		if(n==1)
			return (x*cosh(x)-sinh(x))/(x*x) ;

		return gn(n, x)*sinh(x) + gn(-(n+1), x)*cosh(x) ;
	}

	private static double gn(int n, double x) {
		if(n==0)
			return 1.0/x ;
		if(n==1)
			return -1.0/(x*x) ;
		if(n <= -1)
			return gn(n+2, x) + (2*n+3)/x * gn(n+1, x) ;

		return gn(n-2, x)-(2*n-1)/x * gn(n-1, x) ;
	}
	
	public static MathFunc in(int n) {
		if(n<0)
			return in(-n)*MathUtils.minusOnePower(n) ;
		if(n==0)
			return sinh(x)/x ;
		if(n==1)
			return (x*cosh(x)-sinh(x))/(x*x) ;

		return gn(n)*sinh(x) + gn(-(n+1))*cosh(x) ;
	}
	
	private static MathFunc gn(int n) {
		if(n==0)
			return 1.0/x ;
		if(n==1)
			return -1.0/(x*x) ;
		if(n <= -1)
			return gn(n+2) + (2*n+3)/x * gn(n+1) ;

		return gn(n-2)-(2*n-1)/x * gn(n-1) ;
	}
	
	
	
	// second kind
	
	public static double kn(int n, double x) {
		if(n==0)
			return exp(-x)/x ;
		if(n==1)
			return exp(-x)*(x+1)/(x*x) ;
		
		return fn(n, x) * Math.exp(-x) ;
	}

	private static double fn(int n, double x) {
		if(n==0)
			return 1.0/x ;
		if(n==1)
			return (x+1.0)/(x*x) ;
		if(n <= -1)
			return fn(n, x) - (2*n-1)/x * fn(n-1, x);

		return fn(n-2, x) + (2*n-1)/x * fn(n-1, x)  ;
	}
	
	public static MathFunc kn(int n) {
		if(n==0)
			return exp(-x)/x ;
		if(n==1)
			return exp(-x)*(x+1)/(x*x) ;
		
		return fn(n) * exp(-x) ;
	}
	
	private static MathFunc fn(int n) {
		if(n==0)
			return 1.0/x ;
		if(n==1)
			return (x+1.0)/(x*x) ;
		if(n <= -1)
			return fn(n) - (2*n-1)/x * fn(n-1);

		return fn(n-2) + (2*n-1)/x * fn(n-1)  ;
	}


	// for test
	public static void main(String[] args) {
//		double[] x = MathUtils.linspace(1, 6.0, 1000) ;
//		double[] y0 = ArrayFunc.apply(t -> kn(0, t), x) ;
//		double[] y1 = ArrayFunc.apply(t -> kn(1, t), x) ;
//		double[] y2 = ArrayFunc.apply(t -> kn(2, t), x) ;
//
//		MatlabChart fig = new MatlabChart() ;
//		fig.plot(x, y0, "b");
//		fig.plot(x, y1, "r");
//		fig.plot(x, y2, "m");
//		fig.RenderPlot();
//		fig.run(true);

		System.out.println(kn(0));
		System.out.println(kn(1));
		System.out.println(kn(2));
		System.out.println(kn(3));
	}

}
