package mathLib.func;

import mathLib.func.intf.RealFunction;
import mathLib.func.intf.RealFunction2D;
import mathLib.func.intf.RealFunction3D;
import mathLib.plot.MatlabChart;
import mathLib.plot.util.MeshGrid;
import mathLib.util.MathUtils;

public class ArrayFunc {

	public static double[] apply(RealFunction func, double[] var0) {
		int n = var0.length ;
		double[] var1 = new double[n] ;
		for(int i=0; i<n; i++)
			var1[i] = func.evaluate(var0[i]) ;
		return var1 ;
	}
	
	public static double[][] apply(RealFunction2D func, double[] var0, double[] var1) {
		int n = var0.length ;
		int m = var1.length ;
		double[][] var2 = new double[n][m] ;
		for(int i=0; i<n; i++)
			for(int j=0; j<m; j++)
				var2[i][j] = func.evaluate(var0[i], var1[j]) ;
		return var2 ;
	}
	
	public static double[][] apply(RealFunction2D func, MeshGrid mesh) {
		int n = mesh.getXDim() ;
		int m = mesh.getYDim() ;
		double[][] var2 = new double[n][m] ;
		for(int i=0; i<n; i++)
			for(int j=0; j<m; j++)
				var2[i][j] = func.evaluate(mesh.getX(i, j), mesh.getY(i, j)) ;
		return var2 ;
	}
	
	public static double[][][] apply(RealFunction3D func, double[] var0, double[] var1, double[] var2) {
		int n = var0.length ;
		int m = var1.length ;
		int k = var2.length ;
		double[][][] var3 = new double[n][m][k] ;
		for(int i=0; i<n; i++)
			for(int j=0; j<m; j++)
				for(int r=0; r<k; r++)
					var3[i][j][r] = func.evaluate(var0[i], var1[j], var2[r]) ;
		return var3 ;
	}

	// for test
	public static void main(String[] args) {
		double[] x = MathUtils.linspace(0, 10, 1000) ;
		double[] y = apply(t -> BesselFunc.j0(t), x) ;
		double[] z = apply(t -> BesselFunc.j1(t), x) ;
		MatlabChart fig = new MatlabChart() ;
		fig.plot(x, y, "b");
		fig.plot(x, z, "r");
		fig.renderPlot();
		fig.setFigLineWidth(0, 2);
		fig.run(true);
	}

}
