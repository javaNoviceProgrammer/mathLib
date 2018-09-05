package mathLib.ode.intf;

import flanagan.integration.DerivFunction;

public interface DerivFunction1D extends DerivFunction {
	double deriv(double x, double y);
}
