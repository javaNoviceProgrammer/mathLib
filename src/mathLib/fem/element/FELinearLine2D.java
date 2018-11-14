/**
 * Copyright (c) 2010, nkliuyueming@gmail.com. All rights reserved.
 * 
 * 
 */
package mathLib.fem.element;


import mathLib.fem.core.Line2DCoord;
import mathLib.fem.core.intf.CoordTrans;
import mathLib.fem.core.intf.FiniteElement;
import mathLib.fem.util.container.VertexList;
import mathLib.func.symbolic.basic.FX;
import mathLib.func.symbolic.intf.MathFunc;

/**
 * Linear finite element on a 2D line element.
 * 
 * The shape functions
 * 
 *  1-----2  -->r
 * -1  0  1
 * 
 * N1 = (1-r)/2
 * N2 = (1+r)/2
 * 
 */
public class FELinearLine2D implements FiniteElement {
	Line2DCoord coord;
	
	//Construct a function with the coordinate of points in an element as parameters
	String[] argsOrder;
	
	public int nDOFs = 2;
	MathFunc[] shapeFuncs = new MathFunc[nDOFs];

	public FELinearLine2D() {
		FX x1 = new FX("x1");
		FX x2 = new FX("x2");
		FX y1 = new FX("y1");
		FX y2 = new FX("y2");

		this.coord = new Line2DCoord(x1,x2,y1,y2);
		MathFunc r = coord.getCoordR();
		
		this.argsOrder = new String[]{x1,x2,y1,y2,r};
		
		this.shapeFuncs[0] = 0.5*(1-r);
		this.shapeFuncs[1] = 0.5*(1+r);
	}

	@Override
	public MathFunc[] getShapeFunctions() {
		return this.shapeFuncs;
	}

	@Override
	public int getNumberOfDOFs() {
		return this.nDOFs;
	}

	@Override
	public String[] getArgsOrder() {
		return this.argsOrder;
	}

	@Override
	public int getGlobalIndex(Mesh mesh, Element e, int localIndex) {
		VertexList vertices = e.vertices();
		return vertices.at(localIndex).globalNode().globalIndex;
	}

	@Override
	public int getTotalNumberOfDOFs(Mesh mesh) {
		throw new UnsupportedOperationException();
	}

	@Override
	public CoordTrans getCoordTrans() {
		return this.coord;
	}

	@Override
	public FiniteElement getBoundaryFE() {
		throw new UnsupportedOperationException();
	}
}
