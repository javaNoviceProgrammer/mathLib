package mathLib.func.symbolic.basic;

import static mathLib.func.symbolic.FMath.C0;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.InstructionFactory;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.MethodGen;
import org.apache.bcel.generic.PUSH;
import org.objectweb.asm.MethodVisitor;

import mathLib.func.symbolic.MathFuncBase;
import mathLib.func.symbolic.Variable;
import mathLib.func.symbolic.VariableArray;
import mathLib.func.symbolic.intf.MathFunc;

/**
 * Constant function: f = c
 * 
 */
public class FC extends MathFuncBase {
	protected double val;
	protected String name;
	
	//Constants cache
	protected static Map<Double, FC> cs = new HashMap<Double, FC>();
	
	public FC(double val) {
		this.val = val;
	}

	public static FC c(double v) {
		FC c = cs.get(v);
		if(c == null) {
			c = new FC(v);
			cs.put(v, c);
			return c;
		} else {
			return c;
		}
	}
	
	@Override
	public double apply(Variable v) {
		return val;
	}

	@Override
	public double apply(double... args) {
		return val;
	}
	
	@Override
	public double apply(Variable v, Map<Object,Object> cache) {
		return val;
	}
	
	@Override
	public double[] applyAll(VariableArray v, Map<Object,Object> cache) {
		int len = v.length();
		double[] rlt = new double[len];
		for(int i=0;i<len;i++)
			rlt[i] = val;
		return rlt;
	}
	
	@Override
	public MathFunc diff(String varName) {
		return C0;
	}
	
	@Override
	public MathFunc copy() {
		//return new FC(this.val);
		return this;
	}
	
	@Override
	public String toString() {
		return String.valueOf(val);
	}
	
	@Override 
	public boolean isConstant() {
		return true;
	}
	
	@Override
	public boolean isInteger() {
		return Math.floor(val)==val;
	}
	
	@Override
	public boolean isZero() {
		return val==0.0;
	}
	
	@Override
	public boolean isReal() {
		return true;
	}
	
	@Override
	public String getName() {
		if(null != this.name)
			return this.name;
		return String.valueOf(val);
	}

	@Override
	public MathFunc setName(String name) {
		this.name = name;
		return this;
	}

	@Override
	public MathFunc setVarNames(List<String> varNames) {
		return this;
		//throw new UnsupportedOperationException();
	}

	@Override
	public List<String> getVarNames() {
		return new ArrayList<String>();
	}

	@Override
	public MathFunc setArgIdx(Map<String, Integer> argsMap) {
		return this;
	}

	@Override
	public Map<String, Integer> getArgIdxMap() {
		return new HashMap<String, Integer>();
	}

	@Override
	public String getExpr() {
		return String.valueOf(val);
	}
	
	@Override
	public InstructionHandle bytecodeGen(String clsName, MethodGen mg,
			ConstantPoolGen cp, InstructionFactory factory,
			InstructionList il, Map<String, Integer> argsMap, int argsStartPos, 
			Map<MathFunc, Integer> funcRefsMap) {
		return il.append(new PUSH(cp, val));
	}

	@Override
	public void bytecodeGen(MethodVisitor mv, Map<String, Integer> argsMap,
			int argsStartPos, Map<MathFunc, Integer> funcRefsMap, String clsName) {
		mv.visitLdcInsn(val);
	}
}
