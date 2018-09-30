package tests;

import static edu.uta.futureye.function.FMath.cos;
import static edu.uta.futureye.function.FMath.sin;
import static edu.uta.futureye.function.FMath.x;
import static edu.uta.futureye.function.FMath.y;

import edu.uta.futureye.function.intf.MathFunc;
import mathLib.plot.ColorMapPlot;
import mathLib.plot.util.ColorMap.ColorMapName;
import mathLib.plot.util.MeshGrid;
import mathLib.util.MathUtils;

public class TestColorMapPlot {
	public static void main(String[] args) {
		double[] xVal = MathUtils.linspace(-5*Math.PI, 5*Math.PI, 1000) ;
		double[] yVal = MathUtils.linspace(0.0, 10.0, 1000) ;
		MeshGrid grid = new MeshGrid(xVal, yVal) ;
		double[][] fVal = new double[xVal.length][yVal.length] ;
		MathFunc f = sin(x)*cos(y) ;
		for(int i=0; i<xVal.length; i++) {
			for(int j=0; j<yVal.length; j++) {
				fVal[i][j] = f.apply(grid.getX(i, j), grid.getY(i, j)) ;
			}
		}
		ColorMapPlot fig = new ColorMapPlot(grid, fVal) ;
		fig.setColorMap(ColorMapName.Rainbow);
		fig.run(true);
	}

}
