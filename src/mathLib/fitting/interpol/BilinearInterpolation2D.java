package mathLib.fitting.interpol;

import mathLib.func.ArrayFunc;
import mathLib.util.ArrayUtils;
import mathLib.util.MathUtils;
import mathLib.util.Timer;
import plotter.chart.ColorMapPlot;
import plotter.util.MeshGrid;

/**
 * Bilinear Interpolation procedure
 * f(x,y) = a0 + a1*x + a2*y + a3*x*y
 */

public class BilinearInterpolation2D {

	double[] x, y ;
	double[][] func ;

	public BilinearInterpolation2D(double[] x, double[] y, double[][] func) {
		this.x = x ;
		this.y = y ;
		this.func = func ;
	}

	public BilinearInterpolation2D(MeshGrid mesh, double[][] func) {
		x = new double[mesh.getXDim()] ;
		for(int i=0; i<x.length; i++)
			x[i] = mesh.getX(i, 0) ;
		y = new double[mesh.getYDim()] ;
		for(int i=0; i<y.length; i++)
			y[i] = mesh.getY(0, i) ;
		this.func = func ;
	}

	public double interpolate(double var1, double var2) {
		int k = ArrayUtils.getIntervalIndex(x, var1) ;
		int s = ArrayUtils.getIntervalIndex(y, var2) ;
		if(k == -1 || s == -1)
			throw new IllegalArgumentException("the values are outside the range") ;
		if(k==0)
			k++ ;
		if(s==0)
			s++ ;
		double[] a = getCoeffs(x[k-1], x[k], y[s-1], y[s], func[k-1][s-1], func[k-1][s], func[k][s-1], func[k][s]) ;
		return a[0] + a[1]*var1 + a[2]*var2 + a[3]*var1*var2 ;
	}

	private double[] getCoeffs(double x1, double x2, double y1, double y2, double func11, double func12, double func21, double func22) {
		double[] coeff = new double[4] ;
		double a = 1.0/((x1-x2)*(y1-y2)) ;
		coeff[0] = a * (func11 *x2*y2 - func12 *x2*y1 - func21 *x1*y2 + func22 *x1*y1) ;
		coeff[1] = a * (-func11 *y2 + func12 *y1 + func21 *y2 - func22 *y1) ;
		coeff[2] = a * (-func11 *x2 + func12 *x2 + func21 *x1 - func22 *x1) ;
		coeff[3] = a * (func11 - func12 - func21 + func22) ;

		return coeff ;
	}

	// for test
	public static void main(String[] args) {
		double[] x = MathUtils.linspace(0, Math.PI, 30) ;
		double[] y = MathUtils.linspace(0, Math.PI, 30) ;
		MeshGrid mesh = new MeshGrid(x, y) ;
		double[][] func = ArrayFunc.apply((r,s)-> Math.sin(r+s), mesh) ;
		new ColorMapPlot(mesh, func).run(true);

		double[] xx = MathUtils.linspace(0, Math.PI, 500) ;
		double[] yy = MathUtils.linspace(0, Math.PI, 500) ;
		MeshGrid mesh1 = new MeshGrid(xx, yy) ;

		Timer timer = new Timer() ;
		timer.start();

		BilinearInterpolation2D interpolate = new BilinearInterpolation2D(mesh, func) ;
		double[][] vals = ArrayFunc.apply((r,s) -> interpolate.interpolate(r, s), mesh1) ;

		timer.stop();
		System.out.println(timer);

		new ColorMapPlot(mesh1, vals).run(true);

	}

}
