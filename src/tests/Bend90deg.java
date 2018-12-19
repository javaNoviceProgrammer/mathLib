package tests;

import static java.lang.Math.*;

import static mathLib.func.GammaFunc.*;

import flanagan.integration.RungeKutta;
import flanagan.roots.RealRoot;
import flanagan.roots.RealRootFunction;
import mathLib.func.ArrayFunc;
import mathLib.ode.intf.DerivFunction1D;
import mathLib.ode.intf.DerivnFunction1D;
import mathLib.plot.MatlabChart;
import mathLib.util.ArrayUtils;
import mathLib.util.MathUtils;

public class Bend90deg {
	public static void main(String[] args) {
		double b = 2.49 ;
		double xi = (3.0*b-1.0)/(2.0*b) ;
		double R0 = 5 ;
		double a1 = sqrt(Math.PI)/2.0 * gamma(xi-0.5)/gamma(xi) ;
		double a2 = 0.73669 ;
		System.out.println(a1-a2);
		
		double[] xx0 = MathUtils.linspace(0, R0, 20) ;
		double[] yyR = new double[xx0.length] ;
		
		for(int i=0; i<xx0.length; i++) {
			double x0 = xx0[i] ;
			double A = (a1-a2)/(R0 - x0) ;
			
			RungeKutta rk = new RungeKutta() ;
			DerivnFunction1D func = new DerivnFunction1D() {
				
				@Override
				public double[] derivn(double x, double[] yy) {
					// z = y'
					double y = yy[0] ;
					double z = yy[1] ;
					double yprime = z ;
					double zprime = A * Math.pow(1+z*z, xi) ;
					return new double[] {yprime, zprime};
				}
			};
			
			rk.setStepSize(1e-4);
			rk.setInitialValueOfX(x0);
			rk.setFinalValueOfX(R0);
			rk.setInitialValueOfY(new double[] {R0-x0, 1});
			double[] yz = rk.fourthOrder(func) ;
			yyR[i] = yz[0] ;
		}

		MatlabChart fig = new MatlabChart() ;
		fig.plot(xx0, yyR);
		fig.renderPlot();
		fig.run(true);
		fig.markerON();
		
		//********************* solving for x0
		
		RealRootFunction funcX0 = new RealRootFunction() {
			
			@Override
			public double function(double x) {
				double A = (a1-a2)/(R0 - x) ;
				
				RungeKutta rk = new RungeKutta() ;
				DerivnFunction1D func = new DerivnFunction1D() {
					
					@Override
					public double[] derivn(double x, double[] yy) {
						// z = y'
						double y = yy[0] ;
						double z = yy[1] ;
						double yprime = z ;
						double zprime = A * Math.pow(1+z*z, xi) ;
						return new double[] {yprime, zprime};
					}
				};
				
				rk.setStepSize(1e-4);
				rk.setInitialValueOfX(x);
				rk.setFinalValueOfX(R0);
				rk.setInitialValueOfY(new double[] {R0-x, 1});
				double[] yz = rk.fourthOrder(func) ;
				return yz[0]-R0 ;
			}
		};
		
		RealRoot root = new RealRoot() ;
		double x0 = root.bisect(funcX0, 0, R0) ;
		System.out.println(x0);
		
		
		//************ now calculating the bend
		
		double A = (a1-a2)/(R0 - x0) ;
		double[] xx = MathUtils.linspace(x0, R0*0.995, 100) ;
		xx = ArrayUtils.concat(xx, MathUtils.linspace(0.995*R0, R0, 200)) ;
		double[] yy = new double[xx.length] ;
		double[] yyprime = new double[xx.length] ;
		
		for(int i=0; i<xx.length; i++) {
			
			RungeKutta rk = new RungeKutta() ;
			DerivnFunction1D func = new DerivnFunction1D() {
				
				@Override
				public double[] derivn(double x, double[] yy) {
					// z = y'
					double y = yy[0] ;
					double z = yy[1] ;
					double yprime = z ;
					double zprime = A * Math.pow(1+z*z, xi) ;
					return new double[] {yprime, zprime};
				}
			};
			
			rk.setStepSize(1e-4);
			rk.setInitialValueOfX(x0);
			rk.setFinalValueOfX(xx[i]);
			rk.setInitialValueOfY(new double[] {R0-x0, 1});
			double[] yz = rk.fourthOrder(func) ;
			yy[i] = yz[0] ;
			yyprime[i] = yz[1] ;
		}
		
		MatlabChart fig1 = new MatlabChart() ;
		fig1.plot(xx, yy, "r");
		fig1.renderPlot();
		fig1.run(true);
		
		
		MatlabChart fig2 = new MatlabChart() ;
		fig2.plot(xx, yyprime, "k");
		fig2.renderPlot();
		fig2.run(true);
		
		//************* calculating the curvature
		double[] C = ArrayFunc.apply(t -> A/Math.pow(1+t*t, 1/(2*b)), yyprime) ;
		double[] R = ArrayFunc.apply(t -> 1/t, C) ;
		
		MatlabChart fig3 = new MatlabChart() ;
		fig3.plot(xx, R, "g");
		fig3.renderPlot();
		fig3.run(true);
		
	}
	
	
	

}
