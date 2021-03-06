package mathLib.fem.tutorial;

import java.util.HashMap;

import mathLib.fem.assembler.AssemblerScalar;
import mathLib.fem.core.Mesh;
import mathLib.fem.core.NodeType;
import mathLib.fem.element.FELinearTriangleOld;
import mathLib.fem.util.container.ElementList;
import mathLib.fem.weakform.WeakFormLaplace2D;
import mathLib.func.symbolic.MultiVarFunc;
import mathLib.func.symbolic.Variable;
import mathLib.func.symbolic.basic.FC;
import mathLib.func.symbolic.basic.Vector2MathFunc;
import mathLib.func.symbolic.intf.MathFunc;
import mathLib.matrix.algebra.CompressedRowMatrix;
import mathLib.matrix.algebra.FullVector;
import mathLib.matrix.algebra.intf.AlgebraMatrix;
import mathLib.matrix.algebra.intf.SparseMatrix;
import mathLib.matrix.algebra.intf.SparseVector;
import mathLib.matrix.algebra.intf.Vector;
import mathLib.matrix.algebra.solver.Solver;
import mathLib.util.io.MeshReader;

/**
 * d2dt(u) - c2*Laplace(u) = 0
 * =>
 * (u_{n+2}-2*u_{n+1}+u_{n})/Dt^2  - c2*Laplace(u) = 0
 * =>
 * -Dt^2*c2*Laplace(u_{n+2}) + u_{n+2} = 2*u_{n+1} - u_{n}
 *
 *  u(t=0) = u0(x);
 *  u + u_n(x,t) = 0, x on border of \Omega
 *
 * @author liuyueming
 *
 */
public class T06Wave {
	protected static String outputFolder = "tutorial\\wave";
	protected Mesh mesh = null;

	//Laplace2D weak form
	WeakFormLaplace2D weakForm = new WeakFormLaplace2D();

	//Time step size
	double Dt;

	//Speed^2
	double c2;

	//2*u_{n+1} - u_{n}
	MathFunc u_n = null;
	MathFunc u_n1 = null;

	//u(t=0)=u0(x)
	MathFunc u0 = null;

	//Function f = null;

	public void readMesh() {
		//Read a triangle mesh from an input file
		//[-3,3]x[-3,3]
		//MeshReader reader = new MeshReader("triangle_refine80x80.grd");

		//[-10,10]x[-100,10]
		//MeshReader reader = new MeshReader("rectangle_big.grd");
		//MeshReader reader = new MeshReader("rectangle_big2.grd");
		//MeshReader reader = new MeshReader("triangle_big.grd");
		//MeshReader reader = new MeshReader("triangle_big2.grd");

		//凸型区域[-40,40]x[-30,25]
		//MeshReader reader = new MeshReader("two_rectangle.grd");
		MeshReader reader = new MeshReader("three_rectangle.grd");

		//[-100,100]x[-100,100]
		//MeshReader reader = new MeshReader("triangle_bigbig.grd");
		//MeshReader reader = new MeshReader("triangle_bigbig80x80.grd");
		mesh = reader.read2DMesh();
		//Geometry relationship
		mesh.computeNodeBelongsToElements();
	}

	public void initParam() {
		//Mark border type
		HashMap<NodeType, MathFunc> mapNTF = new HashMap<NodeType, MathFunc>();
		mapNTF.put(NodeType.Robin, null);
		mesh.markBorderNode(mapNTF);

		//Use element library to assign degree of freedom (DOF) to element
		ElementList eList = mesh.getElementList();
		FELinearTriangleOld fe = new FELinearTriangleOld();
		//FEBilinearRectangle fe = new FEBilinearRectangle();
		for(int i=1;i<=eList.size();i++)
			fe.assignTo(eList.at(i));
	}

	public Vector solverOneStep(final int step) {
//		f = new AbstractFunction("x","y") {
//			@Override
//			public double value(Variable v) {
//				double x = v.get("x");
//				double y = v.get("y");
//				if(Math.sqrt((x)*(x)+y*y)<0.05)
//					return 10.0*Math.cos(4*Math.PI*Dt*step);
//				//else if(Math.sqrt((x-5)*(x-5)+y*y)<0.3)
//				//	return 100.0;
//				//else if(Math.sqrt((x-5)*(x-5)+(y-5)*(y-5))<0.3)
//				//	return 100.0;
//				else
//					return 0.0;
//			}
//		};
//		System.out.println(f.value(new Variable("x",0).set("y", 0)));

		FC k = new FC(Dt*Dt*c2);
		//Function ff = this.u_n1.mult(FC.c(2.0)).minus(this.u_n).plus(f.mult(FC.c(Dt*Dt)));
		MathFunc ff = this.u_n1.M(FC.c(2.0)).S(this.u_n).M(FC.c(1+13*Dt*step));
		weakForm.setF(ff);
		weakForm.setParam(k, FC.c(1.0).M(FC.c(1+13*Dt*step)), null, k);

		//Assemble
		AssemblerScalar assembler = new AssemblerScalar(mesh, weakForm);
		assembler.assemble();
		SparseMatrix stiff = assembler.getStiffnessMatrix();
		SparseVector load = assembler.getLoadVector();
		//Boundary condition
		//assembler.imposeDirichletCondition(new FC(0.0));

		//CG
		Solver solver = new Solver();
		SparseVector u = load.copy();
		u.setAll(1.0);
		System.out.println("begin construct AlgebraMatrix...");
		AlgebraMatrix algStiff = new CompressedRowMatrix((SparseMatrix)stiff,true);
		System.out.println("end construct AlgebraMatrix!");
		FullVector algLoad = new FullVector((SparseVector)load);
		FullVector algU = new FullVector(u);
		solver.solveCG(algStiff, algLoad, algU);
		double[] data = algU.getData();
		for(int i=0;i<data.length;i++) {
			u.set(i+1, data[i]);
		}
		//System.out.println("u=");
		//for(int i=1;i<=u.getDim();i++)
		//	System.out.println(String.format("%.3f", u.get(i)));

		Tools.plotVector(mesh, outputFolder, String.format("u_t%03d.dat",step), u);

		this.u_n = this.u_n1;
		this.u_n1 = u_n;

	    return u;

	}

	public void run() {
		readMesh();
		initParam();

		//Time step size
		Dt = 0.004;
		c2 = 500;

		u0 = new MultiVarFunc("x","y") {

//[-3,3]x[-3,3]
//			@Override
//			public double value(Variable v) {
//				double x = v.get("x");
//				double y = v.get("y");
//				if(Math.sqrt(x*x+y*y)<0.01)
//					return 10.0;
//				else
//					return 0.0;
//			}

//凸型区域[-40,40]x[-30,25]
			@Override
			public double apply(Variable v) {
				double x = v.get("x");
				double y = v.get("y");
				if(Math.sqrt(x*x+y*y)<0.55)
					return 20.0;
				else if(Math.sqrt((x-7)*(x-7)+y*y)<0.5)
					return 20.0;
				else if(Math.sqrt((x+15)*(x+15)+y*y)<0.5)
					return 20.0;
				else if(Math.sqrt(x*x+(y-12)*(y-12))<0.2)
					return 100.0;
				else if(Math.sqrt(x*x+(y+10)*(y+10))<2)
					return 5.0;
				else
					return 0.0;
			}

			@Override
			public double apply(double... args) {
				// TODO Auto-generated method stub
				return 0;
			}

//[-100,100]x[-100,100]
//			@Override
//			public double value(Variable v) {
//				double x = v.get("x");
//				double y = v.get("y");
//				if(Math.sqrt((x-30)*(x-30)+y*y)<2)
//					return 100.0;
//				else if(Math.sqrt((x+30)*(x+30)+y*y)<2)
//					return 100.0;
//				else
//					return 0.0;
//			}
		};
		u_n = u0;
		u_n1 = u0;
		for(int i=1;i<=300;i++) {
			Vector rlt = solverOneStep(i);
			u_n1 = new Vector2MathFunc(rlt);
		}
	}

	public static void main(String[] args) {
		T06Wave wave = new T06Wave();
		wave.run();
	}
}
