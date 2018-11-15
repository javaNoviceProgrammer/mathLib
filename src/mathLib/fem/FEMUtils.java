package mathLib.fem;

import mathLib.fitting.interpol.BilinearInterpolation2D;
import mathLib.func.ArrayFunc;
import mathLib.matrix.algebra.intf.Vector;
import mathLib.plot.ColorMapPlot;
import mathLib.plot.util.MeshGrid;
import mathLib.util.ArrayUtils;
import mathLib.util.MathUtils;

public class FEMUtils {

	public static void plotResult(MeshGrid grid, Vector u) {
		int nx = grid.getXDim() ;
		int ny = grid.getYDim() ;
		double[] vals1d = new double[u.getDim()] ;
		for(int i=0; i<u.getDim(); i++)
			vals1d[i] = u.get(i+1) ;
		double[][] sol = new double[nx][ny] ;
		sol = ArrayUtils.reshapeColumn(vals1d, nx, ny) ;
		ColorMapPlot fig = new ColorMapPlot(grid, sol) ;
		fig.run(true);
	}

	public static void plotResultDense(MeshGrid grid, Vector u) {
		int nx = grid.getXDim() ;
		int ny = grid.getYDim() ;
		double[] vals1d = new double[u.getDim()] ;
		for(int i=0; i<u.getDim(); i++)
			vals1d[i] = u.get(i+1) ;
		double[][] sol = new double[nx][ny] ;
		sol = ArrayUtils.reshapeColumn(vals1d, nx, ny) ;

		double xmin = grid.getX(0, 0) ;
		double xmax = grid.getX(grid.getXDim()-1, 0) ;
		double[] xdense = MathUtils.linspace(xmin*1.0001, xmax*0.9999, 1000) ;
		double ymin = grid.getY(0, 0) ;
		double ymax = grid.getY(0, grid.getYDim()-1) ;
		double[] ydense = MathUtils.linspace(ymin*1.0001, ymax*0.9999, 1000) ;
		MeshGrid gridDense = new MeshGrid(xdense, ydense) ;
		BilinearInterpolation2D interpolate = new BilinearInterpolation2D(grid, sol) ;
		double[][] solDense = ArrayFunc.apply((r,s)-> interpolate.interpolate(r, s), gridDense) ;

		ColorMapPlot fig = new ColorMapPlot(gridDense, solDense) ;
		fig.run(true);
	}

	public static void plotResultDense(MeshGrid grid, Vector u, int scaleX, int scaleY) {
		int nx = grid.getXDim() ;
		int ny = grid.getYDim() ;
		double[] vals1d = new double[u.getDim()] ;
		for(int i=0; i<u.getDim(); i++)
			vals1d[i] = u.get(i+1) ;
		double[][] sol = new double[nx][ny] ;
		sol = ArrayUtils.reshapeColumn(vals1d, nx, ny) ;

		double xmin = grid.getX(0, 0) ;
		double xmax = grid.getX(grid.getXDim()-1, 0) ;
		double[] xdense = MathUtils.linspace(xmin*1.001, xmax*0.999, nx*scaleX) ;
		double ymin = grid.getY(0, 0) ;
		double ymax = grid.getY(0, grid.getYDim()-1) ;
		double[] ydense = MathUtils.linspace(ymin*1.001, ymax*0.999, nx*scaleY) ;
		MeshGrid gridDense = new MeshGrid(xdense, ydense) ;
		BilinearInterpolation2D interpolate = new BilinearInterpolation2D(grid, sol) ;
		double[][] solDense = ArrayFunc.apply((r,s)-> interpolate.interpolate(r, s), gridDense) ;

		ColorMapPlot fig = new ColorMapPlot(gridDense, solDense) ;
		fig.run(true);
	}

}
