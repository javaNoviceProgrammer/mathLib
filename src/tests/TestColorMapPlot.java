package tests;

import edu.uta.futureye.function.intf.MathFunc;
import mathLib.util.MathUtils;
import plotter.chart.ColorMapPlot;
import plotter.util.MeshGrid;
import plotter.util.ColorMap.ColorMapName;

import static edu.uta.futureye.function.FMath.*;

public class TestColorMapPlot {
	public static void main(String[] args) {
		double[] xVal = MathUtils.linspace(-5*Math.PI, 5*Math.PI, 500) ;
		double[] yVal = MathUtils.linspace(0.0, 10.0, 500) ;
		MeshGrid grid = new MeshGrid(xVal, yVal) ;
		double[][] fVal = new double[yVal.length][xVal.length] ;
		MathFunc f = sin(x)*y ;
		for(int i=0; i<yVal.length; i++) {
			for(int j=0; j<xVal.length; j++) {
				fVal[i][j] = f.apply(grid.getX(i, j), grid.getY(i, j)) ;
			}
		}
		ColorMapPlot fig = new ColorMapPlot(grid, fVal) ;
		fig.setColorMap(ColorMapName.Rainbow);
		fig.run(true);
	}

}
