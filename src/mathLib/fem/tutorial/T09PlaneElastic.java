package mathLib.fem.tutorial;

import java.util.HashMap;

import mathLib.fem.assembler.AssemblerVector;
import mathLib.fem.element.FEBilinearRectangleVector;
import mathLib.fem.util.container.ElementList;
import mathLib.fem.util.container.ObjIndex;
import mathLib.fem.weakform.WeakFormElasticIsoPlaneStress2D;
import mathLib.func.symbolic.FMath;
import mathLib.func.symbolic.MultiVarFunc;
import mathLib.func.symbolic.basic.SpaceVectorFunction;
import mathLib.func.symbolic.intf.MathFunc;
import mathLib.matrix.algebra.SparseBlockMatrix;
import mathLib.matrix.algebra.SparseBlockVector;
import mathLib.matrix.algebra.solver.external.SolverJBLAS;
import mathLib.util.io.MeshReader;
import mathLib.util.io.MeshWriter;

public class T09PlaneElastic {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//        MeshReader reader = new MeshReader("grids/patch_elastic_hand.grd");
//        MeshReader reader = new MeshReader("grids/patch_elastic.grd");
//        MeshReader reader = new MeshReader("grids/elastic.grd");
        MeshReader reader = new MeshReader("grids/elastic2.grd");
        Mesh mesh = reader.read2DMesh();
        //Compute geometry relationship of nodes and elements
        mesh.computeNodeBelongsToElements();

        //2.Mark border types
        HashMap<NodeType, MathFunc> mapNTF =
                new HashMap<NodeType, MathFunc>();
        mapNTF.put(NodeType.Robin, new MultiVarFunc("x","y"){
        	@Override
        	public double apply(Variable v) {
        		double x = v.get("x");
        		double y = v.get("y");
        		if(Math.abs(x)<Constant.meshEps && 
        			(y>-Constant.meshEps && y<1.0+Constant.meshEps))
            	//if(Math.abs(x)<Constant.meshEps)
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
        		if(Math.abs(x-10.0)<Constant.meshEps)
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
        	System.out.println(nodes.at(i));
        }
        for(int i=1;i<=eles.size();i++) {
        	System.out.println(eles.at(i));
        }

        //3.Use element library to assign degrees of
        //  freedom (DOF) to element
        ElementList eList = mesh.getElementList();
        FEBilinearRectangleVector fe = new FEBilinearRectangleVector();
        fe.initDOFIndexGenerator(mesh.getNodeList().size());
        for(int i=1;i<=eList.size();i++)
            fe.assignTo(eList.at(i));

        //4.Weak form
        WeakFormElasticIsoPlaneStress2D weakForm = new WeakFormElasticIsoPlaneStress2D();
        SpaceVectorFunction b = new SpaceVectorFunction(2);
        SpaceVectorFunction t = new SpaceVectorFunction(2);
        b.set(1,FMath.C0);
        b.set(2,FMath.C0);
//        t.set(1,FC.c0);
        t.set(1,new MultiVarFunc("x","y"){
        	@Override
        	public double apply(Variable v) {
        		//double y = v.get("y");
        		return 1;
        		//return y*y;
        	}

			@Override
			public double apply(double... args) {
				// TODO Auto-generated method stub
				return 0;
			}
        });
        t.set(2,FMath.C0);
        weakForm.setF(b,t);

        //5.Assembly process
        AssemblerVector assembler =
                new AssemblerVector(mesh, weakForm, fe);
        assembler.assemble();
        SparseBlockMatrix stiff = assembler.getStiffnessMatrix();
        SparseBlockVector load = assembler.getLoadVector();
        //stiff.print();
        //load.print();
        //Boundary condition
        SpaceVectorFunction diri = new SpaceVectorFunction(2);
        diri.set(1,FMath.C0);
        diri.set(2,FMath.C0);
        assembler.imposeDirichletCondition(diri);

        //6.Solve linear system
        SolverJBLAS solver = new SolverJBLAS();
        SparseBlockVector u = solver.solveDGESV(stiff, load);
        System.out.println("u=");
        for(int i=1;i<=u.getDim();i++)
            System.out.println(String.format("%.3f", u.get(i)));

        //7.Output results to an Techplot format file
        MeshWriter writer = new MeshWriter(mesh);
        writer.writeTechplot("Elastic.dat", u.getBlock(1),
        		u.getBlock(2));
	}

}
