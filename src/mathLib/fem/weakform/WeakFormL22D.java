package mathLib.fem.weakform;


import mathLib.fem.core.Element;
import mathLib.fem.core.Node;
import mathLib.fem.core.geometry.Point;
import mathLib.fem.util.Utils;
import mathLib.func.symbolic.Variable;
import mathLib.func.symbolic.basic.FXY;
import mathLib.func.symbolic.intf.MathFunc;

/**
 * 
 * Solve: (Uu, v) = (f, v) - (k\nabla{U},\nabla{v})
 *   where u is unknown
 *   U,f and k is known
 *   
 */
public class WeakFormL22D extends AbstractScalarWeakForm {
	protected MathFunc g_U = null;
	protected MathFunc g_Ux = null;
	protected MathFunc g_Uy = null;
	protected MathFunc g_f = null;
	protected MathFunc g_k = null;


	@Override
	public MathFunc leftHandSide(Element e, ItemType itemType) {
		if(itemType==ItemType.Domain)  {
			//Integrand part of Weak Form on element e
			MathFunc integrand = null;
			MathFunc fU = Utils.interpolateOnElement(g_U, e);
			integrand = fU.M(u.M(v));
			return integrand;
		}
		return null;
	}

	@Override
	public MathFunc rightHandSide(Element e, ItemType itemType) {
		if(itemType==ItemType.Domain)  {
			MathFunc ff = Utils.interpolateOnElement(g_f, e);
			MathFunc fk = Utils.interpolateOnElement(g_k, e);
			
			MathFunc integrand = null;
			if(g_Ux == null) {
	
				int N = e.nodes.size();
				double[] f = new double[N];
				for(int i=1;i<=N;i++) {
					Node node = e.nodes.at(i);
					Variable var = Variable.createFrom(g_U, node, node.globalIndex);
					f[i-1] = g_U.apply(var);
				}
				double[] a = Utils.computeBilinearFunctionCoef(e.nodes.toArray(new Point[0]), f);
				//d(a1 + a2*x + a3*y + a4*x*y)/dx
				MathFunc dx = new FXY(0.0,a[3],a[1]);
				//d(a1 + a2*x + a3*y + a4*x*y)/dy
				MathFunc dy = new FXY(a[3],0.0,a[2]);
				
				MathFunc fUx = Utils.interpolateOnElement(dx, e);
				MathFunc fUy = Utils.interpolateOnElement(dy, e);
				integrand = 
					ff.M(v)
					.S(
						fk.M(
							fUx.M(v.diff("x")).A(fUy.M(v.diff("y")))
						)
					);	
//æ–°æ–¹æ³•2ï¼šè®¡ç®—å¯¼æ•°
				//åˆ©ç”¨ç±»DuDx
				
				
//æ—§æ–¹æ³•ï¼šè®¡ç®—å¯¼æ•°				
//				Function rlt_dx = new FC(0.0);
//				Function rlt_dy = new FC(0.0);
//				int nNode = e.nodes.size();
//				for(int i=1;i<=nNode;i++) {
//					DOFList dofListI = e.getNodeDOFList(i);
//					for(int k=1;k<=dofListI.size();k++) {
//						DOF dofI = dofListI.at(k);
//						Variable var = Variable.createFrom(g_U, (Node)dofI.getOwner(), dofI.getGlobalIndex());
//						Function PValue = new FC(g_U.value(var));
//						ScalarShapeFunction shape = dofI.getSSF();
//						//ä»¥å‰�ç‰ˆæœ¬éœ€è¦�è°ƒç”¨shapeFun.asignElement(e)ï¼ŒçŽ°åœ¨ç‰ˆæœ¬ä¸�éœ€è¦�è°ƒç”¨äº†
//						rlt_dx = rlt_dx.A(PValue.M(shape._d("x")));
//						rlt_dy = rlt_dy.A(PValue.M(shape._d("y")));
//					}
//				}
//				
//				integrand = 
//						ff.M(v)
//						.S(
//							fk.M(
//								rlt_dx.M(v._d("x")).A(rlt_dy.M(v._d("y")))
//							)
//						);
			} else {
				MathFunc fUx = Utils.interpolateOnElement(g_Ux, e);
				MathFunc fUy = Utils.interpolateOnElement(g_Uy, e);
				integrand = 
					ff.M(v)
					.S(
						fk.M(
							fUx.M(v.diff("x")).A(fUy.M(v.diff("y")))
						)
					);
				
			}
			return integrand;
		}
		return null;
	}

	public void setF(MathFunc f) {
		this.g_f = f;
	}
	
	public void setParam(MathFunc k,MathFunc U) {
		this.g_k = k;
		this.g_U = U;
	}
	
	public void setParam(MathFunc k, MathFunc U, MathFunc Ux, MathFunc Uy) {
		this.g_k = k;
		this.g_U = U;
		this.g_Ux = Ux;
		this.g_Uy = Uy;
	}
}
