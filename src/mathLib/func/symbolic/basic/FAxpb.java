package mathLib.func.symbolic.basic;

import java.util.Map;

import mathLib.func.symbolic.FMath;
import mathLib.func.symbolic.SingleVarFunc;
import mathLib.func.symbolic.VariableArray;
import mathLib.func.symbolic.intf.MathFunc;

/**
 * f(x) = a*x + b
 */
public class FAxpb extends SingleVarFunc {
	protected double a;
	protected double b;

	public FAxpb(double a, double b) {
		super("", Constant.x);
		this.a = a;
		this.b = b;
	}
	
	public FAxpb(String varName, double a, double b) {
		super("", varName);
		this.a = a;
		this.b = b;
	}
	
	@Override
	public MathFunc diff(String varName) {
		if(this.varName.equals(varName))
			return new FC(a);
		else
			return FMath.C0;
	}

	@Override
	public double apply(Variable v) {
		return a*v.get(varName)+b;
	}

	@Override
	public double apply(double... args) {
		return a*args[argIdx]+b;
	}
	
	@Override
	public double apply(Variable v, Map<Object,Object> cache) {
		return a*v.get(varName)+b;
	}
	
	@Override
	public double[] applyAll(VariableArray v, Map<Object,Object> cache) {
		int len = v.length();
		double[] rlt = new double[len];
		double[] vs = v.get(varName);
		for(int i=0;i<len;i++)
			rlt[i] = a*vs[i]+b;
		return rlt;
	}
	
	@Override
	public int getOpOrder() {
		if(Double.compare(a, 0.0) == 0)
			return OP_ORDER0;
		if(Double.compare(b, 0.0) == 0)
			return OP_ORDER2;
		else
			return OP_ORDER3;
	}
	
	public String getExpr() {
		if(Double.compare(a, 1.0) == 0) {
			if(Double.compare(b, 0.0) == 0)
				return varName;
			else
				return varName+"+"+b;
		} else if(Double.compare(a, 0.0) == 0) {
				return b+"";
		} else if(Double.compare(b, 0.0) == 0) {
			return a+"*"+varName;
		}
		return a+"*"+varName+"+"+b;
	}

//TODO	
//	/**
//	 * varNamesåœ¨ç”±å?•ä¸ªè‡ªå?˜é‡?è¡¨è¾¾å¼?è¿?ç®—ç»„å?ˆè€Œæˆ?çš„å¤šè‡ªå?˜é‡?å‡½æ•°æƒ…å†µä¸‹è®¡ç®—å¯¼æ•°å?Žå?¯èƒ½è¢«ä¿®æ”¹ï¼Œ
//	 * è¿™ç§?ä¿®æ”¹æ˜¯å…?è®¸çš„ï¼Œä¾‹å¦‚ï¼š(x+1)*(y+1)å…³äºŽxæ±‚å¯¼æ•°å?Žï¼ŒåŽŸæ?¥(y+1)çš„varNamesç”±[y]å?˜ä¸º[x,y]
//	 * 
//	 */
//	@Override
//	public MathFunc setVarNames(List<String> varNames) {
//		this.varNames = varNames;
//		return this;
//	}
}
