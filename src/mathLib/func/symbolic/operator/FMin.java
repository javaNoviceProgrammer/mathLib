package mathLib.func.symbolic.operator;

import java.util.Map;

import org.apache.bcel.Constants;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.InstructionFactory;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.MethodGen;
import org.apache.bcel.generic.Type;
import org.objectweb.asm.MethodVisitor;

import com.sun.xml.internal.ws.org.objectweb.asm.Opcodes;

import mathLib.func.symbolic.intf.MathFunc;

@SuppressWarnings("deprecation")
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
