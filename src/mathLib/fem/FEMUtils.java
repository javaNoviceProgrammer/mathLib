package mathLib.fem;

import edu.uta.futureye.algebra.intf.Vector;
import mathLib.fitting.interpol.BilinearInterpolation2D;
import mathLib.func.ArrayFunc;
import mathLib.util.MathUtils;
import plotter.chart.ColorMapPlot;
import plotter.util.MeshGrid;

public class FEMUtils {
	
	public static void plotResult(MeshGrid grid, Vector u) {
		int nx = grid.getXDim() ;
		int ny = grid.getYDim() ;
		double[] vals1d = new double[u.getDim()] ;
		for(int i=0; i<u.getDim(); i++)
			vals1d[i] = u.get(i+1) ;
		double[][] sol = new double[nx][ny] ;
		for(int i=0; i<nx; i++)
			for(int j=0; j<ny; j++)
				sol[i][j] = vals1d[i+j*ny] ;
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
		for(int i=0; i<nx; i++)
			for(int j=0; j<ny; j++)
				sol[i][j] = vals1d[i+j*ny] ;
		
		double xmin = grid.getX(0, 0) ;
		double xmax = grid.getX(grid.getXDim()-1, 0) ;
		double[] xdense = MathUtils.linspace(xmin + 0.001, xmax - 0.001, 1000) ;
		double ymin = grid.getY(0, 0) ;
		double ymax = grid.getY(0, grid.getYDim()-1) ;
		double[] ydense = MathUtils.linspace(ymin + 0.001, ymax - 0.001, 1000) ;
		MeshGrid gridDense = new MeshGrid(xdense, ydense) ;
		BilinearInterpolation2D interpolate = new BilinearInterpolation2D(grid, sol) ;
		double[][] solDense = ArrayFunc.apply((r,s)-> interpolate.interpolate(r, s), gridDense) ;
		
		ColorMapPlot fig = new ColorMapPlot(gridDense, solDense) ;
		fig.run(true);
	}

}
