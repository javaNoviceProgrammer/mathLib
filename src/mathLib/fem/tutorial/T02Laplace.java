package mathLib.fem.tutorial;

import java.util.HashMap;

import mathLib.fem.assembler.AssemblerScalar;
import mathLib.fem.core.Element;
import mathLib.fem.core.Mesh;
import mathLib.fem.core.NodeType;
import mathLib.fem.util.MeshGenerator;
import mathLib.fem.weakform.WeakFormBuilder;
import mathLib.func.symbolic.FMath;
import mathLib.func.symbolic.SingleVarFunc;
import mathLib.func.symbolic.intf.MathFunc;
import mathLib.func.symbolic.intf.ScalarShapeFunction;
import mathLib.matrix.algebra.intf.Matrix;
import mathLib.matrix.algebra.intf.Vector;
import mathLib.matrix.algebra.solver.external.SolverJBLAS;


/**
 * <blockquote><pre>
 * Problem:
 *   -\Delta{u} = f
 *   u(x,y)=0, (x,y) \in \partial{\Omega}
 * where
 *   \Omega = [-3,3]*[-3,3]
 *   f = -2*(x^2+y^2)+36
 * Solution:
 *   u = (x^2-9)*(y^2-9)
 * </blockquote></pre>
 * 
 * @author liuyueming
 */
public class T02Laplace {
	public Mesh mesh;
	public Vector u;
	public void run() {
        Mesh mesh = MeshGenerator.linear1D(0.0, 5.0, 1000) ;
        mesh.computeNodeBelongsToElements();

        HashMap<NodeType, MathFunc> mapNTF =
                new HashMap<NodeType, MathFunc>();
        mapNTF.put(NodeType.Dirichlet, null);
        mesh.markBorderNode(mapNTF);


        MathFunc f = FMath.x ;
        f.setName("f") ;
        //4.Weak form
        WeakFormBuilder wfb = new WeakFormBuilder(f) {

			@Override
			public MathFunc makeExpression(Element e, Type type) {
				MathFunc f = getParam("f", e) ;
				ScalarShapeFunction u = getScalarTrial() ;
				ScalarShapeFunction v = getScalarTest() ;
				switch(type) {
					case LHS_Domain:
						return -u.diff("x")*v.diff("x") + u*v ;
					case LHS_Border:
						return null ;
					case RHS_Domain:
						return f*v ;
					case RHS_Border:
						return null ;
				}
				return null ;
			}
        	
        } ;
        
        wfb.addParam("f", f) ;

        //5.Assembly process
        AssemblerScalar assembler =
                new AssemblerScalar(mesh, wfb.getScalarWeakForm());
        assembler.assemble();
        Matrix stiff = assembler.getStiffnessMatrix();

        Vector load = assembler.getLoadVector();

        //Boundary condition
        assembler.imposeDirichletCondition(new SingleVarFunc("boundary", "x") {
			
			@Override
			public double apply(double... args) {
				double x = args[0] ;
				if (x<1) return 0 ;
				else return -10 ;
			}
		});

        //6.Solve linear system
        SolverJBLAS solver = new SolverJBLAS();
        Vector u = solver.solveDGESV(stiff, load);
        System.out.println("u=");
        for(int i=1;i<=u.getDim();i++)
            System.out.println(String.format("%.3f ", u.get(i)));

        
		double[] x = new double[u.getDim()] ;
		double[] y = new double[u.getDim()] ;
//		System.out.println("u=");
		for (int i = 1; i <= u.getDim(); i++) {
//			System.out.println(String.format("%.4f", u.get(i)));
			x[i-1] = mesh.getNodeList().at(i).coord(1);
			y[i-1] = u.get(i)  ;
		}

		// 6. Output the result to a MATLAB chart
		
        this.mesh = mesh;
        this.u = u;
	}
	
    public static void main(String[] args) {
    	T02Laplace ex1 = new T02Laplace();
    	ex1.run();
    }
}
