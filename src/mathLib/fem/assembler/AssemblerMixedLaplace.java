package mathLib.fem.assembler;

import mathLib.fem.core.intf.AssemblerOld;
import mathLib.fem.core.intf.WeakFormOld;
import mathLib.fem.util.container.ElementList;
import mathLib.func.symbolic.intf.MathFunc;
import mathLib.func.symbolic.intf.VecMathFunc;
import mathLib.matrix.algebra.SparseBlockMatrix;
import mathLib.matrix.algebra.SparseBlockVector;
import mathLib.matrix.algebra.SparseMatrixRowMajor;
import mathLib.matrix.algebra.SparseVectorHashMap;

public class AssemblerMixedLaplace implements AssemblerOld {
	protected Mesh mesh;
	protected WeakFormOld weakForm;
	protected SparseBlockMatrix globalStiff;
	protected SparseBlockVector globalLoad;

	public AssemblerMixedLaplace(Mesh mesh, WeakFormOld weakForm) {
		this.mesh = mesh;
		this.weakForm = weakForm;
		
		int edgeDOF = mesh.getEdgeList().size();
		int elementDOF = mesh.getEdgeList().size();
		
		globalStiff = new SparseBlockMatrix(2,2);
		globalStiff.setBlock(1, 1, 
				new SparseMatrixRowMajor(edgeDOF,edgeDOF));
		globalStiff.setBlock(1, 2, 
				new SparseMatrixRowMajor(edgeDOF,elementDOF));
		globalStiff.setBlock(2, 1, 
				new SparseMatrixRowMajor(elementDOF,edgeDOF));
		globalStiff.setBlock(2, 2, 
				new SparseMatrixRowMajor(elementDOF,elementDOF));
		
		globalLoad = new SparseBlockVector(2);
		globalLoad.setBlock(1, new SparseVectorHashMap(edgeDOF));
		globalLoad.setBlock(2, new SparseVectorHashMap(elementDOF));
		
	}

	@Override
	public void assemble() {
		ElementList eList = mesh.getElementList();
		int nEle = eList.size();
		for(int i=1; i<=nEle; i++) {
			eList.at(i).adjustVerticeToCounterClockwise();
			weakForm.assembleElement(eList.at(i), 
					globalStiff, globalLoad);
			if(i%3000==0)
				System.out.println("Assemble..."+
						String.format("%.0f%%", 100.0*i/nEle));
		}
		return;
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

	@Override
	public void imposeDirichletCondition(VecMathFunc diri) {
		throw new UnsupportedOperationException();
	}
}
