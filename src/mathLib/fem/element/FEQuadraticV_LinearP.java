/**
 * Copyright (c) 2010, nkliuyueming@gmail.com. All rights reserved.
 * 
 * 
 */
package mathLib.fem.element;


import static edu.uta.futureye.function.FMath.C0;

import mathLib.fem.core.DOF;
import mathLib.fem.core.TriAreaCoord;
import mathLib.fem.core.geometry.GeoEntity;
import mathLib.fem.core.intf.CoordTrans;
import mathLib.fem.core.intf.VecFiniteElement;
import mathLib.func.symbolic.basic.FX;
import mathLib.func.symbolic.basic.SpaceVectorFunction;
import mathLib.func.symbolic.intf.MathFunc;
import mathLib.func.symbolic.intf.VecMathFunc;

/**
 * P2/P1 on triangle element
 * 
 * -Continuous quadratic velocity
 * -Piecewise linear pressure
 * 
 * * Velocity: Quadratic shape function
 * * 速度：三角形局部�??标，二次函数
 * 
 * 3
 * | \
 * |  \
 * 6   5
 * |    \
 * |     \
 * 1--4-- 2
 * 
 * NV = NV(r,s,t) = NV( r(x,y), s(x,y), t(x,y) )
 * NV1 = (2*r-1)*r
 * NV2 = (2*s-1)*s
 * NV3 = (2*t-1)*t
 * NV4 = 4*r*s
 * NV5 = 4*s*t
 * NV6 = 4*r*t
 * 
 * * Pressure: Linear shape function
 * * 压强：三角形局部�??标，线性型函数
 * 3
 * | \
 * |  \
 * |   \
 * |    \
 * 1---- 2
 * 
 * NP = NP(r,s,t) = NP( r(x,y), s(x,y), t(x,y) )
 * NP1 = r
 * NP2 = s
 * NP3 = t
 *
 * * 2D vector valued shape functions
 * * 二维�?�元上的形函数，速度压强共15个自由度：
 * Ni = (v1,v2,p)', i=1,...,15
 * 
 * N1  =  (NV1, 0, 0)'
 * N2  =  (NV2, 0, 0)'
 * N3  =  (NV3, 0, 0)'
 * N4  =  (NV4, 0, 0)'
 * N5  =  (NV5, 0, 0)'
 * N6  =  (NV6, 0, 0)'
 * N7  =  (0, NV1, 0)'
 * N8  =  (0, NV2, 0)'
 * N9  =  (0, NV3, 0)'
 * N10 =  (0, NV4, 0)'
 * N11 =  (0, NV5, 0)'
 * N12 =  (0, NV6, 0)'
 * N13 =  (0, 0, NP1)'
 * N14 =  (0, 0, NP2)'
 * N15 =  (0, 0, NP3)'
 *
 *
 * @author liuyueming
 */
public class FEQuadraticV_LinearP implements VecFiniteElement {
	TriAreaCoord coord;
	
	//Construct a function with the coordinate of points in an element as parameters
	String[] argsOrder;
	
	public int nDOFs = 6+6+3;
	VecMathFunc[] shapeFuncs = new VecMathFunc[nDOFs];

	public FEQuadraticV_LinearP() {
		FX x1 = new FX("x1");
		FX x2 = new FX("x2");
		FX x3 = new FX("x3");
		FX x4 = new FX("x4");
		FX x5 = new FX("x5");
		FX x6 = new FX("x6");
		
		FX y1 = new FX("y1");
		FX y2 = new FX("y2");
		FX y3 = new FX("y3");
		FX y4 = new FX("y4");
		FX y5 = new FX("y5");
		FX y6 = new FX("y6");

		this.coord = new TriAreaCoord(x1, x2, x3,
				y1, y2, y3);
		
		MathFunc r = coord.getCoordR();
		MathFunc s = coord.getCoordS();
		MathFunc t = coord.getCoordT();
		
		argsOrder = new String[]{x1,x2,x3,x4,x5,x6,
				y1,y2,y3,y4,y5,y6,
				r,s,"t"};
		
		//shape functions
		MathFunc NV1 = (2*r-1)*r;
		MathFunc NV2=  (2*s-1)*s;
		MathFunc NV3 = (2*t-1)*t;
		MathFunc NV4 = 4*r*s;
		MathFunc NV5 = 4*s*t;
		MathFunc NV6 = 4*r*t;
		
		MathFunc NP1 = r;
		MathFunc NP2 = s;
		MathFunc NP3 = t;

		shapeFuncs[0]  = new SpaceVectorFunction(NV1, C0, C0);
		shapeFuncs[1]  = new SpaceVectorFunction(NV2, C0, C0);
		shapeFuncs[2]  = new SpaceVectorFunction(NV3, C0, C0);
		shapeFuncs[3]  = new SpaceVectorFunction(NV4, C0, C0);
		shapeFuncs[4]  = new SpaceVectorFunction(NV5, C0, C0);
		shapeFuncs[5]  = new SpaceVectorFunction(NV6, C0, C0);
		shapeFuncs[6]  = new SpaceVectorFunction(C0, NV1, C0);
		shapeFuncs[7]  = new SpaceVectorFunction(C0, NV2, C0);
		shapeFuncs[8]  = new SpaceVectorFunction(C0, NV3, C0);
		shapeFuncs[9]  = new SpaceVectorFunction(C0, NV4, C0);
		shapeFuncs[10] = new SpaceVectorFunction(C0, NV5, C0);
		shapeFuncs[11] = new SpaceVectorFunction(C0, NV6, C0);
		shapeFuncs[12] = new SpaceVectorFunction(C0, C0, NP1);
		shapeFuncs[13] = new SpaceVectorFunction(C0, C0, NP2);
		shapeFuncs[14] = new SpaceVectorFunction(C0, C0, NP3);
	}

	@Override
	public VecMathFunc[] getShapeFunctions() {
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
	public VecFiniteElement getBoundaryFE() {
		return new FEQuadraticV_LinearPLine2D();
	}

	@Override
	public boolean isDOFCoupled(int idx1, int idx2) {
		if(idx1 == 8 || idx2 == 8)
			return true;
		if(idx1 <= 3 && idx2 >= 4)
			return false;
		if(idx2 <= 3 && idx1 >= 4)
			return false;
		return true;
	}

	@Override
	public int getGlobalIndex(Mesh mesh, Element e, int localIndex) {
		if(localIndex>=1 && localIndex <= 6) {
			return e.nodes.at(localIndex).globalIndex;
		} else if(localIndex>=7 && localIndex<=12) {
			int nNode = mesh.getNodeList().size();
			return nNode + e.nodes.at(localIndex-6).globalIndex;
		} else if(localIndex>=13 && localIndex<=15) {
			int nNode = mesh.getNodeList().size();
			return 2*nNode + e.vertices().at(localIndex-12).globalNode().globalIndex;
		} else {
			throw new RuntimeException("local index = "+localIndex+". It should be in 1...15");
		}
	}

	@Override
	public int getTotalNumberOfDOFs(Mesh mesh) {
		int nNode  = mesh.getNodeList().size();
		return 3*nNode;
	}

	@Override
	public int getNumberOfDOFs(Mesh mesh, int nVVFComponentIndex) {
		int nNode  = mesh.getNodeList().size();
		if(nVVFComponentIndex >= 1 && nVVFComponentIndex <= 2)
			return nNode;
		else if(nVVFComponentIndex == 3)
			return mesh.nVertex;
		else
			throw new RuntimeException("nVVFComponentIndex should be 1, 2 or 3");
	}

	@Override
	public int getVVFComponentIndex(int localIndex) {
		if(localIndex >= 1 && localIndex <= 6)
			return 1;
		else if(localIndex >= 7 && localIndex <= 12)
			return 2;
		else if(localIndex >=13 && localIndex <=15)
			return 3;
		else
			throw new RuntimeException("local index should be in the range of [1,"+(shapeFuncs.length+1)+"]");
	}
	
	@Override
	public CoordTrans getCoordTrans() {
		return this.coord;
	}

	@Override
	public NodeType getDOFType(Element e, int localIndex) {
		if(localIndex >= 1 && localIndex <= 6)
			return e.nodes.at(localIndex).getNodeType(1);
		else if(localIndex >= 7 && localIndex <= 12)
			return e.nodes.at(localIndex-6).getNodeType(2);
		else if(localIndex >=13 && localIndex <=15)
			return e.vertices().at(localIndex-12).globalNode().getNodeType(3);
		else
			throw new RuntimeException("local index should be in the range of [1,"+(shapeFuncs.length+1)+"]");
	}

	@Override
	public GeoEntity getGeoEntity(Element e, int localIndex) {
		if(localIndex >= 1 && localIndex <= 6)
			return e.nodes.at(localIndex);
		else if(localIndex >= 7 && localIndex <= 12)
			return e.nodes.at(localIndex-6);
		else if(localIndex >=13 && localIndex <=15)
			return e.getGeoEntity2D();
		else
			throw new RuntimeException("local index should be in the range of [1,"+(shapeFuncs.length+1)+"]");
	}

	@Override
	public DOF getDOF(int localIndex) {
		// TODO Auto-generated method stub
		return null;
	}

//	//@Override
//	public DOF getDOF(Mesh mesh, Element e, int localIndex) {
//		if(localIndex >= 1 && localIndex <= 6) {
//			DOF dof_u1 = new DOF(
//					localIndex,
//					this.getGlobalIndex(mesh, e, localIndex),
//					null); //no shape functions.
//			
//			dof_u1.setOwner(enty);
//			dof_u1.setVVFComponent(1);
//			return dof_u1;
//		}
//		else if(localIndex >= 7 && localIndex <= 12)
//			return 2;
//		else if(localIndex >=13 && localIndex <=15)
//			return 3;
//		else
//			throw new RuntimeException("local index should be in the range of [1,"+(shapeFuncs.length+1)+"]");
//			
//	}
}
