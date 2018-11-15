package mathLib.func.symbolic.basic;

import mathLib.fem.assembler.AssembleParam;
import mathLib.fem.core.Edge;
import mathLib.fem.core.Element;
import mathLib.fem.core.Face;
import mathLib.fem.core.geometry.GeoEntity;
import mathLib.fem.util.FutureyeException;
import mathLib.func.symbolic.FMath;
import mathLib.func.symbolic.MultiVarFunc;
import mathLib.func.symbolic.Variable;
import mathLib.func.symbolic.intf.ElementDependentFunction;
import mathLib.func.symbolic.intf.MathFunc;
import mathLib.matrix.algebra.intf.Vector;

/**
 * \frac{ \partial{u} }{ \partial{\mathbf{n}} }
 *
 */
public class DuDn extends MultiVarFunc implements ElementDependentFunction {
	protected Element e = null;
	protected MathFunc u = null;
	protected MathFunc u_x = null;
	protected MathFunc u_y = null;
	protected MathFunc u_z = null;
	protected Vector norm = null;
	
	public DuDn(MathFunc u) {
		this.u = u;
	}
	
	public DuDn(MathFunc u_x, MathFunc u_y, MathFunc u_z) {
		this.u_x = u_x;
		this.u_y = u_y;
		this.u_z = u_z;
	}
	
	@Override
	public void setElement(Element e) {
		this.e = e;
		//Compute outer normal vector 
		GeoEntity ge = e.getGeoEntity();
		if(ge instanceof Edge) {
			norm = ((Edge)ge).getNormVector();
		} else if(ge instanceof Face) {
			norm = ((Face)ge).getNormVector();
		} else {
			throw new FutureyeException("Unsuported element type");
		}
	}

	@Override
	public double apply(Variable v) {
		MathFunc rlt = null;
		this.setElement(v.getElement());
		if(u != null) {
			//u is passed into constructor
			rlt = FMath.grad(u).dot(norm);
		} else if(this.norm.getDim() == 2) {
			//2D case
			rlt = u_x.M(new FC(norm.get(1)))
					.A(
				  u_y.M(new FC(norm.get(2)))
					);
		} else if(this.norm.getDim() == 3) {
			//3D case
			rlt = FMath.sum(
					u_x.M(new FC(norm.get(1))),
					u_y.M(new FC(norm.get(2))),
					u_z.M(new FC(norm.get(3)))
					);
		} else {
			throw new FutureyeException(
					"Error: u="+u+", this.norm.getDim()="+this.norm.getDim());
		}
		return rlt.apply(v);
	}
	
	public String toString() {
		return "DuDn";
	}

	@Override
	public double apply(AssembleParam ap, double... args) {
		MathFunc rlt = null;
		this.setElement(ap.element);
		if(u != null) {
			//u is passed into constructor
			rlt = FMath.grad(u).dot(norm);
		} else if(this.norm.getDim() == 2) {
			//2D case
			rlt = u_x.M(new FC(norm.get(1)))
					.A(
				  u_y.M(new FC(norm.get(2)))
					);
		} else if(this.norm.getDim() == 3) {
			//3D case
			rlt = FMath.sum(
					u_x.M(new FC(norm.get(1))),
					u_y.M(new FC(norm.get(2))),
					u_z.M(new FC(norm.get(3)))
					);
		} else {
			throw new FutureyeException(
					"Error: u="+u+", this.norm.getDim()="+this.norm.getDim());
		}
		return rlt.apply(ap, args);
	}

	@Override
	public double apply(double... args) {
		return apply(null, args);
	}
}
