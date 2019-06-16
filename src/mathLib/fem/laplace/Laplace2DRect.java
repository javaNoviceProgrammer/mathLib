package mathLib.fem.laplace;


import java.util.HashMap;
import java.util.Map;

import com.sun.swing.internal.plaf.basic.resources.basic;

import mathLib.fem.FEMUtils;
import mathLib.fem.assembler.Assembler;
import mathLib.fem.core.Mesh;
import mathLib.fem.core.Node;
import mathLib.fem.core.NodeType;
import mathLib.fem.core.intf.FiniteElement;
import mathLib.fem.element.FELinearTriangle;
import mathLib.fem.mesh.Mesh2DRect;
import mathLib.fem.mesh.mesh2d.Mesh2DRectangleElement;
import mathLib.fem.mesh.mesh2d.Mesher2D;
import mathLib.fem.util.Utils;
import mathLib.fem.weakform.WeakForm;
import mathLib.func.ArrayFunc;
import mathLib.func.symbolic.MultiVarFunc;
import mathLib.func.symbolic.basic.DiscreteIndexFunction;
import mathLib.func.symbolic.basic.FX;
import mathLib.func.symbolic.intf.MathFunc;
import mathLib.geometry.algebra.Point;
import mathLib.matrix.algebra.intf.Matrix;
import mathLib.matrix.algebra.intf.Vector;
import mathLib.matrix.algebra.solver.Solver;
import mathLib.plot.ColorMapPlot;
import mathLib.plot.MeshPlot;
import mathLib.plot.util.MeshGrid;
import mathLib.util.MathUtils;
import mathLib.util.Timer;
import sun.net.www.content.image.gif;


public class Laplace2DRect {

	/*
	 * Solving the laplace problem Laplace(V) = 0,
	 */

	public static void main(String[] args) {

		double a = 1.0 ;
		double b = 2.0 ; 
		
		Timer timer = new Timer() ;
		timer.start();
		
		Mesh2DRect mesh2d = new Mesh2DRect(Point.getInstance(0, 0), Point.getInstance(a, b), 50, 50) ;
		Mesh mesh = mesh2d.getMesh() ;

//		Mesh2DRectangleElement rect1 = new Mesh2DRectangleElement("rect1", 0.0, 0.0, a, b, 50) ;
////		rect1.refine(5);
//		Mesher2D mesher2d = new Mesher2D() ;
//		mesher2d.addElement(rect1);
//		mesher2d.triangulate();
////		mesher2d.getCanvas().run(true);
////		mesher2d.showNodeNumbers(1e-2, 1e-2);
//		Mesh mesh = mesher2d.getMesh() ;
		
		
		timer.stop();
		System.out.println(timer);

//		System.out.println(mesh.getNodeList().size());

//		System.out.println(mesh.getNodeList().at(51));
//		System.out.println(mesh.getNodeList().at(52));
//
//		Node node51 = mesh.getNodeList().at(51) ;
//		Node node52 = mesh.getNodeList().at(52) ;
//		Node node = new Node(52, node51.coord(1), node51.coord(2)) ;
//		System.out.println(node51.equals(node52));


		// Compute geometry relationship between nodes and elements
		mesh.computeNodeBelongsToElements();
		// 2.Mark boundary type(s)
		Map<NodeType, MathFunc> mapNTF = new HashMap<>() ;
		mapNTF.put(NodeType.Dirichlet, null) ;
		mesh.markBorderNode(mapNTF);

		// 3. Weak form definition
		FiniteElement fe = new FELinearTriangle() ; // Linear triangular finite element
		MathFunc f = 0*FX.x+0*FX.y ; //Right hand side (RHS)
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
				else if(Math.abs(y-b)<eps)
					return Math.sin(Math.PI/1.0 * x) ;
				else if(Math.abs(x)<eps)
					return 0.0 ;
				else
					return 0.0;
			}
		}) ;



		// 5. Solve the linear system
		Solver solver = new Solver() ;
		Vector u = solver.solveAuto(stiff, load) ;

//		System.out.println(u.getDim());
		double[] vals1d = new double[u.getDim()] ;
		for(int i=0; i<u.getDim(); i++) {
			vals1d[i] = u.get(i+1) ;
//			System.out.println(vals1d[i]);
		}
			


		

		// 6. Output the result to a MATLAB chart
		
		MeshPlot plot = new MeshPlot(mesh, u) ;
		plot.run(true);

//		MeshGrid grid = mesh2d.getGrid() ;

//		FEMUtils.plotResult(grid, u);
//		FEMUtils.plotResultDense(grid, u, 5, 5);
		
//		double[] x = MathUtils.linspace(0.0, a, 1000) ;
//		double[] y = MathUtils.linspace(0.0, b, 1000) ;
//		MeshGrid grid = new MeshGrid(x, y) ;
//		double[][] solExact = ArrayFunc.apply((r,s)-> Math.sin(Math.PI*r)*Math.sinh(Math.PI*s)/Math.sinh(Math.PI) , grid) ;
//		ColorMapPlot figExact = new ColorMapPlot(grid, solExact) ;
//		figExact.run(true);

		

	}

}
