package mathLib.fem.tutorial;

import java.util.HashMap;

import mathLib.fem.assembler.AssemblerVector;
import mathLib.fem.core.Mesh;
import mathLib.fem.core.NodeType;
import mathLib.fem.element.FEBilinearRectangleVector;
import mathLib.fem.util.Constant;
import mathLib.fem.util.container.ElementList;
import mathLib.fem.util.container.NodeList;
import mathLib.fem.util.container.ObjIndex;
import mathLib.fem.weakform.WeakFormElasticIsoPlaneStress2D;
import mathLib.func.symbolic.FMath;
import mathLib.func.symbolic.MultiVarFunc;
import mathLib.func.symbolic.Variable;
import mathLib.func.symbolic.basic.SpaceVectorFunction;
import mathLib.func.symbolic.intf.MathFunc;
import mathLib.matrix.algebra.SparseBlockMatrix;
import mathLib.matrix.algebra.SparseBlockVector;
import mathLib.matrix.algebra.solver.Solver;
import mathLib.util.io.MeshReader;
import mathLib.util.io.MeshWriter;

public class T09PlaneElasticHole {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
      MeshReader reader = new MeshReader("grids/elastic_hole.grd");
//      MeshReader reader = new MeshReader("grids/elastic_hole2.grd");
      Mesh mesh = reader.read2DMesh();
      //Compute geometry relationship of nodes and elements
      mesh.computeNodeBelongsToElements();

      //2.Mark border types
      HashMap<NodeType, MathFunc> mapNTF =
              new HashMap<NodeType, MathFunc>();
      mapNTF.put(NodeType.Robin, new MultiVarFunc("x","y"){
      	@Override
      	public double apply(Variable v) {
      		//double x = v.get("x");
      		double y = v.get("y");
      		if(Math.abs(y)<Constant.meshEps ||
      				Math.abs(y-10)<Constant.meshEps)
      			return 1;
      		else
      			return 0;
      	}

		@Override
		public double apply(double... args) {
			// TODO Auto-generated method stub
			return 0;
		}
      });
      mapNTF.put(NodeType.Dirichlet, new MultiVarFunc("x","y"){
      	@Override
      	public double apply(Variable v) {
      		double x = v.get("x");
      		double y = v.get("y");
      		if(Math.abs(x)<Constant.meshEps &&
      				y<5.2+Constant.meshEps &&
      				y>4.8-Constant.meshEps
      				)
      			return 1;
      		else
      			return 0;
      	}

		@Override
		public double apply(double... args) {
			// TODO Auto-generated method stub
			return 0;
		}
      });

      mesh.markBorderNode(new ObjIndex(1,2),mapNTF);

      NodeList nodes = mesh.getNodeList();
      ElementList eles = mesh.getElementList();
      for(int i=1;i<=nodes.size();i++) {
      	if(nodes.at(i).getNodeType()==NodeType.Robin)
      		System.out.println(NodeType.Robin+":"+nodes.at(i));
      	if(nodes.at(i).getNodeType()==NodeType.Dirichlet)
      		System.out.println(NodeType.Dirichlet+":"+nodes.at(i));

      }
      for(int i=1;i<=eles.size();i++) {
      	System.out.println(eles.at(i));
      }

      //3.Use element library to assign degrees of
      //  freedom (DOF) to element
      ElementList eList = mesh.getElementList();
      FEBilinearRectangleVector fe = new FEBilinearRectangleVector();
//      FELinearTriangleVector fe = new FELinearTriangleVector();
      fe.initDOFIndexGenerator(mesh.getNodeList().size());
      for(int i=1;i<=eList.size();i++)
          fe.assignTo(eList.at(i));

      //4.Weak form
      WeakFormElasticIsoPlaneStress2D weakForm = new WeakFormElasticIsoPlaneStress2D();
      SpaceVectorFunction b = new SpaceVectorFunction(2);
      SpaceVectorFunction t = new SpaceVectorFunction(2);
      b.set(1,FMath.C0);
      b.set(2,FMath.C0);
      t.set(1,FMath.C0);
      t.set(2,new MultiVarFunc("x","y"){
        	@Override
          	public double apply(Variable v) {
          		double y = v.get("y");
          		if(Math.abs(y)<Constant.meshEps)
          			return -1;
          		else
          			return 1;
          	}

			@Override
			public double apply(double... args) {
				// TODO Auto-generated method stub
				return 0;
			}
          });
      weakForm.setF(b,t);

      //5.Assembly process
      AssemblerVector assembler =
              new AssemblerVector(mesh, weakForm, fe);
      assembler.assemble();
      SparseBlockMatrix stiff = assembler.getStiffnessMatrix();
      SparseBlockVector load = assembler.getLoadVector();
      //Boundary condition
      SpaceVectorFunction diri = new SpaceVectorFunction(2);
      diri.set(1,FMath.C0);
      diri.set(2,FMath.C0);
      assembler.imposeDirichletCondition(diri);

      //6.Solve linear system
      Solver solver = new Solver();
      SparseBlockVector u = solver.solveCGS(stiff, load);

      System.out.println("u=");
      for(int i=1;i<=u.getDim();i++)
          System.out.println(String.format("%.3f", u.get(i)));

      //7.Output results to an Techplot format file
      MeshWriter writer = new MeshWriter(mesh);
      writer.writeTechplot("ElasticHole.dat", u.getBlock(1),
      		u.getBlock(2));
      }

}
