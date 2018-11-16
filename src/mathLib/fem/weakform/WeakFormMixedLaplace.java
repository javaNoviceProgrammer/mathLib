package mathLib.fem.weakform;

import static mathLib.func.symbolic.FMath.*;

import mathLib.fem.core.DOF;
import mathLib.fem.core.Element;
import mathLib.fem.util.Utils;
import mathLib.fem.util.container.DOFList;
import mathLib.func.symbolic.basic.FC;
import mathLib.func.symbolic.intf.MathFunc;
import mathLib.func.symbolic.intf.VectorShapeFunction;
import mathLib.func.symbolic.operator.FOIntegrate;
import mathLib.matrix.algebra.intf.BlockMatrix;
import mathLib.matrix.algebra.intf.BlockVector;
import mathLib.matrix.algebra.intf.Matrix;
import mathLib.matrix.algebra.intf.Vector;

/**
 * Problem:
 * 
 *   div \mathbf{p} + f = 0
 *   \mathbf{p} = \nabla u
 * 
 * Weak Form: 
 *  seek \mathbf{p} \in H_{g,N}(div,\Omega) and u \in L^{2}(\Omega)
 *  such that
 *  (p,q)_{\Omega} + (u,\div{q})_{\Omega} = u_D*(q,\mu)_{\Gamma_{D}},  
 *  							for all q \in H_{0,N}(div,\Omega)
 *  (v,\div{p})_{\Omega} = -(v,f)_{\Omega}, 
 *  							for all v \in L^{2}(\Omega)
 * 
 * Algebra System:
 * 
 * ( B  C )(p)   (b0)
 * ( C' 0 )(u) = (bf)
 * 
 *
 */
public class WeakFormMixedLaplace extends AbstractVectorWeakForm {
	protected MathFunc g_f = null;
	protected MathFunc g_k = null;
	//protected Function g_c = null;
	//protected Function g_g = null;
	//protected Function g_d = null;
	
	public void setF(MathFunc f) {
		this.g_f = f;
	}
	
	//Robin:  d*u + k*u_n = g
	public void setParam(MathFunc k,MathFunc c,MathFunc g,MathFunc d) {
		this.g_k = k;
		//this.g_c = c;
		//this.g_g = g;
		//this.g_d = d;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public void assembleElement(Element e, 
			Matrix globalStiff, Vector globalLoad){
		
		DOFList edgeDOFs = e.getAllEdgeDOFList();
		//获�?��?�元体对应的自由度列表
		DOFList eleDOFs = e.getVolumeDOFList();
		int nEdgeDOF = edgeDOFs.size();
		int nElementDOF = eleDOFs.size();

		BlockMatrix blockMat = (BlockMatrix)globalStiff;
		BlockVector blockVec = (BlockVector)globalLoad;
		
//�?需�?使用�?�独的�?个分�?��?矩阵，直接使用整个�?�矩阵就�?�以了，
//�?��?过程由于整个�?�矩阵的特性，会自动将相应的元素放入对应的�?�?�中。		
//		Matrix m11 = blockMat.getBlock(1, 1);
//		Matrix m12 = blockMat.getBlock(1, 2);
//		Matrix m21 = blockMat.getBlock(2, 1);
//		//Matrix m22 = blockMat.getBlock(2, 2);
		
		e.updateJacobinLinear2D();
		for(int i=1;i<=nEdgeDOF;i++) {
			edgeDOFs.at(i).getVSF().assignElement(e);
		}
		
		//边自由度�?�循环
		for(int j=1;j<=nEdgeDOF;j++) {
			DOF dofV = edgeDOFs.at(j);
			VectorShapeFunction vecV = dofV.getVSF();
			for(int i=1;i<=nEdgeDOF;i++) {
				DOF dofU = edgeDOFs.at(i);
				VectorShapeFunction vecU = dofU.getVSF();
				
				//B = (p,q)_{\Omega}
				MathFunc integrandB = null;
				integrandB = vecU.dot(vecV);
				//�?�元上数值积分
				if(e.vertices().size() == 3) {
					double val = FOIntegrate.intOnTriangleRefElement(
							integrandB.M(e.getJacobin()),4);
					blockMat.add(dofU.getGlobalIndex(), dofV.getGlobalIndex(), val);
				}
			}
			//�?�自由度循环（2D�?�元）
			for(int k=1;k<=nElementDOF;k++) {
				DOF dofE = eleDOFs.at(k);
				//C = (u,\div{q})_{\Omega}
				MathFunc integrandC = null;
				integrandC = div(vecV);
				double val = FOIntegrate.intOnTriangleRefElement(
						integrandC.M(e.getJacobin()),4);
				blockMat.add(dofV.getGlobalIndex(), dofE.getGlobalIndex(), val);
				//C' = (v,\div{p})_{\Omega}
				blockMat.add(dofE.getGlobalIndex(), dofV.getGlobalIndex(), val);
			}
			
			//b0 = 0 //Dirichlet�?�件如何处�?�？
		}
		
		for(int k=1;k<=nElementDOF;k++) {
			DOF dofE = eleDOFs.at(k);
			//ShapeFunction sf = dofE.getSF(); //分片常数元，在积分项中系数是1
			MathFunc integrand = Utils.interpolateOnElement(g_f, e);
			//bf = -(v,f)_{\Omega}
			integrand = FC.c(-1.0).M(integrand);
			double val = 0.0;
			if(e.vertices().size() == 3) {
				val = FOIntegrate.intOnTriangleRefElement(
						integrand.M(e.getJacobin()),4
					);
			} else if (e.vertices().size() == 4) {
				val = FOIntegrate.intOnRectangleRefElement(
						integrand.M(e.getJacobin()),2 //TODO
						);
			}
			blockVec.add(dofE.getGlobalIndex(), val);
		}
	}
}
