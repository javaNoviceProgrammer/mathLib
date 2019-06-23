package tests;


import java.util.HashMap;
import java.util.Map;

import Jama.EigenvalueDecomposition;
import mathLib.fem.assembler.Assembler;
import mathLib.fem.core.Mesh;
import mathLib.fem.core.NodeType;
import mathLib.fem.core.intf.FiniteElement;
import mathLib.fem.element.FELinearLine1D;
import mathLib.fem.util.MeshGenerator;
import mathLib.fem.util.Utils;
import mathLib.fem.weakform.WeakForm;
import mathLib.func.symbolic.MultiVarFunc;
import mathLib.func.symbolic.basic.FX;
import mathLib.func.symbolic.intf.MathFunc;
import mathLib.matrix.algebra.intf.Matrix;
import mathLib.matrix.algebra.intf.Vector;
import mathLib.numbers.Complex;
import mathLib.util.Timer;


public class Laplace1DEigenVal {

	/*
	 * Solving the laplace problem Laplace(V) = 0,
	 */

	public static void main(String[] args) {

		double a = 2.0 ;

		Timer timer = new Timer() ;
		timer.start();

		// create mesh

		Mesh mesh = MeshGenerator.linear1D(0.0, a, 100) ;

		// Compute geometry relationship between nodes and elements
		mesh.computeNodeBelongsToElements();

		// Mark boundary type(s)
		Map<NodeType, MathFunc> mapNTF = new HashMap<>() ;
		mapNTF.put(NodeType.Dirichlet, null) ;
		mesh.markBorderNode(mapNTF);

		// Weak form definition
		FiniteElement fe = new FELinearLine1D() ; // Linear triangular finite element
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
		Utils.imposeDirichletCondition(stiff, load, fe, mesh, new MultiVarFunc("bc", "x") {

			@Override
			public double apply(double... var1) {
				return 0.0 ;
			}
		}) ;

		// Weak form definition
		FiniteElement fe2 = new FELinearLine1D() ; // Linear triangular finite element
		MathFunc f2 = 0*FX.x+0*FX.y ; //Right hand side (RHS)
		WeakForm wf2 = new WeakForm(fe2,
				(u,v) -> u*v ,
				v -> f2 * v
			);
		wf2.compile();

		// Assembly and boundary condition(s)
		Assembler assembler2 = new Assembler(mesh, wf2) ;
		assembler2.assembleGlobal();
		Matrix stiff2 = assembler2.getGlobalStiffMatrix();
		Vector load2 = assembler2.getGlobalLoadVector();

		// Apply zero Dirichlet boundary condition
		Utils.imposeDirichletCondition(stiff2, load2, fe2, mesh, new MultiVarFunc("bc", "x") {

			@Override
			public double apply(double... var1) {
				return 0.0 ;
			}
		}) ;
		
		int m = stiff.getRowDim() ;
		int n = stiff.getColDim() ;
		double[][] mat = new double[m][n] ;
		double[][] mat2 = new double[m][n] ;
		for(int i=0; i<m; i++) {
			for(int j=0; j<n; j++) {
				mat[i][j] = stiff.get(i+1, j+1) ;
				mat2[i][j] = stiff2.get(i+1, j+1) ;
			}
		}
		
		mathLib.matrix.Matrix matrix1 = mat ;
		mathLib.matrix.Matrix matrix2 = mat2 ;
		
		mathLib.matrix.Matrix matTot = matrix2.inv() * matrix1 ;
		EigenvalueDecomposition eigen = new EigenvalueDecomposition(matTot.getJamaMatrix()) ;
		
		double[] reals = eigen.getRealEigenvalues() ;
		double[] imags = eigen.getImagEigenvalues() ;
		for(int i=0; i<reals.length; i++) {
			System.out.println(reals[i]+Complex.j * imags[i]);
		}
		
		
		
		
		
		
		
		
		
		
		
		
		

	}


}
