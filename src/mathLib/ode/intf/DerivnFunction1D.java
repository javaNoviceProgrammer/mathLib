package mathLib.ode.intf;

import flanagan.integration.DerivnFunction;

public interface DerivnFunction1D extends DerivnFunction {
	double[] derivn(double x, double[] yy);
}
