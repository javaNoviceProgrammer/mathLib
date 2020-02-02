package mathLib.ode.fdm;

import java.awt.Color;

import mathLib.matrix.Matrix;
import mathLib.matrix.sys.LinearSystem;
import mathLib.ode.solvers.DerivFunction;
import mathLib.ode.solvers.OdeSolver;
import mathLib.plot.MatlabChart;
import mathLib.root.RealRoot;
import mathLib.root.RealRootFunction;
import mathLib.util.MathUtils;


public class Example1 {

	public static void main(String[] args) {
//		test1() ;
//		test2() ;
		test3() ;
//		test4() ;
		test5() ;
	}

	public static void test1() {
		// y' = -y + 2x with y(0) = 1 (IVP)
		DerivFunction func = (x,y) -> -y+2.0*x ;
		double x0 = 0.0, y0 = 1.0 ;
		OdeSolver ode = new OdeSolver(func, x0, y0) ;
		double[] xvals = MathUtils.linspace(x0, x0+10.0, 1000) ;
		double[] yvals = ode.fehlberg(xvals) ;

		MatlabChart fig = new MatlabChart() ;
		fig.plot(xvals, yvals, "b", 2f, "Fehlberg");
		fig.renderPlot();
		fig.xlabel("X");
		fig.ylabel("y(x)");
		fig.font(17);
		fig.legendON();
		fig.show(true);
	}

	public static void test2() {
		// solving with Finite Difference Method (FDM)
		// y' = -y + 2x with y(0) = 1 (IVP) --> (D+I) Y = 2X
		double x0 = 0.0 ;
		double xN = x0 + 10.0 ;
		double y0 = 1.0 ;
		int gridSize = 501 ;
		int N = gridSize-1 ;
		// row matrix for xvals
		Matrix xvals = MathUtils.linspace(x0, xN, N+1) ;
		double dx = xvals.getElement(0, 1) - xvals.getElement(0, 0) ; // x[1]-x[0]
		System.out.println(dx);
		double[] yvals = new double[N] ; // [y1, y2, ..., yN]: y0 is known
		ForwardDiffOperator diff = new ForwardDiffOperator(N+1, dx) ; // N+1 equations
		Matrix identity = Matrix.identity(N+1) ;
		Matrix rightHand = 2.0*xvals ;
		Matrix coeffMatrix = diff.getDxMatrix() + identity ;
		// impose boundary conditions
		rightHand.setElement(0, 0, 2.0*x0-coeffMatrix.getElement(0, 0)*y0) ;
		// solve A*Y = b = 2*X with A = D+I
		double[][] A = coeffMatrix.getBlock(0, N-1, 1, N).getData() ;
		double[] b = rightHand.getBlock(0, 0, 0, N-1).getData()[0] ;
		new LinearSystem(A, b, yvals) ;
		yvals = MathUtils.Arrays.concat(new double[]{y0}, yvals) ;

		MatlabChart fig = new MatlabChart() ;
		fig.plot(xvals.getData()[0], yvals, "r", 2f, "FDM");
		fig.renderPlot();
		fig.xlabel("X");
		fig.ylabel("y(x)");
		fig.font(17);
		fig.legendON();
		fig.show(true);
	}

	public static void test3() {
		// y' = y + 2x with y(10) = 1
		// direct method (backward solution) --> IVP + negative step size (h<0)
		DerivFunction func = (x,y) -> y+2.0*x ;
		double x0 = 10.0, y0 = 1.0 ;
		OdeSolver ode = new OdeSolver(func, x0, y0) ;
		double[] xvals = MathUtils.linspace(x0, x0-10.0, 1000) ;
		double[] yvals = ode.fehlberg(xvals) ;

		MatlabChart fig = new MatlabChart() ;
		fig.plot(xvals, yvals, "b", 2f, "Fehlberg");
		fig.renderPlot();
		fig.xlabel("X");
		fig.ylabel("y(x)");
		fig.font(17);
		fig.legendON();
		fig.show(true);
	}

	public static void test4() {
		// y' = y + 2x with y(10) = 1
		// shooting method: y(0) = ?
		DerivFunction func = (x,y) -> y+2.0*x ;
		double x0 = 0.0 ;
		OdeSolver ode = new OdeSolver(func, x0, 0.0) ;
		RealRootFunction funcY0 = y0 -> {
			ode.setY0(y0);
			double y1 = ode.fehlberg(x0+10) ;
			return y1-1 ; // equation: y1-1 = 0 (enforcing the final condition)
		} ;
		RealRoot rootFinder = new RealRoot(funcY0) ;
		double y0 = rootFinder.newton(10.0, 100) ;
		System.out.println("Found y0 = " + y0);
		double[] xvals = MathUtils.linspace(x0, x0+10.0, 1000) ;
		ode.setY0(y0);
		double[] yvals = ode.fehlberg(xvals) ; // solving forward with h>0

		MatlabChart fig = new MatlabChart() ;
		fig.plot(xvals, yvals, "b", 2f, "Fehlberg");
		fig.renderPlot();
		fig.xlabel("X");
		fig.ylabel("y(x)");
		fig.font(17);
		fig.legendON();
		fig.setFigColor(0, Color.RED);
		fig.show(true);
	}

	public static void test5() {
		// solving with Finite Difference Method (FDM)
		double x0 = 0.0 ;
		double xN = x0 + 10.0 ;
		double yN = 1.0 ;
		int gridSize = 501 ; // gridSize = N+1
		int N = gridSize-1 ;
		Matrix xvals = MathUtils.linspace(x0, xN, N+1) ;
		double dx = xvals.getElement(0, 1) - xvals.getElement(0, 0) ;
		System.out.println(dx);
		double[] yvals = new double[N] ; // [y0, y1, ..., yN-1]
		ForwardDiffOperator diff = new ForwardDiffOperator(N+1, dx) ;
		Matrix identity = Matrix.identity(N+1) ;
		Matrix rightHand = 2.0*xvals ;
		Matrix coeffMatrix = diff.getDxMatrix() - identity ;
		// impose boundary conditions
		rightHand.setElement(0, N-1, 2.0*xvals.getElement(0, N-1)-coeffMatrix.getElement(N-1, N)*yN) ;
		// solve A*Y = b = 2*X with A = D-I
		double[][] A = coeffMatrix.getBlock(0, N-1, 0, N-1).getData() ;
		double[] b = rightHand.getBlock(0, 0, 0, N-1).getData()[0] ;
		new LinearSystem(A, b, yvals) ;
		yvals = MathUtils.Arrays.concat(yvals, new double[]{yN}) ;

		MatlabChart fig = new MatlabChart() ;
		fig.plot(xvals.getData()[0], yvals, "k", 2f, "FDM");
		fig.renderPlot();
		fig.xlabel("X");
		fig.ylabel("y(x)");
		fig.font(17);
		fig.legendON();
		fig.show(true);
	}

}
