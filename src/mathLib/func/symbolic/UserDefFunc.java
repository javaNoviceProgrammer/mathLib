package mathLib.func.symbolic;

import java.util.List;
import java.util.Map;

import mathLib.fem.assembler.AssembleParam;
import mathLib.func.symbolic.intf.MathFunc;

public class UserDefFunc extends MathFuncBase {
	@Override
	public MathFunc setName(String name) {
		return this;
	}

	@Override
	public MathFunc setVarNames(List<String> varNames) {
		return this;
	}

	@Override
	public List<String> getVarNames() {
		return null;
	}

	@Override
	public MathFunc setArgIdx(Map<String, Integer> argsMap) {
		return this;
	}

	@Override
	public Map<String, Integer> getArgIdxMap() {
		return null;
	}

	@Override
	public boolean isConstant() {
		return false;
	}

	@Override
	public boolean isInteger() {
		return false;
	}

	@Override
	public boolean isZero() {
		return false;
	}

	@Override
	public boolean isReal() {
		return false;
	}

	@Override
	public double apply(double... args) {
		throw new UnsupportedOperationException();
	}

	@Override
	public double apply(AssembleParam ap, double... args) {
		return apply(args);
	}
}
