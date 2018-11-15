package mathLib.func.symbolic.operator;

import java.util.List;
import java.util.Map;

import mathLib.fem.util.Utils;
import mathLib.func.symbolic.MultiVarFunc;
import mathLib.func.symbolic.intf.MathFunc;

public abstract class FUniaryOp extends MultiVarFunc {
	public MathFunc arg;
	
	public FUniaryOp(MathFunc arg) {
		this.arg = arg;
		List<String> list = arg.getVarNames();
		Map<String, Integer> map = Utils.getIndexMap(list);
		setVarNames(list);
		setArgIdx(map);
	}
	
	public MathFunc arg() {
		return arg;
	}
	
	@Override
	public MathFunc setArgIdx(Map<String, Integer> argsMap) {
		//Allocate new array each time due to the "copy on change"
		int[] idx = new int[varNames.length];
		for(int i=0; i<varNames.length; i++) {
			idx[i] = argsMap.get(varNames[i]);
		}
		this.argIdx = idx;
		
		//Copy on change
		if(!Utils.isMapContain(argsMap, this.arg.getArgIdxMap()))
			this.arg = this.arg.copy().setArgIdx(argsMap);
		
		return this;
	}

}
