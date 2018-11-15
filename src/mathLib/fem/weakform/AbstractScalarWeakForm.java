package mathLib.fem.weakform;

import mathLib.fem.core.DOF;
import mathLib.fem.core.Element;
import mathLib.fem.core.intf.WeakFormOld;
import mathLib.fem.util.FutureyeException;
import mathLib.func.symbolic.intf.MathFunc;
import mathLib.func.symbolic.intf.ScalarShapeFunction;
import mathLib.func.symbolic.operator.FOIntegrate;
import mathLib.matrix.algebra.intf.Matrix;
import mathLib.matrix.algebra.intf.Vector;

public abstract class AbstractScalarWeakForm implements WeakFormOld {
	protected DOF trialDOF = null;
	protected DOF testDOF = null;
	protected ScalarShapeFunction u = null;
	protected ScalarShapeFunction v = null;
	protected int uDOFLocalIndex;
	protected int vDOFLocalIndex;

	@Override
	public void assembleElement(Element e, 
			Matrix globalStiff,	Vector globalLoad) {
		throw new UnsupportedOperationException();
	}

	@Override
	public MathFunc leftHandSide(Element e, ItemType itemType) {
		throw new UnsupportedOperationException();
	}

	@Override
	public MathFunc rightHandSide(Element e, ItemType itemType) {
		throw new UnsupportedOperationException();
	}

//	@Override
//	public void setShapeFunction(ShapeFunction trial, int trialDofLocalIndex,
//			ShapeFunction test, int testDofLocalIndex) {
//		this.u = (ScalarShapeFunction)trial;
//		this.v = (ScalarShapeFunction)test;
//		this.uDOFLocalIndex = trialDofLocalIndex;
//		this.vDOFLocalIndex = testDofLocalIndex;
//	}

	@Override
	public void setDOF(DOF trialDOF, DOF testDOF) {
		this.trialDOF = trialDOF;
		this.testDOF = testDOF;
		if(trialDOF != null) {
			this.u = trialDOF.getSSF();
			this.uDOFLocalIndex = trialDOF.getLocalIndex();
		}
		if(testDOF != null) {
			this.v = testDOF.getSSF();
			this.vDOFLocalIndex = testDOF.getLocalIndex();
		}
	}
	
	@Override
	public DOF getTrialDOF() {
		return this.trialDOF;
	}
	
	@Override
	public DOF getTestDOF() {
		return this.testDOF;
	}
	
	@Override
	public double integrate(Element e, MathFunc fun) {
		if(fun == null) return 0.0;
		if(e.dim() == 2) {
			if(e.vertices().size() == 3) {
				return FOIntegrate.intOnTriangleRefElement(
						fun.M(e.getJacobin()),2
						);
			} else if (e.vertices().size() == 4) {
				return FOIntegrate.intOnRectangleRefElement(
						fun.M(e.getJacobin()),5 //TODO
						);
			}
		} else if(e.dim() == 3) {
			if(e.vertices().size() == 4) {
				return FOIntegrate.intOnTetrahedraRefElement(
						fun.M(e.getJacobin()),2
					);
			} else if(e.vertices().size() == 8) {
				return FOIntegrate.intOnHexahedraRefElement(
						fun.M(e.getJacobin()),2
						
					);
			}
		} else if(e.dim() == 1) {
			return FOIntegrate.intOnLinearRefElement(
					fun.M(e.getJacobin()),5
				);
		} else {
			throw new FutureyeException(
					"Can NOT integrate on e" + e.vertices());
		}
		throw new FutureyeException("Error");
	}
	
	/**
	 * No meaning for scalar valued problems
	 */
	public boolean isVVFComponentCoupled(int nComponent1, int nComponent2) {
		return false;
	}
	
	public void preProcess(Element e) {
	}
}
