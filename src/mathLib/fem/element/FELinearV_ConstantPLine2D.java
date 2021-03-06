package mathLib.fem.element;

import static mathLib.func.symbolic.FMath.*;

import mathLib.fem.core.DOF;
import mathLib.fem.core.Element;
import mathLib.fem.core.Line2DCoord;
import mathLib.fem.core.Mesh;
import mathLib.fem.core.NodeType;
import mathLib.fem.core.geometry.GeoEntity;
import mathLib.fem.core.intf.CoordTrans;
import mathLib.fem.core.intf.VecFiniteElement;
import mathLib.func.symbolic.basic.FX;
import mathLib.func.symbolic.basic.SpaceVectorFunction;
import mathLib.func.symbolic.intf.MathFunc;
import mathLib.func.symbolic.intf.VecMathFunc;

public class FELinearV_ConstantPLine2D implements VecFiniteElement {
	Line2DCoord coord;
	
	//Construct a function with the coordinate of points in an element as parameters
	String[] argsOrder;
	
	public int nDOFs = 2+2+1;
	VecMathFunc[] shapeFuncs = new VecMathFunc[nDOFs];

	public FELinearV_ConstantPLine2D() {
		FX x1 = new FX("x1");
		FX x2 = new FX("x2");
		FX y1 = new FX("y1");
		FX y2 = new FX("y2");

		this.coord = new Line2DCoord(x1,x2,y1,y2);
		
		MathFunc r = this.coord.getCoordR();
		this.argsOrder = new String[]{x1,x2,y1,y2,r};
		
		//shape function for velocity component
		MathFunc NV1 = 0.5*(1-r);
		MathFunc NV2 = 0.5*(1+r);

		shapeFuncs[0] = new SpaceVectorFunction(NV1, C0, C0);
		shapeFuncs[1] = new SpaceVectorFunction(NV2, C0, C0);
		shapeFuncs[2] = new SpaceVectorFunction(C0, NV1, C0);
		shapeFuncs[3] = new SpaceVectorFunction(C0, NV2, C0);
		shapeFuncs[4] = new SpaceVectorFunction(C0, C0, C1);
	}

	@Override
	public int getNumberOfDOFs() {
		return this.nDOFs;
	}

	@Override
	public VecMathFunc[] getShapeFunctions() {
		return this.shapeFuncs;
	}

	@Override
	public String[] getArgsOrder() {
		return this.argsOrder;
	}

	@Override
	public VecFiniteElement getBoundaryFE() {
		return null;
	}

	@Override
	public boolean isDOFCoupled(int idx1, int idx2) {
		if(idx1 == 4 || idx2 == 4)
			return true;
		if(idx1 <= 1 && idx2 >=2)
			return false;
		if(idx2 <= 1 && idx1 >=2)
			return false;
		return true;
	}

	@Override
	public int getGlobalIndex(Mesh mesh, Element e, int localIndex) {
		if(localIndex>=1 && localIndex <= 2) {
			return e.vertices().at(localIndex).globalNode().getIndex();
		} else if(localIndex>=3 && localIndex<=4) {
			int nNode = mesh.getNodeList().size();
			return nNode + e.vertices().at(localIndex-2).globalNode().getIndex();
		} else if(localIndex == 5) {
			int nNode = mesh.getNodeList().size();
			return 2*nNode + e.parent.globalIndex;
		} else {
			throw new RuntimeException("local index should be in 1...9");
		}
	}

	@Override
	public int getTotalNumberOfDOFs(Mesh mesh) {
		throw new UnsupportedOperationException("Call FEBilinearV_ConstantP.getTotalNumberOfDOFs() intstead");
	}
	
	@Override
	public int getVVFComponentIndex(int localIndex) {
		if(localIndex >= 1 && localIndex <= 2)
			return 1;
		else if(localIndex >= 3 && localIndex <= 4)
			return 2;
		else if(localIndex == 5)
			return 3;
		else
			throw new RuntimeException("local index should be in the range of [1,"+(shapeFuncs.length+1)+"]");
	}

	@Override
	public int getNumberOfDOFs(Mesh mesh, int nVVFComponentIndex) {
		throw new UnsupportedOperationException("Call FEBilinearV_ConstantP.getNumberOfNOFs(...) intstead");
	}

	@Override
	public CoordTrans getCoordTrans() {
		return this.coord;
	}

	@Override
	public NodeType getDOFType(Element e, int localIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DOF getDOF(int localIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GeoEntity getGeoEntity(Element e, int localIndex) {
		// TODO Auto-generated method stub
		return null;
	}
}
