package mathLib.fem.weakform;

import java.util.Map;

import mathLib.fem.core.intf.FiniteElement;
import mathLib.fem.core.intf.LHSExpr;
import mathLib.fem.core.intf.RHSExpr;
import mathLib.func.symbolic.intf.MathFunc;
import mathLib.util.CompiledFunc;

public class WeakForm {
	FiniteElement fe;
	
	MathFunc jac;
	MathFunc[][] matLHS;
	MathFunc[] vecRHS;

	CompiledFunc cjac;
	CompiledFunc[][] clhs;
	CompiledFunc[] crhs;

	public WeakForm(FiniteElement fe, LHSExpr lhsExpr, RHSExpr rhsExpr) {
		this.fe = fe;
		this.jac = fe.getCoordTrans().getJacobian();
 
		int nDOFs = this.fe.getNumberOfDOFs();
		MathFunc[] shapeFuncs = fe.getShapeFunctions();
		Map<String, MathFunc> map = fe.getCoordTrans().getCoordTransMap();
		matLHS = new MathFunc[nDOFs][nDOFs];
		vecRHS = new MathFunc[nDOFs];

		for(int j=0; j<nDOFs; j++) {
			MathFunc v = shapeFuncs[j];
			for(int i=0; i<nDOFs; i++) {
				MathFunc u = shapeFuncs[i];
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
				clhs[j][i] = matLHS[j][i].compileWithASM(argsOrder);
				//clhs[j][i] = matLHS[j][i].compile(argsOrder);
			}
			crhs[j] = vecRHS[j].compileWithASM(argsOrder);
			//crhs[j] = vecRHS[j].compile(argsOrder);
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
	
	public FiniteElement getFiniteElement() {
		return this.fe;
	}
}
