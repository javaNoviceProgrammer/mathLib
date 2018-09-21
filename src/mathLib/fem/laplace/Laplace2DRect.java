package mathLib.fem.laplace;

import static edu.uta.futureye.function.FMath.x;
import static edu.uta.futureye.function.FMath.y;

import java.util.HashMap;
import java.util.Map;

import edu.uta.futureye.algebra.intf.Matrix;
import edu.uta.futureye.algebra.intf.Vector;
import edu.uta.futureye.algebra.solver.Solver;
import edu.uta.futureye.core.Mesh;
import edu.uta.futureye.core.NodeType;
import edu.uta.futureye.core.intf.FiniteElement;
import edu.uta.futureye.function.MultiVarFunc;
import edu.uta.futureye.function.intf.MathFunc;
import edu.uta.futureye.lib.assembler.Assembler;
import edu.uta.futureye.lib.element.FELinearTriangle;
import edu.uta.futureye.lib.weakform.WeakForm;
import edu.uta.futureye.util.Utils;
import mathLib.fem.FEMUtils;
import mathLib.fem.Mesh2DRect;
import mathLib.func.ArrayFunc;
import mathLib.geometry.algebra.Point;
import mathLib.util.Timer;
import plotter.chart.ColorMapPlot;
import plotter.util.MeshGrid;

public class Laplace2DRect {
	
	/*
	 * Solving the laplace problem Laplace(V) = 0, 
	 */
	
	public static void main(String[] args) {
		Mesh2DRect mesh2d = new Mesh2DRect(Point.getInstance(0, 0), Point.getInstance(1, 1), 50, 50) ;
		Mesh mesh = mesh2d.getMesh() ;
		// Compute geometry relationship between nodes and elements
		mesh.computeNodeBelongsToElements();
		// 2.Mark boundary type(s)
		Map<NodeType, MathFunc> mapNTF = new HashMap<>() ;
		mapNTF.put(NodeType.Dirichlet, null) ;
		mesh.markBorderNode(mapNTF);
		
//		System.out.println(mesh.getNodeList());
//		System.out.println(mesh.getBoundaryNodeList());


		// 3. Weak form definition
		FiniteElement fe = new FELinearTriangle() ; // Linear triangular finite element
		MathFunc f = 0*x+0*y ; //Right hand side (RHS)
		WeakForm wf = new WeakForm(fe,
				(u,v) -> -u.diff("x")*v.diff("x")-u.diff("y")*v.diff("y") , 
				v -> f * v
			);
		wf.compile();

		// 4. Assembly and boundary condition(s)
		Assembler assembler = new Assembler(mesh, wf) ;
		assembler.assembleGlobal();
		Matrix stiff = assembler.getGlobalStiffMatrix();
		Vector load = assembler.getGlobalLoadVector();
		
		// Apply zero Dirichlet boundary condition
		Utils.imposeDirichletCondition(stiff, load, fe, mesh, new MultiVarFunc("bc", "x", "y") {
			
			@Override
			public double apply(double... var1) {
				double eps = 1e-5 ;
				double x = var1[0] ;
				double y = var1[1] ;
				if(Math.abs(y)<eps)
					return 0.0 ;
				else if(Math.abs(y-1)<eps)
					return Math.sin(Math.PI/1.0 * x) ;
				else if(Math.abs(x)<eps)
					return 0.0 ;
				else
					return 0.0;
			}
		}) ;
		
		Timer timer = new Timer() ;
		timer.start();
		
		// 5. Solve the linear system
		Solver solver = new Solver() ;
		Vector u = solver.solveAuto(stiff, load) ;
		
//		System.out.println(u.getDim());
		double[] vals1d = new double[u.getDim()] ;
		for(int i=0; i<u.getDim(); i++)
			vals1d[i] = u.get(i+1) ;
		
		timer.stop();
		System.out.println(timer);
		
		// 6. Output the result to a MATLAB chart
		MeshGrid grid = mesh2d.getGrid() ;
//		int nx = grid.getXDim() ;
//		int ny = grid.getYDim() ;
//
//		for(int i=0; i<u.getDim(); i++)
//			vals1d[i] = u.get(i+1) ;
//		double[][] sol = new double[nx][ny] ;
//		for(int i=0; i<nx; i++)
//			for(int j=0; j<ny; j++)
//				sol[i][j] = vals1d[i+j*ny] ;
//		ColorMapPlot fig = new ColorMapPlot(grid, sol) ;
//		fig.run(true);
//		
//		double[] xdense = MathUtils.linspace(0.001, 0.999, 1000) ;
//		double[] ydense = MathUtils.linspace(0.001, 0.999, 1000) ;
//		MeshGrid gridDense = new MeshGrid(xdense, ydense) ;
//		BilinearInterpolation2D interpolate = new BilinearInterpolation2D(grid, sol) ;
//		double[][] solDense = ArrayFunc.apply((r,s)-> interpolate.interpolate(r, s), gridDense) ;
//		ColorMapPlot fig3 = new ColorMapPlot(gridDense, solDense) ;
//		fig3.run(true);
		
		FEMUtils.plotResult(grid, u);
		FEMUtils.plotResultDense(grid, u);
		
		double[][] solExact = ArrayFunc.apply((a,b)-> Math.sin(Math.PI*a)*Math.sinh(Math.PI*b)/Math.sinh(Math.PI) , grid) ;
		ColorMapPlot figExact = new ColorMapPlot(grid, solExact) ;
		figExact.run(true);
	}

}
