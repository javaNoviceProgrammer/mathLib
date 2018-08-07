package mathLib.ode;

import edu.uta.futureye.function.intf.MathFunc;

public class WeakFormFirstOrder {

	// diff equation: a*y' + b*y = f , over [x0, x1] , Boundary value:  y[x0] = y0 or y[x1] = y0

	double x0, x1, y0 ;
	MathFunc a, b, f ;
	String boundary ;

	public WeakFormFirstOrder(MathFunc a, MathFunc b, MathFunc f){
		this.a = a ;
		this.b = b ;
		this.f = f ;
	}

	public void setBoundary(double min, double max){
		this.x0 = min ;
		this.x1 = max ;
	}

	public void setBoundaryValue(double y0, String boundary){
		this.y0 = y0 ;
		this.boundary = boundary ;
	}

}
