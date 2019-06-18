package mathLib.fem.laplace;


import java.util.HashMap;
import java.util.Map;

import mathLib.fem.MeshPlot2D;
import mathLib.fem.assembler.Assembler;
import mathLib.fem.core.Mesh;
import mathLib.fem.core.NodeType;
import mathLib.fem.core.intf.FiniteElement;
import mathLib.fem.element.FELinearTriangle;
import mathLib.fem.mesh.mesh2d.Mesh2DRectangleElement;
import mathLib.fem.mesh.mesh2d.Mesher2D;
import mathLib.fem.util.Utils;
import mathLib.fem.weakform.WeakForm;
import mathLib.func.symbolic.MultiVarFunc;
import mathLib.func.symbolic.Variable;
import mathLib.func.symbolic.basic.FX;
import mathLib.func.symbolic.basic.Vector2MathFunc;
import mathLib.func.symbolic.intf.MathFunc;
import mathLib.matrix.algebra.intf.Matrix;
import mathLib.matrix.algebra.intf.Vector;
import mathLib.matrix.algebra.solver.Solver;
import mathLib.util.Timer;


public class Laplace2DRect {

	/*
	 * Solving the laplace problem Laplace(V) = 0,
	 */

	public static void main(String[] args) {

		double a = 1.0 ;
		double b = 1.0 ;

		Timer timer = new Timer() ;
		timer.start();

		// create mesh
		Mesh2DRectangleElement rect1 = new Mesh2DRectangleElement("rect1", 0.0, 0.0, a, b) ;
		rect1.refine(1);
		Mesher2D mesher2d = new Mesher2D() ;
		mesher2d.addElement(rect1);
		mesher2d.triangulate();
		mesher2d.getCanvas().run(true);
		mesher2d.showNodeNumbers(1e-2, 1e-2);
		Mesh mesh = mesher2d.getMesh() ;

		// Compute geometry relationship between nodes and elements
		mesh.computeNodeBelongsToElements();

		// Mark boundary type(s)
		Map<NodeType, MathFunc> mapNTF = new HashMap<>() ;
		mapNTF.put(NodeType.Dirichlet, null) ;
		mesh.markBorderNode(mapNTF);

		// Weak form definition
		FiniteElement fe = new FELinearTriangle() ; // Linear triangular finite element
		MathFunc f = 0*FX.x+0*FX.y ; //Right hand side (RHS)
		WeakForm wf = new WeakForm(fe,
				(u,v) -> -u.diff("x")*v.diff("x")-u.diff("y")*v.diff("y") ,
				v -> f * v
			);
		wf.compile();

		// Assembly and boundary condition(s)
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
				else if(Math.abs(y-b)<eps)
					return Math.sin(Math.PI/1.0 * x) ;
				else if(Math.abs(x)<eps)
					return 0.0 ;
				else
					return 0.0;
			}
		}) ;

		// Solve the linear system
		Solver solver = new Solver() ;
		Vector u = solver.solveAuto(stiff, load) ;

		timer.stop();
		System.out.println(timer);

		// Output the result to a MATLAB chart

		MeshPlot2D plot = new MeshPlot2D(mesh, u) ;
		plot.setPlotDensity(20);
		plot.run(true);

		// interpolate the result over the mesh

		Vector2MathFunc func = new Vector2MathFunc(u, mesh, "x", "y") ;
		Variable v = new Variable().set("x", 0.8).set("y", 0.8) ;
		System.out.println(func.apply(v));


//		double[][] solExact = ArrayFunc.apply((r,s)-> Math.sin(Math.PI*r)*Math.sinh(Math.PI*s)/Math.sinh(Math.PI) , grid) ;
//		ColorMapPlot figExact = new ColorMapPlot(grid, solExact) ;
//		figExact.run(true);

	}


}
