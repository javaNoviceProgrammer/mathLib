package mathLib.fem.shapefun;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import mathLib.fem.core.Element;
import mathLib.fem.util.FutureyeException;
import mathLib.fem.util.Utils;
import mathLib.fem.util.container.ObjList;
import mathLib.fem.util.container.VertexList;
import mathLib.func.symbolic.MultiVarFunc;
import mathLib.func.symbolic.Variable;
import mathLib.func.symbolic.basic.FAxpb;
import mathLib.func.symbolic.basic.FC;
import mathLib.func.symbolic.intf.MathFunc;
import mathLib.func.symbolic.intf.ScalarShapeFunction;

public class SFLinearLocal1D extends MultiVarFunc  implements ScalarShapeFunction {
	private int funIndex;
	private MathFunc funCompose = null;
	private MathFunc funOuter = null;
	private ObjList<String> innerVarNames = null;

	private Element e = null;

	/**
	 * 
	 *  1-----2  -->r
	 * -1  0  1
	 * 
	 * N1 = (1-r)/2
	 * N2 = (1+r)/2
	 * 
	 * @param funID = 1,2
	 * 
	 */
	public SFLinearLocal1D(int funID) {
		funIndex = funID - 1;
		if(funID<1 || funID>2) {
			throw new FutureyeException("ERROR: funID should be 1 or 2.");
		}
		
		this.varNames = new String[]{"r"};
		innerVarNames = new ObjList<String>("x");
		
		Map<String, MathFunc> fInners = new HashMap<String, MathFunc>();
		List<String> varNamesInner = new LinkedList<String>();
		varNamesInner.add("x");
		
		/*
		 *  x = x1*N1 + x2*N2
		 *    = x1*(1-r)/2 + x2*(1+r)/2
		 *    = [ x1+x2 + (x2-x1)*r ]/2
		 *  =>
		 *  r = [2*x - (x1+x2)]/(x2-x1) 
		 *  r_x = 2/(x2-x1)  
		 */
		fInners.put("r", new MultiVarFunc("r", varNamesInner) {	
			public MathFunc diff(String var) {
				if(var.equals("x")) {
					VertexList vl = e.vertices();
					if(vl.size() == 2) {
						//TODO ??? 1-0? 0-1?
						double delta = vl.at(2).coord(1)-vl.at(1).coord(1);
						return new FC(2.0/delta);
					} else {
						throw new FutureyeException(
								"ERROR: SFLinearLocal1D vl.size()!=2, vl.size()="+vl.size());
					}
				}
				return null;
			}
			@Override
			public double apply(double... args) {
				throw new UnsupportedOperationException();
			}
		});
		
		if(funIndex == 0)
			funOuter = new FAxpb("r",-0.5,0.5);
		else
			funOuter = new FAxpb("r",0.5,0.5);
		funCompose = funOuter.compose(fInners);
		funCompose.setActiveVarByNames(funOuter.getVarNames());
		/**
		 * The default active variable names of a composite function is the inner variable names.
		 * Shape function needs the outer variable names as the active variable names.
		 */
		//funCompose.setActiveVarByNames(funOuter.getVarNames()); //this function doesn't set the flag
		funCompose.setOuterVarActive();
		funCompose.setArgIdx(Utils.getIndexMap(this.getVarNames()));
	}
	
	@Override
	public void assignElement(Element e) {
		this.e = e;
	}

	@Override
	public MathFunc diff(String varName) {
		return funCompose.diff(varName);
	}

	@Override
	public double apply(Variable v) {
		return funCompose.apply(v);
	}

	public String getExpr() {
		return "N"+(funIndex+1)+"(r)";
	}
	
	public String toString() {
		return "N"+(funIndex+1)+": "+funOuter.toString();
	}

	@Override
	public ScalarShapeFunction restrictTo(int funIndex) {
		throw new UnsupportedOperationException();
	}

	@Override
	public ObjList<String> innerVarNames() {
		return innerVarNames;
	}

	@Override
	public double apply(double... args) {
		return this.funCompose.apply(args);
	}

}
