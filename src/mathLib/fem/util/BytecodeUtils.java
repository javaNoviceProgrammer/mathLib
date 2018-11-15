package mathLib.fem.util;

import static org.apache.bcel.Constants.ACC_PUBLIC;
import static org.apache.bcel.Constants.ACC_STATIC;
import static org.apache.bcel.Constants.ACC_SUPER;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.sun.org.apache.bcel.internal.generic.ClassGen;
import com.sun.org.apache.bcel.internal.generic.ConstantPoolGen;
import com.sun.org.apache.bcel.internal.generic.InstructionConstants;
import com.sun.org.apache.bcel.internal.generic.InstructionFactory;
import com.sun.org.apache.bcel.internal.generic.InstructionList;
import com.sun.org.apache.bcel.internal.generic.MethodGen;

import mathLib.fem.assembler.AssembleParam;
import mathLib.func.symbolic.basic.FComposite;
import mathLib.func.symbolic.intf.MathFunc;
import mathLib.func.symbolic.operator.FBinaryOp;

public class BytecodeUtils {
	public static void postOrder(MathFunc func, List<MathFunc> list) {
		if(func instanceof FBinaryOp) {
			postOrder(((FBinaryOp) func).arg1, list);
			postOrder(((FBinaryOp) func).arg2, list);
		} else if(func instanceof FComposite) {
			FComposite fc = (FComposite)func;
			for(Entry<String, MathFunc> e : fc.fInners.entrySet()) {
				postOrder(e.getValue(), list);
			}
			postOrder(fc.fOuter, list);
		}
		list.add(func);
	}

	public static Map<MathFunc, Integer> getFuncRefsMap(MathFunc func) {
		List<MathFunc> list = new ArrayList<MathFunc>();
		postOrder(func, list);
		Map<MathFunc, Integer> map = new HashMap<MathFunc, Integer>();
		for(int i=0; i<list.size(); i++) {
			map.put(list.get(i), i);
		}
		return map;
	}

	public static ClassGen genClass(MathFunc func, String[] varNames, String funcClsName,
			boolean writeClassFile, boolean staticMethod) {
		String packageName = "edu.uta.futureye.bytecode";
		String clsName = funcClsName;
		String fullClsName = packageName+"."+clsName;
		ClassGen cg = new ClassGen(fullClsName, "edu.uta.futureye.bytecode.CompiledFunc",
				"<generated>", ACC_PUBLIC | ACC_SUPER, null);
		ConstantPoolGen cp = cg.getConstantPool(); // cg creates constant pool
		InstructionList il = new InstructionList();
		InstructionFactory factory = new InstructionFactory(cg);

		short acc_flags = ACC_PUBLIC;
		if(staticMethod)
			acc_flags |= ACC_STATIC;
		MethodGen mg = new MethodGen(acc_flags, // access flags
				Type.DOUBLE,                    // return type
				new Type[] {                    // argument types
					Type.getType(AssembleParam.class),
					new ArrayType(Type.DOUBLE, 1)
				},
				new String[] { "ap", "args" }, // argument names
				"apply",                       //method name
				fullClsName,                   //class name
				il, cp);

		HashMap<String, Integer> argsMap = new HashMap<String, Integer>();
		if(varNames == null || varNames.length == 0) {
			List<String> args = func.getVarNames();
			for(int i=0; i<args.size(); i++) {
				argsMap.put(args[i], i);
			}
//			System.out.println("JIT Compiled: "+func);
		} else {
			StringBuilder sb = new StringBuilder();
			sb.append("(");
			for(int i=0; i<varNames.length; i++) {
				argsMap.put(varNames[i], i);
				sb.append(varNames[i]).append(",");
			}
			sb.delete(sb.length()-1, sb.length());
			sb.append(")");
			//func.setArgIdx(argsMap); //No need, this is for user defined apply() method not for compile()
//			System.out.println("JIT Compiled: "+func.getName()+sb.toString()+" = "+func.getExpr());
		}

		//Recursively set the argument index (order) before calling 'getFuncRefsMap()'
		//since the call of 'setArgIdx()' may change the expression tree
		//TODO the call of 'setArgIdx()' in 'bytecodeGen()' may be not necessary
		func.setArgIdx(argsMap);

		Map<MathFunc, Integer> refsMap = getFuncRefsMap(func);

		if(staticMethod)
			func.bytecodeGen(clsName, mg, cp, factory, il, argsMap, BytecodeConst.argIdx, refsMap);
		else
			func.bytecodeGen(clsName, mg, cp, factory, il, argsMap, BytecodeConst.argIdx+1, refsMap);
		il.append(InstructionConstants.DRETURN);

		mg.setMaxStack();
		cg.addMethod(mg.getMethod());
		il.dispose(); // Allow instruction handles to be reused

		cg.addEmptyConstructor(ACC_PUBLIC);
		if(writeClassFile) {
			try {
				cg.getJavaClass().dump("bin/edu/uta/futureye/bytecode/"+clsName+".class");
			} catch (java.io.IOException e) {
				System.err.println(e);
			}
		}
		return cg;
	}
}
