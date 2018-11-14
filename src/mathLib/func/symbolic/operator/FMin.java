package mathLib.func.symbolic.operator;

import java.util.Map;

import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import com.sun.org.apache.bcel.internal.generic.InstructionFactory;
import com.sun.org.apache.bcel.internal.generic.InstructionHandle;
import com.sun.org.apache.bcel.internal.generic.InstructionList;
import com.sun.org.apache.bcel.internal.generic.MethodGen;
import com.sun.xml.internal.ws.org.objectweb.asm.Opcodes;

import mathLib.func.symbolic.intf.MathFunc;

public class FMin extends FBinaryOp {
	/**
	 * Construct function: min(f(x), g(y))
	 * 
	 * @param f
	 * @param g
	 */
	public FMin(MathFunc f, MathFunc g) {
		super(f, g);
	}
	
	@Override
	public double apply(double... args) {
		return Math.min(arg1.apply(args), arg2.apply(args));
	}
	
	@Override
	public InstructionHandle bytecodeGen(String clsName, MethodGen mg,
			ConstantPoolGen cp, InstructionFactory factory,
			InstructionList il, Map<String, Integer> argsMap, int argsStartPos, 
			Map<MathFunc, Integer> funcRefsMap) {
		arg1.bytecodeGen(clsName, mg, cp, factory, il, argsMap, argsStartPos, funcRefsMap);
		arg2.bytecodeGen(clsName, mg, cp, factory, il, argsMap, argsStartPos, funcRefsMap);
		return  il.append(factory.createInvoke("java.lang.Math", "min",
				Type.DOUBLE, 
				new Type[] { Type.DOUBLE, Type.DOUBLE },
		Constants.INVOKESTATIC));
	}
	
	@Override
	public void bytecodeGen(MethodVisitor mv, Map<String, Integer> argsMap,
			int argsStartPos, Map<MathFunc, Integer> funcRefsMap, String clsName) {
		if (this.compileToStaticField && this.isCompiledToStaticFiled) {
			mv.visitFieldInsn(Opcodes.GETSTATIC, this.genClassName, this.staticFieldName, "D");
		} else {
			arg1.bytecodeGen(mv, argsMap, argsStartPos, funcRefsMap, clsName);
			arg2.bytecodeGen(mv, argsMap, argsStartPos, funcRefsMap, clsName);
			mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Math", "min", "(DD)D", false);
		}
	}

	@Override
	public String getExpr() {
		return "min("+arg1.getExpr()+","+arg2.getExpr()+")";
	}
}
