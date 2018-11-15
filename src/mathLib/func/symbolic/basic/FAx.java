package mathLib.func.symbolic.basic;

import mathLib.fem.assembler.AssembleParam;
import mathLib.fem.util.Constant;
import mathLib.func.symbolic.FMath;
import mathLib.func.symbolic.SingleVarFunc;
import mathLib.func.symbolic.Variable;
import mathLib.func.symbolic.intf.MathFunc;

/**
 * f(x) = a*x
 *
 */
public class FAx extends SingleVarFunc {
	protected double a;
	
	public FAx(double a) {
		super(null, Constant.x);
		this.a = a;
	}
	
	public FAx(String varName, double a) {
		super("", varName);
		this.a = a;
	}
	
	@Override
	public MathFunc diff(String varName) {
		if(this.getVarNames().contains(varName))
			return new FC(a);
		else
			return FMath.C0;
	}

	@Override
	public double apply(Variable v) {
		return a*v.get(getVarNames().get(0));
	}

	@Override
	public double apply(AssembleParam ap, double... args) {
		return a*args[argIdx];
	}

	@Override
	public double apply(double... args) {
		return a*args[argIdx];
	}
	
//	@Override
//	public MathFunc copy() {
//		FAx ret = new FAx(this.varName, a);
//		ret.fName = this.fName;
//		ret.argIdx = this.argIdx;
//		return ret;
//	}

	@Override
	public String getExpr() {
		if(Double.compare(a, 1.0) == 0)
			return varName;
		else if(Double.compare(a, 0.0) == 0)
			return "0.0";
		return a+"*"+varName;
	}
}
