package mathLib.ode.special;

import static mathLib.func.symbolic.FMath.C;
import static mathLib.numbers.Complex.j;
import static mathLib.numbers.ComplexMath.absSquared;

import flanagan.integration.DerivnFunction;
import flanagan.integration.RungeKutta;
import mathLib.func.symbolic.SingleVarFunc;
import mathLib.func.symbolic.intf.MathFunc;
import mathLib.numbers.Complex;
import mathLib.plot.MatlabChart;
import mathLib.util.MathUtils;

/**
 * solving ordinary differential equation of the form  y' = b(x) y + f(x)
 *
 *	b(x) and f(x) can be null.
 *  y(x) = yr(x) + j * yi(x)
 */

public class OdeComplexFirstOrder extends RungeKutta implements DerivnFunction {

	MathFunc br, fr ;
	MathFunc bi, fi ;

	public OdeComplexFirstOrder(MathFunc br, MathFunc bi, MathFunc fr, MathFunc fi) {
		if(br == null)
			br = C(0) ;
		if(bi == null)
			bi = C(0) ;
		if(fr == null)
			fr = C(0) ;
		if(fi == null)
			fi = C(0) ;

		this.br = br ;
		this.bi = bi ;
		this.fr = fr ;
		this.fi = fi ;
	}

	@Override
	public double[] derivn(double x, double[] y) {
		// yr' = g(x, y)
		// yi' = h(x,y)
		double yr = y[0] ;
		double yi = y[1] ;
		double g = fr.apply(x) + br.apply(x) * yi - bi.apply(x) * yr  ;
		double h = fi.apply(x) + bi.apply(x) * yr + br.apply(x) * yi ;
		return new double[] {g, h} ;
	}

	public void setInitialValueOfY(Complex y0) {
		this.setInitialValueOfY(new double[] {y0.re(), y0.im()});
	}

	@Override
	public String toString() {
		return "dy/dx = " + "("+br.getExpr()+"+ j* " + bi.getExpr()+") " + " y + " + "("+fr.getExpr()+"+ j* " + fi.getExpr()+") ";
	}

	// for test
	public static void main(String[] args) {
		double lambdaRes = 1550e-9 ;
		double omegaRes = 3e8/lambdaRes*2*Math.PI ;
		double lambdaLaser = 1551e-9 ;
		double omegaLaser = 3e8/lambdaLaser*2*Math.PI ;
		double tauI = 10e-10 ;
		double tauC = 10e-10 ;
		double mu = Math.sqrt(2.0/tauC) ;
		double Tb = 1.0/1e9 ;

		MathFunc si = new SingleVarFunc("si", "x") {

			@Override
			public double apply(double... args) {
				double t = args[0] ;
				if(0<t && t<Tb)
//				if(0<t )
					return 1 ;
				else
					return 0 ;
			}

			@Override
			public String getExpr() {
				return "step(x)";
			}


		};

		OdeComplexFirstOrder ode = new OdeComplexFirstOrder(-C(1.0/tauI + 1.0/tauC), C(omegaRes-omegaLaser), C(0), -mu*si) ;
		ode.setInitialValueOfX(-Tb);
		ode.setInitialValueOfY(0+j*0);
		ode.setStepSize(1e-2*tauI);
		double[] xVal = MathUtils.linspace(-Tb, 5*Tb, 1000) ;
		double[][] y = new double[xVal.length][2];
		double[] yr = new double[xVal.length] ;
		double[] yi = new double[xVal.length] ;
		double[] sor = new double[xVal.length] ;
		double[] soi = new double[xVal.length] ;
		double[] soSquared = new double[xVal.length] ;

		double[] input = new double[xVal.length] ;

		for(int i=0; i<xVal.length; i++) {
			ode.setFinalValueOfX(xVal[i]);
			y[i] = ode.fourthOrder(ode) ;
			yr[i] = y[i][0] ;
			yi[i] = y[i][1] ;
			sor[i] = si.apply(xVal[i]) + mu*yi[i] ;
			soi[i] = -mu * yr[i] ;
			soSquared[i] = absSquared(sor[i] + j * soi[i]*0) ;
			input[i] = si.apply(xVal[i]) ;
		}

		System.out.println(ode);

		MatlabChart fig = new MatlabChart() ;
		fig.plot(xVal, input, "b");
		fig.plot(xVal, soSquared, "r");
		fig.renderPlot();
//		fig.markerON();
		fig.xlabel("X");
		fig.ylabel("Solution Y");
		fig.run(true);
	}

}
