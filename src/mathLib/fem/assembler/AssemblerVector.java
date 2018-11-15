package mathLib.fem.assembler;

import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import mathLib.fem.core.DOF;
import mathLib.fem.core.DOFOrder;
import mathLib.fem.core.Edge;
import mathLib.fem.core.EdgeLocal;
import mathLib.fem.core.Element;
import mathLib.fem.core.Face;
import mathLib.fem.core.FaceLocal;
import mathLib.fem.core.Mesh;
import mathLib.fem.core.Node;
import mathLib.fem.core.NodeType;
import mathLib.fem.core.Volume;
import mathLib.fem.core.geometry.GeoEntity;
import mathLib.fem.core.geometry.GeoEntity2D;
import mathLib.fem.core.geometry.GeoEntity3D;
import mathLib.fem.core.intf.AssemblerOld;
import mathLib.fem.core.intf.WeakFormOld;
import mathLib.fem.element.FiniteElementType;
import mathLib.fem.util.container.DOFList;
import mathLib.fem.util.container.ElementList;
import mathLib.fem.util.container.VertexList;
import mathLib.func.symbolic.Variable;
import mathLib.func.symbolic.intf.MathFunc;
import mathLib.func.symbolic.intf.VecMathFunc;
import mathLib.matrix.algebra.FullMatrix;
import mathLib.matrix.algebra.SparseBlockMatrix;
import mathLib.matrix.algebra.SparseBlockVector;
import mathLib.matrix.algebra.SparseMatrixRowMajor;
import mathLib.matrix.algebra.SparseVectorHashMap;
import mathLib.matrix.algebra.intf.Matrix;
import mathLib.matrix.algebra.intf.Vector;

public class AssemblerVector implements AssemblerOld {
	protected Mesh mesh;
	protected WeakFormOld weakForm;
	protected SparseBlockMatrix globalStiff;
	protected SparseBlockVector globalLoad;
	
	/**
	 * 
	 * @param mesh 
	 * @param weakForm 
	 * @param feType 
	 */
	public AssemblerVector(Mesh mesh, WeakFormOld weakForm, 
			FiniteElementType feType) {
		this.mesh = mesh;
		this.weakForm = weakForm;
		
		int blockDim = feType.getVectorShapeFunctionDim();

		int[] dims = new int[blockDim];
		for(int i=1;i<=blockDim;i++) {
			dims[i-1] = feType.getDOFNumOnMesh(mesh, i);
		}
		globalStiff = new SparseBlockMatrix(blockDim,blockDim);
		globalLoad = new SparseBlockVector(blockDim);
		for(int i=1;i<=blockDim;i++) {
			for(int j=1;j<=blockDim;j++) {
				globalStiff.setBlock(i, j, 
						new SparseMatrixRowMajor(dims[i-1],dims[j-1]));
			}
			globalLoad.setBlock(i, 
					new SparseVectorHashMap(dims[i-1]));
		}
		
	}
	
	@Override
	public void assemble() {
		ElementList eList = mesh.getElementList();
		int nEle = eList.size();
		int nProgress = 20;
		System.out.print("Assemble[0%");
		for(int i=0;i<nProgress-6;i++)
			System.out.print("-");
		System.out.println("100%]");
		
		System.out.print("Progress[");
		int nPS = nEle/nProgress;
		int nProgressPrint = 0;
		for(int i=1; i<=nEle; i++) {
			eList.at(i).adjustVerticeToCounterClockwise();
			assembleGlobal(eList.at(i),	globalStiff,globalLoad);
			if(i%nPS==0) {
				nProgressPrint++;
				System.out.print("*");
			}
		}
		if(nProgressPrint<nProgress)
			System.out.print("*");
		System.out.println("]Done!");
		
		//procHangingNode(mesh);
	}
	

	/**
	 * 
	 * @param e
	 * @param stiff
	 * @param load
	 */
	public void assembleGlobal(Element e, Matrix stiff, Vector load) {
		DOFList DOFs = e.getAllDOFList(DOFOrder.NEFV);
		int nDOFs = DOFs.size();
		
		//Update Jacobin on e
		e.updateJacobin();
		
		for(int i=1;i<=nDOFs;i++) {
			DOFs.at(i).getVSF().assignElement(e);
		}
		
		weakForm.preProcess(e);

		for(int i=1;i<=nDOFs;i++) {
			DOF dofI = DOFs.at(i);
			int nGlobalRow = dofI.getGlobalIndex();
			int nVVFCmptI = dofI.getVVFComponent();
			for(int j=1;j<=nDOFs;j++) {
				DOF dofJ = DOFs.at(j);
				int nGlobalCol = dofJ.getGlobalIndex();
				int nVVFCmptJ = dofJ.getVVFComponent();

				if(weakForm.isVVFComponentCoupled(nVVFCmptI,nVVFCmptJ)) { 
					//Local stiff matrix

					weakForm.setDOF(dofJ, dofI);
					MathFunc lhs = weakForm.leftHandSide(e, WeakFormOld.ItemType.Domain);
					double lhsVal = weakForm.integrate(e, lhs);
					stiff.add(nGlobalRow, nGlobalCol, lhsVal);
					//System.out.println(nVVFCmptI+"  "+nVVFCmptJ+"   "+lhsVal);
				}
			}
			//Local load vector
			weakForm.setDOF(null, dofI);
			MathFunc rhs = weakForm.rightHandSide(e, WeakFormOld.ItemType.Domain);
			double rhsVal = weakForm.integrate(e, rhs);
			load.add(nGlobalRow, rhsVal);
		}
		
		if(e.isBorderElement()) {
			ElementList beList = e.getBorderElements();
			for(int n=1;n<=beList.size();n++) {
				Element be = beList.at(n);
				
				be.updateJacobin();
				weakForm.preProcess(be);
				
				DOFList beDOFs = be.getAllDOFList(DOFOrder.NEFV);
				int nBeDOF = beDOFs.size();
				
				for(int i=1;i<=nBeDOF;i++) {
					beDOFs.at(i).getVSF().assignElement(be);
				}
				
				for(int i=1;i<=nBeDOF;i++) {
					DOF dofI = beDOFs.at(i);
					int nGlobalRow = dofI.getGlobalIndex();
					int nVVFCmptI = dofI.getVVFComponent();
					for(int j=1;j<=nBeDOF;j++) {
						DOF dofJ = beDOFs.at(j);
						int nGlobalCol = dofJ.getGlobalIndex();
						int nVVFCmptJ = dofJ.getVVFComponent();
						
						if(weakForm.isVVFComponentCoupled(nVVFCmptI,nVVFCmptJ)) { 
							//Check node type
							NodeType nodeType = be.getBorderNodeType(nVVFCmptI);
							if(nodeType == NodeType.Neumann || nodeType == NodeType.Robin) {
								//Local stiff matrix for border
								weakForm.setDOF(dofJ, dofI);
								MathFunc lhsBr = weakForm.leftHandSide(be, WeakFormOld.ItemType.Border);
								double lhsBrVal = weakForm.integrate(be, lhsBr);
								stiff.add(nGlobalRow, nGlobalCol, lhsBrVal);
							}
						}
					}
					//Check node type
					NodeType nodeType = be.getBorderNodeType(nVVFCmptI);
					if(nodeType == NodeType.Neumann || nodeType == NodeType.Robin) {
						//Local load vector for border
						weakForm.setDOF(null, dofI);
						MathFunc rhsBr = weakForm.rightHandSide(be, WeakFormOld.ItemType.Border);
						double rhsBrVal = weakForm.integrate(be, rhsBr);
						load.add(nGlobalRow, rhsBrVal);
					}
				}
			}
		}
	}
	
	@Override
	public SparseBlockVector getLoadVector() {
		return globalLoad;
	}

	@Override
	public SparseBlockMatrix getStiffnessMatrix() {
		return globalStiff;
	}

	@Override
	public void imposeDirichletCondition(MathFunc diri) {
		throw new UnsupportedOperationException();

	}
	
	
	protected void setDirichlet(FullMatrix stiff, int matIndex, double value) {
		int row = matIndex-1;
		int col = matIndex-1;
		double[][] data = stiff.getData();

		data[row][col] = 1.0;
		this.globalLoad.set(row+1,value);
		
		int nRow = stiff.getRowDim();
		int nCol = stiff.getColDim();
		if(Math.abs(value) > Matrix.zeroEps) {
			for(int r=0; r<nRow; r++) { 
				if(r != row) {
					//bugfix
					//this.globalLoad.add(r+1,-this.globalStiff.get(r+1, col+1)*value);
					this.globalLoad.add(r+1,-data[r][col]*value);
					data[r][col] = 0.0; 
				}
			}
		} else {
			for(int r=0; r<nRow; r++) {
				if(r != row) {
					data[r][col] = 0.0; 
				}
			}
		}
		for(int c=0; c<nCol; c++) {
			if(c != col) {
				data[row][c] = 0.0; 
			}
		}
	}
	
	protected void setDirichlet(int matIndex, double value) {
		int row = matIndex;
		int col = matIndex;
		this.globalStiff.set(row, col, 1.0);
		this.globalLoad.set(row,value);
		int nRow = globalStiff.getRowDim();
		int nCol = globalStiff.getColDim();
		//value!=0
		if(Math.abs(value) > Matrix.zeroEps) {
			for(int r=1;r<=nRow;r++) {
				if(r != row) {
					this.globalLoad.add(r,-this.globalStiff.get(r, col)*value);
					this.globalStiff.set(r, col, 0.0);
				}
			}
		} else { //value=0
			for(int r=1;r<=nRow;r++) {
				if(r != row) {
					this.globalStiff.set(r, col, 0.0);
				}
			}
		}
		for(int c=1;c<=nCol;c++) {
			if(c != col) {
				this.globalStiff.set(row, c, 0.0);
			}
		}
	}
	
	@SuppressWarnings({"unused", "rawtypes" })
	@Override
	public void imposeDirichletCondition(VecMathFunc diri) {
		ElementList eList = mesh.getElementList();
		
		int nRow = this.globalStiff.getRowDim();
		int nCol = this.globalStiff.getColDim();
		FullMatrix fStiff = new FullMatrix(nRow,nCol);
		double [][]fsData = fStiff.getData();
		//System.out.println(this.globalStiff.getNonZeroNumber());
		Map<Integer,Map<Integer,Double>> sData = this.globalStiff.getAll();
		//int size = 0;
		for(Entry<Integer,Map<Integer,Double>> e1 : sData.entrySet()) {
			int r = e1.getKey();
			Map<Integer,Double> row = e1.getValue();
			for(Entry<Integer,Double> e2 : row.entrySet()) {
				int c = e2.getKey();
				Double v = e2.getValue();
				fsData[r-1][c-1] = v;
				//size++;
			}
		}
		//System.out.println(this.globalStiff.getNonZeroNumber()+"=="+size);
		
		Set<Integer> nodeDOFSet = new HashSet<Integer>();
		for(int ie=1;ie<=eList.size();ie++) {
			Element e = eList.at(ie);
			DOFList DOFs = e.getAllDOFList(DOFOrder.NEFV);
			for(int j=1;j<=DOFs.size();j++) {
				DOF dof = DOFs.at(j);
				GeoEntity ge = dof.getOwner();
				int nVVFCmpt = dof.getVVFComponent();
				MathFunc fdiri = diri.get(nVVFCmpt);
				if(ge instanceof Node) {
					//if(!nodeDOFSet.contains(dof.getGlobalIndex())) {
						Node n = (Node)ge;
						if(n.getNodeType(nVVFCmpt) == NodeType.Dirichlet) {
							Variable v = Variable.createFrom(fdiri, n, 0);
							setDirichlet(fStiff,dof.getGlobalIndex(),fdiri.apply(v));
						}
					//	nodeDOFSet.add(dof.getGlobalIndex());
					//}
				} else if(ge instanceof EdgeLocal) {

					EdgeLocal edge = (EdgeLocal)ge;
					if(edge.getBorderType(nVVFCmpt) == NodeType.Dirichlet) {

					}
					
				} else if(ge instanceof FaceLocal) {

					FaceLocal face = (FaceLocal)ge;
					if(face.getBorderType(nVVFCmpt) == NodeType.Dirichlet) {
						//TODO
					}
				} else if(ge instanceof Edge) {
					VertexList vs = ((GeoEntity2D) ge).getVertices();
					for(int k=1;k<=vs.size();k++) {
						Node n = vs.at(k).globalNode();
						if(NodeType.Dirichlet == n.getNodeType(nVVFCmpt)) {
							Variable v = Variable.createFrom(fdiri, n, 0);
							setDirichlet(fStiff,dof.getGlobalIndex(),fdiri.apply(v));
						}
					}
				} else if(ge instanceof Face) {
					
					VertexList vs = ((GeoEntity2D) ge).getVertices();
					for(int k=1;k<=vs.size();k++) {
						Node n = vs.at(k).globalNode();
						if(NodeType.Dirichlet == n.getNodeType(nVVFCmpt)) {
							Variable v = Variable.createFrom(fdiri, n, 0);
							setDirichlet(fStiff,dof.getGlobalIndex(),fdiri.apply(v));
						}
					}
				} else if(ge instanceof Volume) {
					VertexList vs = ((GeoEntity3D) ge).getVertices();
					for(int k=1;k<=vs.size();k++) {
						Node n = vs.at(k).globalNode();
						if(NodeType.Dirichlet == n.getNodeType(nVVFCmpt)) {
							Variable v = Variable.createFrom(fdiri, n, 0);
							setDirichlet(fStiff,dof.getGlobalIndex(),fdiri.apply(v));
						}
					}
				}
			}
		}
		this.globalStiff.clearData();
		long begin = System.currentTimeMillis();
		for(int i=nRow; --i>=0; ) {
		//for(int i=0; i<nRow; i++) {
			double[] _fsDatai = fsData[i];
			for(int j=nCol; --j>=0; ) {
			//for(int j=0; j<nCol; j++) {
				double v = _fsDatai[j];
				if(Math.abs(v) > Matrix.zeroEps) {
					this.globalStiff.set(i+1, j+1, v);
				}
			}
		}
		long end = System.currentTimeMillis();
		System.out.println("time="+(end-begin)+"ms");
	}		
}
