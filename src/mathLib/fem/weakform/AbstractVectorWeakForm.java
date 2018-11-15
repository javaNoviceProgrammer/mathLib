package mathLib.fem.weakform;

import mathLib.fem.core.DOF;
import mathLib.fem.core.Element;
import mathLib.fem.core.intf.WeakFormOld;
import mathLib.fem.util.FutureyeException;
import mathLib.func.symbolic.intf.MathFunc;
import mathLib.func.symbolic.intf.VectorShapeFunction;
import mathLib.func.symbolic.operator.FOIntegrate;
import mathLib.matrix.algebra.intf.Matrix;
import mathLib.matrix.algebra.intf.Vector;

public abstract class AbstractVectorWeakForm implements WeakFormOld {
	protected DOF trialDOF = null; //包�?�试探函数的自由度（试探解）
	protected DOF testDOF = null;  //包�?�检验函数的自由度
	protected VectorShapeFunction u = null;
	protected VectorShapeFunction v = null;
	protected int uDOFLocalIndex; //trial
	protected int vDOFLocalIndex; //test

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
//		this.u = (VectorShapeFunction)trial;
//		this.v = (VectorShapeFunction)test;
//		this.uDOFLocalIndex = trialDofLocalIndex;
//		this.vDOFLocalIndex = testDofLocalIndex;
//	}
	
	@Override
	public void setDOF(DOF trialDOF, DOF testDOF) {
		this.trialDOF = trialDOF;
		this.testDOF = testDOF;
		if(trialDOF != null) {
			this.u = trialDOF.getVSF();
			this.uDOFLocalIndex = trialDOF.getLocalIndex();
		}
		if(testDOF != null) {
			this.v = testDOF.getVSF();
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
				//三角形�?�元
				return FOIntegrate.intOnTriangleRefElement(
							fun.M(e.getJacobin()),4
						);
			} else if (e.vertices().size() == 4) {
				//四边形�?�元
				return FOIntegrate.intOnRectangleRefElement(
							fun.M(e.getJacobin()),2 //TODO
						);
			}
		} else if(e.dim() == 3) {
			if(e.vertices().size() == 4) {
				//四�?�体�?�元
				return FOIntegrate.intOnTetrahedraRefElement(
						fun.M(e.getJacobin()),2
					);
			} else if(e.vertices().size() == 8) {
				//六�?�体�?�元
				return FOIntegrate.intOnHexahedraRefElement(
						fun.M(e.getJacobin()),2
					);
			}
		} else if(e.dim() == 1) {
			//一维�?�元
			return FOIntegrate.intOnLinearRefElement(
					fun.M(e.getJacobin()),5
				);
		} else {
			throw new FutureyeException(
					"Can integrate on e" + e.vertices());
		}
		throw new FutureyeException("Error: integrate");
	}
	
	public boolean isVVFComponentCoupled(int nComponent1, int nComponent2) {
		throw new FutureyeException("Please specify coupling informaton of components of vector valued funtion(VVF) problem!");
	}
	
	public void preProcess(Element e) {
	}
}
