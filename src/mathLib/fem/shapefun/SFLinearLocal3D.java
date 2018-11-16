package mathLib.fem.shapefun;

import mathLib.fem.core.Element;
import mathLib.fem.util.FutureyeException;
import mathLib.fem.util.container.ObjList;
import mathLib.func.symbolic.MultiVarFunc;
import mathLib.func.symbolic.Variable;
import mathLib.func.symbolic.basic.FC;
import mathLib.func.symbolic.intf.MathFunc;
import mathLib.func.symbolic.intf.ScalarShapeFunction;
import mathLib.func.symbolic.intf.ShapeFunction;

/**
 * 3D tetrahedral coordinates
 * 
 * 3D 四�?�体 局部�??标 线性形函数
 * 
 * 创建形函数需�?的主�?步骤：
 * 
 * 1. 创建形函数表达�?（局部�??标）
 * 2. （�?�?�）函数求值（局部�??标），（�?�?�）函数求导数（关于物�?��??标）
 * 3. �??供�?维�?�的形函数
 * 
 * 本类采用直接定义法，并且函数值和导数已�?有推导出�?�的表达�?。
 * �?�?�函数法�?：
 * SFQuadraticLocal2D函数值和导数没有完全推到出�?�，需�?的计算�?较大
 * SFLinearLocal2D函数值和导数已�?有推导出�?�的表达�?。
 * 
 * N = N(r,s,t,u) = N( r(x,y,z), s(x,y,z), t(x,y,z), u(x,y,z) )
 * N1 = r
 * N2 = s
 * N3 = t
 * N4 = u
 * 
 * @author liuyueming
 *
 */
public class SFLinearLocal3D extends MultiVarFunc 
							 implements ScalarShapeFunction {
	private int funIndex;
	private ObjList<String> innerVarNames = null;
	
	protected Element e = null;
	private double x1,x2,x3,x4;
	private double y1,y2,y3,y4;
	private double z1,z2,z3,z4;
	private double a1,a2,a3,a4;
	private double b1,b2,b3,b4;
	private double c1,c2,c3,c4;
	private double volume;
	
	/**
	 * 构造下列形函数中的一个：
	 * @param funID = 1,2,3,4 (N1,N2,N3,N4)
	 * 
	 */
	public void Create(int funID) {
		funIndex = funID - 1;
		if(funID<1 || funID>4) {
			System.out.println("ERROR: funID should be 1,2,3 or 4.");
			return;
		}
		
		varNames[0] = "r";
		varNames[1] = "s";
		varNames[2] = "t";
		varNames[3] = "u";
		innerVarNames = new ObjList<String>("x","y","z");
		
	}
	
	public SFLinearLocal3D(int funID) {
		this.Create(funID);
	}
	
	@Override
	public void assignElement(Element e) {
		this.e = e;
		
		x1 = e.nodes.at(1).coord(1);
		x2 = e.nodes.at(2).coord(1);
		x3 = e.nodes.at(3).coord(1);
		x4 = e.nodes.at(4).coord(1);
		y1 = e.nodes.at(1).coord(2);
		y2 = e.nodes.at(2).coord(2);
		y3 = e.nodes.at(3).coord(2);
		y4 = e.nodes.at(4).coord(2);
		z1 = e.nodes.at(1).coord(3);
		z2 = e.nodes.at(2).coord(3);
		z3 = e.nodes.at(3).coord(3);
		z4 = e.nodes.at(4).coord(3);
		
		a1=y2*(z4-z3)-y3*(z4-z2)+y4*(z3-z2);
		a2=-y1*(z4-z3)+y3*(z4-z1)-y4*(z3-z1);
		a3=y1*(z4-z2)-y2*(z4-z1)+y4*(z2-z1);
		a4=-y1*(z3-z2)+y2*(z3-z1)-y3*(z2-z1);
		
		b1=-x2*(z4-z3)+x3*(z4-z2)-x4*(z3-z2);
		b2=x1*(z4-z3)-x3*(z4-z1)+x4*(z3-z1);
		b3=-x1*(z4-z2)+x2*(z4-z1)-x4*(z2-z1);
		b4=x1*(z3-z2)-x2*(z3-z1)+x3*(z2-z1);
		
		c1=x2*(y4-y3)-x3*(y4-y2)+x4*(y3-y2);
		c2=-x1*(y4-y3)+x3*(y4-y1)-x4*(y3-y1);
		c3=x1*(y4-y2)-x2*(y4-y1)+x4*(y2-y1);
		c4=-x1*(y3-y2)+x2*(y3-y1)-x3*(y2-y1);
		
		/*
		      |x2-x1 x3-x1 x4-x1| |1 2 3|
		6*v = |y2-y1 y3-y1 y4-y1|=|4 5 6|=1*(5*9-8*6) + 4*(8*3-2*9) + 7*(2*9-8*3)
		      |z2-z1 z3-z1 z4-z1| |7 8 9|
		*/
		volume = (x2-x1)*((y3-y1)*(z4-z1)-(y4-y1)*(z3-z1))
			   + (y2-y1)*((x4-x1)*(z3-z1)-(x3-x1)*(z4-z1))
			   + (z2-z1)*((x3-x1)*(y4-y1)-(x4-x1)*(y3-y1));
		//TODO 去掉�?对值
		volume = Math.abs(volume/6.0);
		
	}

	SFLinearLocal2D[] faceSF = {
			new SFLinearLocal2D(1),
			new SFLinearLocal2D(2),
			new SFLinearLocal2D(3)			
		};
	
	@Override
	public ShapeFunction restrictTo(int funID) {
		return faceSF[funID-1];
	}

	@Override
	public MathFunc diff(String var) {
		if(this.volume < 0.0)
			throw new FutureyeException("SFLinearLocal3D: volume < 0.0");
		
		//关于自由�?��?r,s,t求导，u为�?�自由�?��?
		if( var.equals("r") || var.equals("s") || var.equals("t") ) {
			//u=1-r-s-t
			if(funIndex == 3)
				return new FC(-1.0);
			if(var.equals(varNames[funIndex]))
				return new FC(1.0);
			else
				return new FC(0.0);
		} else if(var.equals("u")) {
			throw new FutureyeException("Error: u is not free variable");
		}
		
		//关于x,y,z求导
		if(var.equals("x")) {
			if(funIndex == 0)
				return new FC(a1/(6*volume));
			else if(funIndex == 1)
				return new FC(a2/(6*volume));
			else if(funIndex == 2)
				return new FC(a3/(6*volume));
			else if(funIndex == 3)
				return new FC(a4/(6*volume));
			else 
				throw new FutureyeException("Error: derivative(x), funIndex="+funIndex);
		} else if(var.equals("y")) {
			if(funIndex == 0)
				return new FC(b1/(6*volume));
			else if(funIndex == 1)
				return new FC(b2/(6*volume));
			else if(funIndex == 2)
				return new FC(b3/(6*volume));
			else if(funIndex == 3)
				return new FC(b4/(6*volume));
			else 
				throw new FutureyeException("Error: derivative(y), funIndex="+funIndex);
		} else if(var.equals("z")) {
			if(funIndex == 0)
				return new FC(c1/(6*volume));
			else if(funIndex == 1)
				return new FC(c2/(6*volume));
			else if(funIndex == 2)
				return new FC(c3/(6*volume));
			else if(funIndex == 3)
				return new FC(c4/(6*volume));
			else 
				throw new FutureyeException("Error: derivative(z), funIndex="+funIndex);
		} else
			throw new FutureyeException("Error: derivative(), var="+var+", should be x,y or z!");
	}

	@Override
	public double apply(Variable v) {
		if(funIndex == 0)
			return v.get("r");
		else if(funIndex == 1)
			return v.get("s");
		else if(funIndex == 2)
			return v.get("t");
		else if(funIndex == 3)
			return v.get("u");
		else
			throw new FutureyeException("Error: funIndex="+funIndex);
	}

	public String toString() {
		return varNames[funIndex];
	}

	@Override
	public ObjList<String> innerVarNames() {
		return innerVarNames;
	}

	@Override
	public double apply(double... args) {
		throw new UnsupportedOperationException();
	}
}