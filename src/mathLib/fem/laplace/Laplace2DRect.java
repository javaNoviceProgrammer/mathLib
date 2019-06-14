package mathLib.fem.laplace;


import static mathLib.func.symbolic.FMath.x;
import static mathLib.func.symbolic.FMath.y;

import java.util.HashMap;
import java.util.Map;

import mathLib.fem.FEMUtils;
import mathLib.fem.assembler.Assembler;
import mathLib.fem.core.Mesh;
import mathLib.fem.core.NodeType;
import mathLib.fem.core.intf.FiniteElement;
import mathLib.fem.element.FELinearTriangle;
import mathLib.fem.mesh.Mesh2DRect;
import mathLib.fem.util.Utils;
import mathLib.fem.weakform.WeakForm;
import mathLib.func.symbolic.MultiVarFunc;
import mathLib.func.symbolic.intf.MathFunc;
import mathLib.geometry.algebra.Point;
import mathLib.matrix.algebra.intf.Matrix;
import mathLib.matrix.algebra.intf.Vector;
import mathLib.matrix.algebra.solver.Solver;
import mathLib.plot.util.MeshGrid;
import mathLib.util.Timer;

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
					return Math.sin(Math.PI/2.0 * x) ;
				else if(Math.abs(x)<eps)
					return Math.sin(Math.PI/2.0 * y) ;
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

//		FEMUtils.plotResult(grid, u);
		FEMUtils.plotResultDense(grid, u, 5, 5);

//		double[][] solExact = ArrayFunc.apply((a,b)-> Math.sin(Math.PI*a)*Math.sinh(Math.PI*b)/Math.sinh(Math.PI) , grid) ;
//		ColorMapPlot figExact = new ColorMapPlot(grid, solExact) ;
//		figExact.run(true);

	}

}
