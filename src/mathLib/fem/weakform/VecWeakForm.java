/**
 * Copyright (c) 2010, nkliuyueming@gmail.com. All rights reserved.
 * 
 * 
 */
package mathLib.fem.weakform;

import java.util.Map;

import mathLib.fem.core.intf.LHSVecExpr;
import mathLib.fem.core.intf.RHSVecExpr;
import mathLib.fem.core.intf.VecFiniteElement;
import mathLib.func.symbolic.intf.MathFunc;
import mathLib.func.symbolic.intf.VecMathFunc;
import mathLib.util.CompiledFunc;

public class VecWeakForm {
	VecFiniteElement fe;
	
	MathFunc jac;
	MathFunc[][] matLHS;
	MathFunc[] vecRHS;

	CompiledFunc cjac;
	CompiledFunc[][] clhs;
	CompiledFunc[] crhs;

	public VecWeakForm(VecFiniteElement fe, LHSVecExpr lhsExpr, RHSVecExpr rhsExpr) {
		this.fe = fe;
		this.jac = fe.getCoordTrans().getJacobian();
 
		int nDOFs = this.fe.getNumberOfDOFs();
		VecMathFunc[] shapeFuncs = fe.getShapeFunctions();
		Map<String, MathFunc> map = fe.getCoordTrans().getCoordTransMap();
		matLHS = new MathFunc[nDOFs][nDOFs];
		vecRHS = new MathFunc[nDOFs];

		for(int j=0; j<nDOFs; j++) {
			VecMathFunc v = shapeFuncs[j];
			for(int i=0; i<nDOFs; i++) {
				VecMathFunc u = shapeFuncs[i];
				matLHS[j][i] = lhsExpr.apply(u, v).compose(map)*jac;
				matLHS[j][i].setName("LHS"+i+""+j);
			}
			vecRHS[j] = rhsExpr.apply(v).compose(map)*jac;
			vecRHS[j].setName("RHS"+j);
		}
	}

	public void compile() {
		String[] argsOrder = fe.getArgsOrder();
		jac.compileToStaticField(true);
		cjac = jac.compileWithASM(argsOrder);

		int nDOFs = this.fe.getNumberOfDOFs();
		clhs = new CompiledFunc[nDOFs][nDOFs];
		crhs = new CompiledFunc[nDOFs];
		for(int j=0; j<nDOFs; j++) {
			for(int i=0; i<nDOFs; i++) {
				//System.out.println("compile:"+i+"_"+j);
				clhs[j][i] = matLHS[j][i].compileWithASM(argsOrder);
			}
			crhs[j] = vecRHS[j].compileWithASM(argsOrder);
		}
	}
	
	public CompiledFunc[][] getCompiledLHS() {
		return clhs;
	}
	
	public CompiledFunc[] getCompiledRHS() {
		return crhs;
	}
	
	public CompiledFunc getCompiledJac() {
		return this.cjac;
	}
	
	public VecFiniteElement getFiniteElement() {
		return this.fe;
	}
}
