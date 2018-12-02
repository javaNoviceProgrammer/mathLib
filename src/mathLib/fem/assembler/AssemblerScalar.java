package mathLib.fem.assembler;

import java.util.Map;
import java.util.Map.Entry;

import mathLib.fem.core.DOF;
import mathLib.fem.core.DOFOrder;
import mathLib.fem.core.Edge;
import mathLib.fem.core.EdgeLocal;
import mathLib.fem.core.Element;
import mathLib.fem.core.Face;
import mathLib.fem.core.FaceLocal;
import mathLib.fem.core.Mesh;
import mathLib.fem.core.Node;
import mathLib.fem.core.NodeRefined;
import mathLib.fem.core.NodeType;
import mathLib.fem.core.Volume;
import mathLib.fem.core.geometry.GeoEntity;
import mathLib.fem.core.geometry.GeoEntity2D;
import mathLib.fem.core.geometry.GeoEntity3D;
import mathLib.fem.core.intf.AssemblerOld;
import mathLib.fem.core.intf.WeakFormOld;
import mathLib.fem.core.intf.WeakFormOld.ItemType;
import mathLib.fem.util.FutureyeException;
import mathLib.fem.util.container.DOFList;
import mathLib.fem.util.container.ElementList;
import mathLib.fem.util.container.VertexList;
import mathLib.func.symbolic.Variable;
import mathLib.func.symbolic.intf.MathFunc;
import mathLib.func.symbolic.intf.VecMathFunc;
import mathLib.matrix.algebra.SparseMatrixRowMajor;
import mathLib.matrix.algebra.SparseVectorHashMap;
import mathLib.matrix.algebra.intf.Matrix;
import mathLib.matrix.algebra.intf.SparseMatrix;
import mathLib.matrix.algebra.intf.SparseVector;
import mathLib.matrix.algebra.intf.Vector;

public class AssemblerScalar implements AssemblerOld {
	private int status = 0;
	protected Mesh mesh;
	protected WeakFormOld weakForm;
	protected SparseMatrix globalStiff;
	protected SparseVector globalLoad;
	private boolean printInfo = true;

	public AssemblerScalar(Mesh mesh, WeakFormOld weakForm) {
		this.mesh = mesh;
		this.weakForm = weakForm;
		
		int dim = mesh.getNodeList().size();
		globalStiff = new SparseMatrixRowMajor(dim,dim);
		globalLoad = new SparseVectorHashMap(dim);
	}
	
	@Override
	public SparseMatrix getStiffnessMatrix() {
		if(status == 0)
			throw new FutureyeException("Call assemble() function first!");
		return this.globalStiff;
	}
	
	@Override
	public SparseVector getLoadVector() {
		if(status == 0)
			throw new FutureyeException("Call assemble() function first!");
		return this.globalLoad;
	}
	
	@Override
	public void assemble() {
		assemble(true);
	}
	
	public void assemble(boolean procHangingNode) {
		status = 1;
		ElementList eList = mesh.getElementList();
		int nEle = eList.size();
		int nProgress = 20;
		if(printInfo) {
			System.out.print("Assemble[0%");
			for(int i=0;i<nProgress-6;i++)
				System.out.print("-");
			System.out.println("100%]");
			System.out.print("Progress[");
		}
		
		int nPS = nEle/nProgress;
		if(nPS == 0) nPS = 1;
		int nProgressPrint = 0;
		for(int i=1; i<=nEle; i++) {
			eList.at(i).adjustVerticeToCounterClockwise();

			assembleGlobal(eList.at(i),	globalStiff,globalLoad);
			if(printInfo) {
				if(i%nPS==0) {
					nProgressPrint++;
					System.out.print("*");
				}
			}
		}
		if(printInfo) {
			if(nProgressPrint<nProgress)
				System.out.print("*");
			System.out.println("]Done!");
		}
		
		if(procHangingNode)
			procHangingNode(mesh);
	}
	
	public void printInfo(boolean flag) {
		this.printInfo = flag;
	}

	
	protected void setDirichlet(int matIndex, double value) {
		int row = matIndex;
		int col = matIndex;
		this.globalStiff.set(row, col, 1.0);
		this.globalLoad.set(row,value);
		for(int r=1;r<=this.globalStiff.getRowDim();r++) {
			if(r != row) {
				this.globalLoad.add(r,-this.globalStiff.get(r, col)*value);
				this.globalStiff.set(r, col, 0.0);
			}
		}
		for(int c=1;c<=this.globalStiff.getColDim();c++) {
			if(c != col) {
				this.globalStiff.set(row, c, 0.0);
			}
		}
	}
	
	@SuppressWarnings({ "rawtypes" })
	@Override
	public void imposeDirichletCondition(MathFunc diri) {
		ElementList eList = mesh.getElementList();
		for(int i=1;i<=eList.size();i++) {
			Element e = eList.at(i);
			DOFList DOFs = e.getAllDOFList(DOFOrder.NEFV);
			for(int j=1;j<=DOFs.size();j++) {
				DOF dof = DOFs.at(j);
				GeoEntity ge = dof.getOwner();
				if(ge instanceof Node) {
					Node n = (Node)ge;
					if(n.getNodeType() == NodeType.Dirichlet) {
						Variable v = Variable.createFrom(diri, n, n.globalIndex); 
						setDirichlet(dof.getGlobalIndex(),diri.apply(v));
					}
				} else if(ge instanceof EdgeLocal) {
					EdgeLocal edge = (EdgeLocal)ge;
					if(edge.getBorderType() == NodeType.Dirichlet) {
					}
					
				} else if(ge instanceof FaceLocal) {
					FaceLocal face = (FaceLocal)ge;
					if(face.getBorderType() == NodeType.Dirichlet) {
					}
				} else if(ge instanceof Edge) {
					VertexList vs = ((GeoEntity2D) ge).getVertices();
					for(int k=1;k<=vs.size();k++) {
						Node n = vs.at(k).globalNode();
						if(NodeType.Dirichlet == n.getNodeType()) {
							Variable v = Variable.createFrom(diri, n, 0);
							setDirichlet(dof.getGlobalIndex(),diri.apply(v));
						}
					}
				} else if(ge instanceof Face) {
					
					VertexList vs = ((GeoEntity2D) ge).getVertices();
					for(int k=1;k<=vs.size();k++) {
						Node n = vs.at(k).globalNode();
						if(NodeType.Dirichlet == n.getNodeType()) {
							Variable v = Variable.createFrom(diri, n, 0);
							setDirichlet(dof.getGlobalIndex(),diri.apply(v));
						}
					}
				} else if(ge instanceof Volume) {
					VertexList vs = ((GeoEntity3D) ge).getVertices();
					for(int k=1;k<=vs.size();k++) {
						Node n = vs.at(k).globalNode();
						if(NodeType.Dirichlet == n.getNodeType()) {
							Variable v = Variable.createFrom(diri, n, 0);
							setDirichlet(dof.getGlobalIndex(),diri.apply(v));
						}
					}
				}
			}
		}
	}
	
	public void assembleGlobal(Element e, Matrix stiff, Vector load) {
		status = 3;
		DOFList DOFs = e.getAllDOFList(DOFOrder.NEFV);
		int nDOFs = DOFs.size();
		
		//Update Jacobin on e
		e.updateJacobin();
		
		for(int i=1;i<=nDOFs;i++) {
			DOFs.at(i).getSSF().assignElement(e);
		}
		
		weakForm.preProcess(e);
		
		for(int i=1;i<=nDOFs;i++) {
			DOF dofI = DOFs.at(i);
			int nGlobalRow = dofI.getGlobalIndex();
			for(int j=1;j<=nDOFs;j++) {
				DOF dofJ = DOFs.at(j);
				int nGlobalCol = dofJ.getGlobalIndex();

				weakForm.setDOF(dofJ, dofI); 
				MathFunc lhs = weakForm.leftHandSide(e, ItemType.Domain);
				double lhsVal = weakForm.integrate(e, lhs);
				stiff.add(nGlobalRow, nGlobalCol, lhsVal);
			}
			//Local load vector
			weakForm.setDOF(null,dofI);
			MathFunc rhs = weakForm.rightHandSide(e, ItemType.Domain);
			double rhsVal = weakForm.integrate(e, rhs);
			load.add(nGlobalRow, rhsVal);
		}
		
		if(e.isBorderElement()) {
			ElementList beList = e.getBorderElements();
			for(int n=1;n<=beList.size();n++) {
				Element be = beList.at(n);

				//Check node type
				NodeType nodeType = be.getBorderNodeType();
				if(nodeType == NodeType.Neumann || nodeType == NodeType.Robin) {
					be.updateJacobin();
					weakForm.preProcess(be);
					DOFList beDOFs = be.getAllDOFList(DOFOrder.NEFV);
					int nBeDOF = beDOFs.size();

					for(int i=1;i<=nBeDOF;i++) {
						beDOFs.at(i).getSSF().assignElement(be);
					}

					for(int i=1;i<=nBeDOF;i++) {
						DOF dofI = beDOFs.at(i);
						int nGlobalRow = dofI.getGlobalIndex();
						for(int j=1;j<=nBeDOF;j++) {
							DOF dofJ = beDOFs.at(j);
							int nGlobalCol = dofJ.getGlobalIndex();
							//Local stiff matrix for border

							weakForm.setDOF(dofJ, dofI);
							MathFunc lhsBr = weakForm.leftHandSide(be, ItemType.Border);
							double lhsBrVal = weakForm.integrate(be, lhsBr);
							stiff.add(nGlobalRow, nGlobalCol, lhsBrVal);
						}
						//Local load vector for border
						weakForm.setDOF(null, dofI);
						MathFunc rhsBr = weakForm.rightHandSide(be, ItemType.Border);
						double rhsBrVal = weakForm.integrate(be, rhsBr);
						load.add(nGlobalRow, rhsBrVal);
					}
				}
			}
		}
	}
	
	/**
  	 * @param e
	 * @param localStiff (I/O): 
	 *   local stiff matrix corresponds to the integration part
	 *   in element e
	 * @param localStiffBorder (I/O): 
	 *   local stiff matrix corresponds to the integration part 
	 *   on the border of element e
	 * @param localLoad (I/O): 
	 *   local load vector
	 * @param localLoadBorder (I/O): 
	 *   local load vector for border
	 */
	public void assembleLocal(Element e, 
			SparseMatrix localStiff, SparseMatrix localStiffBorder, 
			SparseVector localLoad, SparseVector localLoadBorder) {
		status = 4;
		
		localStiff.clearAll();
		localLoad.clearAll();
		
		DOFList DOFs = e.getAllDOFList(DOFOrder.NEFV);
		int nDOFs = DOFs.size();
		
		localStiff.setRowDim(nDOFs);
		localStiff.setColDim(nDOFs);
		localLoad.setDim(nDOFs);
		
		//Update Jacobin on e
		e.updateJacobin();
		
		for(int i=1;i<=nDOFs;i++) {
			DOFs.at(i).getSSF().assignElement(e);
		}
		
		for(int i=1;i<=nDOFs;i++) {
			DOF dofI = DOFs.at(i);
			int nLocalRow = dofI.getLocalIndex();
			for(int j=1;j<=nDOFs;j++) {
				DOF dofJ = DOFs.at(j);
				int nLocalCol = dofJ.getLocalIndex();
				//Local stiff matrix
				weakForm.setDOF(dofJ, dofI);
				MathFunc lhs = weakForm.leftHandSide(e, WeakFormOld.ItemType.Domain);
				double lhsVal = weakForm.integrate(e, lhs);
				localStiff.add(nLocalRow, nLocalCol, lhsVal);
			}
			//Local load vector
			weakForm.setDOF(null, dofI);
			MathFunc rhs = weakForm.rightHandSide(e, WeakFormOld.ItemType.Domain);
			double rhsVal = weakForm.integrate(e, rhs);
			localLoad.add(nLocalRow, rhsVal);
		}
		
		if(e.isBorderElement()) {
			ElementList beList = e.getBorderElements();
			for(int n=1;n<=beList.size();n++) {
				Element be = beList.at(n);
				//Check node type
				NodeType nodeType = be.getBorderNodeType();
				if(nodeType == NodeType.Neumann || nodeType == NodeType.Robin) {
					be.updateJacobin();
					DOFList beDOFs = be.getAllDOFList(DOFOrder.NEFV);
					int nBeDOF = beDOFs.size();
					
					localStiffBorder.setRowDim(nDOFs);
					localStiffBorder.setColDim(nDOFs);
					for(int i=1;i<=nBeDOF;i++) {
						beDOFs.at(i).getSSF().assignElement(e);
					}
					
					for(int i=1;i<=nBeDOF;i++) {
						DOF dofI = beDOFs.at(i);
						int nLocalRow = dofI.getLocalIndex();
						for(int j=1;j<=nBeDOF;j++) {
							DOF dofJ = beDOFs.at(j);
							int nLocalCol = dofJ.getLocalIndex();
							//Local stiff matrix for border
							weakForm.setDOF(dofJ, dofI);
							MathFunc lhsBr = weakForm.leftHandSide(be, WeakFormOld.ItemType.Border);
							double lhsBrVal = weakForm.integrate(be, lhsBr);
							localStiffBorder.add(nLocalRow, nLocalCol, lhsBrVal);
						}
						//Local load vector for border
						weakForm.setDOF(null, dofI);
						MathFunc rhsBr = weakForm.rightHandSide(be, WeakFormOld.ItemType.Border);
						double rhsBrVal = weakForm.integrate(be, rhsBr);
						localLoadBorder.add(nLocalRow, rhsBrVal);
					}
				}
			}
		}
	}
	
	public void procHangingNode(Mesh mesh) {
		status = 5;
		for(int i=1;i<=mesh.getNodeList().size();i++) {
			Node node = mesh.getNodeList().at(i);
			if(node instanceof NodeRefined) {
				NodeRefined nRefined = (NodeRefined)node;
				if(nRefined.isHangingNode()) {
					globalStiff.set(nRefined.globalIndex, nRefined.globalIndex, 1.0);
					globalStiff.set(nRefined.globalIndex,
							nRefined.constrainNodes.at(1).globalIndex,-0.5);
					globalStiff.set(nRefined.globalIndex,
							nRefined.constrainNodes.at(2).globalIndex,-0.5);
				}
			}
		}
	}

	public void plusToGlobalLoad(Element e, Vector local) {
		for(int i=1; i<=local.getDim(); i++) {
			int ngRow = e.local2GlobalDOFIndex(i);
			this.globalLoad.add(ngRow, local.get(i));
		}
	}
	
	public void plusToGlobalStriff(Element e, SparseMatrix local) {
		Map<Integer,Map<Integer,Double>> m = local.getAll();
		for(Entry<Integer,Map<Integer,Double>> er : m.entrySet()) {
			int nRow = er.getKey();
			int ngRow = e.local2GlobalDOFIndex(nRow);
			Map<Integer,Double> row = er.getValue();
			for(Entry<Integer,Double> ec : row.entrySet()) {
				int nCol = ec.getKey();
				double val = ec.getValue();
				int ngCol = e.local2GlobalDOFIndex(nCol);

				this.globalStiff.add(ngRow, ngCol, val);
			}
		}
	}
	
	@Override
	public void imposeDirichletCondition(VecMathFunc diri) {
		throw new UnsupportedOperationException();
	}	
}
